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

import java.util.Vector;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.voxattendant.manager.LoggerManager;


public class LoggerBean implements HttpSessionBindingListener {

   public static final int MASK_OPERATOR_BY_USER      = 0x00000001;
   public static final int MASK_OPERATOR_BY_ERROR     = 0x00000002;
   public static final int MASK_CORPORATE_DIRECTION   = 0x00000004;
   public static final int MASK_APPLICATION_INFO      = 0x00000008;

   private int loggerId;
   private long startTimestamp;
   private long endTimestamp;
   private String callerId;
   private int requestedId;
   private int ownerTypeId;
   private int appInfoBits = 0x00000000;
   private Vector cancellations = new Vector();

   public LoggerBean() {
   }

   public int getLoggerId() {
      return loggerId;
   }

   public void setLoggerId(int loggerId) {
      this.loggerId = loggerId;
   }

   public long getStartTimestamp() {
      return startTimestamp;
   }

   public void setStartTimestamp(long startTimestamp) {
      this.startTimestamp = startTimestamp;
   }

   public long getEndTimestamp() {
      return endTimestamp;
   }

   public void setEndTimestamp(long endTimestamp) {
      this.endTimestamp = endTimestamp;
   }

   public String getCallerId() {
      if(callerId == null)
         return "";
      else
         return callerId;
   }

   public void setCallerId(String callerId) {
      this.callerId  = callerId;
   }

   public int getRequestedId() {
      return requestedId;
   }

   public void setRequestedId(int requestedId) {
      this.requestedId = requestedId;
   }

   public int getOwnerTypeId() {
      return ownerTypeId;
   }

   public void setOwnerTypeId(int ownerTypeId) {
      this.ownerTypeId = ownerTypeId;
   }

   public int getAppInfoBits() {
      return appInfoBits;
   }

   public void setAppInfoBits(int mask) {
      appInfoBits = setBit(appInfoBits, mask);
   }

   public void addCancellation(int id, int ownerTypeId) {
      ////
      //System.out.println("Cancelled: " + id + " Type: " + ownerTypeId);
      ////
      CancellationBean cancellation = new CancellationBean();
      cancellation.setCancelledId(id);
      cancellation.setOwnerTypeId(ownerTypeId);

      cancellations.add(cancellation);
   }

   public Vector getCancellations() {
      return cancellations;
   }

   public void dump() {
      ////
      System.out.println("====== DUMP =====");
      System.out.println("Start: " + startTimestamp + " End: " + endTimestamp);
      System.out.println("CallerId: " + callerId);
      System.out.println("RequestedId: " + requestedId);
      System.out.println("OwnerType: " + ownerTypeId);
      for(int i = 0; i < cancellations.size(); i++) {
         CancellationBean cancellation = (CancellationBean)cancellations.get(i);
         System.out.println("Cancelled: " + cancellation.getCancelledId());
      }
      System.out.println("AppInfoBits: " + Integer.toBinaryString(appInfoBits));
      System.out.println("======= END of DUMP ==========");
      ////
   }
   //
   // Helper functions
   //
   /**
    * Determine if a bit is set
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return boolean true if it is set, otherwise false
    */
   public static boolean isBitOn(int bits, int mask) {
      return (0 != (bits & mask));
   }
   /**
    * Setting the modified grammar bit on
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return int the modified grammar
    */
   public static int setBit(int bits, int mask) {
      return (bits | mask);
   }
   /**
    * Unsets the modified grammar bit
    *
    * @param bits the grammar bits
    * @param mask the mask used to set the bit
    * @return int the modified grammar
    */
   public static int unsetBit(int bits, int mask) {
      return (bits & ~mask);
   }

   //
   // Implements HttpSessionBindingListener methods
   //

   synchronized public void valueBound(HttpSessionBindingEvent event) {
      ////
      System.out.println("In ValueBound");
      ////
   }

   synchronized public void valueUnbound(HttpSessionBindingEvent event) {
      ////
      System.out.println("In ValueUnbound");
      ////
      if(LoggerManager.insertLog(this)) {
         ////
         System.out.println("--------- Successfully insert into database.");
         ////
      }
      else {
         ////
         System.out.println("xxxxxxxxx Failed to insert into database.");
         ////
      }
      ////
      dump();
      ////
   }
}
