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

package org.voxattendant.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class DigitsGrammarTag implements Tag {
   /**
    *
    */
   private PageContext pageContext;
   private Tag parent;

   public void setPageContext(PageContext pageContext) {
      this.pageContext = pageContext;
   }
   public void setParent(Tag parent) {
      this.parent = parent;
   }
   public Tag getParent() {
      return parent;
   }
   public int doStartTag() throws JspException {
      try
      {
         StringBuffer buffer = new StringBuffer();
         buffer.append("<grammar>\n<![CDATA[\n[\n");
         for(int i = getStart(); i < getStart() + getCount(); i ++) {
            buffer.append("[");
            buffer.append(convertToWord(i));
            if(isDtmf())
               buffer.append(" dtmf-" + i);
            buffer.append("]\t{<" + getSlotname() + " \"" + i + "\">}\n");
         }
         buffer.append("]\n]]>\n");
         ////
         //System.out.println("Buffer: " + buffer);
         ////
         pageContext.getOut().write(buffer.toString());
      }
      catch(Exception e) {
         throw new JspTagException("IO Error: " + e.getMessage());
      }
      return EVAL_PAGE;
   }
   public int doEndTag() throws JspException {
      try
      {
         pageContext.getOut().write("</grammar>\n");
      }
      catch(java.io.IOException e)
      {
         throw new JspTagException("IO Error: " + e.getMessage());
      }
      return EVAL_PAGE;
   }
   public void release() {
   }
   //
   // Accessor methods
   //
   // Attribute: start
   private int start;

   public void setStart(int start) {
      ////
      System.out.println("Start: " + start);
      ////
      this.start = start;
   }

   public int getStart() {
      return start;
   }

   // Attribute: count
   private int count;

   public void setCount(int count) {
      ////
      System.out.println("Count: " + count);
      ////
      this.count = count;
   }

   public int getCount() {
      return count;
   }

   // Attribute: slotname
   private String slotname;

   public void setSlotname(String slotname) {
      ////
      System.out.println("Slotname: " + slotname);
      ////
      this.slotname = slotname;
   }

   public String getSlotname() {
      return slotname;
   }

   // Attribute: dtmf
   private boolean dtmf;

   public void setDtmf(boolean dtmf) {
      ////
      System.out.println("DTMF: " + dtmf);
      ////
      this.dtmf = dtmf;
   }

   public boolean isDtmf() {
      return dtmf;
   }
   //
   // Helper functions
   //
   protected String convertToWord(int i) {
      switch(i) {
         case 1: return "one";
         case 2: return "two";
         case 3: return "three";
         case 4: return "four";
         case 5: return "five";
         case 6: return "six";
         case 7: return "seven";
         case 8: return "eight";
         case 9: return "nine";
         case 0:
         default: return "zero";
      }
   }
}
