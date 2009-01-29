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
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.voxattendant.manager.AltSpellingManager;
import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.manager.EntityManager;
import org.voxattendant.model.AltSpellingBean;
import org.voxattendant.model.EntityBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.FormError;
import org.voxattendant.util.GrammarBuilder;

public class EntityController extends BaseController {
    
    private static final long serialVersionUID = 7177692508592286231L;
    
    public static final String ENTITY_PREFIX = "entity-";
    
    public static final String ENTITY_NAVIGATE_ONLY = "only";
    
    public static final String ENTITY_NAVIGATE_MIDDLE = "middle";
    
    public static final String ENTITY_NAVIGATE_FIRST = "first";
    
    public static final String ENTITY_NAVIGATE_LAST = "last";
    
    /**
     *
     */
    public static final String ERROR_ID_NOTFOUND = "That department/sub-department could not be located. Most likely it has already been deleted.";
    
    
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        try {
            if (!isUserSessionValid(req)) {
                getServletContext().getRequestDispatcher(
                        LoginController.CONTROLLER).forward(req, res);
                return;
            }
            
            String reqState = req.getParameter("reqState");
            if (reqState == null || reqState.length() == 0) {
                reqState = "htmlShowEntities";
            }
            // //
            System.out.println("EntityController ReqState is: " + reqState);
            // //
            if (reqState.equals("htmlShowEntities")) {
                htmlShowEntities(req, res);
            } else if (reqState.equals("htmlShowEntity")) {
                htmlShowEntity(req, res);
            } else if (reqState.equals("htmlShowEntityAdd")) {
                htmlShowEntityAdd(req, res);
            } else if (reqState.equals("htmlShowSubEntityAdd")) {
                htmlShowSubEntityAdd(req, res);
            } else if (reqState.equals("htmlShowEntityEdit")) {
                htmlShowEntityEdit(req, res);
            } else if (reqState.equals("htmlShowEntityEditAll")) {
                htmlShowEntityEditAll(req, res);
            } else if (reqState.equals("htmlShowEntitySpelling")) {
                htmlShowEntitySpelling(req, res);
            } else if (reqState.equals("htmlShowEntityNavigate")) {
                htmlShowEntityNavigate(req, res);
            } else if (reqState.equals("htmlProcessEntityDelete")) {
                htmlProcessEntityDelete(req, res);
            } else if (reqState.equals("htmlProcessSubEntityDelete")) {
                htmlProcessSubEntityDelete(req, res);
            } else if (reqState.equals("htmlProcessEntityAdd")) {
                htmlProcessEntityAdd(req, res);
            } else if (reqState.equals("htmlProcessEntityEdit")) {
                htmlProcessEntityEdit(req, res);
            } else if (reqState.equals("htmlProcessEntityEditAll")) {
                htmlProcessEntityEditAll(req, res);
            } else if (reqState.equals("htmlProcessEntitySpellingEdit")) {
                htmlProcessEntitySpellingEdit(req, res);
            } else {
                // //
                System.out.println("EntityController reqState: " + reqState
                        + " does not match any known reqStates");
                // //
                htmlShowEntities(req, res);
            }
        } catch (Exception e) {
            // //
            System.err.println("Error: EntityController");
            // //
            e.printStackTrace();
        }
    }
    
    /**
     * displays all the entities
     */
    private void htmlShowEntities(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        BeanCollection entities = retrieveEntityBeanList(req);
        if (entities == null) {
            entities = createEntityBeanList(req);
            storeEntityBeanList(req, entities);
        }
        
        BeanCollection topLevelEntities = findTopLevelEntities(entities);
        if (topLevelEntities != null) {
            List sortedList = getSortedEntityList(topLevelEntities);
            
            req.setAttribute("entityList", sortedList);
        }
        displayHtmlJsp(req, res, "entities.jsp");
    }
    
    /**
     * displays the read only details for an entity
     */
    private void htmlShowEntity(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        if (entity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        
        // entity found, put in the request
        req.setAttribute("entity", entity);
        
        // check if this is a subEntity. if it is, put the parentEntity in the
        // request
        EntityBean parentEntity = null;
        if (entity.getParentId() != EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            parentEntity = findEntityBeanById(entityBeanList, entity
                    .getParentId());
            if (parentEntity == null) {
                displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
                return;
            }
            req.setAttribute("parentEntity", parentEntity);
            
            // set the entityBeanList to be the subEntities list
            entityBeanList = parentEntity.getSubEntities();
        }
        if (entityBeanList != null) {
            req.setAttribute("navigate", setNavigate(entityBeanList, entity));
        }
        displayHtmlJsp(req, res, "entity.jsp");
    }
    
    /**
     * displays add entity form
     */
    private void htmlShowEntityAdd(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        displayHtmlJsp(req, res, "entityEdit.jsp");
    }
    
    /**
     * displays add sub entity form
     */
    private void htmlShowSubEntityAdd(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        BeanCollection entities = retrieveEntityBeanList(req);
        EntityBean parentEntity = findEntityBeanById(entities, entityId);
        if (parentEntity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        
        req.setAttribute("parentEntity", parentEntity);
        displayHtmlJsp(req, res, "entityEdit.jsp");
    }
    
    /**
     * displays the form for editing selected entity
     */
    private void htmlShowEntityEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        if (entity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        // contact found, put in the request, and redirect
        req.setAttribute("entity", entity);
        
        // determine which navigation buttons to show
        if (entity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            req.setAttribute("navigate", setNavigate(entityBeanList, entity));
        } else {
            EntityBean parent = findEntityBeanById(entityBeanList, entity
                    .getParentId());
            if (parent != null && parent.getSubEntities() != null) {
                req.setAttribute("navigate", setNavigate(parent
                        .getSubEntities(), entity));
            }
            req.setAttribute("parentEntity", parent);
        }
        displayHtmlJsp(req, res, "entityEdit.jsp");
    }
    
    /**
     * displays displays the form for editing all the entity phone numbers
     */
    private void htmlShowEntityEditAll(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        BeanCollection entities = retrieveEntityBeanList(req);
        if (entities == null) {
            entities = createEntityBeanList(req);
            storeEntityBeanList(req, entities);
        }
        
        BeanCollection topLevelEntities = findTopLevelEntities(entities);
        if (topLevelEntities != null) {
            List sortedList = getSortedEntityList(topLevelEntities);
            req.setAttribute("entityList", sortedList);
        }
        displayHtmlJsp(req, res, "entityEditAll.jsp");
    }
    
    /**
     * displays the form for editing spellings of selected entity
     */
    private void htmlShowEntitySpelling(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        String buttonPressed = req.getParameter("buttonPressed");
        if (buttonPressed == null)
            buttonPressed = "";
        
        BeanCollection entityBeanList = this.retrieveEntityBeanList(req);
        
        EntityBean entity = null;
        if (buttonPressed.equalsIgnoreCase("next")) {
            entity = findNextEntityBean(entityBeanList, Integer
                    .parseInt(entityId));
        } else if (buttonPressed.equalsIgnoreCase("previous")) {
            entity = findPreviousEntityBean(entityBeanList, Integer
                    .parseInt(entityId));
        } else {
            entity = findEntityBeanById(entityBeanList, entityId);
        }
        
        if (entity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        // entity found
        req.setAttribute("entity", entity);
        
        // check to see if there is a parentEntity
        EntityBean parentEntity = this.getParentEntity(entityBeanList, entity);
        if (parentEntity != null) {
            req.setAttribute("navigate", setNavigate(parentEntity
                    .getSubEntities(), entity));
            req.setAttribute("parentEntity", parentEntity);
        } else {
            req.setAttribute("navigate", setNavigate(entityBeanList, entity));
        }
        // redirect
        displayHtmlJsp(req, res, "entitySpelling.jsp");
    }
    
    /**
     * delete an entity
     */
    private void htmlProcessEntityDelete(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId != null) {
            deleteEntity(req, entityId);
            // show the list of all Entities
            htmlShowEntities(req, res);
        } else {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
        }
    }
    
    /**
     * delete a sub entity
     */
    private void htmlProcessSubEntityDelete(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        Enumeration params = req.getParameterNames();
        String name;
        String type;
        String id;
        while (params.hasMoreElements()) {
            name = (String) params.nextElement();
            type = name.substring(0, 3);
            id = name.substring(3);
            
            if (type.equals("del")) {
                deleteEntity(req, id);
                break;
            }
        }
        // show the parent entity
        htmlShowEntity(req, res);
    }
    
    /**
     * add an entity
     */
    private void htmlProcessEntityAdd(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            if (req.getParameter("entityId") != null) {
                htmlShowEntity(req, res);
            } else {
                htmlShowEntities(req, res);
            }
            return;
        }
        // if you have reached this point, it means we are going to try to
        // add a the entity
        String name = req.getParameter("name");
        String phoneNumber = req.getParameter("phoneNumber");
        String note = req.getParameter("note");
        String sParentId = req.getParameter("entityId");
        String audioName = req.getParameter("audioName");
        int parentId = 0;
        
        // check to make sure parameters are set
        if (name == null || phoneNumber == null || note == null) {
            // //
            System.out
                    .println("do not have all parameters to proceed with add Contact");
            // //
            htmlShowEntities(req, res);
            return;
        }
        
        // trim the entity name
        name = name.trim();
        
        // set parentId as an int
        if (sParentId != null && sParentId.length() > 0) {
            parentId = Integer.parseInt(sParentId);
        }
        
        // put values in a EntityBean
        EntityBean entity = new EntityBean();
        entity.setName(name);
        entity.setPhoneNumber(phoneNumber);
        entity.setNote(note);
        entity.setActive(true);
        entity.setParentId(parentId);
        entity.setAudioName(audioName);
        
        // add to the session
        if (EntityManager.addEntity(entity)) {
            addEntityInSession(req, entity);
        }
        // redirect
        if (parentId == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            htmlShowEntities(req, res);
        } else {
            htmlShowEntity(req, res);
        }
    }
    
    /**
     * edit an entity
     */
    private void htmlProcessEntityEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            htmlShowEntity(req, res);
            return;
        }
        
        // if you have reached this point, it means we are going to try to do an
        // update on the contact
        String entityId = req.getParameter("entityId");
        String name = req.getParameter("name");
        String phoneNumber = req.getParameter("phoneNumber");
        String note = req.getParameter("note");
        String isActive = req.getParameter("isActive");
        String audioName = req.getParameter("audioName");
        if (isActive == null)
            isActive = "false";
        
        // check to make sure parameters are set
        if (entityId == null || name == null || phoneNumber == null
                || note == null || isActive == null) {
            // //
            System.out.println("do not have all parameters to proceed with update Entity");
            // //
            htmlShowEntities(req, res);
            return;
        }
        
        // trim name
        name = name.trim();
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        entity.setName(name);
        entity.setPhoneNumber(phoneNumber);
        entity.setNote(note);
        entity.setActive(isActive.equals("true"));
        entity.setAudioName(audioName);
        
        if (EntityManager.updateEntity(entity)) {
            updateEntityInSession(req, entity);
            
            // check if next, previous, or save button was pressed
            // to determine the next page
            EntityBean other = null;
            String buttonPressed = req.getParameter("buttonPressed");
            if (buttonPressed != null) {
                if (buttonPressed.equalsIgnoreCase("next")) {
                    other = findNextEntityBean(entityBeanList, entity
                            .getEntityId());
                    if (other != null) {
                        req.setAttribute("entity", other);
                        req.setAttribute("navigate", setNavigate(
                                entityBeanList, other));
                        displayHtmlJsp(req, res, "entityEdit.jsp");
                        return;
                    } else {
                        displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
                        return;
                    }
                } else if (buttonPressed.equalsIgnoreCase("previous")) {
                    other = findPreviousEntityBean(entityBeanList, entity
                            .getEntityId());
                    if (other != null) {
                        req.setAttribute("entity", other);
                        req.setAttribute("navigate", setNavigate(
                                entityBeanList, other));
                        displayHtmlJsp(req, res, "entityEdit.jsp");
                        return;
                    } else {
                        displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
                        return;
                    }
                }
            }
        }
        // //
        System.out.println("error with update");
        // //
        htmlShowEntity(req, res);
    }
    
    /**
     * process edit all the entities
     */
    private void htmlProcessEntityEditAll(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            htmlShowEntities(req, res);
            return;
        }
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        if (entityBeanList == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        Enumeration params = req.getParameterNames();
        String name;
        String value;
        String id;
        // expecting parameters that begin with "en" followed by the entity id
        // for example: en6
        while (params.hasMoreElements()) {
            name = (String) params.nextElement();
            value = req.getParameter(name);
            
            if (name.length() > 2 && name.substring(0, 2).equals("en")) {
                // this is an entry for an entity
                id = name.substring(2);
                
                EntityBean entity = findEntityBeanById(entityBeanList, id);
                // only update the phone number if it is different than the one
                // we already have
                if (!entity.getPhoneNumber().equals(value)) {
                    entity.setPhoneNumber(value);
                    if (!EntityManager.updatePhoneNumber(entity)) {
                        // //System.out.println("phone number updated in db");
                        displayHtmlJsp(req, res, "error.jsp");
                        return;
                    }
                }
            }
            else if (name.length() > 2 && name.substring(0, 2).equals("af")) {
                // this is an entry for an entity
                id = name.substring(2);
                
                EntityBean entity = findEntityBeanById(entityBeanList, id);
                // only update the phone number if it is different than the one
                // we already have
                if (!entity.getAudioName().equals(value)) {
                    entity.setAudioName(value);
                    if (!EntityManager.updateEntity(entity)) {
                        // //System.out.println("phone number updated in db");
                        displayHtmlJsp(req, res, "error.jsp");
                        return;
                    }
                }
            }
        }
        htmlShowEntities(req, res);
    }
    
    /**
     * edit the spellings
     */
    private void htmlProcessEntitySpellingEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // //Show me all the parameters
                {
                    Enumeration params = req.getParameterNames();
                    String name;
                    String value;
                    while (params.hasMoreElements()) {
                        name = (String) params.nextElement();
                        value = req.getParameter(name);
                        // //
                        System.out.println("--- " + name + ":\t" + value);
                        // //
                    }
                }
                // //
                String buttonPressed = req.getParameter("buttonPressed");
                if (buttonPressed == null)
                    buttonPressed = "";
                
                // determine which button was pressed
                // either save, cancel, or else try to find one of the "del" buttons
                if (buttonPressed.equalsIgnoreCase("save")
                || buttonPressed.equalsIgnoreCase("next")
                || buttonPressed.equalsIgnoreCase("previous")
                || req.getParameter("apply") != null) {
                    
                    String entityId = req.getParameter("entityId");
                    
                    // prepare the AltSpellingBean that we'll use to send to the manager
                    AltSpellingBean altSpellingBean = new AltSpellingBean();
                    altSpellingBean.setOwnerId(Integer.parseInt(entityId));
                    altSpellingBean.setOwnerTypeId(EntityBean.ENTITY_TYPE_ID);
                    altSpellingBean
                            .setAltspellingTypeId(AltSpellingBean.TYPE_ENTITYNAME);
                    
                    String spelling = "";
                    String id = "";
                    String name = "";
                    String type = "";
                    String audioName = "";
                    
                    // check if there is a new spelling to add
                    spelling = req.getParameter("newSpelling");
                    if (spelling != null && spelling.length() > 1) {
                        altSpellingBean.setSpelling(spelling);
                        AltSpellingManager.insertAltSpelling(altSpellingBean);
                    }
                    
                    // now update all the alternate spellings
                    // loop thru all the parameters, and look for the ones that are
                    // called
                    // splX - where X is the altSpellingId
                    Enumeration params = req.getParameterNames();
                    while (params.hasMoreElements()) {
                        name = (String) params.nextElement();
                        type = name.substring(0, 3);
                        id = name.substring(3);
                        
                        if (type.equals("spl")) {
                            String _name = req.getParameter(name);
                            if (_name == null || _name.length() == 0) {
                                altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                                AltSpellingManager.deleteAltSpelling(altSpellingBean);
                            } else {
                                altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                                altSpellingBean.setSpelling(_name);
                                AltSpellingManager.updateAltSpelling(altSpellingBean);
                            }
                        }
                    }
                    // get all the spellings and update the contactBean
                    BeanCollection entityBeanList = retrieveEntityBeanList(req);
                    BeanCollection altSpellings = new BeanCollection();
                    
                    EntityBean entity = findEntityBeanById(entityBeanList, entityId);
                    AltSpellingManager.lookupAltSpellingByOwner(entity.getEntityId(),
                            EntityBean.ENTITY_TYPE_ID, altSpellings);
                    entity.setAltSpellings(altSpellings);
                    updateEntityInSession(req, entity);
                    
                    if (buttonPressed.equalsIgnoreCase("save")) {
                        // redirect back to contactDetail page
                        htmlShowEntity(req, res);
                    } else {
                        // display the contact spelling page
                        htmlShowEntitySpelling(req, res);
                    }
                } else if (buttonPressed.equalsIgnoreCase("cancel")) {
                    htmlShowEntity(req, res);
                    return;
                } else {
                    htmlProcessEntitySpellingDelete(req, res);
                    return;
                }
    }
    
    /**
     * delete a spelling this method will be called by
     * htmlProcessEntitySpellingEdit since there is only one form in
     * entitySpelling.jsp
     */
    private void htmlProcessEntitySpellingDelete(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String entityId = req.getParameter("entityId");
        if (entityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        String buttonPressed = req.getParameter("buttonPressed");
        
        EntityBean entity;
        BeanCollection entityBeanList;
        BeanCollection altSpellings = new BeanCollection();
        AltSpellingBean altSpellingBean = new AltSpellingBean();
        String id;
        
        if (buttonPressed != null
                && buttonPressed.substring(0, 3).equals("del")) {
            id = buttonPressed.substring(3);
            altSpellingBean.setAltSpellingId(Integer.parseInt(id));
            
            // update the entity in the session if we were able to update db
            if (AltSpellingManager.deleteAltSpelling(altSpellingBean)) {
                
                entityBeanList = retrieveEntityBeanList(req);
                entity = findEntityBeanById(entityBeanList, entityId);
                
                if (entity != null) {
                    AltSpellingManager.lookupAltSpellingByOwner(Integer
                            .parseInt(entityId), EntityBean.ENTITY_TYPE_ID,
                            altSpellings);
                    entity.setAltSpellings(altSpellings);
                    updateEntityInSession(req, entity);
                }
            }
        }
        // display the contactSpelling page
        htmlShowEntitySpelling(req, res);
    }
    
    /**
     * displays either the next or previous contact
     */
    private void htmlShowEntityNavigate(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String currentEntityId = req.getParameter("currentEntityId");
        if (currentEntityId == null) {
            displayHtmlJsp(req, res, "error.jsp");
            return;
        }
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        EntityBean currentEntity = findEntityBeanById(entityBeanList,
                currentEntityId);
        if (currentEntity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        
        EntityBean entity = null;
        EntityBean parentEntity = null;
        if (currentEntity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            if (req.getParameter("previous") != null) {
                entity = findPreviousEntityBean(entityBeanList, Integer
                        .parseInt(currentEntityId));
            } else if (req.getParameter("next") != null) {
                entity = findNextEntityBean(entityBeanList, Integer
                        .parseInt(currentEntityId));
            }
        } else {
            parentEntity = findEntityBeanById(entityBeanList, currentEntity
                    .getParentId());
            if (parentEntity == null) {
                displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
                return;
            }
            entityBeanList = parentEntity.getSubEntities();
            if (entityBeanList == null) {
                displayHtmlJsp(req, res, "error.jsp");
                return;
            }
            
            if (req.getParameter("previous") != null) {
                entity = findPreviousEntityBean(entityBeanList, Integer
                        .parseInt(currentEntityId));
            } else if (req.getParameter("next") != null) {
                entity = findNextEntityBean(entityBeanList, Integer
                        .parseInt(currentEntityId));
            }
        }
        
        if (entity == null) {
            displayErrorMessage(req, res, ERROR_ID_NOTFOUND);
            return;
        }
        
        // entity found, put in the request, and redirect
        req.setAttribute("entity", entity);
        req.setAttribute("parentEntity", parentEntity);
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(entityBeanList, entity));
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(entityBeanList, entity));
        
        displayHtmlJsp(req, res, "entity.jsp");
    }
    
    //
    // Helper functions
    //
    /**
     * helper function to delete any type of entity (top level or sub-entity)
     */
    private void deleteEntity(HttpServletRequest req, String entityId)
    throws ServletException, IOException {
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        BeanCollection subEntityBeanList = null;
        EntityBean subEntity = null;
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        
        // check to see if the entity is a parent or a subEntity
        if (entity == null) {
            ErrorBean errorBean = new ErrorBean();
            errorBean.addError(ERROR_ID_NOTFOUND, FormError.SEVERITY_INFO);
            req.setAttribute("errorBean", errorBean);
            return;
        }
        
        // if this is a subEntity, just go ahead and delete it
        if (entity.getParentId() != EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            if (EntityManager.deleteEntity(entity)) {
                deleteSubEntityInSession(req, entity);
            }
        } // this is a parent Entity, so we need to delete all subEntities
        else {
            // delete the subEntities
            subEntityBeanList = entity.getSubEntities();
            if (subEntityBeanList != null && subEntityBeanList.size() > 0) {
                for (int i = 0; i < subEntityBeanList.size(); i++) {
                    subEntity = (EntityBean) subEntityBeanList.getItem(i);
                    if (EntityManager.deleteEntity(subEntity)) {
                        deleteSubEntityInSession(req, subEntity);
                    }
                }
            }
            // delete the parent
            if (EntityManager.deleteEntity(entity)) {
                deleteEntityInSession(req, entity);
            }
        }
    }
    
    /**
     * helper method delete entity and all subEntities from session
     * EntityBeanList
     */
    private void deleteEntityInSession(HttpServletRequest req, EntityBean entity)
    throws ServletException, IOException {
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        entityBeanList.removeItem(entity);
        
        // Set the grammar bit
        ApplicationManager.setModifiedGrammar(retrieveApplicationBean(),
                GrammarBuilder.MASK_CONTACTS_GRAMMAR);
    }
    
    /**
     * helper method delete sub entity bean in the session EntityBeanList
     */
    private void deleteSubEntityInSession(HttpServletRequest req,
            EntityBean entity) throws ServletException, IOException {
        
        // retrieve from session
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        
        // remove from entityBeanList
        entityBeanList.removeItem(entity);
        
        // remove from the parentEntity
        EntityBean parentEntity = findEntityBeanById(entityBeanList, entity
                .getParentId());
        BeanCollection subEntities = parentEntity.getSubEntities();
        subEntities.removeItem(entity);
        
        // Set the grammar bit
        ApplicationManager.setModifiedGrammar(retrieveApplicationBean(),
                GrammarBuilder.MASK_CONTACTS_GRAMMAR);
    }
    
    /**
     * helper method adds a new Entity to the session
     */
    private void addEntityInSession(HttpServletRequest req, EntityBean entity)
    throws ServletException, IOException {
        
        // retrieve from session
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        
        // add both entity or subEntity to the top level list
        entityBeanList.addItem(entity);
        
        EntityBean parentEntity = null;
        BeanCollection subEntities = null;
        
        int parentId = entity.getParentId();
        if (parentId != EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            parentEntity = findEntityBeanById(entityBeanList, parentId);
            if (parentEntity != null) {
                subEntities = parentEntity.getSubEntities();
                
                // if there were no subEntities previously, create new
                // otherwise, just go ahead and add it
                if (subEntities == null) {
                    subEntities = new BeanCollection();
                    subEntities.addItem(entity);
                    parentEntity.setSubEntities(subEntities);
                } else {
                    subEntities.addItem(entity);
                }
            } else {
                // //
                System.out.println("Could not find parent entity");
                // //
                return;
            }
        }
        // store back to session
        storeEntityBeanList(req, entityBeanList);
        // Set the grammar bit
        ApplicationManager.setModifiedGrammar(retrieveApplicationBean(),
                GrammarBuilder.MASK_ENTITIES_GRAMMAR);
    }
    
    /**
     * helper method update contact in the session ContactSortableBeanList
     */
    private void updateEntityInSession(HttpServletRequest req, EntityBean entity)
    throws ServletException, IOException {
        
        // delete
        if (entity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            deleteEntityInSession(req, entity);
        } else {
            deleteSubEntityInSession(req, entity);
        }
        // add
        addEntityInSession(req, entity);
    }
    
    /**
     * returns the previous EntityBean if there is no previous item (the id
     * passed in is the first element in the list) will return null
     */
    protected EntityBean findPreviousEntityBean(BeanCollection entityBeanList,
            int entityId) {
        
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        if (entity == null) {
            return null;
        }
        
        List sortedList = null;
        if (entity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            BeanCollection topLevelList = findTopLevelEntities(entityBeanList);
            sortedList = getSortedEntityList(topLevelList);
        } else {
            // Sub Entities
            sortedList = getSortedEntityList(entityBeanList);
        }
        
        for (int i = 0; i < sortedList.size(); i++) {
            entity = (EntityBean) sortedList.get(i);
            if (entity.getEntityId() == entityId) {
                if (i == 0) {
                    entity = null;
                } else {
                    entity = (EntityBean) sortedList.get(i - 1);
                }
                break;
            }
        }
        return entity;
    }
    
    /**
     * returns the next EntityBean if there is no next item (the id passed in is
     * the last element in the list) will return null
     */
    protected EntityBean findNextEntityBean(BeanCollection entityBeanList,
            int entityId) {
        
        EntityBean entity = findEntityBeanById(entityBeanList, entityId);
        if (entity == null) {
            return null;
        }
        
        List sortedList = null;
        if (entity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            BeanCollection topLevelList = findTopLevelEntities(entityBeanList);
            sortedList = getSortedEntityList(topLevelList);
        } else {
            // Sub Entities
            sortedList = getSortedEntityList(entityBeanList);
        }
        
        EntityBean current = null;
        for (int i = 0; i < sortedList.size(); i++) {
            current = (EntityBean) sortedList.get(i);
            if (current.getEntityId() == entityId) {
                if (i + 1 < sortedList.size()) {
                    entity = (EntityBean) sortedList.get(i + 1);
                }
                break;
            }
        }
        return entity;
    }
    
    /**
     * helper function if the entity is the first in the list, returns "first"
     * last in the list, returns "last" otherwise, returns null
     */
    private String setNavigate(BeanCollection entityBeanList, EntityBean entity) {
        
        List sortedList = null;
        if (entity.getParentId() == EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            sortedList = getSortedEntityList(findTopLevelEntities(entityBeanList));
        } else {
            sortedList = getSortedEntityList(entityBeanList);
        }
        
        if (!sortedList.isEmpty()) {
            if (sortedList.size() == 1) {
                return EntityController.ENTITY_NAVIGATE_ONLY;
            }
            
            EntityBean con = (EntityBean) sortedList.get(0);
            if (con.getEntityId() == entity.getEntityId()) {
                return EntityController.ENTITY_NAVIGATE_FIRST;
            }
            
            con = (EntityBean) sortedList.get(sortedList.size() - 1);
            if (con.getEntityId() == entity.getEntityId()) {
                return EntityController.ENTITY_NAVIGATE_LAST;
            }
        }
        return EntityController.ENTITY_NAVIGATE_MIDDLE;
    }
    
    /**
     * helper function returns the parent entity bean if there is one, if not,
     * returns null
     */
    private EntityBean getParentEntity(BeanCollection entityBeanList,
            EntityBean entity) {
        
        // check if this is a subEntity. if it is, put the parentEntity in the
        // request
        EntityBean parentEntity = null;
        if (entity.getParentId() != EntityBean.ENTITY_TOP_LEVEL_PARENT_ID) {
            parentEntity = findEntityBeanById(entityBeanList, entity
                    .getParentId());
        }
        return parentEntity;
    }
    
    /**
     *
     */
    private void displayErrorMessage(HttpServletRequest req,
            HttpServletResponse res, String errorMessage)
            throws ServletException, IOException {
        
        ErrorBean errorBean = new ErrorBean();
        errorBean.addError(errorMessage, FormError.SEVERITY_INFO);
        req.setAttribute("errorBean", errorBean);
        htmlShowEntities(req, res);
    }
}
