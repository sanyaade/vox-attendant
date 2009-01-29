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
import java.util.Date;
import java.util.GregorianCalendar;

public class Validator
{

    public Validator()
    {
    }

    public static boolean validEmail(String emailAddress)
    {
        int atCount = 0;
        String validAlphaNumChars = "abcdefghijklmnopqurstuvwxyz1234567890@-_.";
        String validAlphaChars = "abcdefghijklmnopqurstuvwxyz";
        if(emailAddress.length() > 256)
            return false;
        if(emailAddress.indexOf(".") < 0 || emailAddress.indexOf('@') < 0)
            return false;
        if(emailAddress.indexOf('.') == 0 || emailAddress.indexOf('@') == 0)
            return false;
        if(emailAddress.lastIndexOf('.') == emailAddress.length() || emailAddress.lastIndexOf('@') == emailAddress.length())
            return false;
        if(emailAddress.indexOf('@') > 64)
            return false;
        if(emailAddress.length() - emailAddress.indexOf('@') > 192)
            return false;
        if(emailAddress.lastIndexOf('.') + 3 > emailAddress.length())
            return false;
        if(emailAddress.lastIndexOf('.') + 4 < emailAddress.length())
            return false;
        for(int i = 0; i < emailAddress.length(); i++)
        {
            if(!validChar(emailAddress.charAt(i), validAlphaNumChars))
                return false;
            if(emailAddress.charAt(i) == '@')
                atCount++;
            if(atCount > 1)
                return false;
            if(emailAddress.charAt(i) == '.' && (i > 0 && emailAddress.charAt(i + 1) == '.' || i > 0 && emailAddress.charAt(i - 1) == '.'))
                return false;
        }

        return validStr(emailAddress.substring(emailAddress.lastIndexOf(".") + 1), validAlphaChars);
    }

    public static boolean validChar(char chr, String list)
    {
        chr = Character.toLowerCase(chr);
        list = list.toLowerCase();
        return list.indexOf(chr) > -1;
    }

    public static boolean validStr(String str, String list)
    {
        boolean isOK = true;
        for(int i = 0; i < str.length(); i++)
            if(!validChar(str.charAt(i), list))
                isOK = false;

        return isOK;
    }

    public static boolean validPhone(String str)
    {
        if((str = Text.getDigitsFromString(str)) == null)
            return false;
        if(str.length() < 10)
            return false;
        int firstDigit = Character.digit(str.charAt(0), 10);
        int secondDigit = Character.digit(str.charAt(1), 10);
        return str.length() == 10 && firstDigit > 1 || str.length() == 11 && firstDigit == 1 && secondDigit > 1;
    }

    public static boolean validCreditCard(String ccNum)
    {
        boolean good = false;
        String ccPrefixes[] = {
            "51", "52", "53", "54", "55", "4", "34", "37", "6011"
        };
        if((ccNum = Text.getDigitsFromString(ccNum)) == null)
            return false;
        if(ccNum.length() > 16 || ccNum.length() < 13)
            return false;
        int i = 0;
        do
        {
            if(i >= ccPrefixes.length)
                break;
            if(ccNum.startsWith(ccPrefixes[i]))
            {
                good = true;
                break;
            }
            i++;
        } while(true);
        if(!good)
            return false;
        int sum = 0;
        int weight = (ccNum.length() - 1) % 2 + 1;
        for(i = 0; i < ccNum.length(); i++)
        {
            int curDigit = Character.digit(ccNum.charAt(i), 10);
            int product = curDigit * weight;
            sum += product <= 9 ? product : product - 9;
            weight = weight % 2 + 1;
        }

        return sum % 10 == 0;
    }

    public static boolean containsOnlyLetters(String s)
    {
        char chars[] = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        for(int i = 0; i < s.length(); i++)
            if(!Character.isLetter(chars[i]))
                return false;

        return true;
    }

