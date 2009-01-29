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

public class PromptBean extends CustomizableBean {
   /**
    *
    */
   private int promptId;
   private String name;
   private String audio;
   private boolean active;
   /**
    *
    */
   public int getPromptId(){
     return promptId;
   }
   /**
    *
    */
   public void setPromptId(int promptId) {
      this.promptId = promptId;
   }
   /**
    *
    */
   public String getName(){
     return name;
   }
   /**
    *
    */
   public void setName(String name){
      this.name = name;
   }
   /**
    *
    */
   public String getAudio(){
      return audio;
   }
   /**
    *
    */
   public void setAudio(String audio){
      this.audio = audio;
   }
   /**
    *
    */
   public boolean isActive(){
      return active;
   }
   /**
    *
    */
   public void setActive(boolean active){
      this.active = active;
   }
}
