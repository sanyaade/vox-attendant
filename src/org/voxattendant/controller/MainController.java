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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.voxattendant.VoxAttendant;

import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.manager.ContactManager;
import org.voxattendant.manager.EntityManager;
import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.CalleeBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.EntityBean;
import org.voxattendant.model.LoggerBean;
import org.voxattendant.model.OperatorBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.model.PromptBean;
import org.voxattendant.taglib.AudioTag;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.GrammarBuilder;



/**
 * This is the main controller that handles voiceXML requests.
 *
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2007-02-24 15:50:52 $
 */
public class MainController extends BaseController {
    
    private static final long serialVersionUID = -7255629433525342213L;
    
    /**
     * A constant that delimiters multiple contact ids or entity ids.
     */
    public static final String ID_DELIMITER = ";";
    
    /**
     * A constant specifies the relative directory to the context to store
     * grammar files.
     */
    public static final String GRAMMAR_DIRECTORY = "/view/vxml/grammar/";
    
    /**
     * Constants to indicate whether confirmation should be implicit or
     * explicit.
     */
    public static final int CONFIRMATION_IMPLICIT = 0;
    
    public static final int CONFIRMATION_EXPLICIT = 1;
    
    /**
     * Constants indicates the types of data sent from VoiceXML
     */
    public static final int TYPE_CONTACTS = 100;
    
    public static final int TYPE_ENTITIES = 101;
    
    public static final int TYPE_SUBENTITIES = 102;
    
    public static final int TYPE_MIXED = 103;
    
    public static final int TYPE_UNKNOWN = 104;
    
    public static final int RESULT_ALL_ENTITIES = 1;
    
    public static final int RESULT_ALL_SUBENTITIES = 1;
    
    /**
     * COnstant indicating the default Maximum allowable matches.
     */
    public static int MaxAllowableMatches = 5;
    
    /**
     * Whether the logger is turned on or off
     */
    private boolean loggerOn = true;
    
