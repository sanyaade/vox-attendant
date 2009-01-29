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
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.manager.ContactManager;
import org.voxattendant.manager.EntityManager;
import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.EntityBean;
import org.voxattendant.model.OperatorBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.BeanComparator;
import org.voxattendant.util.UtilBundle;

public class BaseController extends HttpServlet {
    
    private static final long serialVersionUID = -1456551365362186585L;
    
    /**
     * Constants
     */
    public static String REAL_ROOT_PATH = null;
    
    /**
     * Predefined paths to the different types of View.
     */
    protected static final String HTML_PATH_PERFIX = "/view/html/";
    
    protected static final String VXML_PATH_PERFIX = "/view/vxml/";
    
    /**
     * This init() method
     *
     * @param config
     *            The ServletConfig object
     */
    public void init(javax.servlet.ServletConfig config)
    throws javax.servlet.ServletException {
        
        super.init(config);
        REAL_ROOT_PATH = config.getServletContext().getRealPath("/");
        UtilBundle.setWebRootPath(REAL_ROOT_PATH);
    }
    
    /**
     * Helper function. Display the specified HTML JSP file.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     * @param jspName
     *            The name of the JSP file to display
     * @throws ServletException
     * @throws IOException
     */
    protected void displayHtmlJsp(HttpServletRequest req,
            HttpServletResponse res, String jspName) throws IOException,
            ServletException {
        
        
        String filename = HTML_PATH_PERFIX + jspName;
        UtilBundle.log.logMessage("BaseController", "displayHtmlJsp", filename, 3);
        displayJSP(req, res, filename);
    }
    
    /**
     * Helper function. Display the specified VXML JSP file.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     * @param jspName
     *            The name of the JSP file to display
     * @throws ServletException
     * @throws IOException
     */
    protected void displayVxmlJsp(HttpServletRequest req,
            HttpServletResponse res, String jspName) throws IOException,
            ServletException {
        
        String filename = VXML_PATH_PERFIX + jspName;
        displayJSP(req, res, filename);
    }
    
