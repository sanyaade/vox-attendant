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

public class AltSpellingBean extends CustomizableBean {
 
   public static final int TYPE_FIRSTNAME = 1;
   public static final int TYPE_LASTNAME = 2;
   public static final int TYPE_ENTITYNAME = 3;
   public static final int TYPE_NAME_ALIAS = 4;

   private int altSpellingId;
   private int ownerId;
   private int ownerTypeId;
   private int altspellingTypeId;
   private String spelling = "";
   /**
    *
    */
   public AltSpellingBean() {
   }
   /**
    *
    */
   public int getAltSpellingId() {
      return altSpellingId;
   }
   /**
    *
    */
   public void setAltSpellingId(int altSpellingId) {
      this.altSpellingId = altSpellingId;
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
   public String getSpelling() {
      return spelling;
   }
   /**
    *
    */
   public void setSpelling(String spelling) {
      this.spelling = formatName(spelling);
   }
   /**
    *
    */
   public int getAltspellingTypeId() {
      return altspellingTypeId;
   }
   /**
    *
    */
   public void setAltspellingTypeId(int altspellingTypeId) {
      this.altspellingTypeId = altspellingTypeId;
   }

   /**
    * returns the same string with the first letter in uppercase
    * and the rest in lower case
    */

   private String formatName(String name) {
      if(name == null) return null;

      String newName = name.toUpperCase().substring(0,1) +
                       name.toLowerCase().substring(1);
      return newName;
   }
}
