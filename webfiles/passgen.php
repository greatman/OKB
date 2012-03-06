<?php
include "config.inc.php";
mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
mysql_select_db($dbname);
if (!isset($_GET['s'])){}else{
$s = mysql_real_escape_string($_GET['s']);
if ($s !== $phppass){}else{
if (!isset($_GET['t'])){}else{
$t = mysql_real_escape_string($_GET['t']);
if (!isset($_GET['p'])){}else{
$p = mysql_real_escape_string($_GET['p']);
if (!isset($_GET['u'])){}else{
$u = mysql_real_escape_string($_GET['u']);

header("content-type: text/plain");

if ($t == "phpbb"){
include "hash.inc.php";
$p_hasher = new PasswordHash(8, TRUE);
if ($sqlprefix == ""){
$hash = mysql_query("SELECT user_password FROM users WHERE username = '".$u."'");
}else{
$hash = mysql_query("SELECT user_password FROM ".$sqlprefix."users WHERE username = '".$u."'");}
while ($hash2 = mysql_fetch_array($hash)){
$hash3 = $hash2['user_password'];
$check = $p_hasher->CheckPassword($p, $hash3);
if ($check == "true"){
echo $hash3; }
unset($p_hasher); } }

if ($t == "vanilla"){
include "PasswordHash.php";
$p_hasher = new PasswordHash(8, TRUE);
if ($sqlprefix == ""){
$hash = mysql_query("SELECT Password FROM gdn_user WHERE Name = '".$u."'");
}else{
$hash = mysql_query("SELECT Password FROM ".$sqlprefix."gdn_user WHERE Name = '".$u."'");}
while ($hash2 = mysql_fetch_array($hash)){
$hash3 = $hash2['Password'];
$check = $p_hasher->CheckPassword($p, $hash3);
if ($check == "true"){
echo $hash3; }
unset($p_hasher); } }

if ($t == "bbpress"){
include "PasswordHash.php";
$p_hasher = new PasswordHash(8, TRUE);
if ($sqlprefix == ""){
$hash = mysql_query("SELECT user_pass FROM bb_users WHERE user_login = '".$u."'");
}else{
$hash = mysql_query("SELECT user_pass FROM ".$sqlprefix."users WHERE user_login = '".$u."'");}
while ($hash2 = mysql_fetch_array($hash)){
$hash3 = $hash2['user_pass'];
$check = $p_hasher->CheckPassword($p, $hash3);
if ($check == "true"){
echo $hash3; }
unset($p_hasher); } }

if ($t == "custom"){
//Use this to generate a hash or something. Afterwards just echo it.
//Set 'forum' in config.yml to 'custom'
}

}
}
}
}
}

?>