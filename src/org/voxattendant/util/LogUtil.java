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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;

public class LogUtil
{

    public LogUtil(String filename)
        throws InitializationException
    {
        maxSeverity = 5;
        try
        {
            out = new PrintWriter(new FileWriter(filename, true), true);
        }
        catch(IOException e)
        {
            throw new InitializationException("Could not open logfile: ".concat(String.valueOf(String.valueOf(filename))));
        }
    }

    public LogUtil(String filename, int severity)
        throws InitializationException
    {
        maxSeverity = 5;
        try
        {
            maxSeverity = severity;
            out = new PrintWriter(new FileWriter(filename, true), true);
        }
        catch(IOException e)
        {
            throw new InitializationException("Could not open logfile: ".concat(String.valueOf(String.valueOf(filename))));
        }
    }

    public void logMessage(String logMessage, String location, String identifier, String errorID, int severity)
    {
        try
        {
            out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(new Date())))).append("|%").append(location).append("|%").append(identifier).append("|%").append(severity).append("|%").append(logMessage).append("|%").append(errorID).append("|^"))));
        }
        catch(Exception exception) { }
    }

    public void logMessage(String logMessage, String location, String identifier, int severity)
    {
        try
        {
            out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(new Date())))).append("|%").append(location).append("|%").append(identifier).append("|%").append(severity).append("|%").append(logMessage).append("|%").append("").append("|^"))));
        }
        catch(Exception exception) { }
    }

    public void logMessage(SQLException except, String identifier, int severity)
    {
        try
        {
            out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(new Date())))).append("|%").append("|%").append(identifier).append("|%").append(severity).append("|%"))));
            except.printStackTrace(out);
            out.println("|%");
            out.println("|^");
        }
        catch(Exception exception) { }
    }

    public void logMessage(Exception except, String identifier, int severity)
    {
        try
        {
            out.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(new Date())))).append("|%").append("|%").append(identifier).append("|%").append(severity).append("|%"))));
            except.printStackTrace(out);
            out.println("|%");
            out.println("|^");
        }
        catch(Exception exception) { }
    }

    public void print(String message, int severity)
    {
        if(severity <= maxSeverity)
            try
            {
                out.print(message);
            }
            catch(Exception exception) { }
    }

    public void print(String message)
    {
        print(message, 1);
    }

    public void println(String message, int severity)
    {
        if(severity <= maxSeverity)
            try
            {
                out.println(message);
            }
            catch(Exception exception) { }
    }

    public void println(String message)
    {
        println(message, 1);
    }

    public PrintWriter out;
    public static final String fieldDelimiter = "|%";
    public static final String recordDelimiter = "|^";
    public int maxSeverity;
    public static final int SEVERE = 1;
    public static final int MEDIUM = 2;
    public static final int INFO = 3;
    public static final int TESTING = 4;
    public static final int GENERIC = 5;

}
