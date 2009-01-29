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

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class VxmlEventTag implements Tag {
    /**
     *
     */
    public static final String SET_DELIMITER = ";";
    public static final String PROMPT_DELIMITER = ",";
    /**
     *
     */
    public static final String TYPE_NOINPUT = "noinput";
    public static final String TYPE_NOMATCH = "nomatch";
    public static final String TYPE_HELP = "help";
    /**
     *
     */
    public static final String TTS_NOINPUT = "I'm sorry, I didn't hear anything.";
    public static final String TTS_NOMATCH = "I'm sorry I didn't get that.";
    public static final String TTS_HELP = "Sorry no help is implemented yet.";
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
    public int doStartTag() throws JspException {
        try {
            // Parse out the prompts to find out how many prompts are there...
            Vector set = parsePromptSet(getPromptSet());
            handleTag(set, type);
        } catch(Exception e) {
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
    private String type = null;
    private String finalURI = null;
    private String promptSet = null;
    private boolean reprompt = false;
    
    public String getType() {
        return (this.type);
    }
    public void setType(String type) {
        ////
        //System.out.println("Type: " + type);
        ////
        this.type = type;
    }
    public String getFinalURI() {
        return (this.finalURI);
    }
    public void setFinalURI(String finalURI) {
        ////
        //System.out.println("finalURI: " + finalURI);
        ////
        this.finalURI = finalURI;
    }
    public String getPromptSet() {
        return (this.promptSet);
    }
    public void setPromptSet(String promptSet) {
        ////
        //System.out.println("promptSet: " + promptSet);
        ////
        this.promptSet = promptSet;
    }
    public boolean isReprompt() {
        return reprompt;
    }
    public void setReprompt(boolean reprompt) {
        this.reprompt = reprompt;
    }
    //
    // Helper functions
    //
    /**
     *
     */
    private void handleTag(Vector promptSet, String tag) throws IOException {
        
        JspWriter out = pageContext.getOut();
        
        if(promptSet.size() == 0) {
            // Special case...
            out.println("\t" + startTag(tag, 1));
            if(finalURI != null)
                out.println("\t\t<goto next=\"" + getFinalURI() + "\"/>");
            out.println("\t" + endTag(tag));
        } else {
            for(int i = 0; i < promptSet.size(); i++) {
                Vector prompts = (Vector)promptSet.get(i);
                Iterator it = prompts.iterator();
                
                boolean first = true;
                out.println("\t" + startTag(tag, i+1));
                while(it.hasNext()) {
                    out.println("\t\t<audio src=\"" + (String)it.next() + "\">");
                    if(first) {
                        out.println("\t\t" + tts(tag));
                        first = false;
                    }
                    out.println("\t\t</audio>");
                }
                if((i == promptSet.size() - 1) &&
                        finalURI != null) {
                    out.println("\t\t<goto next=\"" + getFinalURI() + "\"/>");
                } else if(isReprompt()) {
                    out.println("\t\t<reprompt/>");
                }
                out.println("\t" + endTag(tag));
            }
        }
    }
    /**
     *
     */
    private Vector parsePromptSet(String promptSet) {
        
        Vector vSet = new Vector();
        
        if(promptSet != null) {
            StringTokenizer tSet = new StringTokenizer(promptSet, SET_DELIMITER);
            while(tSet.hasMoreTokens()) {
                String prompts = tSet.nextToken();
                StringTokenizer tPrompts = new StringTokenizer(prompts, PROMPT_DELIMITER);
                
                Vector vPrompts = new Vector();
                while(tPrompts.hasMoreTokens()) {
                    String prompt = tPrompts.nextToken();
                    vPrompts.add(prompt.trim());
                }
                vSet.add(vPrompts);
            }
        }
        return vSet;
    }
    /**
     *
     */
    private String startTag(String tag, int count) {
        return ("<catch event=\"" + tag + "\" count=\"" + count + "\">");
    }
    private String endTag(String tag) {
        return ("</catch>");
    }
    private String tts(String tag) {
        if(tag.equals(TYPE_NOINPUT))
            return TTS_NOINPUT;
        if(tag.equals(TYPE_NOMATCH))
            return TTS_NOMATCH;
        if(tag.equals(TYPE_HELP))
            return TTS_HELP;
        return "";
    }
}
