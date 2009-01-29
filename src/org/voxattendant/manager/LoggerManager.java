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

package org.voxattendant.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import org.voxattendant.db.SQLConnManager;
import org.voxattendant.model.CancellationBean;
import org.voxattendant.model.LoggerBean;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.LogUtil;
import org.voxattendant.util.UtilBundle;


/**
 * This Manager class handles all SQL requests related to the Logger and
 * Cancellation tables.
 *
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2007-02-24 15:50:52 $
 *
 */
public class LoggerManager {
   /**
    * Inserts a log entry into Logger and/or Cancellation tables.
    *
    * @param loggerBean The bean that contains the collected information.
    * @return boolean true if successful, otherwise false.
    */
   public static boolean insertLog(LoggerBean loggerBean) {

      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         String statementName = "insertLog";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setTimestamp(1, new java.sql.Timestamp(loggerBean.getStartTimestamp()));
               stmt.setTimestamp(2, new java.sql.Timestamp(loggerBean.getEndTimestamp()));
               stmt.setString(3, loggerBean.getCallerId());
               stmt.setInt(4, loggerBean.getRequestedId());
               stmt.setInt(5, loggerBean.getOwnerTypeId());
               stmt.setInt(6, loggerBean.getAppInfoBits());
               stmt.execute();

               Vector cancellations = loggerBean.getCancellations();
               if(cancellations.size() > 0) {

                  // First, get the Id back
                  statementName = "lookupLastInsertId";
                  stmt = connMgr.loadPreparedStatement(statementName);
                  rset = stmt.executeQuery();
                  // Then try to insert cancellation ids
                  if(rset.next()) {
                     int loggerId = rset.getInt(1);

                     statementName = "insertCancellation";
                     stmt = connMgr.loadPreparedStatement(statementName);

                     for(int i = 0; i < cancellations.size(); i++) {
                        CancellationBean cancellation = (CancellationBean)cancellations.get(i);

                        stmt.setInt(1, loggerId);
                        stmt.setInt(2, cancellation.getCancelledId());
                        stmt.setInt(3, cancellation.getOwnerTypeId());
                        stmt.execute();
                     }
                  }
               }
            }
         }
         catch (Exception e) {
            good = false;
            UtilBundle.log.logMessage(e, statementName, LogUtil.SEVERE);
         }
         // Free resources allocated.
         connMgr.shutDown(true);
      }
      catch (Exception e) {
         good=false;
         e.printStackTrace();
      }
      return good;
   }
   /**
    *
    */
   public static boolean createTables() {

		boolean result = true;

		try {
			SQLConnManager connMgr = new SQLConnManager("createTables", new ErrorBean());
			PreparedStatement stmt = null;
			try {
				if ((connMgr.getConn()) == null) {
					result = false;
				}
				else {
               String statementName = "createLoggerTable";
               stmt = connMgr.loadPreparedStatement(statementName);
					stmt.execute();

               statementName = "createCancellationTable";
               stmt = connMgr.loadPreparedStatement(statementName);
					stmt.execute();
				}
			}
			catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			connMgr.shutDown(true);
		}
		catch (Exception e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
}
