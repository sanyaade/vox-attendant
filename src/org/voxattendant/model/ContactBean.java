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

package org.voxattendant.model;

import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.MetaPhone;

public class ContactBean extends CustomizableBean {
   /**
    *
    */
   public static final int CONTACT_TYPE_ID = 3;
   /**
    *
    */
   private int contactId = -1;
   private String firstname = "";
   private String lastname = "";
   private String nickname = "";
   private String note = "";
   private String distinctInfo = "";
   private String departmentName = "";
   private boolean active = true;
   private boolean voicemailEnabled = true;
   private String phoneticCode = "";
   private String workPhoneNumber = "";
   private String mobilePhoneNumber = "";
   private String homePhoneNumber = "";
   private String extension="";
   private BeanCollection altSpellings = null;
   private String audioName = "";
   private String email = "";

   /**
    * constructor
    */
   public ContactBean() {
   }

   public int getContactId() {
      return contactId;
   }

   public void setContactId(int contactId) {
      this.contactId = contactId;
   }
   
   public String getAudioName() {
      return audioName;
   }

   public void setAudioName(String audioName) {
      this.audioName = audioName;
   }
   public String getFirstname() {
      return firstname;
   }

   public void setFirstname(String firstname) {
      this.firstname = formatName(firstname);
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = formatName(lastname);
   }

   public String getNickname() {
      return nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = formatName(nickname);
   }

   public String getNote() {
      return note;
   }

   public void setNote(String note) {
      this.note = note;
   }

   public String getDistinctInfo() {
      return distinctInfo;
   }

   public void setDistinctInfo(String distinctInfo) {
      this.distinctInfo = distinctInfo;
   }

   public String getDepartmentName() {
      return departmentName;
   }

   public void setDepartmentName(String departmentName) {
      this.departmentName = departmentName;
   }

   public boolean isActive() {
      return active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }
   
   public boolean isVoicemailEnabled() {
      return voicemailEnabled;
   }

   public void setVoicemailEnabled(boolean voicemailEnabled) {
      this.voicemailEnabled = voicemailEnabled;
   }

   public String getPhoneticCode() {
      return phoneticCode;
   }

   public void setPhoneticCode() {
      setPhoneticCode(generatePhoneticCode(firstname,lastname));
   }

   public void setPhoneticCode(String phoneticCode) {
      this.phoneticCode = phoneticCode;
   }

   public String getWorkPhoneNumber() {
      return workPhoneNumber;
   }

   public void setWorkPhoneNumber(String workPhoneNumber) {
      this.workPhoneNumber = workPhoneNumber;
   }

   public String getMobilePhoneNumber() {
      return mobilePhoneNumber;
   }

   public void setMobilePhoneNumber(String mobilePhoneNumber) {
      this.mobilePhoneNumber = mobilePhoneNumber;
   }
   
   public String getHomePhoneNumber() {
      return homePhoneNumber;
   }

   public void setHomePhoneNumber(String homePhoneNumber) {
      this.homePhoneNumber = homePhoneNumber;
   }
   
   public String getExtension() {
      return extension;
   }

   public void setExtension(String extension) {
      this.extension = extension;
   }

   public BeanCollection getAltSpellings() {
      return altSpellings;
   }

   public void setAltSpellings(BeanCollection altSpellings) {
      this.altSpellings = altSpellings;
   }
   
   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   /**
    * returns the same string with the first letter in uppercase
    * and the rest in lower case
    */

   private String formatName(String name) {
      String newName = "";

      if(name == null) {
         return null;
      } else if(name.length() == 1) {
         newName = name.toUpperCase();
      } else if(name.length() > 1) {
         newName = name.toUpperCase().substring(0,1) +
                         name.toLowerCase().substring(1);
      }
      return newName;
   }

   /**
    * standard method in this application to generate the Phonetic code
    */
   public static String generatePhoneticCode(String firstname, String lastname) {
      return MetaPhone.generate(firstname) + MetaPhone.generate(lastname);
   }
   
   public static String generateDtmfExtension(String extension)
   {
       extension = extension.replaceAll("0","dtmf-0 ");
       extension = extension.replaceAll("1","dtmf-1 ");
       extension = extension.replaceAll("2","dtmf-2 ");
       extension = extension.replaceAll("3","dtmf-3 ");
       extension = extension.replaceAll("4","dtmf-4 ");
       extension = extension.replaceAll("5","dtmf-5 ");
       extension = extension.replaceAll("6","dtmf-6 ");
       extension = extension.replaceAll("7","dtmf-7 ");
       extension = extension.replaceAll("8","dtmf-8 ");
       extension = extension.replaceAll("9","dtmf-9 ");
       return extension;
       
   }
   
   public static void main(String[] args)
   {
       System.out.println(ContactBean.generateDtmfExtension("3124232"));
   }
}