    /**
     * Helper function. Display the specified JSP file.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     * @param jspName
     *            The name of the JSP file to display
     * @throws ServletException
     * @throws IOException
     */
    protected void displayJSP(HttpServletRequest req, HttpServletResponse res,
            String filename) throws IOException, ServletException {
        // //
        System.err.println("Displaying: " + filename);
        UtilBundle.log.logMessage("BaseController", "displayJSP", filename, 3);
        // //
        try {
            this.getServletConfig().getServletContext().getRequestDispatcher(
                    filename).include(req, res);
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * puts the ApplicationBean in the servlet context
     */
    protected void storeApplicationBean(ApplicationBean appBean)
    throws IOException, ServletException {
        getServletContext().setAttribute("ApplicationBean", appBean);
    }
    
    /**
     * gets the ApplicationBean in the current session. Creates one if it is not
     * available.
     */
    protected ApplicationBean retrieveApplicationBean()
    throws ServletException, IOException {
        return retrieveApplicationBean(true);
    }
    
    protected ApplicationBean retrieveApplicationBean(boolean create)
    throws ServletException, IOException {
        
        ApplicationBean appBean = (ApplicationBean) getServletContext()
        .getAttribute("ApplicationBean");
        
        if (appBean == null && create) {
            appBean = new ApplicationBean();
            if (ApplicationManager.lookupApplicationBean(appBean)) {
                storeApplicationBean(appBean);
            } else {
                appBean = null;
            }
        }
        return appBean;
    }
    
    /**
     * puts "ContactSortableBeanList" - the Contact SortableBeanList into the
     * session
     */
    protected void storeContactSortableBeanList(HttpServletRequest req,
            BeanCollection contactSortableBeanList) throws IOException,
            ServletException {
        
        HttpSession session = req.getSession();
        if (session == null) {
            return;
        }
        session
                .setAttribute("ContactSortableBeanList",
                contactSortableBeanList);
    }
    
    /**
     * gets the "ContactSortableBeanList" in the current session
     */
    protected BeanCollection retrieveContactSortableBeanList(
            HttpServletRequest req) throws IOException, ServletException {
        
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        return (BeanCollection) session.getAttribute("ContactSortableBeanList");
    }
    
    /**
     * gets the "ContactList" in the current session
     */
    protected List retrieveContactList(HttpServletRequest req)
    throws IOException, ServletException {
        // //
        System.out.println("retrieveContactList....");
        // //
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        BeanCollection beanCollection = (BeanCollection) session
                .getAttribute("ContactSortableBeanList");
        if (beanCollection == null || beanCollection.size() < 1) {
            return null;
        } else {
            return getSortedContactList(beanCollection);
        }
    }
    
    /**
     *
     */
    protected BeanCollection createContactSortableBeanList(
            HttpServletRequest req) throws IOException, ServletException {
        
        BeanCollection contactSortableBeanList = new BeanCollection();
        
        if (ContactManager.lookupAllContacts(contactSortableBeanList)) {
            ;
            storeContactSortableBeanList(req, contactSortableBeanList);
            return contactSortableBeanList;
        } else {
            return null;
        }
    }
    
    /**
     *
     */
    protected List findContactBeanByIds(BeanCollection contactBeanList,
            String[] contactIds) {
        ContactBean[] selectedIds = new ContactBean[contactIds.length];
        ContactBean contactBean = null;
        
        for (int i = 0; i < contactBeanList.size(); i++) {
            contactBean = (ContactBean) contactBeanList.getItem(i);
            for (int j = 0; j < contactIds.length; j++) {
                if (contactBean.getContactId() == Integer
                        .parseInt(contactIds[j])) {
                    selectedIds[j] = contactBean;
                    break;
                }
            }
        }
        return Arrays.asList(selectedIds);
    }
    
    /**
     *
     */
    protected ContactBean findContactBeanById(BeanCollection contactBeanList,
            String contactId) {
        return findContactBeanById(contactBeanList, Integer.parseInt(contactId));
    }
    
    /**
     *
     */
    protected ContactBean findContactBeanById(BeanCollection contactBeanList,
            int contactId) {
        for (int i = 0; i < contactBeanList.size(); i++) {
            if (((ContactBean) contactBeanList.getItem(i)).getContactId() == contactId) {
                return (ContactBean) contactBeanList.getItem(i);
            }
        }
        return null;
    }
    
    protected ContactBean findContactBeanByAni(BeanCollection contactBeanList,
            String ani) {
        for (int i = 0; i < contactBeanList.size(); i++) {
            if (((ContactBean) contactBeanList.getItem(i)).getHomePhoneNumber().equals(ani)) {
                return (ContactBean) contactBeanList.getItem(i);
            }
            else if (((ContactBean) contactBeanList.getItem(i)).getWorkPhoneNumber().equals(ani)) {
                return (ContactBean) contactBeanList.getItem(i);
            }
            else if (((ContactBean) contactBeanList.getItem(i)).getMobilePhoneNumber().equals(ani)) {
                return (ContactBean) contactBeanList.getItem(i);
            }
        }
        return null;
    }
    
    /**
     *
     */
    protected BeanCollection retrievePromptBeanList(HttpServletRequest req)
    throws IOException, ServletException {
        
        HttpSession session = req.getSession();
        if (session == null)
            return null;
        
        BeanCollection promptBeanList = (BeanCollection) session
                .getAttribute("promptBeanList");
        if (promptBeanList == null) {
            promptBeanList = new BeanCollection();
            if (ApplicationManager.lookupAllPrompts(promptBeanList))
                session.setAttribute("promptBeanList", promptBeanList);
        }
        return promptBeanList;
    }
    
    /**
     * Todo: Do we need to put it into the session?
     */
    protected OperatorBean retrieveOperatorBean(HttpServletRequest req)
    throws IOException, ServletException {
        OperatorBean operatorBean = new OperatorBean();
        ApplicationManager.lookupOperatorNumbers(operatorBean);
        return operatorBean;
    }
    
    /**
     * stores the BeanCollection of entities in the session
     */
    protected void storeEntityBeanList(HttpServletRequest req,
            BeanCollection entityBeanList) throws IOException, ServletException {
        
        HttpSession session = req.getSession();
        if (session == null) {
            return;
        }
        session.setAttribute("EntityBeanList", entityBeanList);
    }
    
    /**
     * creates the EntityBeanList
     */
    protected BeanCollection createEntityBeanList(HttpServletRequest req)
    throws IOException, ServletException {
        
        BeanCollection entityBeanList = new BeanCollection();
        if (EntityManager.lookupAllEntities(entityBeanList)) {
            storeEntityBeanList(req, entityBeanList);
            return entityBeanList;
        } else {
            return null;
        }
    }
    
    /**
     * retrieves the BeanCollection of entities from the session
     */
    protected BeanCollection retrieveEntityBeanList(HttpServletRequest req)
    throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        return (BeanCollection) session.getAttribute("EntityBeanList");
    }
    
    /**
     *
     */
    protected List findEntityBeanByIds(BeanCollection entityBeanList,
            String[] entityIds) {
        // EntityBean[] selectedIds = new EntityBean[entityIds.length];
        Vector selectedIds = new Vector();
        EntityBean entityBean = null;
        
        for (int j = 0; j < entityIds.length; j++) {
            
            for (int i = 0; i < entityBeanList.size(); i++) {
                
                entityBean = (EntityBean) entityBeanList.getItem(i);
                
                if (entityBean.getEntityId() == Integer.parseInt(entityIds[j])) {
                    // selectedIds[j] = entityBean;
                    selectedIds.add(entityBean);
                    break;
                }
            }
        }
        for (int i = 0; i < selectedIds.size(); i++)
            System.out.println("Found ID: "
                    + ((EntityBean) selectedIds.get(i)).getEntityId());
        // return Arrays.asList(selectedIds);
        return selectedIds;
    }
    
    /**
     *
     */
    protected EntityBean findEntityBeanById(BeanCollection entityBeanList,
            String entityId) {
        return findEntityBeanById(entityBeanList, Integer.parseInt(entityId));
    }
    
    /**
     *
     */
    protected EntityBean findEntityBeanById(BeanCollection entityBeanList,
            int entityId) {
        for (int i = 0; i < entityBeanList.size(); i++) {
            if (((EntityBean) entityBeanList.getItem(i)).getEntityId() == entityId) {
                return (EntityBean) entityBeanList.getItem(i);
            }
        }
        return null;
    }
    
    /**
     * returns the subEntities of a given parentId
     */
    protected BeanCollection findSubEntities(BeanCollection entityBeanList,
            String parentId) {
        EntityBean parentEntityBean = findEntityBeanById(entityBeanList,
                parentId);
        if (parentEntityBean == null) {
            return null;
        }
        return parentEntityBean.getSubEntities();
    }
    
    /**
     * return all the top level Entities
     */
    protected BeanCollection findTopLevelEntities(BeanCollection entityBeanList) {
        BeanCollection topLevelEntityList = null;
        EntityBean entityBean = null;
        List sortedList = null;
        if (entityBeanList.size() > 0) {
            sortedList = entityBeanList.getSortedList("parentId");
            topLevelEntityList = new BeanCollection();
            
            for (int i = 0; i < sortedList.size(); i++) {
                entityBean = (EntityBean) sortedList.get(i);
                if (entityBean.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
                    topLevelEntityList.addItem(entityBean);
                } else {
                    break;
                }
            }
        } else {
            System.out.println("entityBeanList has no items");
        }
        return topLevelEntityList;
    }
    
    /**
     *
     */
    protected void storeUserSession(HttpServletRequest req) throws IOException,
            ServletException {
        HttpSession session = req.getSession();
        if (session == null) {
            return;
        }
        session.setAttribute("user", "true");
    }
    
    /**
     *
     */
    protected boolean isUserSessionValid(HttpServletRequest req)
    throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session == null) {
            return false;
        }
        return (session.getAttribute("user") != null);
    }
    
    /**
     *
     */
    protected List getSortedContactList(BeanCollection contactBeanList) {
        Vector comparePropertyNames = new Vector();
        comparePropertyNames.add("lastname");
        comparePropertyNames.add("firstname");
        comparePropertyNames.add("contactId");
        
        BeanComparator bc = new BeanComparator(comparePropertyNames);
        return contactBeanList.getSortedList(bc);
    }
    
    /**
     *
     */
    protected List getSortedEntityList(BeanCollection entityBeanList) {
        Vector comparePropertyNames = new Vector();
        comparePropertyNames.add("name");
        comparePropertyNames.add("entityId");
        
        BeanComparator bc = new BeanComparator(comparePropertyNames);
        return entityBeanList.getSortedList(bc);
    }
    
    public void destroy() 
    {
        UtilBundle.getDBUtil().destroyPool();
    }
}
