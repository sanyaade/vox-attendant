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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.voxattendant.VoxAttendant;

import org.voxattendant.manager.AltSpellingManager;
import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.manager.ContactManager;
import org.voxattendant.model.AltSpellingBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.PhoneNumberBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.ErrorBean;
import org.voxattendant.util.FormError;
import org.voxattendant.util.GrammarBuilder;

public class ContactController extends BaseController {
    
    private static final long serialVersionUID = 2345967272702287291L;
    
    public static final String CONTACT_PREFIX = "contact-";
    
    public static final String CONTACT_NAVIGATE_FIRST = "first";
    
    public static final String CONTACT_NAVIGATE_LAST = "last";
    
    public static final String CONTACT_NAVIGATE_MIDDLE = "middle";
    
    public static final String CONTACT_NAVIGATE_ONLY = "only";
    
    
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
                reqState = "htmlShowContacts";
            }
            // //
            System.out.println("ContactController ReqState is: " + reqState);
            // //
            if (reqState.equals("htmlShowContacts")) {
                htmlShowContacts(req, res);
            } else if (reqState.equals("htmlShowContactDetail")) {
                htmlShowContactDetail(req, res);
            } else if (reqState.equals("htmlShowContactDetailNavigate")) {
                htmlShowContactDetailNavigate(req, res);
            } else if (reqState.equals("htmlShowContactEdit")) {
                htmlShowContactEdit(req, res);
            } else if (reqState.equals("htmlShowContactSpelling")) {
                htmlShowContactSpelling(req, res);
            } else if (reqState.equals("htmlProcessContactEdit")) {
                htmlProcessContactEdit(req, res);
            } else if (reqState.equals("htmlProcessContactDelete")) {
                htmlProcessContactDelete(req, res);
            } else if (reqState.equals("htmlProcessContactSpellingDelete")) {
                htmlProcessContactSpellingDelete(req, res);
            } else if (reqState.equals("htmlProcessContactSpellingEdit")) {
                htmlProcessContactSpellingEdit(req, res);
            } else if (reqState.equals("htmlShowContactAdd")) {
                htmlShowContactAdd(req, res);
            } else if (reqState.equals("htmlProcessContactAdd")) {
                htmlProcessContactAdd(req, res);
            } else if (reqState.equals("htmlProcessConflicts")) {
                htmlProcessConflicts(req, res);
            } else if (reqState.equals("htmlShowContactEditAll")) {
                htmlShowContactEditAll(req, res);
            } else if (reqState.equals("htmlProcessContactEditAll")) {
                htmlProcessContactEditAll(req, res);
            } else {
                System.out.println("ContactController reqState: " + reqState
                        + " does not match any known reqStates");
                htmlShowContacts(req, res);
            }
        } catch (Exception e) {
            System.err.println("Error: ContactController");
            System.err.println(e.toString());
        }
    }
    
    /**
     * displays all the contacts
     */
    private void htmlShowContacts(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        List contactList = this.retrieveContactList(req);
        // if contactList is not found in the session,
        // call ContactManager to get it from db
        if (contactList == null) {
            createContactSortableBeanList(req);
            contactList = retrieveContactList(req);
        }
        req.setAttribute("contactList", contactList);
        displayHtmlJsp(req, res, "contacts.jsp");
    }
    
    /**
     * displays the detail for the selected contact
     */
    private void htmlShowContactDetail(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String contactId = req.getParameter("contactId");
        
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        ContactBean contact = findContactBeanById(contactBeanList, contactId);
        if (contact == null) {
            // //
            // System.out.println("unable to find contact with id: " +
            // contactId);
            // //
            ErrorBean errorBean = new ErrorBean();
            errorBean
                    .addError(
                    "The contact cannot be located. Most likely it has been deleted.",
                    FormError.SEVERITY_INFO);
            
            req.setAttribute("errorBean", errorBean);
            htmlShowContacts(req, res);
            return;
        }
        // contact found, put in the request, and redirect
        req.setAttribute("contact", contact);
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(contactBeanList, contact));
        displayHtmlJsp(req, res, "contactDetail.jsp");
    }
    
    /**
     * displays either the next or previous contact
     */
    private void htmlShowContactDetailNavigate(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String currentContactId = req.getParameter("currentContactId");
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        ContactBean contact = null;
        if (req.getParameter("previous") != null) {
            contact = findPreviousContactBean(contactBeanList, Integer
                    .parseInt(currentContactId));
        } else if (req.getParameter("next") != null) {
            contact = findNextContactBean(contactBeanList, Integer
                    .parseInt(currentContactId));
        }
        if (contact == null) {
            // //
            // System.out.println("unable to find previous contact with
            // currentContactId: " + currentContactId);
            // //
            ErrorBean errorBean = new ErrorBean();
            errorBean
                    .addError(
                    "The contact cannot be located. Most likely it has been deleted.",
                    FormError.SEVERITY_INFO);
            
            req.setAttribute("errorBean", errorBean);
            htmlShowContacts(req, res);
            return;
        }
        // contact found, put in the request, and redirect
        req.setAttribute("contact", contact);
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(contactBeanList, contact));
        displayHtmlJsp(req, res, "contactDetail.jsp");
    }
    
    /**
     * displays add contact form
     */
    private void htmlShowContactAdd(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        displayHtmlJsp(req, res, "contactEdit.jsp");
    }
    
    /**
     * displays edit all contacts form
     */
    private void htmlShowContactEditAll(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        List contactList = this.retrieveContactList(req);
        req.setAttribute("contactList", contactList);
        displayHtmlJsp(req, res, "contactEditAll.jsp");
    }
    
    /**
     * displays the form for editing selected contact
     */
    private void htmlShowContactEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String contactId = req.getParameter("contactId");
        
        BeanCollection contactBeanList = this
                .retrieveContactSortableBeanList(req);
        ContactBean contact = findContactBeanById(contactBeanList, contactId);
        
        if (contact == null) {
            // //
            // System.out.println("unable to find previous contact with
            // currentContactId: " + currentContactId);
            // //
            ErrorBean errorBean = new ErrorBean();
            errorBean
                    .addError(
                    "The contact cannot be located. Most likely it has been deleted.",
                    FormError.SEVERITY_INFO);
            
            req.setAttribute("errorBean", errorBean);
            htmlShowContacts(req, res);
            return;
        }
        // contact found, put in the request, and redirect
        req.setAttribute("contact", contact);
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(contactBeanList, contact));
        displayHtmlJsp(req, res, "contactEdit.jsp");
    }
    
    /**
     * displays the form for editing spellings of selected contact
     */
    private void htmlShowContactSpelling(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String contactId = req.getParameter("contactId");
        
        BeanCollection contactBeanList = this
                .retrieveContactSortableBeanList(req);
        ContactBean contact = null;
        
        //
        // check if this is being called by pressing either NEXT or PREVIOUS
        //
        String buttonPressed = req.getParameter("buttonPressed");
        if (buttonPressed == null)
            buttonPressed = "";
        
        if (buttonPressed.equalsIgnoreCase("next")) {
            contact = findNextContactBean(contactBeanList, Integer
                    .parseInt(contactId));
        } else if (buttonPressed.equalsIgnoreCase("previous")) {
            contact = findPreviousContactBean(contactBeanList, Integer
                    .parseInt(contactId));
        } else {
            contact = findContactBeanById(contactBeanList, contactId);
        }
        
        if (contact == null) {
            // //
            // System.out.println("unable to find contact with id: " +
            // contactId);
            // //
            ErrorBean errorBean = new ErrorBean();
            errorBean
                    .addError(
                    "The contact cannot be located. Most likely it has been deleted.",
                    FormError.SEVERITY_INFO);
            
            req.setAttribute("errorBean", errorBean);
            htmlShowContacts(req, res);
            return;
        }
        // contact found, put in the request, and redirect
        req.setAttribute("contact", contact);
        // determine which navigation buttons to show
        req.setAttribute("navigate", setNavigate(contactBeanList, contact));
        displayHtmlJsp(req, res, "contactSpelling.jsp");
    }
    
    /**
     * add a contact
     */
    private void htmlProcessContactAdd(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            htmlShowContacts(req, res);
            return;
        }
        // if you have reached this point, it means we are going to try to
        // add a the contact
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String note = req.getParameter("note");
        String distinctInfo = req.getParameter("distinctInfo");
        String workPhoneNumber = req.getParameter("workPhoneNumber");
        String mobilePhoneNumber = req.getParameter("mobilePhoneNumber");
        String homePhoneNumber = req.getParameter("homePhoneNumber");
        String extension = req.getParameter("extension");
        String audioName = req.getParameter("audioName");
        String email = req.getParameter("email");
        
        String isVoicemailEnabled = req.getParameter("isVoicemailEnabled");
        if (isVoicemailEnabled == null)
            isVoicemailEnabled = "false";
        
        
        // check to make sure parameters are set
        if (firstname == null || lastname == null
                || /* nickname == null || */note == null || distinctInfo == null
                || workPhoneNumber == null || mobilePhoneNumber == null) {
            
            System.out
                    .println("do not have all parameters to proceed with add Contact");
            htmlShowContacts(req, res);
            return;
        }
        
        // put values in a ContactBean
        ContactBean contact = new ContactBean();
        boolean isSuccess = false;
        
        // trim firstname and lastname
        firstname = firstname.trim();
        lastname = lastname.trim();
        
        contact.setFirstname(firstname);
        contact.setLastname(lastname);
        contact.setNote(note);
        contact.setDistinctInfo(distinctInfo);
        contact.setWorkPhoneNumber(workPhoneNumber);
        contact.setMobilePhoneNumber(mobilePhoneNumber);
        contact.setHomePhoneNumber(homePhoneNumber);
        contact.setExtension(extension);
        contact.setActive(true);
        contact.setVoicemailEnabled(isVoicemailEnabled.equals("true"));
        contact.setPhoneticCode();
        contact.setAudioName(audioName);
        contact.setEmail(email);
        // if conflict resolution has not already been done,
        // check if there are other contacts with the same phonetic code
        // if there are, we will redirect to distinctInfo.jsp
        if (req.getAttribute("conflictResDone") == null) {
            
            BeanCollection contacts = new BeanCollection();
            ContactManager.lookupContactsByPhoneticCode(contact
                    .getPhoneticCode(), contacts);
            if (contacts.size() > 0) {
                // add the new one to the list
                // put the contacts with the same phonetic code into the request
                contacts.addItem(contact);
                req.setAttribute("contacts", contacts.getUnSortedList());
                displayHtmlJsp(req, res, "distinctInfo.jsp");
                return;
            }
        }
        // add the new contact
        // update database
        isSuccess = ContactManager.addContact(contact);
        
        //Was voicemail previously enabled?
        if (contact.isVoicemailEnabled()) {
            String host =req.getServerName();
            if (req.getServerPort() > 0 && req.getServerPort() != 80)
            {
                host = host + ":" + req.getServerPort();
            }
            host = "http://" + host;
            
            this.provisionVoicemail(host, ""+contact.getContactId(),contact.getFirstname(),contact.getLastname(),contact.getWorkPhoneNumber(), contact.getEmail());
        }
        
        if (isSuccess) {
            // //System.out.println("adding the contact to session");
            addContactInSession(req, contact);
        } else {
            System.out.println("error with add contact");
        }
        htmlShowContacts(req, res);
    }
    
    /**
     * processes the multiple edit of distinct info adds the new contact
     */
    private void htmlProcessConflicts(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // update distinct info
        Enumeration params = req.getParameterNames();
        ContactBean contact = new ContactBean();
        String param;
        String value;
        String id;
        BeanCollection contactBeanList = this
                .retrieveContactSortableBeanList(req);
        
        while (params.hasMoreElements()) {
            param = (String) params.nextElement();
            if (param.substring(0, 4).equals("info")) {
                value = req.getParameter(param);
                id = param.substring(4);
                contact = findContactBeanById(contactBeanList, id);
                if (!contact.getDistinctInfo().equals(value)) {
                    // //System.out.println("need to update distinct info for "
                    // + id + " " + contact.getFirstname());
                    contact.setDistinctInfo(value);
                    if (ContactManager.updateContact(contact)) {
                        updateContactInSession(req, contact);
                    }
                }
            }
        }
        // set conflictResDone request attribute to true,
        // call other method to add the contact
        req.setAttribute("conflictResDone", "true");
        
        // determine if this is an add or an edit
        if (Integer.parseInt(req.getParameter("contactId")) != -1) {
            htmlProcessContactEdit(req, res);
            return;
        } else {
            htmlProcessContactAdd(req, res);
            return;
        }
    }
    
    /**
     * edit a contact
     */
    private void htmlProcessContactEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        ContactBean updatedContact = new ContactBean();
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            // //System.out.println("cancelled");
            htmlShowContactDetail(req, res);
            return;
        }
        // if you have reached this point, it means we are going to try to do an
        // update on the contact
        
        // get all the request parameters
        String contactId = req.getParameter("contactId");
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String note = req.getParameter("note");
        String distinctInfo = req.getParameter("distinctInfo");
        String workPhoneNumber = req.getParameter("workPhoneNumber");
        String mobilePhoneNumber = req.getParameter("mobilePhoneNumber");
        String homePhoneNumber = req.getParameter("homePhoneNumber");
        String extension = req.getParameter("extension");
        String isActive = req.getParameter("isActive");
        String isVoicemailEnabled = req.getParameter("isVoicemailEnabled");
        String audioName = req.getParameter("audioName");
        String email = req.getParameter("email");
        
        if (isActive == null)
            isActive = "false";
        
        
        if (isVoicemailEnabled == null)
            isVoicemailEnabled = "false";
        
        // check to make sure parameters are set
        if (firstname == null || contactId == null || isActive == null
                || lastname == null || note == null || distinctInfo == null
                || workPhoneNumber == null || mobilePhoneNumber == null) {
            
            // //System.out.println("do not have all parameters to proceed with
            // update Contact");
            htmlShowContacts(req, res);
            return;
        }
        
        // trim firstname and lastname
        firstname = firstname.trim();
        lastname = lastname.trim();
        
        // put info in a contactBean
        updatedContact.setContactId(Integer.parseInt(contactId));
        updatedContact.setFirstname(firstname);
        updatedContact.setLastname(lastname);
        updatedContact.setNote(note);
        updatedContact.setDistinctInfo(distinctInfo);
        updatedContact.setWorkPhoneNumber(workPhoneNumber);
        updatedContact.setMobilePhoneNumber(mobilePhoneNumber);
        updatedContact.setHomePhoneNumber(homePhoneNumber);
        updatedContact.setExtension(extension);
        updatedContact.setActive(isActive.equals("true"));
        updatedContact.setAudioName(audioName);
        updatedContact.setEmail(email);
        System.out.println("isVoicemailEnabled: " + isVoicemailEnabled.equals("true"));
        updatedContact.setVoicemailEnabled(isVoicemailEnabled.equals("true"));
        
        // get the original contact bean
        BeanCollection contactBeanList = this
                .retrieveContactSortableBeanList(req);
        ContactBean contact = findContactBeanById(contactBeanList, contactId);
        
        //Was voicemail previously enabled?
        if (!contact.isVoicemailEnabled() && updatedContact.isVoicemailEnabled()) {
            String host =req.getServerName();
            if (req.getServerPort() > 0 && req.getServerPort() != 80)
            {
                host = host + ":" + req.getServerPort();
            }
            host = "http://" + host;
            
            this.provisionVoicemail(host, ""+contact.getContactId(),updatedContact.getFirstname(),updatedContact.getLastname(),updatedContact.getWorkPhoneNumber(), updatedContact.getEmail());
        }
        
        // if conflict resolution has not already been done,
        // check if there are other contacts with the same phonetic code
        // if there are, we will redirect to distinctInfo.jsp
        
        if (!isSamePhoneticCode(contact, updatedContact)
        && req.getAttribute("conflictResDone") == null) {
            BeanCollection contacts = getOtherContactsWithSamePhoneticCode(
                    contactId, ContactBean.generatePhoneticCode(firstname,
                    lastname));
            
            if (contacts.size() > 0) {
                // add the modified contact info to the list
                contacts.addItem(updatedContact);
                
                // put the contacts with the same phonetic code into the request
                req.setAttribute("contacts", contacts.getUnSortedList());
                req.setAttribute("updateContactId", String.valueOf(contactId));
                displayHtmlJsp(req, res, "distinctInfo.jsp");
                return;
            }
        }
        
        // proceed with the update
        boolean isSuccess = false;
        
        // update the values in the session
        contact.setContactId(Integer.parseInt(contactId));
        contact.setFirstname(firstname);
        contact.setLastname(lastname);
        contact.setNote(note);
        contact.setDistinctInfo(distinctInfo);
        contact.setWorkPhoneNumber(workPhoneNumber);
        contact.setMobilePhoneNumber(mobilePhoneNumber);
        contact.setActive(isActive.equals("true"));
        contact.setHomePhoneNumber(homePhoneNumber);
        contact.setExtension(extension);
        contact.setVoicemailEnabled(isVoicemailEnabled.equals("true"));
        contact.setAudioName(audioName);
        contact.setEmail(email);
        
        if (!ContactBean.generatePhoneticCode(firstname, lastname).equals(
                contact.getPhoneticCode())) {
            contact.setPhoneticCode();
        }
        
        // update database
        isSuccess = ContactManager.updateContact(contact);
        if (isSuccess) {
            // *need to do this: updateContactInSession(req,contact);
            updateContactInSession(req,contact);
            
            // determine which page to show
            // check if next, previous, or save button was pressed
            //
            ContactBean other = null;
            String buttonPressed = req.getParameter("buttonPressed");
            if (buttonPressed != null) {
                if (buttonPressed.equalsIgnoreCase("next")) {
                    other = findNextContactBean(contactBeanList, contact
                            .getContactId());
                    if (other != null) {
                        req.setAttribute("contact", other);
                        
                        // determine which navigation buttons to show
                        req.setAttribute("navigate", setNavigate(
                                contactBeanList, other));
                        
                        displayHtmlJsp(req, res, "contactEdit.jsp");
                        return;
                    } else {
                        htmlShowContacts(req, res);
                        return;
                    }
                } else if (buttonPressed.equalsIgnoreCase("previous")) {
                    other = findPreviousContactBean(contactBeanList, contact
                            .getContactId());
                    if (other != null) {
                        req.setAttribute("contact", other);
                        
                        // determine which navigation buttons to show
                        req.setAttribute("navigate", setNavigate(
                                contactBeanList, other));
                        
                        displayHtmlJsp(req, res, "contactEdit.jsp");
                        return;
                    } else {
                        htmlShowContacts(req, res);
                        return;
                    }
                }
            }
            htmlShowContactDetail(req, res);
            
        } else {
            System.out.println("error with update");
            htmlShowContactDetail(req, res);
        }
    }
    
    /**
     * process edit all the contacts
     */
    private void htmlProcessContactEditAll(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        // check if the user pressed cancel
        if (req.getParameter("cancel") != null) {
            htmlShowContacts(req, res);
            return;
        }
        
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        if (contactBeanList == null) {
            // //System.out.println("totally messed up");
        }
        Enumeration params = req.getParameterNames();
        ContactBean contact = new ContactBean();
        String name;
        String value;
        String type;
        String id;
        String audioName;
        
        // expecting parameters that begin with "mn" and "wn"
        // followed by the contact id
        // for example: mn6 and wn6
        while (params.hasMoreElements()) {
            name = (String) params.nextElement();
            value = req.getParameter(name);
            type = name.substring(0, 2);
            id = name.substring(2);
            
            // //System.out.println("id: " + id + " type:" + type + " " + name +
            // " " + value);
            
            if (type.equals("mn")) {
                // mobile number
                
                contact = findContactBeanById(contactBeanList, id);
                
                // only update the phone number if it is different than the one
                // we already have
                if (!contact.getMobilePhoneNumber().equals(value)) {
                    contact.setMobilePhoneNumber(value);
                    ContactManager.updatePhoneNumber(contact,
                            PhoneNumberBean.PHONETYPEID_MOBILE);
                    updateContactInSession(req, contact);
                }
            } else if (type.equals("wn")) {
                // work number
                
                contact = findContactBeanById(contactBeanList, id);
                
                // only update the phone number if it is different than the one
                // we already have
                if (!contact.getWorkPhoneNumber().equals(value)) {
                    contact.setWorkPhoneNumber(value);
                    ContactManager.updatePhoneNumber(contact,
                            PhoneNumberBean.PHONETYPEID_WORK);
                    updateContactInSession(req, contact);
                }
            } else if (type.equals("hn")) {
                // home number
                
                contact = findContactBeanById(contactBeanList, id);
                
                // only update the phone number if it is different than the one
                // we already have
                if (!contact.getHomePhoneNumber().equals(value)) {
                    contact.setHomePhoneNumber(value);
                    ContactManager.updatePhoneNumber(contact,
                            PhoneNumberBean.PHONETYPEID_HOME);
                    updateContactInSession(req, contact);
                }
            } else if (type.equals("af")) {
                // home number
                
                contact = findContactBeanById(contactBeanList, id);
                
                // only update the phone number if it is different than the one
                // we already have
                if (!contact.getAudioName().equals(value)) {
                    contact.setAudioName(value);
                    ContactManager.updateContact(contact);
                    updateContactInSession(req, contact);
                }
            }
        }
        htmlShowContacts(req, res);
    }
    
    /**
     * delete a contact
     */
    private void htmlProcessContactDelete(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String contactId = req.getParameter("contactId");
        if (contactId != null) {
            BeanCollection contactBeanList = this
                    .retrieveContactSortableBeanList(req);
            ContactBean contact = findContactBeanById(contactBeanList,
                    contactId);
            if (contact == null) {
                ErrorBean errorBean = new ErrorBean();
                errorBean
                        .addError(
                        "The contact cannot be located. Most likely it has been deleted.",
                        FormError.SEVERITY_INFO);
                req.setAttribute("errorBean", errorBean);
            } else if (ContactManager.deleteContact(contact)) {
                deleteContactInSession(req, contact);
            }
        } else {
            // //
            System.out.println("cannot Delete, no contactId specified.");
            // //
            ErrorBean errorBean = new ErrorBean();
            errorBean
                    .addError(
                    "The contact cannot be located. Most likely it has been deleted.",
                    FormError.SEVERITY_INFO);
            req.setAttribute("errorBean", errorBean);
        }
        htmlShowContacts(req, res);
    }
    
    /**
     * edit the spellings
     */
    private void htmlProcessContactSpellingEdit(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        String buttonPressed = req.getParameter("buttonPressed");
        System.out.println("ContactcController.htmlProcessContactSpellingEdit");
        if (buttonPressed == null)
            buttonPressed = "";
        
        // determine which button was pressed
        // either save, cancel, or else try to find one of the "del" buttons
        if (buttonPressed.equalsIgnoreCase("save")
        || buttonPressed.equalsIgnoreCase("next")
        || buttonPressed.equalsIgnoreCase("previous")
        || req.getParameter("apply") != null) {
            
            String contactId = req.getParameter("contactId");
            
            // prepare the AltSpellingBean that we'll use to send to the manager
            AltSpellingBean altSpellingBean = new AltSpellingBean();
            altSpellingBean.setOwnerId(Integer.parseInt(contactId));
            altSpellingBean.setOwnerTypeId(ContactBean.CONTACT_TYPE_ID);
            
            String spelling = "";
            String id = "";
            String name = "";
            String type = "";
            
            // check if there is a new firstname to add
            spelling = req.getParameter("newFirst");
            if (spelling != null && spelling.length() > 1) {
                altSpellingBean
                        .setAltspellingTypeId(AltSpellingBean.TYPE_FIRSTNAME);
                altSpellingBean.setSpelling(spelling);
                AltSpellingManager.insertAltSpelling(altSpellingBean);
            }
            
            // check if there is a new lastname to add
            spelling = req.getParameter("newLast");
            if (spelling != null && spelling.length() > 1) {
                altSpellingBean
                        .setAltspellingTypeId(AltSpellingBean.TYPE_LASTNAME);
                altSpellingBean.setSpelling(spelling);
                AltSpellingManager.insertAltSpelling(altSpellingBean);
            }
            
            // check if there is a new lastname to add
            spelling = req.getParameter("newAlias");
            if (spelling != null && spelling.length() > 1) {
                altSpellingBean
                        .setAltspellingTypeId(AltSpellingBean.TYPE_NAME_ALIAS);
                altSpellingBean.setSpelling(spelling);
                AltSpellingManager.insertAltSpelling(altSpellingBean);
            }
            
            // now update all the alternate spellings
            // loop thru all the parameters, and look for the ones that are
            // called
            // fstX or lstX - where X is the altSpellingId
            Enumeration params = req.getParameterNames();
            while (params.hasMoreElements()) {
                name = (String) params.nextElement();
                type = name.substring(0, 3);
                id = name.substring(3);
                
                if (type.equals("fst")) {
                    String _name = req.getParameter(name);
                    if (_name == null || _name.length() == 0) {
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        AltSpellingManager.deleteAltSpelling(altSpellingBean);
                    } else {
                        altSpellingBean.setAltspellingTypeId(AltSpellingBean.TYPE_FIRSTNAME);
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        altSpellingBean.setSpelling(_name);
                        AltSpellingManager.updateAltSpelling(altSpellingBean);
                    }
                } else if (type.equals("lst")) {
                    String _name = req.getParameter(name);
                    if (_name == null || _name.length() == 0) {
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        AltSpellingManager.deleteAltSpelling(altSpellingBean);
                    } else {
                        altSpellingBean
                                .setAltspellingTypeId(AltSpellingBean.TYPE_LASTNAME);
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        altSpellingBean.setSpelling(_name);
                        AltSpellingManager.updateAltSpelling(altSpellingBean);
                    }
                } else if (type.equals("als")) {
                    String _name = req.getParameter(name);
                    if (_name == null || _name.length() == 0) {
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        AltSpellingManager.deleteAltSpelling(altSpellingBean);
                    } else {
                        altSpellingBean
                                .setAltspellingTypeId(AltSpellingBean.TYPE_NAME_ALIAS);
                        altSpellingBean.setAltSpellingId(Integer.parseInt(id));
                        altSpellingBean.setSpelling(_name);
                        AltSpellingManager.updateAltSpelling(altSpellingBean);
                    }
                }
            }
            
            // get all the spellings and update the contactBean
            BeanCollection sortableBeanList = retrieveContactSortableBeanList(req);
            BeanCollection altSpellings = new BeanCollection();
            ContactBean contact = this.findContactBeanById(sortableBeanList,
                    contactId);
            AltSpellingManager.lookupAltSpellingByOwner(contact.getContactId(),
                    ContactBean.CONTACT_TYPE_ID, altSpellings);
            contact.setAltSpellings(altSpellings);
            updateContactInSession(req, contact);
            
            if (buttonPressed.equalsIgnoreCase("save")) {
                // redirect back to contactDetail page
                htmlShowContactDetail(req, res);
            } else {
                // display the contact spelling page
                htmlShowContactSpelling(req, res);
            }
            
        } else if (buttonPressed.equalsIgnoreCase("cancel")) {
            htmlShowContactDetail(req, res);
            return;
            
        } else {
            htmlProcessContactSpellingDelete(req, res);
            return;
        }
    }
    
    /**
     * delete a spelling this method will be called by
     * htmlProcessContactSpellingEdit since there is only one form in
     * contactSpelling.jsp
     */
    private void htmlProcessContactSpellingDelete(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        String contactId = req.getParameter("contactId");
        String buttonPressed = req.getParameter("buttonPressed");
        
        ContactBean contact;
        BeanCollection contactList;
        BeanCollection altSpellings = new BeanCollection();
        AltSpellingBean altSpellingBean = new AltSpellingBean();
        boolean isSuccess = false;
        String id;
        boolean foundDel = false;
        
        if (buttonPressed != null
                && buttonPressed.substring(0, 3).equals("del")) {
            id = buttonPressed.substring(3);
            
            altSpellingBean.setAltSpellingId(Integer.parseInt(id));
            isSuccess = AltSpellingManager.deleteAltSpelling(altSpellingBean);
            
            // update the contact in the session if we were able to update db
            if (isSuccess) {
                contactList = retrieveContactSortableBeanList(req);
                contact = findContactBeanById(contactList, contactId);
                if (contact != null) {
                    AltSpellingManager.lookupAltSpellingByOwner(Integer
                            .parseInt(contactId), ContactBean.CONTACT_TYPE_ID,
                            altSpellings);
                    contact.setAltSpellings(altSpellings);
                    updateContactInSession(req, contact);
                }
            }
            foundDel = true;
        }
        
        if (!foundDel) {
            System.out.println("did not find any param starting with del");
        }
        // display the contactSpelling page
        htmlShowContactSpelling(req, res);
    }
    
    /**
     * helper method update contact in the session ContactSortableBeanList
     */
    private void updateContactInSession(HttpServletRequest req,
            ContactBean contact) throws ServletException, IOException {
        
        // delete
        deleteContactInSession(req, contact);
        // add
        addContactInSession(req, contact);
    }
    
    /**
     * helper method delete contact bean in the session ContactSortableBeanList
     */
    private void deleteContactInSession(HttpServletRequest req,
            ContactBean contact) throws ServletException, IOException {
        
        // retrive from session
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        contactBeanList.removeItem(findContactBeanById(contactBeanList, contact
                .getContactId()));
        
        // Set the grammar bit
        ApplicationManager.setModifiedGrammar(retrieveApplicationBean(),
                GrammarBuilder.MASK_CONTACTS_GRAMMAR);
    }
    
    /**
     * helper method add the ContactBean in the session ContactSortableBeanList
     */
    private void addContactInSession(HttpServletRequest req, ContactBean contact)
    throws ServletException, IOException {
        // retrieve from session
        BeanCollection contactBeanList = retrieveContactSortableBeanList(req);
        // add the cont0act
        contactBeanList.addItem(contact);
        // store back to session
        storeContactSortableBeanList(req, contactBeanList);
        // Set the grammar bit
        ApplicationManager.setModifiedGrammar(retrieveApplicationBean(),
                GrammarBuilder.MASK_CONTACTS_GRAMMAR);
    }
    
    /**
     * returns the previous ContactBean if there is no previous item (the id
     * passed in is the first element in the list) will return null
     */
    protected ContactBean findPreviousContactBean(
            BeanCollection contactBeanList, int contactId) {
        System.out.println("findPreviousContactBean");
        ContactBean contactBean = null;
        List sortedList = getSortedContactList(contactBeanList);
        
        for (int i = 0; i < sortedList.size(); i++) {
            contactBean = (ContactBean) sortedList.get(i);
            if (contactBean.getContactId() == contactId) {
                if (i == 0) {
                    contactBean = null;
                } else {
                    contactBean = (ContactBean) sortedList.get(i - 1);
                    // System.out.println("found it: " +
                    // contactBean.getFirstname() + " " +
                    // contactBean.getLastname());
                }
                break;
            }
        }
        return contactBean;
    }
    
    /**
     * returns the next ContactBean if there is no next item (the id passed in
     * is the last element in the list) will return null
     */
    protected ContactBean findNextContactBean(BeanCollection contactBeanList,
            int contactId) {
        
        ContactBean current = null;
        ContactBean contactBean = null;
        List sortedList = getSortedContactList(contactBeanList);
        for (int i = 0; i < sortedList.size(); i++) {
            current = (ContactBean) sortedList.get(i);
            if (current.getContactId() == contactId) {
                if (i + 1 < sortedList.size()) {
                    contactBean = (ContactBean) sortedList.get(i + 1);
                    // //System.out.println("found it: " +
                    // contactBean.getFirstname() + " " +
                    // contactBean.getLastname());
                }
                break;
            }
        }
        return contactBean;
    }
    
    /**
     * helper function if the contact is the first in the list, returns "first"
     * last in the list, returns "last" otherwise, returns null
     */
    private String setNavigate(BeanCollection contactBeanList,
            ContactBean contact) {
        List sortedList = getSortedContactList(contactBeanList);
        ContactBean con = null;
        
        if (!sortedList.isEmpty()) {
            if (sortedList.size() == 1) {
                return ContactController.CONTACT_NAVIGATE_ONLY;
            }
            
            con = (ContactBean) sortedList.get(0);
            if (con.getContactId() == contact.getContactId()) {
                return ContactController.CONTACT_NAVIGATE_FIRST;
            }
            
            con = (ContactBean) sortedList.get(sortedList.size() - 1);
            if (con.getContactId() == contact.getContactId()) {
                return ContactController.CONTACT_NAVIGATE_LAST;
            }
        }
        return ContactController.CONTACT_NAVIGATE_MIDDLE;
    }
    
    /**
     * helper function returns a BeanCollection of all contacts with the same
     * phonetic code excluding the contact with passed contactId
     */
    private BeanCollection getOtherContactsWithSamePhoneticCode(
            String contactId, String phoneticCode) {
        BeanCollection contactList = new BeanCollection();
        ContactBean contact = null;
        ContactManager.lookupContactsByPhoneticCode(phoneticCode, contactList);
        
        // exclude contact with contactId from the list
        for (int i = 0; i < contactList.size(); i++) {
            contact = (ContactBean) contactList.getItem(i);
            if (contact.getContactId() == Integer.parseInt(contactId)) {
                contactList.removeItem(i);
                break;
            }
        }
        return contactList;
    }
    
    /**
     * helper function checks to see if phonetic code is different between two
     * contactBeans
     */
    private boolean isSamePhoneticCode(ContactBean cb1, ContactBean cb2) {
        String pc1 = ContactBean.generatePhoneticCode(cb1.getFirstname(), cb1
                .getLastname());
        String pc2 = ContactBean.generatePhoneticCode(cb2.getFirstname(), cb2
                .getLastname());
        return pc1.equals(pc2);
    }
    
    public boolean provisionVoicemail(String reqHost, String contactId, String firstName, String lastName, String phoneNumber, String email) {
        String host = (String) VoxAttendant.getProps().getProperty("voicemailProvisionUrl");
        host = reqHost + host;
        if (host != null && host.length() > 0) {
            System.out.println("Provisioning voicemail for contactId: " + contactId + ", to host: " + host);
            URL url;
            URLConnection urlConn;
            DataOutputStream printout;
            DataInputStream input;
            try {
                url = new URL(host);
                // URL connection channel.
                urlConn = url.openConnection();
                // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput(true);
                // Let the RTS know that we want to do output.
                urlConn.setDoOutput(true);
                // No caching, we want the real thing.
                urlConn.setUseCaches(false);
                // Specify the content type.
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                
                // Send POST output.
                printout = new DataOutputStream(urlConn.getOutputStream());
                String content =
                        "contactId=" + URLEncoder.encode(contactId,"UTF-8") +
                        "&firstName=" + URLEncoder.encode(firstName,"UTF-8") +
                        "&lastName=" + URLEncoder.encode(lastName,"UTF-8") +
                        "&phoneNumber=" + URLEncoder.encode(phoneNumber,"UTF-8") +
                        "&email=" + URLEncoder.encode(email,"UTF-8");
                printout.writeBytes(content);
                printout.flush();
                printout.close();
                
                // Get response data.
                input = new DataInputStream(urlConn.getInputStream());
                String str;
                while (null != ((str = input.readLine()))) {
                    System.out.println(str);
                }
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
        
    }
    
}
