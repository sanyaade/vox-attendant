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

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.voxattendant.util.Text;

public class DisplayTextTag implements Tag {
   /**
    *
    */
   private PageContext pageContext;
   private Tag parent;

   public void setPageContext(PageContext pc) {
      pageContext = pc;
   }
   public void setParent(Tag t) {
      parent = t;
   }
   public Tag getParent() {
      return parent;
   }
   /**
    * Currently I am assuming that the phone number passed in is a 10-digit
    * string.
    */
   public int doStartTag() throws JspException {
      try {
         StringBuffer buffer = new StringBuffer();

         //
         //determine which way the string should be formatted
         //
         if(format.equalsIgnoreCase("phonenumber")) {
            formatPhoneNumber(buffer);

         } else if(format.equalsIgnoreCase("title")) {
            formatTitle(buffer);
         }
         JspWriter out = pageContext.getOut();
         ////
         //System.out.println("PhoneNumber: " + buffer.toString());
         ////
         out.println(buffer.toString());
      }
      catch(Exception e) {
         e.printStackTrace();
      }
      return (SKIP_BODY);
   }
   public int doEndTag() throws JspException {
      return (EVAL_PAGE);
   }
   public void release() {
   }
   //
   // Accessor methods
   //
   private String format = null;
   private String text = null;

   public String getFormat() {
      return (this.format);
   }
   public void setFormat(String format) {
      ////
      //System.out.println(format);
      ////
      this.format = format;
   }
   public String getText() {
      return (this.text);
   }
   public void setText(String text) {
      ////
      //System.out.println("SetText: " + text);
      ////
      this.text = text;
   }

   //
   // Helper methods
   //

   /**
    * formats the text as a phone number
    * for example: (415)678-4187
    */
   private boolean formatPhoneNumber(StringBuffer buffer) {
      boolean result = false;

      if(text.length() == 0) {
         buffer.append("(none)");
      } else if(text.length() < 10 || text.indexOf("sip") >= 0) {
         //if the phone number is less than 10 digits
         //don't do any formatting
         buffer.append(text);
      } else {
         buffer.append("(");
         buffer.append(text.substring(0, 3));
         buffer.append(") ");
         buffer.append(text.substring(3, 6));
         buffer.append("-");
         buffer.append(text.substring(6));
         result = true;
      }
      return result;
   }

   /**
    * formats the text so that each word is capitalized
    */
   private boolean formatTitle(StringBuffer buffer) {
      boolean result = false;
      List wordList;
      String word;

      if(text.length() == 0)
         buffer.append("");
      else {
         wordList = Text.getListFromString(text);
         for(int i=0; i<wordList.size(); i++) {
            word = (String) wordList.get(i);
            buffer.append(capitalize(word) + " ");
         }
         result = true;
      }
      return result;
   }

   private String capitalize(String word) {
      if(word.length() == 0) {
         return word;
      } else if(word.length() == 1) {
         return word.substring(0,1).toUpperCase();
      } else {
         return word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
      }
   }
}
