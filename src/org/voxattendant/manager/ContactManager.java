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
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.LogUtil;
import org.voxattendant.util.UtilBundle;

public class ContactManager {
   /**
    *
    */
   public static boolean lookupAllContacts(BeanCollection contacts) {
      ////
      System.out.println("in lookupAllContacts....");
      ////
      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         String statementName = "lookupContacts";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
         ContactBean contact = new ContactBean();
         BeanCollection altSpellings = new BeanCollection();

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               rset = stmt.executeQuery();

               int prevId = -1;
               int phoneType = -1;
               String phoneNumber = "";

               while (rset.next()) {
                  if(rset.getInt(1) != prevId) {
                     if(prevId != -1) {
                        //add the contact to the BeanCollection
                        ////
                        
//                        System.out.println("--------------- Contact -------------- ");
//                        System.out.println("contactId: " + contact.getContactId());
//                        System.out.println("firstname: " + contact.getFirstname());
//                        System.out.println("lastname: " + contact.getLastname());
//                        System.out.println("nickname: " + contact.getNickname());
//                        System.out.println("departmentName: " + contact.getDepartmentName());
//                        System.out.println("isActive: " + contact.isActive());
//                        System.out.println("distinctInfo: " + contact.getDistinctInfo());
//                        System.out.println("phoneticCode: " + contact.getPhoneticCode());
//                        System.out.println("note: " + contact.getNote());
//                        System.out.println("workPhoneNumber: " + contact.getWorkPhoneNumber());
//                        System.out.println("mobilePhoneNumber: " + contact.getMobilePhoneNumber());
//                        System.out.println("homePhoneNumber: " + contact.getHomePhoneNumber());
//                        System.out.println("extension: " + contact.getExtension());
//                        System.out.println("isVoicemailEnabled: " + contact.isVoicemailEnabled());
                        
                        ////
                        //add the altSpellings
                        AltSpellingManager.lookupAltSpellingByOwner(contact.getContactId(), ContactBean.CONTACT_TYPE_ID, altSpellings);
                        contact.setAltSpellings(altSpellings);

                        contacts.addItem(contact);
                     }
                     //create a new contact
                     contact = new ContactBean();
                     altSpellings = new BeanCollection();

                     //set values from this row
                     contact.setContactId(rset.getInt(1));
                     contact.setFirstname(rset.getString(2));
                     contact.setLastname(rset.getString(3));
                     contact.setNickname(rset.getString(4));
                     contact.setDepartmentName(rset.getString(5));
                     contact.setActive(rset.getInt(6) == 1);
                     contact.setDistinctInfo(rset.getString(7));
                     contact.setPhoneticCode(rset.getString(8));
                     contact.setNote(rset.getString(9));
                     contact.setVoicemailEnabled(rset.getInt(10) == 1);
                     contact.setAudioName(rset.getString(11));
                     contact.setEmail(rset.getString(12));

                     //reset prevId
                     prevId = contact.getContactId();
                  }
                  //add the phone numbers for the contact
                  phoneType = rset.getInt(13);
                  phoneNumber = rset.getString(14);

                  switch(phoneType) {
                     case PhoneNumberBean.PHONETYPEID_WORK:
                        contact.setWorkPhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_MOBILE:
                        contact.setMobilePhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_HOME:
                        contact.setHomePhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_EXTENSION:
                        contact.setExtension(phoneNumber);
                        break;
                  }

                  // if this is the last record, add it to the list
                  if(rset.isLast()) {
                     System.out.println("got the last record");
                     //add the contact to the SortableBeanList
                     ////
                     /*
                     System.out.println("--------------- Contact -------------- ");
                     System.out.println("contactId: " + contact.getContactId());
                     System.out.println("firstname: " + contact.getFirstname());
                     System.out.println("lastname: " + contact.getLastname());
                     System.out.println("nickname: " + contact.getNickname());
                     System.out.println("departmentName: " + contact.getDepartmentName());
                     System.out.println("isActive: " + contact.isActive());
                     System.out.println("distinctInfo: " + contact.getDistinctInfo());
                     System.out.println("phoneticCode: " + contact.getPhoneticCode());
                     System.out.println("note: " + contact.getNote());
                     System.out.println("workPhoneNumber: " + contact.getWorkPhoneNumber());
                     System.out.println("mobilePhoneNumber: " + contact.getMobilePhoneNumber());
                     */
                     ////
                     //add the altSpellings
                     AltSpellingManager.lookupAltSpellingByOwner(contact.getContactId(), ContactBean.CONTACT_TYPE_ID, altSpellings);
                     contact.setAltSpellings(altSpellings);

                     contacts.addItem(contact);
                  }
               } // while
            } // if
         }
         catch (Exception e) {
            good = false;
            e.printStackTrace();
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
   public static boolean lookupContactsByPhoneticCode(String phoneticCode, BeanCollection contacts) {
      ////
      System.out.println("in lookupContactsByPhoneticCode....");
      ////
      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         String statementName = "lookupContactsByPhoneticCode";
         SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());
         BeanCollection altSpellings = new BeanCollection();

         try {
            // Get database connection
            if ((connMgr.getConn()) == null) {
               good = false;
            }
            else {
               stmt = connMgr.loadPreparedStatement(statementName);
               stmt.setString(1, phoneticCode);
               rset = stmt.executeQuery();

               ContactBean contact = new ContactBean();
               int prevId = -1;
               int phoneType = -1;
               String phoneNumber = "";

               while (rset.next()) {
                  if(rset.getInt(1) != prevId) {
                     if(prevId != -1) {
                        //add the contact to the SortableBeanList
                        ////
                        /*
                        System.out.println("--------------- Contact -------------- ");
                        System.out.println("contactId: " + contact.getContactId());
                        System.out.println("firstname: " + contact.getFirstname());
                        System.out.println("lastname: " + contact.getLastname());
                        System.out.println("nickname: " + contact.getNickname());
                        System.out.println("departmentName: " + contact.getDepartmentName());
                        System.out.println("isActive: " + contact.isActive());
                        System.out.println("distinctInfo: " + contact.getDistinctInfo());
                        System.out.println("phoneticCode: " + contact.getPhoneticCode());
                        System.out.println("note: " + contact.getNote());
                        System.out.println("workPhoneNumber: " + contact.getWorkPhoneNumber());
                        System.out.println("mobilePhoneNumber: " + contact.getMobilePhoneNumber());
                        */
                        ////
                        //add the altSpellings
                        AltSpellingManager.lookupAltSpellingByOwner(contact.getContactId(), ContactBean.CONTACT_TYPE_ID, altSpellings);
                        contact.setAltSpellings(altSpellings);

                        contacts.addItem(contact);
                     }
                     //create a new contact
                     contact = new ContactBean();
                     altSpellings = new BeanCollection();

                     //set values from this row
                     contact.setContactId(rset.getInt(1));
                     contact.setFirstname(rset.getString(2));
                     contact.setLastname(rset.getString(3));
                     contact.setNickname(rset.getString(4));
                     contact.setDepartmentName(rset.getString(5));
                     contact.setActive(rset.getInt(6) == 1);
                     contact.setDistinctInfo(rset.getString(7));
                     contact.setPhoneticCode(rset.getString(8));
                     contact.setNote(rset.getString(9));
                     contact.setVoicemailEnabled(rset.getInt(10) == 1);
                     contact.setAudioName(rset.getString(11));
                     contact.setEmail(rset.getString(12));

                     //reset prevId
                     prevId = contact.getContactId();
                  }
                  //add the phone numbers for the contact
                  phoneType = rset.getInt(13);
                  phoneNumber = rset.getString(14);

                  switch(phoneType) {
                     case PhoneNumberBean.PHONETYPEID_WORK:
                        contact.setWorkPhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_MOBILE:
                        contact.setMobilePhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_HOME:
                        contact.setHomePhoneNumber(phoneNumber);
                        break;
                     case PhoneNumberBean.PHONETYPEID_EXTENSION:
                        contact.setExtension(phoneNumber);
                        break;
                  }

                  // if this is the last record, add it to the list
                  if(rset.isLast()) {
                     //System.out.println("got the last record");
                     //add the contact to the SortableBeanList
                     ////
                     /*
                     System.out.println("--------------- Contact -------------- ");
                     System.out.println("contactId: " + contact.getContactId());
                     System.out.println("firstname: " + contact.getFirstname());
                     System.out.println("lastname: " + contact.getLastname());
                     System.out.println("nickname: " + contact.getNickname());
                     System.out.println("departmentName: " + contact.getDepartmentName());
                     System.out.println("isActive: " + contact.isActive());
                     System.out.println("distinctInfo: " + contact.getDistinctInfo());
                     System.out.println("phoneticCode: " + contact.getPhoneticCode());
                     System.out.println("note: " + contact.getNote());
                     System.out.println("workPhoneNumber: " + contact.getWorkPhoneNumber());
                     System.out.println("mobilePhoneNumber: " + contact.getMobilePhoneNumber());
                     */
                     ////
                     //add the altSpellings
                     AltSpellingManager.lookupAltSpellingByOwner(contact.getContactId(), ContactBean.CONTACT_TYPE_ID, altSpellings);
                     contact.setAltSpellings(altSpellings);

                     contacts.addItem(contact);
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
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    * update contact
    * This is a three step process-
    * 1. update contact in the contact table
    * 2. update mobile phone number in phonenumber table
    * 3. update work phone number in phonenumber table
    */
   public static boolean updateContact(ContactBean contact) {
      boolean good = true;

      try {
         PreparedStatement stmt = null;

         // first: update the contacts table
         {
            String statementName = "updateContact";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  System.out.println("not able to get db connection");
                  good = false;
               }
               else {
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setString(1, contact.getFirstname());
                  stmt.setString(2, contact.getLastname());
                  stmt.setString(3, contact.getNickname());
                  stmt.setString(4, contact.getDepartmentName());
                  stmt.setInt(5,(contact.isActive() ? 1 : 0));
                  stmt.setString(6, contact.getDistinctInfo());
                  stmt.setString(7, contact.getNote());
                  stmt.setString(8,contact.getPhoneticCode());
                  stmt.setInt(9,(contact.isVoicemailEnabled() ? 1 : 0));
                  stmt.setString(10, contact.getAudioName());
                  stmt.setString(11, contact.getEmail());
                  
                  stmt.setInt(12, contact.getContactId());
                  
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
         // second: update the mobilePhoneNumber in the phonenumber table
         updatePhoneNumber(contact,PhoneNumberBean.PHONETYPEID_MOBILE);

         //third: update the workPhoneNumber in the phonenumber table
         updatePhoneNumber(contact,PhoneNumberBean.PHONETYPEID_WORK);
         
         //fourth: update the homePhoneNumber in the phonenumber table
         updatePhoneNumber(contact,PhoneNumberBean.PHONETYPEID_HOME);
         
         //fifth: update the extension in the phonenumber table
         updatePhoneNumber(contact,PhoneNumberBean.PHONETYPEID_EXTENSION);
      }
      catch (Exception e) {
         good=false;
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    * update phone number
    */
   public static boolean updatePhoneNumber(ContactBean contact, int phoneTypeId) {

      PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
      phoneNumberBean.setOwnerId(contact.getContactId());
      phoneNumberBean.setOwnerTypeId(ContactBean.CONTACT_TYPE_ID);
      phoneNumberBean.setPhoneTypeId(phoneTypeId);
      
      //phoneNumberBean.setNumber((phoneTypeId == PhoneNumberBean.PHONETYPEID_MOBILE) ?
      //                              contact.getMobilePhoneNumber(): contact.getWorkPhoneNumber());
      if (phoneTypeId == PhoneNumberBean.PHONETYPEID_MOBILE)
      {
        phoneNumberBean.setNumber(contact.getMobilePhoneNumber());
      }
      else if (phoneTypeId == PhoneNumberBean.PHONETYPEID_WORK)
      {
        phoneNumberBean.setNumber(contact.getWorkPhoneNumber());  
      }
      else if (phoneTypeId == PhoneNumberBean.PHONETYPEID_EXTENSION)
      {
        phoneNumberBean.setNumber(contact.getExtension());  
      }
      else
      {
        phoneNumberBean.setNumber(contact.getHomePhoneNumber());  
      }

      return ApplicationManager.updatePhoneNumberByOwner(phoneNumberBean);
   }
   /**
    * delete contact
    */
   public static boolean deleteContact(ContactBean contact) {
      boolean good = true;

      try {
         PreparedStatement stmt = null;
         {
            // first: delete the contact in the contact table
            String statementName = "deleteContact";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  good = false;
               }
               else {
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setInt(1, contact.getContactId());
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
            phoneNumberBean.setOwnerId(contact.getContactId());
            phoneNumberBean.setOwnerTypeId(ContactBean.CONTACT_TYPE_ID);
            good = ApplicationManager.deletePhoneNumbersByOwner(phoneNumberBean);
         }
         // third: delete the alt spellings
         {
            BeanCollection altSpellings = contact.getAltSpellings();
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
   public static boolean addContact(ContactBean contact) {
      boolean good = true;

      try {
         ResultSet rset = null;
         PreparedStatement stmt = null;

         // first: add the contact in the contact table
         {
            String statementName = "addContact";
            SQLConnManager connMgr = new SQLConnManager(statementName, new ErrorBean());

            try {
               // Get database connection
               if ((connMgr.getConn()) == null) {
                  good = false;
               }
               else {
                  // Load up the correct SQL statement
                  stmt = connMgr.loadPreparedStatement(statementName);
                  stmt.setString(1, contact.getFirstname());
                  stmt.setString(2, contact.getLastname());
                  stmt.setString(3, contact.getNickname());
                  stmt.setInt(4,(contact.isActive() ? 1 : 0));
                  stmt.setString(5, contact.getDistinctInfo());
                  stmt.setString(6, contact.getPhoneticCode());
                  stmt.setString(7, contact.getNote());
                  stmt.setInt(8,(contact.isVoicemailEnabled() ? 1 : 0));
                  stmt.setString(9, contact.getAudioName());
                  stmt.setString(10, contact.getEmail());
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
         // second: get the contact_id
         {
            String statementName = "lookupLastContactId";
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
                     contact.setContactId(rset.getInt(1));
                     ////
                     //System.out.println("the id for the added Contact: " + rset.getInt(1));
                     ////
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
         // third: add the mobilePhoneNumber in the phonenumber table
         addPhoneNumber(contact, PhoneNumberBean.PHONETYPEID_MOBILE);

         // fourth: add the workPhoneNumber in the phonenumber table
         addPhoneNumber(contact, PhoneNumberBean.PHONETYPEID_WORK);
         
         addPhoneNumber(contact, PhoneNumberBean.PHONETYPEID_HOME);
         
         addPhoneNumber(contact, PhoneNumberBean.PHONETYPEID_EXTENSION);
      }
      catch (Exception e) {
         good=false;
         e.printStackTrace(System.err);
      }
      return good;
   }
   /**
    * add phone number
    */
   public static boolean addPhoneNumber(ContactBean contact, int phoneTypeId) {

      PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
      phoneNumberBean.setOwnerId(contact.getContactId());
      phoneNumberBean.setOwnerTypeId(ContactBean.CONTACT_TYPE_ID);
      phoneNumberBean.setPhoneTypeId(phoneTypeId);
      
      if (phoneTypeId == PhoneNumberBean.PHONETYPEID_MOBILE)
      {
        phoneNumberBean.setNumber(contact.getMobilePhoneNumber());
      }
      else if (phoneTypeId == PhoneNumberBean.PHONETYPEID_WORK)
      {
        phoneNumberBean.setNumber(contact.getWorkPhoneNumber());  
      }
      else if (phoneTypeId == PhoneNumberBean.PHONETYPEID_EXTENSION)
      {
        phoneNumberBean.setNumber(contact.getExtension());  
      }
      else
      {
        phoneNumberBean.setNumber(contact.getHomePhoneNumber());  
      }
      //phoneNumberBean.setNumber((phoneTypeId == PhoneNumberBean.PHONETYPEID_MOBILE) ?
      //                              contact.getMobilePhoneNumber(): contact.getWorkPhoneNumber());

      return ApplicationManager.insertPhoneNumber(phoneNumberBean);
   }
}
