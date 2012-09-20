/*
 * This file is part of OKB3.
 *
 * Copyright (c) 2011-2012, Greatman <http://github.com/greatman/>
 *
 * OKB3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OKB3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OKB3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.extras;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools
{
    public static String regmatch(String pattern, String line)
    {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(line);
        while (m.find())
        {
            allMatches.add(m.group(1));
        }
        return allMatches.get(0);
    }

    public static String SHA256(String stuff) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(stuff.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte element : byteData)
        {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        StringBuffer hexString = new StringBuffer();
        for (byte element : byteData)
        {
            String hex = Integer.toHexString(0xff & element);
            if (hex.length() == 1)
            {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String md5(String input) throws NoSuchAlgorithmException
    {
        String result = input;
        if (input != null)
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            if ((result.length() % 2) != 0)
            {
                result = "0" + result;
            }
        }
        return result;
    }

    public static String SHA1(String input) throws NoSuchAlgorithmException
    {
        String result = input;
        if (input != null)
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            if ((result.length() % 2) != 0)
            {
                result = "0" + result;
            }
        }
        return result;
    }
}
