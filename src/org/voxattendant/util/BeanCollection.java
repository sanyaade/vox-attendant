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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class BeanCollection {
   /**
    *
    */
   private List beanList = null;                   // the original list
   private List sortedIndexList = null;            // the sorted indice
   /**
    * Default constructor
    */
   public BeanCollection() {
   }
   /**
    * Sets internal bean list to the bean list provided
    */
   public BeanCollection(List beanList){
      this.beanList = beanList;
   }
   /**
    * Adds item to the unsorted list.
    */
   public void addItem(Object itemBean) {
      if(beanList == null)
         beanList = (List) new Vector();
      if(itemBean != null)
         beanList.add(itemBean);
   }
   /**
    * Returns item at the specified index from the unsorted list.
    */
   public Object getItem(int index) {
      if(beanList != null) {
         return beanList.get(index);
      }
      else {
         return null;
      }
  }
   /**
    * Removes the element at the specified position in this list (optional operation).
    * Shifts any subsequent elements to the left (subtracts one from their indices).
    * Returns the element that was removed from the list.
    *
    * @param index the index of the element to removed.
    *
    * @return the element previously at the specified position.
    */
   public Object removeItem(int index) {
      if(beanList != null) {
         return beanList.remove(index);
      }
      else {
         return null;
      }
   }
   /**
    * Removes the first occurrence in this list of the specified element (optional operation).
    * If this list does not contain the element, it is unchanged. More formally, removes the element
    * with the lowest index i such that (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
    *
    * @param o element to be removed from this list, if present.
    *
    * @return true if this list contained the specified element.
    */
   public boolean removeItem(Object o) {
      if(beanList != null) {
         return beanList.remove(o);
      }
      else {
         return false;
      }
   }
   /**
    * Returns the unsorted list of items in the BeanList.
    */
   public List getUnSortedList() {
      return beanList;
   }
   /**
    * Returns the sorted list of items in the BeanList. Null if it has not been
    * sorted before. Otherwise it returns the previously sorted list
    */
   public List getSortedList(){
      return sortedIndexList;
   }
   /**
    * Compares specified bean parameter in the list of beans using default Comparator (BeanComparator).
    * <br>
    *
    * @param sortPropertyName name of the property value of which will be evaluated for sorting
    *
    * @see com.milo.util.sortablebean.BeanComparator
    *
    * @return  <b>sorted list</b> - if sortable; with unsortable entries in the end<br>
    * 	      <b>original list</b> - if none of the entries are sortable<br>
    * 	      <b>null</b> - if original list or property names are not set<br>
    */
   public List getSortedList(String sortPropertyName){
      return getSortedList(sortPropertyName, new BeanComparator(sortPropertyName));
   }
   /**
    * Compares specified bean parameter in the list of beans using specified Comparator.
    * <br>
    *
    * @param sortPropertyName name of the property value of which will be evaluated for sorting
    * @param beanComparator the comparator to determine the order of beans
    *
    * @return  <b>sorted list</b> - if sortable; with unsortable entries in the end<br>
    * 	      <b>original list</b> - if none of the entries are sortable<br>
    * 	      <b>null</b> - if original list or property names are not set<br>
    */
   public List getSortedList(String sortPropertyName, Comparator beanComparator) {
      if(beanList != null && sortPropertyName != null)   {
         sortedIndexList = beanList;
         Collections.sort(sortedIndexList, beanComparator);
         return sortedIndexList;
      }
      else {
         return null;
      }
   }
   /**
    * Compares specified bean parameter in the list of beans using specified Comparator.
    * <br>
    *
    * @param beanComparator the comparator to determine the order of beans
    *
    * @return  <b>sorted list</b> - if sortable; with unsortable entries in the end<br>
    * 	      <b>original list</b> - if none of the entries are sortable<br>
    * 	      <b>null</b> - if original list or property names are not set<br>
    */
   public List getSortedList(Comparator beanComparator) {
      if(beanList != null && beanComparator != null)   {
         sortedIndexList = beanList;
         Collections.sort(sortedIndexList, beanComparator);
         return sortedIndexList;
      }
      else {
         return null;
      }
   }

   /**
    * Returns the number of elements in this list. If this list contains more than
    * Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
    *
    * @return the number of elements in this list.
    */
   public int size(){
      if(beanList != null) {
         return beanList.size();
      }
      else {
         return 0;
      }
   }
}


