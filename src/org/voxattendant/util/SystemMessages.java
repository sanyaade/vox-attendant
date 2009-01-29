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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class SystemMessages
{

    public SystemMessages(String filename)
        throws FileNotFoundException, IOException
    {
        messages = PropertyLoader.loadProperties(filename);
    }

    public String generateMessage(String messageName)
    {
        return messages.getProperty(messageName);
    }

    public String generateMessage(String messageName, Hashtable parameters)
    {
        String value = messages.getProperty(messageName);
        if(value == null)
            return null;
        StringBuffer outputString = new StringBuffer();
        int startpos = 0;
        int endpos = 0;
        int len = value.length();
        do
        {
            if((endpos = value.indexOf("%#", startpos)) == -1)
                endpos = len;
            outputString.append(value.substring(startpos, endpos));
            if(endpos == len)
                break;
            startpos = endpos + "%#".length();
            if((endpos = value.indexOf("#%", startpos)) == -1)
            {
                outputString.append(value.substring(startpos, len));
                break;
            }
            String curtoken;
            if((curtoken = (String)parameters.get(value.substring(startpos, endpos))) != null)
                outputString.append(curtoken);
            startpos = endpos + "#%".length();
        } while(true);
        return outputString.toString();
    }

    private Properties messages;
    public static final String DELIM1 = "%#";
    public static final String DELIM2 = "#%";

}
