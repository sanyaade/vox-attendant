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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class AlphabetLinkTag implements Tag {
   /**
    *
    */
   protected PageContext pageContext;
   protected Tag parent;

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
      JspWriter out = this.pageContext.getOut();

      try {
         //if "linked" attribute is not set, generate links for A-Z
         if(linked == null || linked.length() < 1) {
            for (char i='A'; i<='Z'; i++) {
              Character thisChar = new Character(i);
              out.print("<a href=\"#" + thisChar + "\">" + thisChar + "</a>&nbsp;&nbsp;");
            }

         } else {
         //otherwise, only put links for the letters specified
            for (char i='A'; i<='Z'; i++) {
              Character thisChar = new Character(i);
              if(linked.indexOf(i) > -1) {
                 out.print("<a href=\"#" + thisChar + "\">" + thisChar + "</a>&nbsp;&nbsp;");
              } else {
                 out.print(thisChar + "&nbsp;&nbsp;");
              }
            }
         }
      }
      catch(java.io.IOException e)
      {
         throw new JspTagException("IO Error: " + e.getMessage());
      }
      return EVAL_BODY_INCLUDE;
   }
   public int doEndTag() throws JspException {
      try
      {
         pageContext.getOut().write("</audio>");
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
   // Attribute: type
   protected String linked;

   public void setLinked(String linked) {
      ////
      //System.out.println("Linked: " + linked);
      ////
      // always upper case
      this.linked = linked.toUpperCase();
   }

   public String getLinked() {
      return linked;
   }
}
