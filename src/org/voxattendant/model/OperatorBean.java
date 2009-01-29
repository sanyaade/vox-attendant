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

public class OperatorBean extends CustomizableBean {
   /**
    *
    */
   public static final int OPERATOR_TYPE_ID = 2;
   /**
    *
    */
   //private int currentOperatorNumberId;
   private BeanCollection phoneNumbers;
   /**
    *
    */
   /*
   public int getCurrentOperatorNumberId(){
     return currentOperatorNumberId;
   }
   */
   /**
    *
    */
   /*
   public void setCurrentOperatorNumberId(int currentOperatorNumberId) {
      this.currentOperatorNumberId = currentOperatorNumberId;
   }
   */
   /**
    *
    */
   public BeanCollection getPhoneNumbers(){
     return phoneNumbers;
   }
   /**
    *
    */
   public void setPhoneNumbers(BeanCollection phoneNumbers){
      this.phoneNumbers = phoneNumbers;
   }
}
