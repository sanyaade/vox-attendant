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
import java.sql.SQLException;

import org.voxattendant.VoxAttendant;
import org.voxattendant.util.InitializationException;


public class DBUtil
{

    public DBUtil(String webContextPath)
        throws InitializationException
    {
        dbUserName = new String();
        dbPassWord = new String();
        dbName = new String();
        dbHostName = new String();
        dbPortNumber = new String();
        try
        {
            myGlobalBroker = new JDBCGlobalBroker(VoxAttendant.getProps(), webContextPath);
        }
        catch(IOException e)
        {
            System.err.println("Caught an IOException in DBUtil constructor function");
            e.printStackTrace(System.err);
        }
    }

    public Connection getPoolConnection()
        throws SQLException
    {
        Connection conn = myGlobalBroker.getConnection();
        if(conn != null)
            conn.setAutoCommit(false);
        return conn;
    }

    public void free(Connection conn)
        throws SQLException
    {
        myGlobalBroker.freeConnection(conn);
    }
    
    public void destroyPool()
    {
        myGlobalBroker.destroy();
    }
    

    public String dbUserName;
    public String dbPassWord;
    public String dbName;
    public String dbHostName;
    public String dbPortNumber;
    JDBCGlobalBroker myGlobalBroker;
}
