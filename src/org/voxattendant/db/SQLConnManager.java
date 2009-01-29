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

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.PropertyLoader;
import org.voxattendant.util.UtilBundle;

public class SQLConnManager
{

    public SQLConnManager(String logMessage, ErrorBean curErrors)
    {
        shutDownList = new Vector();
        this.logMessage = new String();
        conn = null;
        this.curErrors = null;
        this.logMessage = logMessage;
        this.curErrors = curErrors;
    }

    public Connection getConn()
    {
        if(conn != null)
            return conn;
        try
        {
            conn = UtilBundle.getDBUtil().getPoolConnection();
            if(conn == null)
            {
                UtilBundle.log.logMessage("Did NOT get a valid db connection.\n", "getConn:".concat(String.valueOf(String.valueOf(logMessage))), "DatabaseDown", 1);
                curErrors.addError("DatabaseDown", 1);
            }
        }
        catch(SQLException e)
        {
            UtilBundle.log.logMessage(e, "getConn:".concat(String.valueOf(String.valueOf(logMessage))), 1);
            curErrors.addError("DatabaseDown", 1);
            conn = null;
        }
        if(conn != null)
            shutDownList.addElement(conn);
        return conn;
    }

    public static boolean loadStatements(String loadFilename)
    {
        SQLStatements = PropertyLoader.loadProperties(loadFilename);
        return true;
    }

    public void registerPreparedStatement(PreparedStatement stmt)
    {
        if(conn == null)
            UtilBundle.log.logMessage("No valid SQL connection yet", "preparedStatement:".concat(String.valueOf(String.valueOf(logMessage))), "noValidSQLConnection", 1);
        if(stmt != null)
            shutDownList.addElement(stmt);
    }

    public PreparedStatement loadPreparedStatement(String statementName)
    {
        String SQLCode = null;
        if(conn == null)
            UtilBundle.log.logMessage("No valid SQL connection yet", "preparedStatement:".concat(String.valueOf(String.valueOf(logMessage))), "noValidSQLConnection", 1);
        if((SQLCode = SQLStatements.getProperty(statementName)) == null)
            UtilBundle.log.logMessage(String.valueOf(String.valueOf((new StringBuffer("Could not find SQL statement ")).append(statementName).append("; check file: ").append(filename))), "preparedStatement:".concat(String.valueOf(String.valueOf(logMessage))), "missingSQLStatement", 1);
        return preparedStatement(SQLCode);
    }

    public PreparedStatement preparedStatement(String SQLCode)
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement(SQLCode);
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
            UtilBundle.log.logMessage(e, "preparedStatement:".concat(String.valueOf(String.valueOf(logMessage))), 1);
        }
        if(stmt != null)
            shutDownList.addElement(stmt);
        return stmt;
    }

    public ResultSet resultSet(PreparedStatement stmt)
    {
        if(conn == null)
            UtilBundle.log.logMessage("No valid SQL connection yet", "resultSet:".concat(String.valueOf(String.valueOf(logMessage))), "noValidSQLConnection", 1);
        ResultSet rset = null;
        try
        {
            rset = stmt.executeQuery();
        }
        catch(SQLException e)
        {
            UtilBundle.log.logMessage(e, "resultSet:".concat(String.valueOf(String.valueOf(logMessage))), 1);
        }
        return rset;
    }

    public void shutDown(boolean save)
        throws SQLException
    {
        Object tempObj = null;
        if(conn == null)
            UtilBundle.log.logMessage("No valid SQL connection yet", "resultSet:".concat(String.valueOf(String.valueOf(logMessage))), "noValidSQLConnection", 1);
        else
            try
            {
                if(save)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch(SQLException e)
            {
                if(save)
                {
                    UtilBundle.log.logMessage(e, "CommitFailed", 1);
                    curErrors.addError("Commit Failed: ".concat(String.valueOf(String.valueOf(e.getMessage()))), 1);
                    throw new SQLException(e.getMessage(), e.getSQLState(), e.getErrorCode());
                } else
                {
                    UtilBundle.log.logMessage(e, "RollbackFailed", 1);
                    curErrors.addError("Rollback Failed: ".concat(String.valueOf(String.valueOf(e.getMessage()))), 1);
                    throw new SQLException(e.getMessage(), e.getSQLState(), e.getErrorCode());
                }
            }
        for(int i = 0; i < shutDownList.size(); i++)
            try
            {
                tempObj = shutDownList.elementAt(i);
                if(tempObj instanceof Connection)
                {
                    if(tempObj != null)
                        UtilBundle.getDBUtil().free((Connection)tempObj);
                    continue;
                }
                if(tempObj instanceof PreparedStatement)
                {
                    if(tempObj != null)
                        ((PreparedStatement)tempObj).close();
                    continue;
                }
                if(tempObj instanceof ResultSet)
                {
                    if(tempObj != null)
                        ((ResultSet)tempObj).close();
                } else
                {
                    UtilBundle.log.logMessage("Did not know how to kill ".concat(String.valueOf(String.valueOf(tempObj.toString()))), "getConn:".concat(String.valueOf(String.valueOf(logMessage))), "killFailed", 1);
                }
            }
            catch(SQLException e)
            {
                UtilBundle.log.logMessage(e, "CloseFailed", 1);
            }

    }

    public void shutDown()
        throws SQLException
    {
        Object tempObj = null;
        if(conn == null)
        {
            UtilBundle.log.logMessage("No valid SQL connection yet", "resultSet:".concat(String.valueOf(String.valueOf(logMessage))), "noValidSQLConnection", 1);
        } else
        {
            for(int i = 0; i < shutDownList.size(); i++)
                try
                {
                    tempObj = shutDownList.elementAt(i);
                    if(tempObj instanceof Connection)
                    {
                        if(tempObj != null)
                            UtilBundle.getDBUtil().free((Connection)tempObj);
                        continue;
                    }
                    if(tempObj instanceof PreparedStatement)
                    {
                        if(tempObj != null)
                            ((PreparedStatement)tempObj).close();
                        continue;
                    }
                    if(tempObj instanceof ResultSet)
                    {
                        if(tempObj != null)
                            ((ResultSet)tempObj).close();
                    } else
                    {
                        UtilBundle.log.logMessage("Did not know how to kill ".concat(String.valueOf(String.valueOf(tempObj.toString()))), "getConn:".concat(String.valueOf(String.valueOf(logMessage))), "killFailed", 1);
                    }
                }
                catch(SQLException e)
                {
                    UtilBundle.log.logMessage(e, "CloseFailed", 1);
                }

        }
    }

    public static Properties SQLStatements = null;
    private static String filename = null;
    private Vector shutDownList;
    private String logMessage;
    private Connection conn;
    ErrorBean curErrors;

}
