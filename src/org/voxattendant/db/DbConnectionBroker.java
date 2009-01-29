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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Date;

public class DbConnectionBroker
    implements Runnable
{

    public DbConnectionBroker(String dbDriver, String dbServer, String dbLogin, String dbPassword, int minConns, int maxConns, String logFileString, 
            double maxConnTime)
        throws IOException
    {
        connPool = new Connection[maxConns];
        connStatus = new int[maxConns];
        connLockTime = new long[maxConns];
        connCreateDate = new long[maxConns];
        connID = new String[maxConns];
        currConnections = minConns;
        this.maxConns = maxConns;
        this.dbDriver = dbDriver;
        this.dbServer = dbServer;
        this.dbLogin = dbLogin;
        this.dbPassword = dbPassword;
        maxConnMSec = (int)(maxConnTime * 86400000D);
        if(maxConnMSec < 30000)
            maxConnMSec = 30000;
        try
        {
            log = new PrintStream(new FileOutputStream(logFileString));
        }
        catch(IOException e1)
        {
            now = new Date();
            log = new PrintStream(new FileOutputStream(String.valueOf(String.valueOf((new StringBuffer("DCB_")).append(String.valueOf(now.getTime())).append(".log")))));
        }
        log.println("Starting DbConnectionBroker Version 1.0.7:");
        log.println("dbDriver = ".concat(String.valueOf(String.valueOf(dbDriver))));
        log.println("dbServer = ".concat(String.valueOf(String.valueOf(dbServer))));
        log.println("dbLogin = ".concat(String.valueOf(String.valueOf(dbLogin))));
        log.println("log file = ".concat(String.valueOf(String.valueOf(logFileString))));
        log.println("minconnections = ".concat(String.valueOf(String.valueOf(minConns))));
        log.println("maxconnections = ".concat(String.valueOf(String.valueOf(maxConns))));
        log.println(String.valueOf(String.valueOf((new StringBuffer("Total refresh interval = ")).append(maxConnTime).append(" days"))));
        log.println("-----------------------------------------");
        boolean connectionsSucceeded = false;
        int dbLoop = 20;
        try
        {
            for(int i = 1; i < dbLoop; i++)
            {
                try
                {
                    for(int j = 0; j < currConnections; j++)
                        createConn(log, j);

                    connectionsSucceeded = true;
                    break;
                }
                catch(SQLException e)
                {
                	e.printStackTrace();
                    log.println(String.valueOf(String.valueOf((new StringBuffer("--->Attempt (")).append(String.valueOf(i)).append(" of ").append(String.valueOf(dbLoop)).append(") failed to create new connections set at startup: "))));
                    log.println("    ".concat(String.valueOf(String.valueOf(e))));
                    log.println("    Will try again in 15 seconds...");
                }
                try
                {
                    Thread.sleep(15000L);
                }
                catch(InterruptedException interruptedexception) { }
            }

            if(!connectionsSucceeded)
            {
                log.println("\r\nAll attempts at connecting to Database exhausted");
                throw new IOException();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            throw new IOException();
        }
        if(runner == null)
        {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void run()
    {
        boolean forever = true;
        Statement stmt = null;
        while(forever) 
        {
            for(int i = 0; i < currConnections; i++)
                try
                {
                    currSQLWarning = connPool[i].getWarnings();
                    if(currSQLWarning != null)
                    {
                        log.println(String.valueOf(String.valueOf((new StringBuffer("Warnings on connection ")).append(String.valueOf(i)).append(" ").append(currSQLWarning))));
                        connPool[i].clearWarnings();
                    }
                }
                catch(SQLException e)
                {
                    log.println("Cannot access Warnings: ".concat(String.valueOf(String.valueOf(e))));
                }

            for(int i = 0; i < currConnections; i++)
            {
                Date now = new Date();
                long lnow = now.getTime();
                long age = lnow - connCreateDate[i];
                synchronized(connStatus)
                {
                    if(connStatus[i] > 0)
                        continue;
                    connStatus[i] = 2;
                }
                try
                {
                    try
                    {
                        if(age > (long)maxConnMSec)
                            throw new SQLException();
                        stmt = connPool[i].createStatement();
                        connStatus[i] = 0;
                        continue;
                    }
                    catch(SQLException e) { }
                    try
                    {
                        log.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(now.toLocaleString())))).append(" ***** Recycling connection ").append(String.valueOf(i)).append(":"))));
                        connPool[i].close();
                        createConn(log, i);
                    }
                    catch(SQLException e1)
                    {
                        log.println("Failed: ".concat(String.valueOf(String.valueOf(e1))));
                        connStatus[i] = 0;
                    }
                    continue;
                }
                finally
                {
                    try
                    {
                        if(stmt != null)
                            stmt.close();
                    }
                    catch(SQLException sqlexception) { }
                }
            }

            try
            {
                Thread.sleep(10000L);
            }
            catch(InterruptedException interruptedexception) { }
        }
    }

    public Connection getConnection()
    {
        Connection conn = null;
        boolean gotOne = false;
        int outerloop = 1;
        do
        {
            if(outerloop > 10)
                break;
            try
            {
                int loop = 0;
                int roundRobin = connLast + 1;
                if(roundRobin >= currConnections)
                    roundRobin = 0;
                do
                    synchronized(connStatus)
                    {
                        if(connStatus[roundRobin] < 1)
                        {
                            if(!connPool[roundRobin].isClosed())
                            {
                                conn = connPool[roundRobin];
                                connStatus[roundRobin] = 1;
                                Date now = new Date();
                                connLockTime[roundRobin] = now.getTime();
                                connLast = roundRobin;
                                gotOne = true;
                                break;
                            }
                        } else
                        {
                            loop++;
                            if(++roundRobin >= currConnections)
                                roundRobin = 0;
                        }
                    }
                while(!gotOne && loop < currConnections);
            }
            catch(SQLException sqlexception) { }
            if(gotOne)
                break;
            synchronized(this)
            {
                if(currConnections < maxConns)
                {
                    try
                    {
                        createConn(log, currConnections);
                    }
                    catch(SQLException e)
                    {
                        log.println("Unable to create new connection: ".concat(String.valueOf(String.valueOf(e))));
                    }
                    currConnections++;
                }
            }
            try
            {
                Thread.sleep(2000L);
            }
            catch(InterruptedException interruptedexception) { }
            log.println("-----> Connections Exhausted!  Will wait and try again in loop ".concat(String.valueOf(String.valueOf(String.valueOf(outerloop)))));
            outerloop++;
        } while(true);
        return conn;
    }

    public int idOfConnection(Connection conn)
    {
        String tag;
        try
        {
            tag = conn.toString();
        }
        catch(NullPointerException e1)
        {
            tag = "none";
        }
        int match = -1;
        int i = 0;
        do
        {
            if(i >= currConnections)
                break;
            if(connID[i].equals(tag))
            {
                match = i;
                break;
            }
            i++;
        } while(true);
        return match;
    }

    public String freeConnection(Connection conn)
    {
        String res = "";
        int thisconn = idOfConnection(conn);
        if(thisconn >= 0)
        {
            connStatus[thisconn] = 0;
            res = "freed ".concat(String.valueOf(String.valueOf(conn.toString())));
        } else
        {
            log.println("----> Could not free connection!!!");
        }
        return res;
    }

    public long getAge(Connection conn)
    {
        Date now = new Date();
        long lnow = now.getTime();
        int thisconn = idOfConnection(conn);
        long diff = lnow - connLockTime[thisconn];
        return diff;
    }

    private void createConn(PrintStream out, int i)
        throws SQLException
    {
        Date now = new Date();
        try
        {
            Class.forName(dbDriver);
            connPool[i] = DriverManager.getConnection(dbServer, dbLogin, dbPassword);
            connStatus[i] = 0;
            connID[i] = connPool[i].toString();
            connLockTime[i] = 0L;
            now = new Date();
            connCreateDate[i] = now.getTime();
        }
        catch(ClassNotFoundException e2)
        {
            e2.printStackTrace(System.err);
        }
        log.println(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(now.toLocaleString())))).append("  Opening connection ").append(String.valueOf(i)).append(" ").append(connPool[i].toString()).append(":"))));
    }

    public void destroy()
    {
        if(runner != null)
            runner.stop();
        for(int i = 0; i < currConnections; i++)
            try
            {
                connPool[i].close();
                connPool[i] = null;
            }
            catch(SQLException e1)
            {
                log.println("Cannot close connections on Destroy");
            }

    }

    private Thread runner;
    Connection connPool[];
    int connStatus[];
    Date now;
    long connLockTime[];
    long connCreateDate[];
    String connID[];
    String dbDriver;
    String dbServer;
    String dbLogin;
    String dbPassword;
    String logFileString;
    int currConnections;
    int connLast;
    int minConns;
    int maxConns;
    int maxConnMSec;
    PrintStream log;
    SQLWarning currSQLWarning;
}
