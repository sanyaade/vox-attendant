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

import org.voxattendant.db.SQLConnManager;
import org.voxattendant.model.AltSpellingBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.LogUtil;
import org.voxattendant.util.UtilBundle;

public class AltSpellingManager {
   /**
    *
    */
   public static boolean lookupAltSpellingByOwner(int ownerId, int ownerTypeId, BeanCollection altSpellingBeanList) {

      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         // lookup the first part of the application infomation
         String statementName = "lookupAltSpellingByOwner";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               // Load up the correct SQL statement
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, ownerId);
               stmt.setInt(2, ownerTypeId);
               rset = stmt.executeQuery();

               while(rset.next()) {
                  AltSpellingBean altSpellingBean = new AltSpellingBean();
                  altSpellingBean.setOwnerId(ownerId);
                  altSpellingBean.setOwnerTypeId(ownerTypeId);

                  altSpellingBean.setAltSpellingId(rset.getInt(1));
                  altSpellingBean.setSpelling(rset.getString(2));
                  altSpellingBean.setAltspellingTypeId(rset.getInt(3));
                  ////
                  //System.out.println("\t========= AltSpellingBean ==========");
                  //System.out.println("\tAltSpellingId:" + altSpellingBean.getAltSpellingId());
                  //System.out.println("\tOwnerId:" + altSpellingBean.getOwnerId());
                  //System.out.println("\tSpelling #1:" + altSpellingBean.getSpelling());
                  //System.out.println("\tAltspellingTypeId:" + altSpellingBean.getAltspellingTypeId());
                  //System.out.println("\t====================================");
                  ////
                  altSpellingBeanList.addItem(altSpellingBean);
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
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    *
    */
   public static boolean insertAltSpelling(AltSpellingBean altSpellingBean) {

      boolean good = true;

      try {
         String statementName = "insertAltSpelling";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
              good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               ////
               System.out.println("========== Inserting AltSpellingBean =========");
               System.out.println("\tOwner Id:" + altSpellingBean.getOwnerId());
               System.out.println("\tOwner Type Id:" + altSpellingBean.getOwnerTypeId());
               System.out.println("\tSpelling:" + altSpellingBean.getSpelling());
               System.out.println("\tAltspellingTypeId:" + altSpellingBean.getAltspellingTypeId());
               System.out.println("================================================");
               ////
               stmt.setInt( 1, altSpellingBean.getOwnerId());
               stmt.setInt( 2, altSpellingBean.getOwnerTypeId());
               stmt.setString( 3, altSpellingBean.getSpelling());
               stmt.setInt( 4, altSpellingBean.getAltspellingTypeId());
               stmt.executeUpdate();
            }
         }
         catch (Exception e) {
            good=false;
            UtilBundle.log.logMessage(e, statementName, LogUtil.SEVERE);
         }
         // Free resources allocated.
         connMgr.shutDown(true);
      }
      catch (Exception e) {
         good = false;
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    *
    */
   public static boolean deleteAltSpelling(AltSpellingBean altSpellingBean) {

        boolean good = true;

        try {
            String statementName = "deleteAltSpelling";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            PreparedStatement stmt = null;

            try{
                if ((connMgr.getConn()) == null) {
                    good = false;
                }
                else {
                    stmt = connMgr.loadPreparedStatement(statementName);
                    stmt.setInt(1, altSpellingBean.getAltSpellingId());
                    ////
                    //System.out.println("======== Deleting AltSpellingBean ==========");
                    //System.out.println("ID: " + altSpellingBean.getAltSpellingId());
                    //System.out.println("=========================================");
                    ////
                    stmt.execute();
                }
            }
            catch (Exception e) {
                good=false;
                UtilBundle.log.logMessage(e, statementName, LogUtil.SEVERE);
            }
            // Free resources allocated.
            connMgr.shutDown(true);
        }
        catch (Exception e) {
            good = false;
            e.printStackTrace(System.err);
        }
        return good;
   }
   /**
    *
    */
   public static boolean updateAltSpelling(AltSpellingBean altSpellingBean) {

      boolean good = true;

      try {
         String statementName = "updateAltSpelling";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
             if ((connMgr.getConn()) == null) {
                 good = false;
             }
             else {
                 stmt = connMgr.loadPreparedStatement(statementName);
                 ////
                 //System.out.println("======== Update AltSpellingBean ==========");
                 //System.out.println("\tSpelling:" + altSpellingBean.getSpelling());
                 //System.out.println("==========================================");
                 ////
                 stmt.setString( 1, altSpellingBean.getSpelling());
                 stmt.setInt( 2, altSpellingBean.getAltSpellingId());
                 stmt.executeUpdate();
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
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    *
    */
   public static boolean updateAltSpellings(BeanCollection altSpellingBeanList) {

      boolean good = true;

      for(int i = 0; i < altSpellingBeanList.size(); i ++) {
         if(!updateAltSpelling((AltSpellingBean)altSpellingBeanList.getItem(i)))
            good = false;
      }
      return good;
   }

   /**
    * delete a list of altSpellings
    */
   public static boolean deleteAltSpellings(BeanCollection altSpellings) {
      boolean good = true;
      AltSpellingBean altSpelling = null;

      for(int i=0; i < altSpellings.size(); i++) {
         altSpelling = (AltSpellingBean) altSpellings.getItem(i);
         good = deleteAltSpelling(altSpelling);
         if(good == false) break;
      }
      return good;
   }
}
