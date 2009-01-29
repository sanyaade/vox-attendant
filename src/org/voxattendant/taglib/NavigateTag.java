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

public class NavigateTag implements Tag {
   /**
    *
    */
   public static final String NAVIGATE_VALUE_FIRST = "first";
   public static final String NAVIGATE_VALUE_MIDDLE = "middle";
   public static final String NAVIGATE_VALUE_LAST = "last";
   public static final String NAVIGATE_VALUE_ONLY = "only";

   public static final String NAVIGATE_TYPE_BUTTON = "button";
   public static final String NAVIGATE_TYPE_SUBMIT = "submit";

   public static final String BUTTON_VALUE_PREVIOUS=" value=\"Previous\"";
   public static final String BUTTON_VALUE_NEXT = " value=\"&nbsp;&nbsp;&nbsp;Next&nbsp;&nbsp;&nbsp;\"";
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
      try
      {
         if(value.equals(NavigateTag.NAVIGATE_VALUE_MIDDLE)) {

            if(type.equals(NavigateTag.NAVIGATE_TYPE_SUBMIT)) {

               pageContext.getOut().write("<input type=\"submit\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS + ">&nbsp;");

               pageContext.getOut().write("<input type=\"submit\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + ">");

            } else if(type.equals(NavigateTag.NAVIGATE_TYPE_BUTTON)) {
               pageContext.getOut().write("<input type=\"button\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS + " onClick=\"" + onClick + "\">&nbsp;");

               pageContext.getOut().write("<input type=\"button\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + "onClick=\"" + onClick + "\">");
            }

         } else if(value.equals(NavigateTag.NAVIGATE_VALUE_FIRST)) {
            if(type.equals(NavigateTag.NAVIGATE_TYPE_SUBMIT)) {
               pageContext.getOut().write("<input type=\"submit\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS  + " disabled>&nbsp;");
               pageContext.getOut().write("<input type=\"submit\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + ">");

            } else if(type.equals(NavigateTag.NAVIGATE_TYPE_BUTTON)) {
               pageContext.getOut().write("<input type=\"button\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS + " onClick=\"" + onClick + "\" disabled>&nbsp;");

               pageContext.getOut().write("<input type=\"button\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + " onClick=\"" + onClick + "\">");
            }

         } else if(value.equals(NavigateTag.NAVIGATE_VALUE_LAST)) {

            if(type.equals(NavigateTag.NAVIGATE_TYPE_SUBMIT)) {
               pageContext.getOut().write("<input type=\"submit\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS  + ">&nbsp;");
               pageContext.getOut().write("<input type=\"submit\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + " disabled>");

            } else if(type.equals(NavigateTag.NAVIGATE_TYPE_BUTTON)) {
               pageContext.getOut().write("<input type=\"button\" name=\"previous\"" +
                  NavigateTag.BUTTON_VALUE_PREVIOUS + " onClick=\"" + onClick + "\">");

               pageContext.getOut().write("<input type=\"button\" name=\"next\"" +
                  NavigateTag.BUTTON_VALUE_NEXT + " onClick=\"" + onClick + "\" disabled>");
            }
         }
         //note, not going to write anything if type is NavigateTag.NAVIGATE_VALUE_ONLY
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
   // Attribute: value
   protected String value;

   public void setValue(String value) {
      ////
      //System.out.println("value: " + value);
      ////
      this.value = value;
   }

   // Attribute: type
   protected String type;

   public void setType(String type) {
      ////
      //System.out.println("type: " + type);
      ////
      this.type = type;
   }

   public String getType() {
      return type;
   }

   // Attribute: onClick
   protected String onClick;

   public void setOnClick(String onClick) {
      ////
      //System.out.println("onClick: " + onClick);
      ////
      this.onClick = onClick;
   }

   public String getOnClick() {
      return onClick;
   }

}
