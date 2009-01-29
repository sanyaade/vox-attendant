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

package org.voxattendant.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.EntityBean;
import org.voxattendant.taglib.AudioTag;
import org.voxattendant.util.BeanCollection;


/**
 *
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2007-02-24 15:50:52 $
 */
public class RecordController extends BaseController {
    
    private static final long serialVersionUID = 6549134292451261424L;
    
    public static final String RECORD_CONTROLLER = "/RecordController";
    
    public void init(javax.servlet.ServletConfig config)
    throws javax.servlet.ServletException {
        
        super.init(config);
    }
    
    
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String reqState;
            if ((reqState = ((String) req.getParameter("reqState"))) == null) {
                reqState = "vxmlShowLogin";
            }
            // //
            System.out.println("RecordController ReqState is: " + reqState);
            // //
            if (reqState.equals("vxmlShowRecord")) {
                showRecord(req, res);
            } else if (reqState.equals("vxmlShowLogin")) {
                showLogin(req, res);
            } else if (reqState.equals("vxmlProcessLogin")) {
                processLogin(req, res);
            } else if (reqState.equals("vxmlStartRecord")) {
                startRecord(req, res);
            } else if (reqState.equals("vxmlShowRecordContact")) {
                req.setAttribute("jumpToContact", "true");
                showRecord(req, res);
            } else if (reqState.equals("vxmlShowRecordEntity")) {
                req.setAttribute("jumpToEntity", "true");
                showRecord(req, res);
            } else {
                displayVxmlJsp(req, res, "error.jsp");
            }
        } catch (Exception e) {
            System.err.println("Error: RecordController");
            e.printStackTrace();
        }
    }
    
    /**
     *
     */
    protected void showLogin(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        displayVxmlJsp(req, res, "recordLogin.jsp");
    }
    
    /**
     *
     */
    protected void processLogin(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        String passcode = req.getParameter("passcode");
        
        ApplicationBean appBean = retrieveApplicationBean();
        // check if passcode matches
        if (appBean.getPasscode().equals(passcode)) {
            storeUserSession(req);
            displayVxmlJsp(req, res, "recordLogin2.jsp");
        } else {
            req.setAttribute("error", "true");
            showLogin(req, res);
        }
    }
    
    /**
     *
     */
    protected void showRecord(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        displayVxmlJsp(req, res, "recordInit.jsp");
    }
    
    /**
     *
     */
    protected void startRecord(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        String fileid = req.getParameter("fileid");
        String recCategory = req.getParameter("recCategory");
        String recType = req.getParameter("recType");
        // //
        System.out.println("FileId: " + fileid);
        System.out.println("Rec Category: " + recCategory);
        System.out.println("Rec Type: " + recType);
        // //
        
        String type = null;
        if (recCategory.equals(AudioTag.AUDIO_TYPE_CONTACT)
        && recType.equals(AudioTag.AUDIO_TYPE_DISTINCTINFO))
            type = recType;
        else
            type = recCategory;
        
        // Check if the id is valid...
        if (!validFileId(req, fileid, type)) {
            // //
            System.out.println("*******************>>> Invalid ID");
            // //
            req.setAttribute("error", "true");
            req.setAttribute("category", recCategory);
            req.setAttribute("type", recType);
            showRecord(req, res);
        } else {
            String audioName = "";
            String audioTTS = fetchTTS(req, type, fileid);
            
            req.setAttribute("audioTTS", audioTTS);
            req.setAttribute("audioName", audioName);
            req.setAttribute("recCategory", recCategory);
            displayVxmlJsp(req, res, "record.jsp");
        }
    }
    
    /**
     * A quick hack...
     */
    protected String fetchTTS(HttpServletRequest req, String type, String id)
    throws ServletException, IOException {
        
        if (type.equals(AudioTag.AUDIO_TYPE_CONTACT)) {
            BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
            if (contactBeanList == null) {
                contactBeanList = createContactSortableBeanList(req);
            }
            ContactBean contactBean = findContactBeanById(contactBeanList, id);
            return (contactBean.getFirstname() + " " + contactBean
                    .getLastname());
        } else if (type.equals(AudioTag.AUDIO_TYPE_ENTITY)) {
            BeanCollection entityBeanList = retrieveEntityBeanList(req);
            if (entityBeanList == null) {
                entityBeanList = createEntityBeanList(req);
            }
            EntityBean entityBean = findEntityBeanById(entityBeanList, id);
            return entityBean.getName();
        } else if (type.equals(AudioTag.AUDIO_TYPE_DISTINCTINFO)) {
            BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
            if (contactBeanList == null) {
                contactBeanList = createContactSortableBeanList(req);
            }
            ContactBean contactBean = findContactBeanById(contactBeanList, id);
            return contactBean.getDistinctInfo();
        } else
            return "";
    }
    
    /**
     *
     */
    protected boolean validFileId(HttpServletRequest req, String id, String type)
    throws ServletException, IOException {
        
        if (type.equals(AudioTag.AUDIO_TYPE_CONTACT)
        || type.equals(AudioTag.AUDIO_TYPE_DISTINCTINFO)) {
            BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
            if (contactBeanList == null) {
                contactBeanList = createContactSortableBeanList(req);
            }
            ContactBean contactBean = findContactBeanById(contactBeanList, id);
            // //
            System.out.println("ContactBean: " + contactBean);
            // //
            return (contactBean != null);
        } else if (type.equals(AudioTag.AUDIO_TYPE_ENTITY)) {
            BeanCollection entityBeanList = retrieveEntityBeanList(req);
            if (entityBeanList == null) {
                entityBeanList = createEntityBeanList(req);
            }
            EntityBean entityBean = findEntityBeanById(entityBeanList, id);
            
            return (entityBean != null);
        } else
            return false;
    }
}
