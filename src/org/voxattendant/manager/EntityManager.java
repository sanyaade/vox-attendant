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
import org.voxattendant.model.EntityBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.LogUtil;
import org.voxattendant.util.UtilBundle;

public class EntityManager {
   /**
    *
    */
   public static boolean lookupAllEntities(BeanCollection entities) {
      ////
      System.out.println("in EntityManager: lookupAllContacts....");
      ////
      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         String statementName = "lookupEntities";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
         EntityBean entity = null;
         BeanCollection altSpellings = null;
         BeanCollection subEntities = null;

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               rset = stmt.executeQuery();

               int prevParentId = -1;

               // note:
               // in order for entities to be populated correctly,
               // the resultset being ordered by parentId (handled in sql stmt)
               while (rset.next()) {
                  // populate values in a new EntityBean
                  entity = new EntityBean();
                  entity.setEntityId(rset.getInt(1));
                  entity.setParentId(rset.getInt(2));
                  entity.setName(rset.getString(3));
                  entity.setActive(rset.getInt(4) == 1);
                  entity.setNote(rset.getString(5));
                  entity.setPhoneNumber(rset.getString(6));
                  entity.setAudioName(rset.getString(7));
                  //add the altSpellings
                  altSpellings = new BeanCollection();
                  AltSpellingManager.lookupAltSpellingByOwner(entity.getEntityId(), EntityBean.ENTITY_TYPE_ID, altSpellings);
                  entity.setAltSpellings(altSpellings);

                  if(entity.getParentId() != 0) {
                     //this is a subEntity,
                     // - if the parentId is different from the previous,
                     //      create a new subentity bean collection
                     //      and update the value of prevParentId
                     //
                     // - add to the current subentity bean collection
                     if(entity.getParentId() != prevParentId) {
                        if(prevParentId != -1) {
                           addSubEntities(entities, prevParentId, subEntities);
                        }
                        prevParentId = entity.getParentId();
                        subEntities = new BeanCollection();
                     }
                     subEntities.addItem(entity);
                     if(rset.isLast()) {
                        addSubEntities(entities, prevParentId, subEntities);
                     }
                  }

                  //
                  entities.addItem(entity);
               }
            }
         }
         catch (Exception e) {
            good = false;
            ////e.printStackTrace(System.err);
            UtilBundle.log.logMessage(e, statementName, LogUtil.SEVERE);
         }
         // Free resources allocated.
         connMgr.shutDown(true);
      }
      catch (Exception e) {
         good=false;
         ////e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    * delete entity
    */
   public static boolean deleteEntity(EntityBean entity) {
      boolean good = true;

      try {
         PreparedStatement stmt = null;
         {
            // first: delete the contact in the contact table
            String statementName = "deleteEntity";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  good = false;
               }
               else {
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setInt(1, entity.getEntityId());
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
         // second: delete the phone numbers in the phonenumber table
         {
            PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
            phoneNumberBean.setOwnerId(entity.getEntityId());
            phoneNumberBean.setOwnerTypeId(EntityBean.ENTITY_TYPE_ID);
            good = ApplicationManager.deletePhoneNumbersByOwner(phoneNumberBean);
         }
         // third: delete the alt spellings
         {
            BeanCollection altSpellings = entity.getAltSpellings();
            if(altSpellings != null) {
               good = AltSpellingManager.deleteAltSpellings(altSpellings);
            }
         }
      }
      catch (Exception e) {
         good=false;
         e.printStackTrace(System.err);
      }
      return good;
   }

   /**
    * add contact
    */
   public static boolean addEntity(EntityBean entity) {
      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         // first: add the entity in the entity table
         {
            String statementName = "addEntity";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  good = false;
               }
               else {
                  // Load up the correct SQL statement
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setInt(1, entity.getParentId());
                  stmt.setString(2, entity.getName());
                  stmt.setInt(3,(entity.isActive() ? 1 : 0));
                  stmt.setString(4, entity.getNote());
                  stmt.setString(5, entity.getAudioName());
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
         // second: get the entity
         {
            String statementName = "lookupLastEntityId";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  good = false;
               }
               else {
                  stmt = connMgr.loadPreparedStatement(statementName);
                  rset = stmt.executeQuery();
                  if(rset.next()) {
                     entity.setEntityId(rset.getInt(1));
                     System.out.println("new entity: " + entity.getName() + " has id: " + entity.getEntityId());
                  } else {
                     System.out.println("something is wrong..did not get new id");
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
         // third: add the phoneNumber in the phonenumber table
         if(addPhoneNumber(entity)) {
            System.out.println("added phone number");
         } else {
            System.out.println("did not add phone number");
         }
      }
      catch (Exception e) {
         good=false;
         e.printStackTrace(System.err);
      }
      return good;
   }

/*
updateEntity = \
   update entity \
   set name = ?, \
       isActive = ?, \
       note = ? \
   where entityId = ?
*/
   /**
    * update entity
    * This is a two step process-
    * 1. update entity in the entity table
    * 2. update phone number in phonenumber table
    */
   public static boolean updateEntity(EntityBean entity) {
      boolean good = true;

      try {
         PreparedStatement stmt = null;

         // first: update the contacts table
         {
            String statementName = "updateEntity";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  System.out.println("not able to get db connection");
                  good = false;
               }
               else {
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setString(1, entity.getName());
                  stmt.setInt(2,(entity.isActive() ? 1 : 0));
                  stmt.setString(3, entity.getNote());
                  stmt.setString(4, entity.getAudioName());
                  
                  stmt.setInt(5, entity.getEntityId());
                  

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
         // second: update the phone number in the phonenumber table
         updatePhoneNumber(entity);

      }
      catch (Exception e) {
         good=false;
         e.printStackTrace(System.err);
      }
      return good;
   }

   /**
    * helper function
    * add phone number
    */
   public static boolean addPhoneNumber(EntityBean entity) {
      PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
      phoneNumberBean.setOwnerId(entity.getEntityId());
      phoneNumberBean.setOwnerTypeId(EntityBean.ENTITY_TYPE_ID);
      phoneNumberBean.setPhoneTypeId(PhoneNumberBean.PHONETYPEID_WORK);
      phoneNumberBean.setNumber(entity.getPhoneNumber());

      return ApplicationManager.insertPhoneNumber(phoneNumberBean);
   }

   /**
    * helper function
    * update phone number
    */
   public static boolean updatePhoneNumber(EntityBean entity) {

      PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
      phoneNumberBean.setOwnerId(entity.getEntityId());
      phoneNumberBean.setOwnerTypeId(EntityBean.ENTITY_TYPE_ID);
      phoneNumberBean.setPhoneTypeId(PhoneNumberBean.PHONETYPEID_ENTITY);
      phoneNumberBean.setNumber(entity.getPhoneNumber());

      return ApplicationManager.updatePhoneNumberByOwner(phoneNumberBean);
   }

   /**
    * helper function
    */
   private static void addSubEntities(BeanCollection entities, int parentId,
                               BeanCollection subEntities) {
      EntityBean entity;
      for(int i = 0; i < entities.size(); i ++) {
         entity = (EntityBean)entities.getItem(i);
         if(entity.getEntityId() == parentId) {
            entity.setSubEntities(subEntities);
            break;
         }
      }
   }

}