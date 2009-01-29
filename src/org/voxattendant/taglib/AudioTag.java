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

public class AudioTag implements Tag {
   /**
    *
    */
   public static final String AUDIO_TYPE_CONTACT = "contact"; // default...
   public static final String AUDIO_TYPE_ENTITY = "entity";
   public static final String AUDIO_TYPE_DISTINCTINFO = "distinctinfo";
   /**
    *
    */
//   public static final String AUDIO_PREFIX_CONTACT = "fn";
//   public static final String AUDIO_PREFIX_ENTITY = "en";
//   public static final String AUDIO_PREFIX_DISTINCTINTO = "di";
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
         String audioPath = audiodir + "/" + name;
         pageContext.getOut().write("<audio src=\"" + audioPath + "\">");
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
   protected String type;

   public void setType(String type) {
      ////
      //System.out.println("Type: " + type);
      ////
      this.type = type;
   }

   public String getType() {
      return type;
   }

   // Attribute: id
   protected String name;

   public void setName(String name) {
      ////
      //System.out.println("Id: " + id);
      ////
      this.name = name;
   }

   public String getName() {
      return name;
   }

   // Attribute: audiodir
   protected String audiodir;

   public void setAudiodir(String audiodir) {
      ////
      //System.out.println("Audiodir: " + audiodir);
      ////
      this.audiodir = audiodir;
   }

   public String getAudiodir() {
      return audiodir;
   }
   
}
