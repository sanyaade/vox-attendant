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
import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.OperatorBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.model.PromptBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.GrammarBuilder;
import org.voxattendant.util.LogUtil;
import org.voxattendant.util.UtilBundle;


/**
 *
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2007-02-24 15:50:52 $
 *
 */
public class ApplicationManager {
    
    /*
    NOTE...
    the actual statements come from \WEB-INF\config\sqltext.properties
    */
    
    
   /**
    *
    * @param applicationBean
    * @return boolean true is successful otherwise false
    */
   public static boolean lookupApplicationBean(ApplicationBean applicationBean) {

      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;
         
         // lookup the first part of the application infomation
         String statementName = "lookupApplicationInfo";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
                  
         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               // Load up the correct SQL statement
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, ApplicationBean.APPLICATION_ID);
               rset = stmt.executeQuery();
               if (rset.next()) {
                  applicationBean.setPasscode(rset.getString(1));
                  applicationBean.setCurrentOperatorNumberId(rset.getInt(2));
                  applicationBean.setPlayDirectionAudio(rset.getInt(3) == 1);
                  applicationBean.setModifiedGrammar(rset.getInt(4));
               }
            }
         }
         catch (Exception e) {
            good = false;            
            UtilBundle.log.logMessage(e, statementName, LogUtil.SEVERE);
         }
         // Free resources allocated.
         connMgr.shutDown(true);

         BeanCollection phoneNumberBeanList = new BeanCollection();
         if(lookupPhoneNumbersByOwner(ApplicationBean.APPLICATION_TYPE_ID, phoneNumberBeanList)) {
            for(int i = 0; i < phoneNumberBeanList.size(); i++) {
               PhoneNumberBean phoneNumberBean = (PhoneNumberBean)phoneNumberBeanList.getItem(i);
               if(phoneNumberBean.getPhoneTypeId() == PhoneNumberBean.PHONETYPEID_APPLICATION) {
                  applicationBean.setApplicationNumber(phoneNumberBean.getNumber());
                  break;
               }
            }
         }
         // Create one if it is not already there...
         if((applicationBean.getApplicationNumber() == null) ||
            (applicationBean.getApplicationNumber().length() == 0)) {

            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setOwnerTypeId(ApplicationBean.APPLICATION_TYPE_ID);
            phoneNumberBean.setPhoneTypeId(PhoneNumberBean.PHONETYPEID_APPLICATION);
            phoneNumberBean.setNumber("");

            insertPhoneNumber(phoneNumberBean);
         }
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
   public static boolean updateApplicationBean(ApplicationBean applicationBean) {

      boolean good = true;

      try {
         String statementName = "updateApplicationInfo";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);

               stmt.setString(1, applicationBean.getPasscode());
               stmt.setInt(2, applicationBean.getCurrentOperatorNumberId());
               stmt.setInt(3, (applicationBean.isPlayDirectionAudio()) ? 1 : 0);
               stmt.setInt(4, applicationBean.getModifiedGrammar());
               stmt.setInt(5, ApplicationBean.APPLICATION_ID);
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
   public static void setModifiedGrammar(ApplicationBean appBean, int mask) {
      if(appBean != null) {
         appBean.setModifiedGrammar(GrammarBuilder.setBit(appBean.getModifiedGrammar(), mask));
         ////
         System.out.println("ModifiedGrammar: " +  appBean.getModifiedGrammar());
         ////
         ApplicationManager.updateApplicationBean(appBean);
      }
   }
   /**
    *
    */
   public static boolean lookupOperatorNumbers(OperatorBean operatorBean) {

      BeanCollection phoneNumberBeanList = new BeanCollection();
      if(lookupPhoneNumbersByOwner(OperatorBean.OPERATOR_TYPE_ID, phoneNumberBeanList)) {
         operatorBean.setPhoneNumbers(phoneNumberBeanList);
         return true;
      }
      return false;
   }
   /**
    *
    */
   public static boolean lookupPhoneNumbersByOwner(int ownerTypeId, BeanCollection phoneNumberBeanList) {

      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         // lookup the first part of the application infomation
         String statementName = "lookupPhoneNumbersByOwner";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, ownerTypeId);
               rset = stmt.executeQuery();

               while(rset.next()) {
                  PhoneNumberBean phoneNumberBean = new PhoneNumberBean();

                  phoneNumberBean.setPhoneNumberId(rset.getInt(1));
                  phoneNumberBean.setOwnerId(rset.getInt(2));
                  phoneNumberBean.setOwnerTypeId(ownerTypeId);
                  phoneNumberBean.setPhoneTypeId(rset.getInt(3));
                  phoneNumberBean.setNumber(rset.getString(4));
                  phoneNumberBean.setNote(rset.getString(5));
                  ////
                  //System.out.println("========= PhoneNumber Bean ==========");
                  //System.out.println("PhoneNumber Id: " + phoneNumberBean.getPhoneNumberId());
                  //System.out.println("Owner Id: " + phoneNumberBean.getOwnerId());
                  //System.out.println("OwnerType Id: " + phoneNumberBean.getOwnerTypeId());
                  //System.out.println("PhoneType Id: " + phoneNumberBean.getPhoneTypeId());
                  //System.out.println("Phone Number: " + phoneNumberBean.getNumber());
                  //System.out.println("Note: " + phoneNumberBean.getNote());
                  //System.out.println("========= xxxxxx ==========");
                  ////

                  phoneNumberBeanList.addItem(phoneNumberBean);
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
   public static boolean insertPhoneNumber(PhoneNumberBean phoneNumberBean) {

      boolean good = true;

      try {
         PreparedStatement stmt = null;

         String statementName = "insertPhoneNumber";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, phoneNumberBean.getOwnerId());
               stmt.setInt(2, phoneNumberBean.getOwnerTypeId());
               stmt.setInt(3, phoneNumberBean.getPhoneTypeId());
               stmt.setString(4, phoneNumberBean.getNumber());
               stmt.setString(5, phoneNumberBean.getNote());
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
         e.printStackTrace();
      }
      return good;
   }
   /**
    *
    */
   public static boolean updatePhoneNumber(PhoneNumberBean phoneNumberBean) {

      boolean good = true;

      try {
         String statementName = "updatePhoneNumber";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               ////
               //System.out.println("========= Update PhoneNumberBean ==========");
               //System.out.println("PhoneNumber Id: " + phoneNumberBean.getPhoneNumberId());
               //System.out.println("Owner Id: " + phoneNumberBean.getOwnerId());
               //System.out.println("OwnerType Id: " + phoneNumberBean.getOwnerTypeId());
               //System.out.println("PhoneType Id: " + phoneNumberBean.getPhoneTypeId());
               //System.out.println("Phone Number: " + phoneNumberBean.getNumber());
               //System.out.println("Note: " + phoneNumberBean.getNote());
               //System.out.println("========= xxxxxx ==========");
               ////
               stmt.setString(1, phoneNumberBean.getNumber());
               stmt.setString(2, phoneNumberBean.getNote());
               stmt.setInt(3, phoneNumberBean.getPhoneNumberId());
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
   public static boolean updatePhoneNumberByOwner(PhoneNumberBean phoneNumberBean) {

      boolean good = true;

      try {
         String statementName = "updatePhoneNumberByOwner";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setString(1, phoneNumberBean.getNumber());
               stmt.setString(2, phoneNumberBean.getNote());
               stmt.setInt(3, phoneNumberBean.getOwnerId());
               stmt.setInt(4, phoneNumberBean.getOwnerTypeId());
               stmt.setInt(5, phoneNumberBean.getPhoneTypeId());
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
   public static boolean deletePhoneNumber(PhoneNumberBean phoneNumberBean) {

      boolean good = true;

      try {
         String statementName = "deletePhoneNumber";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
              good = false;
            }
            else {
              stmt = connMgr.loadPreparedStatement(statementName);
              stmt.setInt(1, phoneNumberBean.getPhoneNumberId());
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
   public static boolean deletePhoneNumbersByOwner(PhoneNumberBean phoneNumberBean) {

      boolean good = true;

      try {
         String statementName = "deletePhoneNumbersByOwner";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, phoneNumberBean.getOwnerId());
               stmt.setInt(2, phoneNumberBean.getOwnerTypeId());
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
         e.printStackTrace();
      }
      return good;
   }
   /**
    *
    */
   public static boolean lookupAllPrompts(BeanCollection promptBeanList) {

      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         String statementName = "lookupAllPrompts";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
         try {
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               rset = stmt.executeQuery();

               while(rset.next()) {
                  PromptBean promptBean = new PromptBean();
                  promptBean.setPromptId(rset.getInt(1));
                  promptBean.setName(rset.getString(2));
                  promptBean.setAudio(rset.getString(3));
                  promptBean.setActive((rset.getInt(4) == 1) ? true: false);

                  promptBeanList.addItem(promptBean);
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
   public static boolean updatePrompts(BeanCollection promptBeanList) {

      boolean status = true;
      PromptBean promptBean = null;
      for(int i = 0; i < promptBeanList.size(); i++) {
         promptBean = (PromptBean)promptBeanList.getItem(i);
         status = updatePrompt(promptBean);
      }
      return status;
   }
   /**
    *
    */
   public static boolean updatePrompt(PromptBean promptBean) {

      boolean good = true;

      try {
         String statementName = "updatePrompt";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

         PreparedStatement stmt = null;

         try{
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setInt(1, promptBean.isActive() ? 1: 0);
               stmt.setInt(2, promptBean.getPromptId());
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
}