    public static boolean containsOnlyLettersWhitespace(String s)
    {
        char chars[] = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        for(int i = 0; i < s.length(); i++)
            if(!Character.isLetter(chars[i]) && !Character.isWhitespace(chars[i]))
                return false;

        return true;
    }

    public static boolean containsOnlyLettersDigitsWhitespace(String s)
    {
        char chars[] = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        for(int i = 0; i < s.length(); i++)
            if(!Character.isLetterOrDigit(chars[i]) && !Character.isWhitespace(chars[i]))
                return false;

        return true;
    }

    public static boolean containsOnlyLettersDigitsUnderscores(String s)
    {
        char chars[] = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        for(int i = 0; i < s.length(); i++)
            if(!Character.isLetterOrDigit(chars[i]) && chars[i] != '_')
                return false;

        return true;
    }

    public static boolean containsOnlyDigits(String s)
    {
        char chars[] = new char[s.length()];
        s.getChars(0, s.length(), chars, 0);
        for(int i = 0; i < s.length(); i++)
            if(!Character.isDigit(chars[i]))
                return false;

        return true;
    }

    public static boolean validDate(String ddStr, String mmStr, String yyyyStr)
    {
        return validDate(ddStr, mmStr, yyyyStr, 0, false);
    }

    public static boolean validDate(String ddStr, String mmStr, String yyyyStr, int yearLowerLimit, boolean notFuture)
    {
        if(old == null)
        {
            old = new GregorianCalendar();
            old.set(yearLowerLimit, 0, 1);
            entered.setLenient(false);
        }
        today.setTime(new Date());
        try
        {
            int dd = Integer.parseInt(ddStr);
            int mm = Integer.parseInt(mmStr);
            int year = Integer.parseInt(yyyyStr);
            entered.set(year, mm - 1, dd);
            if(notFuture && entered.after(today))
            {
                boolean flag1 = false;
                return flag1;
            }
            if(entered.before(old))
            {
                boolean flag2 = false;
                return flag2;
            }
        }
        catch(IllegalArgumentException e)
        {
            boolean flag = false;
            return flag;
        }
        return true;
    }

    public static boolean containsPOBox(String address)
    {
        return address.startsWith("PO") || address.startsWith("po") || address.startsWith("P.O") || address.startsWith("p.o") || address.startsWith("BOX") || address.startsWith("Box") || address.startsWith("box");
    }

    public static boolean validDayAndMonth(String dayAndMonth)
    {
        int seperator;
        if((seperator = dayAndMonth.indexOf("/")) == -1)
            return false;
        int days[] = {
            0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 
            31, 30, 31
        };
        try
        {
            int month = Integer.parseInt(dayAndMonth.substring(0, seperator));
            int day = Integer.parseInt(dayAndMonth.substring(seperator + 1, dayAndMonth.length()));
            if(day > 31 || day < 1)
            {
                boolean flag1 = false;
                return flag1;
            }
            if(month > 12 || day < 1)
            {
                boolean flag2 = false;
                return flag2;
            }
            if(day > days[month])
            {
                boolean flag3 = false;
                return flag3;
            }
        }
        catch(NumberFormatException e)
        {
            boolean flag = false;
            return flag;
        }
        return true;
    }

    public static boolean validURL(String str)
    {
        if(str == null)
            return false;
        str = str.toLowerCase();
        if(!str.startsWith("http://") && !str.startsWith("ftp://") && !str.startsWith("gopher://") && !str.startsWith("mailto://") && !str.startsWith("news://") && !str.startsWith("nntp://") && !str.startsWith("telnet://") && !str.startsWith("wais://") && !str.startsWith("file://") && !str.startsWith("prospero://"))
            return false;
        if(str.endsWith("://"))
            return false;
        String validURLCharacters = "abcdefghijklmnopqurstuvwxyz1234567890%#~/?:@=&$-_.,+!*'()";
        return validStr(str, validURLCharacters);
    }

    public static Calendar entered = new GregorianCalendar();
    public static Calendar today = new GregorianCalendar();
    public static Calendar old = null;

}