    /**
     * Initializes the necessary components
     *
     * @param config
     *            the ServletConfig object
     */
    public void init(javax.servlet.ServletConfig config)
    throws javax.servlet.ServletException {
        
        super.init(config);
        GrammarBuilder.init(REAL_ROOT_PATH + GRAMMAR_DIRECTORY);
        buildContactsGrammar();
        buildEntitiesGrammar();
    }
    
    
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        try {
            String reqState;
            boolean playGreeting = false;
            
            if ((reqState = ((String) req.getParameter("reqState"))) == null) {
                reqState = "vxmlShowMainMenu";
                playGreeting = true;
                
                // Logging
                if (loggerOn) {
                    HttpSession session = req.getSession();
                    
                    LoggerBean loggerBean = new LoggerBean();
                    loggerBean.setStartTimestamp(System.currentTimeMillis());
                    loggerBean.setCallerId(req
                            .getParameter("session.telephone.ani"));
                    session.setAttribute("logger", loggerBean);
                }
                
            }
            // //
            System.out.println("MainController ReqState is: " + reqState);
            // //
            if (reqState.equals("vxmlShowMainMenu")) {
                showMainMenu(req, res, playGreeting);
            } else if (reqState.equals("vxmlShowEntities")) {
                showEntities(req, res);
            } else if (reqState.equals("vxmlShowOperator")) {
                showOperator(req, res);
            } else if (reqState.equals("vxmlShowConfirmCall")) {
                showConfirmCall(req, res);
            } else if (reqState.equals("vxmlShowDirections")) {
                showDirections(req, res);
            } else if (reqState.equals("vxmlProcessSkipName")) {
                processSkipName(req, res);
            } else if (reqState.equals("vxmlShowMakeCall")) {
                vxmlShowMakeCall(req, res);
            } else {
                displayVxmlJsp(req, res, "error.jsp");
            }
        } catch (Exception e) {
            System.err.println("Error: MainController");
            e.printStackTrace();
        }
    }
    
    /**
     * Displays the MainMenu page. Have the option to include the greeting
     * audio.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     * @param playGreeting
     *            whether to include playing of the initial greeting or not
     */
    protected void showMainMenu(HttpServletRequest req,
            HttpServletResponse res, boolean playGreeting)
            throws ServletException, IOException {
        
        ApplicationBean appBean = retrieveApplicationBean();
        if (appBean == null) {
            displayVxmlJsp(req, res, "error.jsp");
            return;
        }
        updateGrammar(appBean);
        
        if (playGreeting)
            req.setAttribute("greetingAudio", getNextGreetingAudio(req));
        
        if (appBean.isPlayDirectionAudio())
            req.setAttribute("playDirectionAudio", "true");
        
        ArrayList skipList = retrieveSkipList(req);
        req.setAttribute("skipList", skipList);
        
        displayVxmlJsp(req, res, "main.jsp");
    }
    
    /**
     * Displays the list of Entities.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showEntities(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        if (entityBeanList == null) {
            entityBeanList = createEntityBeanList(req);
        }
        BeanCollection toplevelEntityBeans = findActiveEntities(findTopLevelEntities(entityBeanList));
        
        showEntityListings(req, res, (toplevelEntityBeans == null) ? null
                : toplevelEntityBeans.getSortedList("entityId"));
    }
    
    /**
     * Displays the call the operator page.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showOperator(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        String state = (String) req.getParameter("state");
        if (state == null)
            state = "system";
        
        if (state.equals("user")) {
            req.setAttribute("userInitiated", "true");
            // // Logging:
            if (loggerOn) {
                LoggerBean loggerBean = retrieveLoggerBean(req);
                if (loggerBean != null) {
                    loggerBean.setAppInfoBits(LoggerBean.MASK_OPERATOR_BY_USER);
                }
                // loggerBean.dump();
            }
            // //
        } else {
            // // Logging:
            if (loggerOn) {
                LoggerBean loggerBean = retrieveLoggerBean(req);
                if (loggerBean != null) {
                    loggerBean.setAppInfoBits(LoggerBean.MASK_OPERATOR_BY_ERROR);
                }
                // loggerBean.dump();
            }
            // //
        }
        
        ApplicationBean appBean = retrieveApplicationBean();
        // Make sure that the admin has selected an operator
        if (appBean.getCurrentOperatorNumberId() > -1) {
            OperatorBean operatorBean = retrieveOperatorBean(req);
            
            BeanCollection phoneNumbers = operatorBean.getPhoneNumbers();
            String operatorNum = null;
            for (int i = 0; i < phoneNumbers.size(); i++) {
                PhoneNumberBean phoneNumberBean = (PhoneNumberBean) phoneNumbers
                        .getItem(i);
                if (appBean.getCurrentOperatorNumberId() == phoneNumberBean
                        .getPhoneNumberId()) {
                    operatorNum = phoneNumberBean.getNumber();
                }
            }
            if (operatorNum == null) {
                // We did not find the PhoneNumberBean...
                if (phoneNumbers.size() > 0) {
                    // well, just get the first one if there's one...
                    PhoneNumberBean phoneNumberBean = (PhoneNumberBean) phoneNumbers
                            .getItem(0);
                    operatorNum = phoneNumberBean.getNumber();
                    // and update the database...
                    appBean.setCurrentOperatorNumberId(phoneNumberBean
                            .getPhoneNumberId());
                    ApplicationManager.updateApplicationBean(appBean);
                }
            }
            req.setAttribute("operatorNum", operatorNum);
        }
        displayVxmlJsp(req, res, "operator.jsp");
    }
    
    /**
     * Displays the Confirmation page.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showConfirmCall(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        // //
        String nbestConf = (String) req.getParameter("nbestConf");
        System.out.println("N-Best with Conf: *******" + nbestConf + "********");
        
        String callerId = (String) req.getParameter("callerId");
        System.out.println("Caller Id: *******" + callerId + "********");
        
        String isDtmf = (String) req.getParameter("isDtmf");
        System.out.println("Input mode: *******" + isDtmf + "********");
        req.setAttribute("isDtmf",isDtmf);
        
        String nbest = (String) req.getParameter("nbest");
        System.out.println("N-Best: *******" + nbest + "********");
        
        if (nbest == null)
            return;
        
        int itemType = findItemType(nbest);
        if (itemType == TYPE_UNKNOWN) {
            req.setAttribute("failedToProcess", "true");
            showOperator(req, res);
            return;
        }
        
        // Revision Note: Instead of going to Operator when there's a mix of
        // contacts and entities,
        // at this point, assume the user wanted the contact more than an
        // entity. -- SimonT
        
        // Get the processing option
        int confirmation = CONFIRMATION_EXPLICIT;
        String _confirmation = (String) req.getParameter("confirmation");
        if ((_confirmation != null) && _confirmation.equals("0")) {
            confirmation = CONFIRMATION_IMPLICIT;
        }
        req.setAttribute("confirmation", String.valueOf(confirmation));
        
        if (itemType == TYPE_CONTACTS || itemType == TYPE_MIXED) {
            String[] contactIds = parseIds(nbest,
                    ContactController.CONTACT_PREFIX);
            
            showConfirmCallContacts(req, res, contactIds);
        } else if (itemType == TYPE_ENTITIES) {
            String[] entityIds = parseIds(nbest, EntityController.ENTITY_PREFIX);
            showConfirmCallEntities(req, res, entityIds);
        } else {
            // Oops should not get here..
            // //
            System.out
                    .println("Error: Something is wrong should never get here.");
            // /
        }
    }
    
    /**
     * Shows the Confirmation page for contacts only.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showConfirmCallContacts(HttpServletRequest req,
            HttpServletResponse res, String[] contactIds)
            throws ServletException, IOException {
        
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        if (contactBeanList == null) {
            contactBeanList = createContactSortableBeanList(req);
        }
        
        //ANI Recognition based on incoming Caller ID.  If present in phone numbers of the
        //autoattendant, then allow them access to the Home Phone numbers of others
        String callerId = (String) req.getParameter("callerId");
        ContactBean aniBean = findContactBeanByAni(contactBeanList,callerId);
        boolean testAniRec = false;
        if ((String)req.getSession().getAttribute("aniRecognized") == null) {
            if (aniBean != null) {
                req.getSession().setAttribute("aniRecognized","true");
            } else {
                req.getSession().setAttribute("aniRecognized","false");
            }
        }
        
        // Exactly one item in the list
        if (contactIds.length == 1) {
            // Find the contactBean
            ContactBean contactBean = findContactBeanById(contactBeanList,
                    contactIds[0]);
            if (contactBean == null) {
                displayVxmlJsp(req, res, "error.jsp");
            } else {
                // Creates the calleeBean
                CalleeBean calleeBean = new CalleeBean();
                calleeBean.setName(contactBean.getFirstname() + " " + contactBean.getLastname());
                calleeBean.setAudioName(contactBean.getAudioName());
                calleeBean.setMobilePhoneNumber(contactBean.getMobilePhoneNumber().trim());
                calleeBean.setWorkPhoneNumber(contactBean.getWorkPhoneNumber().trim());
                calleeBean.setHomePhoneNumber(contactBean.getHomePhoneNumber().trim());
                calleeBean.setVoicemailEnabled(contactBean.isVoicemailEnabled());
                // //
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("Name: " + calleeBean.getName());
                System.out.println("Audio Name: " + calleeBean.getAudioName());
                System.out.println("Multiple Number: " + calleeBean.hasMultiplePhoneNumbers());
                System.out.println("PhoneNumber: " + calleeBean.getPhoneNumber());
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                // //
                req.setAttribute("activeId", buildId(contactBean.getContactId(),ContactController.CONTACT_PREFIX));
                req.setAttribute("calleeBean", calleeBean);
                
                String voicemailUrl = (String) VoxAttendant.getProps().getProperty("voicemailTransferUrl");
                String host =req.getServerName();
                if (req.getServerPort() > 0 && req.getServerPort() != 80)
                {
                    host = host + ":" + req.getServerPort();
                }
                voicemailUrl = "http://" + host + voicemailUrl;
                
                System.out.println("VoicemailUrl: " + voicemailUrl);
                req.setAttribute("voicemailUrl", voicemailUrl);
                req.setAttribute("contactId", ""+contactBean.getContactId());
                displayVxmlJsp(req, res, "confirmCall.jsp");
                
                // // Logging:
                if (loggerOn) {
                    LoggerBean loggerBean = retrieveLoggerBean(req);
                    if (loggerBean != null) {
                        loggerBean.setRequestedId(contactBean.getContactId());
                        loggerBean.setOwnerTypeId(ContactBean.CONTACT_TYPE_ID);
                        // loggerBean.dump();
                    }
                }
                // //
            }
        } else {
            // find all the matching contact beans
            List contactBeans = findContactBeanByIds(contactBeanList,
                    contactIds);
            
            if (contactBeans.size() > MaxAllowableMatches) {
                req.setAttribute("failedToProcess", "true");
                showOperator(req, res);
            } else {
                req.setAttribute("contactBeans", contactBeans);
                displayVxmlJsp(req, res, "disambiguateNames.jsp");
            }
        }
    }
    
    /**
     * Plays the page to confirm a call to an entity given the entityBean.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showConfirmCallEntities(HttpServletRequest req,
            HttpServletResponse res, EntityBean entityBean)
            throws ServletException, IOException {
        // Creates the calleeBean
        CalleeBean calleeBean = new CalleeBean();
        calleeBean.setEntity(true);
        calleeBean.setName(entityBean.getName());
        calleeBean.setAudioName(entityBean.getAudioName());
        // calleeBean.setMobilePhoneNumber(contactBean.getMobilePhoneNumber().trim());
        calleeBean.setWorkPhoneNumber(entityBean.getPhoneNumber().trim());
        // //
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("Name: " + calleeBean.getName());
        System.out.println("Audio Name: " + calleeBean.getAudioName());
        System.out.println("Multiple Number: "
                + calleeBean.hasMultiplePhoneNumbers());
        System.out.println("PhoneNumber: " + calleeBean.getPhoneNumber());
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        // //
        req.setAttribute("activeId", buildId(entityBean.getEntityId(),
                EntityController.ENTITY_PREFIX));
        req.setAttribute("calleeBean", calleeBean);
        displayVxmlJsp(req, res, "confirmCall.jsp");
        
        // // Logging:
        if (loggerOn) {
            LoggerBean loggerBean = retrieveLoggerBean(req);
            if (loggerBean != null) {
                loggerBean.setRequestedId(entityBean.getEntityId());
                loggerBean.setOwnerTypeId(EntityBean.ENTITY_TYPE_ID);
            }
            // loggerBean.dump();
        }
        // //
    }
    
    /**
     * Confirms the call to a list of entities.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showConfirmCallEntities(HttpServletRequest req,
            HttpServletResponse res, String[] entityIds)
            throws ServletException, IOException {
        
        BeanCollection entityBeanList = retrieveEntityBeanList(req);
        if (entityBeanList == null) {
            entityBeanList = createEntityBeanList(req);
        }
        
        if (entityIds.length == 1) {
            EntityBean entityBean = findEntityBeanById(entityBeanList,
                    entityIds[0]);
            handleSingleEntity(req, res, entityBean);
        } else {
            // find all the matching contact beans
            List entityBeans = findEntityBeanByIds(entityBeanList, entityIds);
            //
            // Test if we can handle it...
            //
            // We will not handle the following cases:
            // 1) All TopLevel EntityBeans --> pick the best
            // 2) All SubEntity from the same parent --> pick the best
            // 3) Over the allowed limit number of items
            //
            if (entityBeans.size() > MaxAllowableMatches) {
                req.setAttribute("failedToProcess", "true");
                showOperator(req, res);
            } else {
                int result = getProcessingStrategy(entityBeans);
                
                if (result == RESULT_ALL_ENTITIES) {
                    handleSingleEntity(req, res, (EntityBean) entityBeans
                            .get(0));
                } else if (result == RESULT_ALL_SUBENTITIES) {
                    handleSingleEntity(req, res, (EntityBean) entityBeans
                            .get(0));
                } else {
                    req.setAttribute("entityBeans", entityBeans);
                    displayVxmlJsp(req, res, "disambiguateNames.jsp");
                }
                /*
                 * if(cannotHandleIt(entityBeans) || (entityBeans.size() >
                 * MaxAllowableMatches)) { req.setAttribute("failedToProcess",
                 * "true"); showOperator(req, res); } else {
                 * req.setAttribute("entityBeans", entityBeans);
                 * displayVxmlJsp(req, res, "disambiguateNames.jsp"); }
                 */
            }
        }
    }
    
    /**
     *
     */
    protected void handleSingleEntity(HttpServletRequest req,
            HttpServletResponse res, EntityBean entityBean)
            throws ServletException, IOException {
        
        if (entityBean == null) {
            displayVxmlJsp(req, res, "error.jsp");
        } else {
            BeanCollection subEntities = entityBean.getSubEntities();
            if (subEntities == null) {
                // If there's no subEntities just call that entity
                showConfirmCallEntities(req, res, entityBean);
            } else {
                // Need to clean up the subEntities to make sure that:
                // 1) Only Active items at remaining
                BeanCollection activeSubEntities = findActiveEntities(subEntities);
                if (activeSubEntities.size() == 0) {
                    // If there's no subEntities just call that entity
                    showConfirmCallEntities(req, res, entityBean);
                } else if (subEntities.size() == 1) {
                    // if there's only ONE subentity, just play that sub entity
                    showConfirmCallEntities(req, res,
                            (EntityBean) activeSubEntities.getItem(0));
                } else {
                    // Else we need to list the sub entities
                    req.setAttribute("parent", entityBean);
                    showEntityListings(req, res, activeSubEntities
                            .getSortedList("name"));
                }
            }
        }
    }
    
    /**
     * Updates the grammar files.
     *
     * @param appBean
     *            the ApplicationBean
     */
    protected void updateGrammar(ApplicationBean appBean) {
        
        int modifiedGrammar = appBean.getModifiedGrammar();
        // //
        // System.out.println("ModifiedGrammar field #1: " + modifiedGrammar);
        // //
        boolean updateApplicationBean = false;
        if (GrammarBuilder.isBitOn(modifiedGrammar,
                GrammarBuilder.MASK_CONTACTS_GRAMMAR)) {
            buildContactsGrammar();
            modifiedGrammar = GrammarBuilder.unsetBit(modifiedGrammar,
                    GrammarBuilder.MASK_CONTACTS_GRAMMAR);
            appBean.setModifiedGrammar(modifiedGrammar);
            updateApplicationBean = true;
        }
        // //
        // System.out.println("ModifiedGrammar field #2: " + modifiedGrammar);
        // //
        if (GrammarBuilder.isBitOn(modifiedGrammar,
                GrammarBuilder.MASK_ENTITIES_GRAMMAR)) {
            buildEntitiesGrammar();
            modifiedGrammar = GrammarBuilder.unsetBit(modifiedGrammar,
                    GrammarBuilder.MASK_ENTITIES_GRAMMAR);
            appBean.setModifiedGrammar(modifiedGrammar);
            updateApplicationBean = true;
        }
        // //
        // System.out.println("ModifiedGrammar field #3: " + modifiedGrammar);
        // //
        if (updateApplicationBean) {
            ApplicationManager.updateApplicationBean(appBean);
        }
    }
    
    /**
     * Plays the directions flow.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showDirections(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        ApplicationBean appBean = retrieveApplicationBean();
        if (appBean.isPlayDirectionAudio())
            req.setAttribute("playDirectionAudio", "true");
        
        displayVxmlJsp(req, res, "directions.jsp");
        // // Logging:
        if (loggerOn) {
            LoggerBean loggerBean = retrieveLoggerBean(req);
            if (loggerBean != null)
                loggerBean.setAppInfoBits(LoggerBean.MASK_CORPORATE_DIRECTION);
            // loggerBean.dump();
        }
        // //
    }
    
    /**
     * The user has cancelled the current selection. The cancelled Id will be
     * placed into a skip list.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void processSkipName(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String skip = req.getParameter("skip");
        if (skip != null) {
            // //
            System.out.println("Skipping: " + skip);
            // //
            ArrayList skipList = retrieveSkipList(req);
            skipList.add(skip);
            
            // // Logging:
            if (loggerOn) {
                LoggerBean loggerBean = retrieveLoggerBean(req);
                if (loggerBean != null) {
                    int itemType = findItemType(skip);
                    if (itemType == TYPE_CONTACTS) {
                        String[] contactIds = parseIds(skip,
                                ContactController.CONTACT_PREFIX);
                        loggerBean.addCancellation(Integer.parseInt(contactIds[0]),
                                ContactBean.CONTACT_TYPE_ID);
                    } else if (itemType == TYPE_ENTITIES) {
                        String[] entityIds = parseIds(skip,
                                EntityController.ENTITY_PREFIX);
                        loggerBean.addCancellation(Integer.parseInt(entityIds[0]),
                                EntityBean.ENTITY_TYPE_ID);
                    }
                    // loggerBean.dump();
                }
            }
            // //
        }
        showMainMenu(req, res, false);
    }
    
    /**
     * Plays the call flow that makes the call to the user.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void vxmlShowMakeCall(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String phoneNumber = req.getParameter("phoneNumber");
        String aniNumber = req.getParameter("aniNumber");
        req.setAttribute("phoneNumber", phoneNumber);
        req.setAttribute("aniNumber", aniNumber);
        displayVxmlJsp(req, res, "makeCall.jsp");
        
        // // Logging:
        if (loggerOn) {
            LoggerBean loggerBean = retrieveLoggerBean(req);
            if (loggerBean != null)
                loggerBean.setEndTimestamp(System.currentTimeMillis());
            // loggerBean.dump();
        }
        // //
    }
    
    //
    // Helper functions
    //
    /**
     * Handles the rotating greeting prompts.
     *
     * @param req
     *            HttpServletRequest
     */
    protected String getNextGreetingAudio(HttpServletRequest req)
    throws ServletException, IOException {
        
        int index = 0;
        BeanCollection promptBeanList = retrievePromptBeanList(req);
        if (promptBeanList.size() == 0)
            return null;
        
        String strIndex = (String) getServletContext().getAttribute(
                "currentGreetingIndex");
        if (strIndex == null) {
            for (int i = 0; i < promptBeanList.size(); i++) {
                if (((PromptBean) promptBeanList.getItem(i)).isActive()) {
                    index = i;
                    break;
                }
            }
        } else
            index = Integer.parseInt(strIndex);
        
        PromptBean promptBean = (PromptBean) promptBeanList.getItem(index);
        boolean done = false;
        for (int i = index + 1; i < promptBeanList.size(); i++) {
            if (((PromptBean) promptBeanList.getItem(i)).isActive()) {
                index = i;
                done = true;
                break;
            }
        }
        if (!done) {
            for (int i = 0; i < promptBeanList.size(); i++) {
                if (((PromptBean) promptBeanList.getItem(i)).isActive()) {
                    index = i;
                    done = true;
                    break;
                }
            }
        }
        getServletContext().setAttribute("currentGreetingIndex",
                String.valueOf(index));
        return promptBean.getAudio();
    }
    
    /**
     * Builds the Contact grammar file
     */
    protected void buildContactsGrammar() {
        // //
        System.out.println("Re-generating contacts grammar...");
        // //
        BeanCollection contactBeanList = new BeanCollection();
        if (ContactManager.lookupAllContacts(contactBeanList)) {
            GrammarBuilder.buildContactsGrammar(contactBeanList
                    .getSortedList("firstname"));
        }
    }
    
    /**
     * Builds the Entity grammar file
     */
    protected void buildEntitiesGrammar() {
        // //
        System.out.println("Re-generating entities grammar...");
        // //
        BeanCollection entityBeanList = new BeanCollection();
        if (EntityManager.lookupAllEntities(entityBeanList)) {
            GrammarBuilder.buildEntitiesGrammar(entityBeanList
                    .getSortedList("parentId"));
        }
    }
    
    /**
     * Helper to generate the Id with prefix.
     */
    protected String buildId(int id, String prefix) {
        return (prefix + id);
    }
    
    /**
     * The user might have said a names that is ambigous and thus, more than one
     * Id is being received. This funtion is responsible to parse out the Ids
     *
     * @param strIds
     *            the list of Ids received from user
     * @param prefix
     *            the prefix of the Ids
     */
    protected String[] parseIds(String strIds, String prefix) {
        Vector ids = new Vector();
        
        StringTokenizer tknizer = new StringTokenizer(strIds, ID_DELIMITER);
        String contactId = null;
        while (tknizer.hasMoreTokens()) {
            contactId = tknizer.nextToken();
            
            // If it does not have the right prefix, skip it.
            if (!contactId.startsWith(prefix))
                continue;
            
            String id = stripPrefix(contactId, prefix);
            // Skips all unknown items...
            if (id != null)
                ids.add(id);
        }
        String[] array = new String[ids.size()];
        ids.copyInto(array);
        return array;
    }
    
    /**
     * Removes the prefix from the Ids
     *
     * @param text
     *            the original Ids
     * @param prefix
     *            the prefix to remove
     */
    protected String stripPrefix(String text, String prefix) {
        if (text.startsWith(prefix)) {
            String id = text.substring(prefix.length());
            // //
            // System.out.println("Id: " + id);
            // //
            return id;
        } else
            return null;
    }
    
    /**
     * Determines the type of Ids that has been returned in the nbest list
     *
     * @param nbest
     *            the list of Ids
     */
    protected int findItemType(String nbest) {
        int type = TYPE_UNKNOWN;
        
        if (nbest.indexOf(ContactController.CONTACT_PREFIX) != -1)
            type = TYPE_CONTACTS;
        
        if (nbest.indexOf(EntityController.ENTITY_PREFIX) != -1) {
            if (type == TYPE_UNKNOWN)
                type = TYPE_ENTITIES;
            else
                type = TYPE_MIXED;
        }
        return type;
    }
    
    /**
     * Checks to see if we can deal with this type of Id list. Currently we
     * cannot deal with a mixed list.
     *
     * @param the
     *            list of EntityBeans
     */
        /*
         * protected boolean cannotHandleIt(List entityBeanList) { boolean entities =
         * false; boolean subentities = false;
         *
         * for(int i = 0; i < entityBeanList.size(); i++) { EntityBean entityBean =
         * (EntityBean)entityBeanList.get(i);
         *
         * if(entityBean.isSubEntity()) { System.out.println("isSubentity: " +
         * entityBean.getEntityId()); subentities = true; } else {
         * System.out.println("is NOT Subentity: " + entityBean.getEntityId());
         * entities = true; }
         *
         * //if(entities ^ subentities) // return false; } if(subentities) return
         * false; else return true; }
         */
    /**
     *
     */
    protected int getProcessingStrategy(List entityBeanList) {
        int entities = 0;
        int subentities = 0;
        
        for (int i = 0; i < entityBeanList.size(); i++) {
            EntityBean entityBean = (EntityBean) entityBeanList.get(i);
            
            if (entityBean.isSubEntity()) {
                subentities++;
            } else {
                entities++;
            }
        }
        if (subentities == 0)
            return RESULT_ALL_ENTITIES;
        else
            return RESULT_ALL_SUBENTITIES;
    }
    
    /**
     * Plays the Entity Listing call flow.
     *
     * @param req
     *            HttpServletRequest
     * @param res
     *            HttpServletResponse
     */
    protected void showEntityListings(HttpServletRequest req,
            HttpServletResponse res, List entityBeans) throws ServletException,
            IOException {
        
        if (entityBeans != null) {
            // Build up the inline grammar for the sub entities
            String inline = GrammarBuilder
                    .buildInlineSubEntityGrammar(entityBeans);
            req.setAttribute("inlineGrammar", inline);
            req.setAttribute("entityBeans", entityBeans);
        }
        displayVxmlJsp(req, res, "entities.jsp");
    }
    
    /**
     * Retrives the current skip list from session.
     *
     * @param req
     *            HttpServletRequest
     */
    protected ArrayList retrieveSkipList(HttpServletRequest req)
    throws IOException, ServletException {
        
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        ArrayList skipList = (ArrayList) session.getAttribute("skipList");
        if (skipList == null) {
            skipList = new ArrayList();
            session.setAttribute("skipList", skipList);
        }
        return skipList;
    }
    
    /**
     * Finds all the Active Entities.
     *
     * @param entities
     *            The list of Entities
     */
    protected BeanCollection findActiveEntities(BeanCollection entities) {
        
        if (entities == null)
            return null;
        
        BeanCollection activeEntities = new BeanCollection();
        for (int i = 0; i < entities.size(); i++) {
            
            EntityBean entityBean = (EntityBean) entities.getItem(i);
            if (entityBean.isActive())
                activeEntities.addItem(entityBean);
        }
        return activeEntities;
    }
    
    /**
     * Retrieves the LoggerBean from session.
     *
     * @param req
     *            HttpServletRequest
     */
    protected LoggerBean retrieveLoggerBean(HttpServletRequest req)
    throws IOException, ServletException {
        
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        // //
        // System.out.println("Initializing Logger: " + session.toString());
        // //
        return (LoggerBean) session.getAttribute("logger");
    }
    
    
}
