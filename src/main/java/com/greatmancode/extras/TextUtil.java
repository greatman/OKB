package com.greatmancode.extras;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class TextUtil
{
    public static String titleize(String str)
    {
        String line = ChatColor.GOLD + repeat("_", 60);
        String center = ".[ " + ChatColor.YELLOW + str + ChatColor.GOLD + " ].";
        int pivot = line.length() / 2;
        int eatLeft = center.length() / 2;
        int eatRight = center.length() - eatLeft;

        if (eatLeft < pivot)
            return line.substring(0, pivot - eatLeft) + center + line.substring(pivot + eatRight);
        else
            return center;
    }

    public static String merge(List<String> message, int start)
    {
        String theString = "";
        for (int i = start; i < message.size(); i++)
        {
            theString += message.get(i);
        }
        return theString;
    }

    public static String repeat(String s, int times)
    {
        if (times <= 0)
            return "";
        else
            return s + repeat(s, times - 1);
    }

    public static ArrayList<String> split(String str)
    {
        return new ArrayList<String>(Arrays.asList(str.trim().split("\\s+")));
    }

    public static String implode(List<String> list, String glue)
    {
        String ret = "";
        for (int i = 0; i < list.size(); i++)
        {
            if (i != 0)
            {
                ret += glue;
            }
            ret += list.get(i);
        }
        return ret;
    }

    public static String implode(List<String> list)
    {
        return implode(list, " ");
    }

    /*
     * public static String commandHelp(List<String> aliases, String param,
     * String desc) { ArrayList<String> parts = new ArrayList<String>();
     * parts.add(Conf.colorCommand+Conf.aliasBase.get(0));
     * parts.add(TextUtil.implode(aliases, ", ")); if (param.length() > 0) {
     * parts.add(Conf.colorParameter+param); } if (desc.length() > 0) {
     * parts.add(Conf.colorSystem+desc); } //Log.debug(TextUtil.implode(parts,
     * " ")); return TextUtil.implode(parts, " "); }
     */

    public static String getMaterialName(Material material)
    {
        String ret = material.toString();
        ret = ret.replace('_', ' ');
        ret = ret.toLowerCase();
        return ret.substring(0, 1).toUpperCase() + ret.substring(1);
    }

    // / TODO create tag whitelist!!
    public static HashSet<String> substanceChars = new HashSet<String>(Arrays.asList(new String[]
    { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" }));

    public static String getComparisonString(String str)
    {
        String ret = "";

        for (char c : str.toCharArray())
        {
            if (substanceChars.contains(String.valueOf(c)))
            {
                ret += c;
            }
        }

        return ret.toLowerCase();
    }

    public static String colorizeText(String string)
    {
        string = string.replaceAll("&0", ChatColor.BLACK + "");
        string = string.replaceAll("&1", ChatColor.DARK_BLUE + "");
        string = string.replaceAll("&2", ChatColor.DARK_GREEN + "");
        string = string.replaceAll("&3", ChatColor.DARK_AQUA + "");
        string = string.replaceAll("&4", ChatColor.DARK_RED + "");
        string = string.replaceAll("&5", ChatColor.DARK_PURPLE + "");
        string = string.replaceAll("&6", ChatColor.GOLD + "");
        string = string.replaceAll("&7", ChatColor.GRAY + "");
        string = string.replaceAll("&8", ChatColor.DARK_GRAY + "");
        string = string.replaceAll("&9", ChatColor.BLUE + "");
        string = string.replaceAll("&a", ChatColor.GREEN + "");
        string = string.replaceAll("&b", ChatColor.AQUA + "");
        string = string.replaceAll("&c", ChatColor.RED + "");
        string = string.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
        string = string.replaceAll("&e", ChatColor.YELLOW + "");
        string = string.replaceAll("&f", ChatColor.WHITE + "");
        return string;
    }
}
