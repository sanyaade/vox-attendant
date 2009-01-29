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

public class EntityBean extends CustomizableBean {
   /**
    *
    */
   public static final int ENTITY_TYPE_ID = 4;
   public static final int ENTITY_TOP_LEVEL_PARENT_ID = 0;
   /**
    *
    */
   private int entityId = -1;
   private int parentId = -1;
   private String name = "";
   private String audioName = "";
   private String note = "";
   private boolean active = true;
   private String phoneNumber = "";
   private BeanCollection altSpellings = null;
   private BeanCollection subEntities = null;

   /**
    * constructor
    */
   public EntityBean() {
   }
   public int getEntityId() {
      return entityId;
   }
   public void setEntityId(int newEntityId) {
      entityId = newEntityId;
   }
   public void setParentId(int newParentId) {
      parentId = newParentId;
   }
   public int getParentId() {
      return parentId;
   }
   public void setName(String newName) {
      name = formatName(newName);
   }
   public String getName() {
      return name;
   }
   
   public void setAudioName(String newAudioName) {
      this.audioName = newAudioName;
   }
   public String getAudioName() {
      return audioName;
   }
   
   public void setNote(String newNote) {
      note = newNote;
   }
   public String getNote() {
      return note;
   }
   public void setActive(boolean newActive) {
      active = newActive;
   }
   public boolean isActive() {
      return active;
   }
   public void setPhoneNumber(String newPhoneNumber) {
      phoneNumber = newPhoneNumber;
   }
   public String getPhoneNumber() {
      return phoneNumber;
   }
   public void setAltSpellings(BeanCollection newAltSpellings) {
      altSpellings = newAltSpellings;
   }
   public BeanCollection getAltSpellings() {
      return altSpellings;
   }
   public void setSubEntities(org.voxattendant.util.BeanCollection newSubEntities) {
      subEntities = newSubEntities;
   }
   public org.voxattendant.util.BeanCollection getSubEntities() {
      return subEntities;
   }
   /**
    * Helper function to determine if this is a sub entity.
    */
   public boolean isSubEntity() {
      return (parentId != 0);
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
}