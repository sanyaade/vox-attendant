/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is vox-mail.
 *
 * The Initial Developer of the Original Code is Voxeo Corporation.
 * Portions created by Voxeo are Copyright (C) 2000-2007.
 * All rights reserved.
 * 
 * Contributor(s):
 * ICOA Inc. <info@icoa.com> (http://icoa.com)
 */

package org.voxattendant.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class Text
{

    public Text()
    {
    }

    public static String stripString(String str)
    {
        String invalidChars = " _-!@#$%^&*()+=\\|?/'\",.<>`~:;";
        return stripString(str, invalidChars);
    }

    public static String stripString(String str, String invalidChars)
    {
        StringBuffer newStr = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
            if(!Validator.validChar(str.charAt(i), invalidChars))
                newStr.append(str.charAt(i));

        return newStr.toString();
    }

    public static String HtmlEncode(String str)
    {
        String invalidChars = "_-!@#$%^&*()+[]=\\|?/'\",.<>`~:;";
        StringBuffer newStr = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
            if(Validator.validChar(str.charAt(i), invalidChars))
                newStr.append(String.valueOf(String.valueOf((new StringBuffer("&#x")).append(Integer.toHexString(str.charAt(i))).append(";"))));
            else
                newStr.append(str.charAt(i));

        return newStr.toString();
    }

    public static String quotizeListString(String str)
    {
        if(str != null)
        {
            StringBuffer sb = new StringBuffer();
            StringTokenizer st = new StringTokenizer(str, ", ");
            int i = st.countTokens();
            while(st.hasMoreTokens()) 
            {
                sb.append("\"".concat(String.valueOf(String.valueOf(st.nextToken()))));
                if(--i > 0)
                    sb.append("\", ");
                else
                    sb.append("\"");
            }
            return sb.toString();
        } else
        {
            return "\"\"";
        }
    }

    public static List getListFromString(String str)
    {
        List l = new Vector();
        if(str != null)
        {
            StringTokenizer st = new StringTokenizer(str, ", ");
            for(; st.hasMoreTokens(); l.add(st.nextToken()));
        }
        return l;
    }

    public static List tokenizeEmailAddresses(String emailAddresses)
    {
        if(emailAddresses == null)
            return null;
        StringTokenizer thisToken = new StringTokenizer(emailAddresses, "\n \t\r!\"#$%&'()*+,/:;<=>?");
        List emailList = new Vector();
        try
        {
            for(; thisToken.hasMoreTokens(); emailList.add(thisToken.nextToken()));
        }
        catch(Exception exception) { }
        return emailList;
    }

    public static String escapeCharactersInString(String str, char charToEscape, char escapeChar)
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == charToEscape)
            {
                sb.append(escapeChar);
                sb.append(str.charAt(i));
            } else
            {
                sb.append(str.charAt(i));
            }

        return sb.toString();
    }

    public static String getDigitsFromString(String str)
    {
        if(str == null)
            return null;
        StringBuffer sb = new StringBuffer();
        int digit = -1;
        for(int i = 0; i < str.length(); i++)
        {
            digit = Character.digit(str.charAt(i), 10);
            if(digit > -1 && digit < 10)
                sb.append(digit);
        }

        if(sb.length() > 0)
            return sb.toString();
        else
            return null;
    }

    public static int parseDollarsToInt(String s)
    {
        byte byte0;
        try
        {
            char chars[] = new char[s.length()];
            s.getChars(0, s.length(), chars, 0);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < chars.length && chars[i] != '.'; i++)
                if(chars[i] != ',' && chars[i] != '$')
                    sb.append(chars[i]);

            int j = Integer.parseInt(sb.toString());
            return j;
        }
        catch(NumberFormatException e)
        {
            byte0 = -1;
        }
        return byte0;
    }

    public static String formatStr(String str, String format)
    {
        char formatIdentifier = '#';
        return formatStr(str, format, formatIdentifier);
    }

    public static String formatStr(String str, String format, char formatIdentifier)
    {
        if(str != null && !str.equals(""))
        {
            StringBuffer sb = new StringBuffer();
            int inStrIndex = 0;
            for(int i = 0; i < format.length(); i++)
                if(format.charAt(i) == formatIdentifier)
                {
                    if(inStrIndex >= str.length())
                        break;
                    sb.append(str.charAt(inStrIndex));
                    inStrIndex++;
                } else
                {
                    sb.append(format.charAt(i));
                }

            if(inStrIndex < str.length())
                sb.append(str.substring(inStrIndex));
            return sb.toString();
        } else
        {
            return str;
        }
    }

    public static String getCreditCardType(String creditCardNumber)
    {
        if(creditCardNumber == null || creditCardNumber.equals(""))
            return null;
        HashMap creditCardPrefixes = new HashMap();
        creditCardPrefixes.put("visa", new String[] {
            "4"
        });
        creditCardPrefixes.put("mastercard", new String[] {
            "51", "52", "53", "54", "55"
        });
        creditCardPrefixes.put("amex", new String[] {
            "34", "37"
        });
        creditCardPrefixes.put("discover", new String[] {
            "6011"
        });
        for(Iterator creditCardPrefixIterator = creditCardPrefixes.keySet().iterator(); creditCardPrefixIterator.hasNext();)
        {
            String creditCardType = (String)creditCardPrefixIterator.next();
            String creditCardPrefixesArray[] = (String[])creditCardPrefixes.get(creditCardType);
            int i = 0;
            while(i < creditCardPrefixesArray.length) 
            {
                if(creditCardNumber.startsWith(creditCardPrefixesArray[i]))
                    return creditCardType;
                i++;
            }
        }

        return null;
    }

    public static String getDateInWeeksDays(int year, int month, int date)
    {
        return getDateInWeeksDays(year, month, date, null);
    }

    public static String getDateInWeeksDays(int year, int month, int date, String format)
    {
        char formatIdentifier = '#';
        long newTime = System.currentTimeMillis();
        Calendar cal = new GregorianCalendar(year, month - 1, date);
        long oldTime = cal.getTime().getTime();
        long timeDiff = newTime - oldTime;
        long weeks = timeDiff / 0x240c8400L;
        long days = (timeDiff % 0x240c8400L) / 0x5265c00L;
        StringBuffer sb = new StringBuffer();
        if(format != null && format.indexOf(formatIdentifier) > -1)
        {
            boolean insertedWeeks = false;
            boolean insertedDays = false;
            for(int i = 0; i < format.length(); i++)
                if(format.charAt(i) == formatIdentifier)
                {
                    if(!insertedWeeks)
                    {
                        sb.append(weeks);
                        insertedWeeks = true;
                    } else
                    if(!insertedDays)
                    {
                        sb.append(days);
                        insertedDays = true;
                    } else
                    {
                        sb.append(format.charAt(i));
                    }
                } else
                {
                    sb.append(format.charAt(i));
                }

        } else
        {
            if(weeks > (long)0)
                sb.append(String.valueOf(String.valueOf(weeks)).concat(" weeks"));
            if(weeks > (long)0 && days > (long)0)
                sb.append(", ");
            if(days > (long)0)
                sb.append(String.valueOf(String.valueOf(days)).concat(" days"));
        }
        return sb.toString();
    }

    public static String replaceStringsInString(String str, String strToReplace, String replaceStr)
    {
        if(str == null || strToReplace == null || replaceStr == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for(int replaceIndex = -1; (replaceIndex = str.indexOf(strToReplace)) > -1;)
        {
            sb.append(String.valueOf(str.substring(0, replaceIndex)) + String.valueOf(replaceStr));
            str = str.substring(replaceIndex + strToReplace.length());
        }

        sb.append(str);
        return sb.toString();
    }

    public static String replaceCharactersInString(String str, char charToReplace, String replaceStr)
    {
        if(str == null || replaceStr == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == charToReplace)
                sb.append(replaceStr);
            else
                sb.append(str.charAt(i));

        return sb.toString();
    }

    public static final int SECOND = 1000;
    public static final int MINUTE = 60000;
    public static final int HOUR = 0x36ee80;
    public static final long DAY = 0x5265c00L;
    public static final long WEEK = 0x240c8400L;

}
