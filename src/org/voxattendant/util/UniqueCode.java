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

import java.util.Hashtable;

public class UniqueCode
{

    public UniqueCode()
    {
    }

    private static void init()
    {
        letterMap.put("99", "a");
        letterMap.put("98", "b");
        letterMap.put("97", "c");
        letterMap.put("96", "d");
        letterMap.put("95", "e");
        letterMap.put("94", "f");
        letterMap.put("93", "g");
        letterMap.put("92", "h");
        letterMap.put("91", "i");
        letterMap.put("90", "j");
        letterMap.put("89", "k");
        letterMap.put("88", "l");
        letterMap.put("87", "m");
        letterMap.put("86", "n");
        letterMap.put("85", "o");
        letterMap.put("84", "p");
        letterMap.put("83", "q");
        letterMap.put("82", "r");
        letterMap.put("81", "s");
        letterMap.put("80", "t");
        letterMap.put("79", "u");
        letterMap.put("78", "v");
        letterMap.put("77", "w");
        letterMap.put("76", "x");
        letterMap.put("75", "y");
        letterMap.put("74", "z");
        letterMap.put("73", "A");
        letterMap.put("72", "B");
        letterMap.put("71", "C");
        letterMap.put("70", "D");
        letterMap.put("69", "E");
        letterMap.put("68", "F");
        letterMap.put("67", "G");
        letterMap.put("66", "H");
        letterMap.put("65", "I");
        letterMap.put("64", "J");
        letterMap.put("63", "K");
        letterMap.put("62", "L");
        letterMap.put("61", "M");
        letterMap.put("60", "N");
        letterMap.put("59", "O");
        letterMap.put("58", "P");
        letterMap.put("57", "Q");
        letterMap.put("56", "R");
        letterMap.put("55", "S");
        letterMap.put("54", "T");
        letterMap.put("53", "U");
        letterMap.put("52", "V");
        letterMap.put("51", "W");
        letterMap.put("50", "X");
        letterMap.put("49", "Y");
        letterMap.put("48", "Z");
    }

    public static synchronized String genUniqueCode()
    {
        long num = System.currentTimeMillis();
        String s = Long.toString(num);
        int len = s.length();
        StringBuffer sb = new StringBuffer(len);
        if(!initialized)
        {
            init();
            initialized = true;
        }
        try
        {
            for(int i = 0; i < len; i += 2)
            {
                Object value = letterMap.get(s.substring(i, i + 2));
                if(value != null)
                    sb.append(value);
                else
                    sb.append(s.substring(i, i + 2));
            }

        }
        catch(NullPointerException nullpointerexception) { }
        while(num == System.currentTimeMillis()) ;
        return sb.toString();
    }

    private static Hashtable letterMap = new Hashtable();
    private static boolean initialized = false;

}
