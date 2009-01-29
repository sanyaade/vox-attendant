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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.UtilBundle;

public class SQLUtil
{

    public SQLUtil()
    {
    }

    public static int getNextSequenceNumber(String seqName, ErrorBean curErrors, Connection conn)
    {
        int qid = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(String.valueOf(String.valueOf((new StringBuffer("SELECT ")).append(seqName).append(".NEXTVAL ").append("FROM dual "))));
            if(rset.next())
                qid = rset.getInt(1);
            rset.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            curErrors.addError("CommunityDBError", 1);
        }
        try
        {
            stmt.close();
        }
        catch(SQLException e)
        {
            UtilBundle.log.logMessage(e, "CloseFailed", 1);
        }
        return qid;
    }

    public static int getCurrSequenceNumber(String seqName, ErrorBean curErrors, Connection conn)
    {
        int qid = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(String.valueOf(String.valueOf((new StringBuffer("SELECT ")).append(seqName).append(".CURRVAL ").append("FROM dual "))));
            if(rset.next())
                qid = rset.getInt(1);
            rset.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            curErrors.addError("CommunityDBError", 1);
        }
        try
        {
            stmt.close();
        }
        catch(SQLException e)
        {
            UtilBundle.log.logMessage(e, "CloseFailed", 1);
        }
        return qid;
    }
}
