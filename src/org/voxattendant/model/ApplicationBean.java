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

public class ApplicationBean extends CustomizableBean {
   /**
    *
    */
   public static final int APPLICATION_TYPE_ID = 1;
   /**
    *
    */
   public static final int APPLICATION_ID = 1; // not needed
   /**
    *
    */
   public static final int RECORDING_NUMBER_ID = 3;
   public static final int APPLICATION_NUMBER_ID = 4;

   private int applicationId;
   private String passcode = "";
   private int currentOperatorNumberId;
   private boolean playDirectionAudio = true;
   private int modifiedGrammar;
   private String applicationNumber = "";
   private String recordingNumber = "";

   /**
    * Constructor
    */
   public ApplicationBean() {
   }
   /**
    * Returns the application id
    */
   public int getApplicationId(){
     return applicationId;
   }
   /**
    * Sets the application id
    */
   public void setApplicationId(int applicationId) {
      this.applicationId = applicationId;
   }
   /**
    * Returns the
    */
   public String getPasscode(){
     return passcode;
   }
   /**
    * Sets the
    */
   public void setPasscode(String passcode){
      this.passcode = passcode;
   }
   /**
    *
    */
   public int getCurrentOperatorNumberId(){
     return currentOperatorNumberId;
   }
   /**
    *
    */
   public void setCurrentOperatorNumberId(int currentOperatorNumberId) {
      this.currentOperatorNumberId = currentOperatorNumberId;
   }
   /**
    * Returns the
    */
   public boolean isPlayDirectionAudio(){
      return playDirectionAudio;
   }
   /**
    * Sets the
    */
   public void setPlayDirectionAudio(boolean playDirectionAudio){
      this.playDirectionAudio = playDirectionAudio;
   }
   /**
    * Helper: Toggles
    */
   public void togglePlayDirectionAudio() {
      playDirectionAudio = !playDirectionAudio;
   }
   /**
    * Returns user's first name
    */
   public int getModifiedGrammar(){
      return modifiedGrammar;
   }
   /**
    * Sets user's first name
    */
   public void setModifiedGrammar(int modifiedGrammar){
      this.modifiedGrammar = modifiedGrammar;
   }
   /**
    * Returns user's last name
    */
   public String getApplicationNumber(){
      return applicationNumber;
   }
   /**
    * Sets user's last name
    */
   public void setApplicationNumber(String applicationNumber){
      this.applicationNumber = applicationNumber;
   }
   /**
    * Returns the operator number
    */
   public String getRecordingNumber(){
      return recordingNumber;
   }
   /**
    * Sets the operator number
    */
   public void setRecordingNumber(String recordingNumber){
      this.recordingNumber = recordingNumber;
   }
}
