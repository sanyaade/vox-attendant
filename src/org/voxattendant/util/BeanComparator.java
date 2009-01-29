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

package org.voxattendant.util;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Vector;


public class BeanComparator implements Comparator {
   /**
    *
    */
   private Vector comparePropertyNames;
   /**
    * Constructs a new BeanComparator with provided compare property name.
    */
   public BeanComparator(String comparePropertyName){
      this.comparePropertyNames = new Vector();
      this.comparePropertyNames.add(comparePropertyName);
   }
   /**
    * Constructs a new BeanComparator with provided Vector of compare property names
    */
   public BeanComparator(Vector comparePropertyNames ) {
      this.comparePropertyNames = comparePropertyNames;
   }
   /**
    * Compares two arguments for order. Returns a negative integer, zero, or a positive integer
    * as the first argument is less than, equal to, or greater than the second.
    *
    * <br><br>
    * If one of the arguments is null, it would be considered greater and if both items are null
    * they are considered equal.<br>
    */
   public int compare(Object o1, Object o2) {
      Object val1;
      Object val2;
      String comparePropertyName;
      int result = 0;

      for(int i=0; i < comparePropertyNames.size(); i++) {
         comparePropertyName = (String) comparePropertyNames.get(i);

         val1 = getObjectPropertyValue(o1,comparePropertyName);
         val2 = getObjectPropertyValue(o2,comparePropertyName);

         if(val1 == null && val2 == null) {
            result = 0;//equal
         } else if(val1 == null) {
            result = 1;//put after 2nd
         } else if(val2 == null) {
            result = -1;//put before 2nd
         } else {
            result = ((Comparable) val1).compareTo(val2);//compare objects
         }

         if(result != 0) {
            break;
         }
      }
      return result;
   }
   /**
    * Returns the value of getPropertyName() of the object by capitalizing the
    * first letter of the propertyName String given in the constructor and
    * attempting to invoke the getProperyName() method of the object using reflection.
    *
    * <br><br>
    * So in order to extract property named "description" BeanComparator will attempt to invoke
    * getDescription() method, value of which it will then be used for comparison.
    */
   private Object getObjectPropertyValue(Object obj, String propertyName) {
      Object propertyValue = null;
      try {
         Class c = obj.getClass();
         if(propertyName != null){
            String methodName = "get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
            Method m = c.getMethod(methodName, new Class[0]);
            propertyValue = m.invoke(obj, new Object[0]);//setting value here
         }
      }
      catch(Exception e) {
         //	e.printStackTrace(System.err);
      }
      return propertyValue;
   }
}

