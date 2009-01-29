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

import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.voxattendant.VoxAttendant;
import org.voxattendant.db.DBUtil;
import org.voxattendant.db.SQLConnManager;

public class UtilBundle
{
    private static String webRootPath;
    public static Date startDate = new Date();
    public static GregorianCalendar cal = new GregorianCalendar();
    public static LogUtil log = null;
    public static LogUtil debug = null;
    public static PrintWriter out = null;
    private static DBUtil db = null;
    public static Properties config = null;
    public static String configFileName = null;
    public static boolean loadedSqlText = false;
    public static boolean DEBUG;

    public static void setWebRootPath(String webRootPath) {
    	UtilBundle.webRootPath = webRootPath;
    }
    
    public static DBUtil getDBUtil() {
    	if (db == null) {
    		try {
				initUtilBundle(webRootPath);
			} catch (InitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return db;
    }

    public static void initUtilBundle(String webRootPath)
        throws InitializationException
    {

    	if (config == null) {
    		config = VoxAttendant.getProps();
    	}
        if(log == null)
        {
            if(DEBUG)
                System.err.println("log");
            String jspLogFileName = config.getProperty("file.jspLogFileName");
            log = new LogUtil(jspLogFileName);
            out = log.out;
        }
        if(debug == null)
        {
            if(DEBUG)
                System.err.println("debug");
            String maxSeverityStr = config.getProperty("file.debugMaxSeverity");
            int maxSeverity = 0;
            if(maxSeverityStr == null || maxSeverityStr.equals(""))
                maxSeverity = 5;
            else
                try
                {
                    maxSeverity = Integer.parseInt(maxSeverityStr);
                }
                catch(NumberFormatException e)
                {
                    maxSeverity = 5;
                }
            String servletLogFileName = config.getProperty("file.servletLogFileName");
            debug = new LogUtil(servletLogFileName, maxSeverity);
        }
        boolean initDb = config.getProperty("initDb") == null || (new Boolean(config.getProperty("initDb"))).booleanValue();
        if(DEBUG)
            System.err.println("initDb: ".concat(String.valueOf(String.valueOf(initDb))));
        if(initDb && !loadedSqlText)
        {
            if(DEBUG)
                System.err.println("sqltext");
            String sqlTextFileName = config.getProperty("file.sqlTextFileName");
            loadedSqlText = SQLConnManager.loadStatements(sqlTextFileName);
        }
        if(initDb && db == null)
        {
            if(DEBUG)
                System.err.println("db");
            db = new DBUtil( webRootPath);
        }
    }


}
