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

package org.voxattendant.db;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class JDBCGlobalBroker
{

    public JDBCGlobalBroker(String dbDriver, String dbServer, String dbLogin, String dbPassword, int minConns, int maxConns, String logFileString, 
            double maxConnTime)
        throws IOException
    {
        if(mainBroker == null)
            mainBroker = new DbConnectionBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns, maxConns, logFileString, maxConnTime);
    }

    public JDBCGlobalBroker(Properties p, String webContextPath)
        throws IOException
    {
        if(mainBroker == null)
        {
            String dbDriver = (String)p.get("dbDriver");
            String dbServer = "";
            if (dbDriver.equalsIgnoreCase("SQLite.JDBCDriver")) {
            	dbServer = "jdbc:sqlite:/" + webContextPath + (String)p.get("dbFileLocation");
            	dbServer = dbServer.replaceAll("\\\\","/");
            } else if (dbDriver.equalsIgnoreCase("org.hsqldb.jdbcDriver")) {
            	dbServer = "jdbc:hsqldb:file:"+webContextPath+"/WEB-INF/db/voxattendant.hsql";
            } else {
            	dbServer = (String)p.get("dbServer");
            }
            String dbLogin = (String)p.get("dbLogin");
            String dbPassword = (String)p.get("dbPassword");
            int minConns = Integer.parseInt((String)p.get("minConns"));
            int maxConns = Integer.parseInt((String)p.get("maxConns"));
            String logFileString = (String)p.get("file.dbLogFileName");
            System.out.println("ID: CONBROKER log file: ".concat(String.valueOf(String.valueOf(logFileString))));
            double maxConnTime = (new Double((String)p.get("maxConnTime"))).doubleValue();
            mainBroker = new DbConnectionBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns, maxConns, logFileString, maxConnTime);
        }
    }

    public Connection getConnection()
    {
        return mainBroker.getConnection();
    }

    public void freeConnection(Connection conn)
    {
        mainBroker.freeConnection(conn);
    }

    public int idOfConnection(Connection conn)
    {
        return mainBroker.idOfConnection(conn);
    }

    public void destroy()
    {
        //JCB
        mainBroker.destroy();
    }

    private static DbConnectionBroker mainBroker = null;

}
