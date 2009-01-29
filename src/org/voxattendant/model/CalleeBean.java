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

public class CalleeBean extends CustomizableBean {

   private String name;
   private String audioName;
   private String workPhoneNumber;
   private boolean validWorkPhoneNumber = false;
   private boolean voicemailEnabled = false;

   private String mobilePhoneNumber;
   private boolean validMobilePhoneNumber = false;
   private boolean isEntity = false;
   
   private String homePhoneNumber;
   private boolean validHomePhoneNumber = false;
   
   public static final int DEST_WORK = 1;
   public static final int DEST_WORK_VOICEMAIL = 2;
   public static final int DEST_WORK_HOME = 3;
   public static final int DEST_WORK_MOBILE = 4;
   public static final int DEST_WORK_VOICEMAIL_HOME = 5;
   public static final int DEST_WORK_VOICEMAIL_MOBILE = 6;
   public static final int DEST_WORK_HOME_MOBILE = 7;
   public static final int DEST_WORK_VOICEMAIL_HOME_MOBILE = 8;

   public String getHelpPrompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Help.wav";
         case 3: return "WH-Help.wav";
         case 4: return "WM-Help.wav";
         case 5: return "WHV-Help.wav";
         case 6: return "WMV-Help.wav";
         case 7: return "WHM-Help.wav";
         case 8: return "WHMV-Help.wav";
         default: return "";
      }
   }
   
   public String getMainOptionPrompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Main.wav";
         case 3: return "WH-Main.wav";
         case 4: return "WM-Main.wav";
         case 5: return "WHV-Main.wav";
         case 6: return "WMV-Main.wav";
         case 7: return "WHM-Main.wav";
         case 8: return "WHMV-Main.wav";
         default: return "";
      }
   }
   
   public String getNoInput1Prompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Noinput-1.wav";
         case 3: return "WH-Noinput-1.wav";
         case 4: return "WM-Noinput-1.wav";
         case 5: return "WHV-Noinput-1.wav";
         case 6: return "WMV-Noinput-1.wav";
         case 7: return "WHM-Noinput-1.wav";
         case 8: return "WHMV-Noinput-1.wav";
         default: return "";
      }
   }
   public String getNoInput2Prompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Noinput-2.wav";
         case 3: return "WH-Noinput-2.wav";
         case 4: return "WM-Noinput-2.wav";
         case 5: return "WHV-Noinput-2.wav";
         case 6: return "WMV-Noinput-2.wav";
         case 7: return "WHM-Noinput-2.wav";
         case 8: return "WHMV-Noinput-2.wav";
         default: return "";
      }
   }
   public String getNoMatch1Prompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Nomatch-1.wav";
         case 3: return "WH-Nomatch-1.wav";
         case 4: return "WM-Nomatch-1.wav";
         case 5: return "WHV-Nomatch-1.wav";
         case 6: return "WMV-Nomatch-1.wav";
         case 7: return "WHM-Nomatch-1.wav";
         case 8: return "WHMV-Nomatch-1.wav";
         default: return "";
      }
   }
   public String getNoMatch2Prompt(int destinationType)
   {
        switch(destinationType){
         case 1: return "";
         case 2: return "WV-Nomatch-2.wav";
         case 3: return "WH-Nomatch-2.wav";
         case 4: return "WM-Nomatch-2.wav";
         case 5: return "WHV-Nomatch-2.wav";
         case 6: return "WMV-Nomatch-2.wav";
         case 7: return "WHM-Nomatch-2.wav";
         case 8: return "WHMV-Nomatch-2.wav";
         default: return "";
      }
   }
   
   
   public int getDestinationsType(boolean offerHomeNumber)
   {
       if (voicemailEnabled && validWorkPhoneNumber && validMobilePhoneNumber && validHomePhoneNumber)
       {
           if (offerHomeNumber)
           {
                return DEST_WORK_VOICEMAIL_HOME_MOBILE;
           }
           else
           {
               return DEST_WORK_VOICEMAIL_MOBILE;
           }
       }
       else if (voicemailEnabled && validWorkPhoneNumber && validMobilePhoneNumber)
       {
           return DEST_WORK_VOICEMAIL_MOBILE;
       }
       else if (voicemailEnabled && validWorkPhoneNumber && validHomePhoneNumber)
       {
           return DEST_WORK_VOICEMAIL_HOME;
       }
       else if (validMobilePhoneNumber && validWorkPhoneNumber && validHomePhoneNumber)
       {
           
           if (offerHomeNumber)
           {
                return DEST_WORK_HOME_MOBILE;
           }
           else
           {
               return DEST_WORK_MOBILE;
           }
       }
       else if (validMobilePhoneNumber && validWorkPhoneNumber)
       {
           return DEST_WORK_MOBILE;
       }
       else if (validHomePhoneNumber && validWorkPhoneNumber)
       {
           if (offerHomeNumber)
           {
                return DEST_WORK_HOME;
           }
           else
           {
               return DEST_WORK;
           }
       }
       else if (voicemailEnabled && validWorkPhoneNumber)
       {
           return DEST_WORK_VOICEMAIL;
       }
       else if (validWorkPhoneNumber)
       {
           return DEST_WORK;
       }
       return -1;
   }
   
   /**
    *
    */
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   /**
    *
    */
   public String getAudioName() {
      return audioName;
   }
   public void setAudioName(String audioName) {
      this.audioName = audioName;
   }
   /**
    *
    */
   public String getPhoneNumber() {
      if(validWorkPhoneNumber)
         return workPhoneNumber;

      if(validMobilePhoneNumber)
         return mobilePhoneNumber;

      return null;
   }
   /**
    *
    */
   public String getWorkPhoneNumber() {
      return workPhoneNumber;
   }
   public void setWorkPhoneNumber(String phoneNumber) {
      this.workPhoneNumber = phoneNumber;

      if(phoneNumber.length() > 0) {
         validWorkPhoneNumber = true;
      }
   }
   /**
    *
    */
   public String getMobilePhoneNumber() {
      return mobilePhoneNumber;
   }
   public void setMobilePhoneNumber(String phoneNumber) {
      this.mobilePhoneNumber = phoneNumber;

      if(phoneNumber.length() > 0) {
         validMobilePhoneNumber = true;
      }
   }
   
   /**
    *
    */
   public String getHomePhoneNumber() {
      return homePhoneNumber;
   }
   public void setHomePhoneNumber(String phoneNumber) {
      this.homePhoneNumber = phoneNumber;

      if(phoneNumber.length() > 0) {
         validHomePhoneNumber = true;
      }
   }
   /**
    *
    */
   public boolean hasMultiplePhoneNumbers() {
      int i = 0;
      if (validWorkPhoneNumber) 
          i++;
      
      if (validMobilePhoneNumber)
          i++;
      
      if (validHomePhoneNumber)
          i++;
       
      if (voicemailEnabled)
          i++;
      
      if (i > 1)
      {
          return true;
      }
      else
      {
          return false;
      }
   }
   
   public boolean isVoicemailEnabled()
   {
       return voicemailEnabled;
   }
   
   public void setVoicemailEnabled(boolean voicemailEnabled)
   {
       this.voicemailEnabled = voicemailEnabled;
   }
   
   public boolean isEntity()
   {
       return isEntity;
   }
   
   public void setEntity(boolean isEntity)
   {
       this.isEntity = isEntity;
   }
   
   public boolean hasWorkPhone()
   {
       return validWorkPhoneNumber;
   }
   public boolean hasMobilePhone()
   {
       return validMobilePhoneNumber;
   }
   public boolean hasHomePhone()
   {
       return validHomePhoneNumber;
   }
}
