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

public class PhoneNumberBean extends CustomizableBean {
   /**
    *
    */
   public static final int PHONETYPEID_ENTITY = 0;
   public static final int PHONETYPEID_WORK   = 1;
   public static final int PHONETYPEID_MOBILE = 2;
   public static final int PHONETYPEID_RECORDING   = 3;
   public static final int PHONETYPEID_APPLICATION = 4;
   public static final int PHONETYPEID_HOME = 5;
   public static final int PHONETYPEID_EXTENSION = 6;
   
   /**
    *
    */
   private int phoneNumberId;
   private int ownerId;
   private int ownerTypeId;
   private int phoneTypeId;
   private String number = "";
   private String note = "";
   /**
    *
    */
   public int getPhoneNumberId(){
     return phoneNumberId;
   }
   /**
    *
    */
   public void setPhoneNumberId(int phoneNumberId) {
      this.phoneNumberId = phoneNumberId;
   }
   /**
    *
    */
   public int getOwnerId() {
      return ownerId;
   }
   /**
    *
    */
   public void setOwnerId(int ownerId) {
      this.ownerId = ownerId;
   }
   /**
    *
    */
   public int getOwnerTypeId() {
      return ownerTypeId;
   }
   /**
    *
    */
   public void setOwnerTypeId(int ownerTypeId) {
      this.ownerTypeId = ownerTypeId;
   }
   /**
    *
    */
   public int getPhoneTypeId() {
      return phoneTypeId;
   }
   /**
    *
    */
   public void setPhoneTypeId(int phoneTypeId) {
      this.phoneTypeId = phoneTypeId;
   }
   /**
    *
    */
   public String getNumber(){
     return number;
   }
   /**
    *
    */
   public void setNumber(String number){
      this.number = number;
   }
   /**
    *
    */
   public String getNote(){
      return note;
   }
   /**
    *
    */
   public void setNote(String note){
      this.note = note;
   }
}
