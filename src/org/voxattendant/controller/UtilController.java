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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.voxattendant.manager.ApplicationManager;
import org.voxattendant.manager.ContactManager;
import org.voxattendant.manager.EntityManager;
import org.voxattendant.model.ApplicationBean;
import org.voxattendant.model.ContactBean;
import org.voxattendant.model.EntityBean;
import org.voxattendant.util.BeanCollection;
import org.voxattendant.util.GrammarBuilder;


/**
 * This is an Utility class that contains helper tools for the AutoAttendant
 * application. Currently, there's only the BatchImporter.
 *
 * @author Simon Tang
 * @version $Revision: 1.2 $ $Date: 2007-02-24 15:50:52 $
 */
public class UtilController extends BaseController {
    
    private static final long serialVersionUID = 3351036986959579761L;
    
    /**
     * The init() method. Currently not used.
     *
     * @param config
     *            the ServletConfig file
     */
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
                reqState = "htmlShowBatchImporter";
            }
            // //
            System.out.println("UtilController ReqState is: " + reqState);
            // //
            if (reqState.equals("htmlShowBatchImporter")) {
                showBatchImporter(req, res);
            } else if (reqState.equals("htmlProcessImport")) {
                processImport(req, res);
            } else {
                displayVxmlJsp(req, res, "error.jsp");
            }
        } catch (Exception e) {
            System.err.println("Error: UtilController");
            System.err.println(e.toString());
        }
    }
    
    /**
     * Displays the main page for importing a batch file.
     *
     * @param req
     *            the request
     * @param res
     *            the response
     */
    protected void showBatchImporter(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        
        displayHtmlJsp(req, res, "batchImport.jsp");
    }
    
    /**
     * Processes the import request. It reads from a CSV file with the following
     * format:
     * <ol>
     * <li>For Contacts: Firstname,Lastname,Office number, Cell number,
     * Distinct Info
     * <li>For Entities: Name,Number
     * </ol>
     *
     * If any of the information is not available, "N/A" should be entered
     * instead. Currently, sub-entities are not being handled.
     *
     * @param req
     *            the request
     * @param res
     *            the response
     */
    protected void processImport(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        String csvData = (String) req.getParameter("csvData");
        String importType = (String) req.getParameter("importType");
        
        ApplicationBean appBean = retrieveApplicationBean();
        BeanCollection conflictList = new BeanCollection();
        
        if (importType.equalsIgnoreCase("contact")) {
            // Read on line at a time...
            BufferedReader br = new BufferedReader(new StringReader(csvData));
            String line;
            while ((line = br.readLine()) != null) {
                importContactData(line, conflictList);
            }
            int modifiedGrammar = GrammarBuilder
                    .setBit(appBean.getModifiedGrammar(),
                    GrammarBuilder.MASK_CONTACTS_GRAMMAR);
            appBean.setModifiedGrammar(modifiedGrammar);
        } else if (importType.equalsIgnoreCase("entity")) {
            // Read on line at a time...
            BufferedReader br = new BufferedReader(new StringReader(csvData));
            String line;
            while ((line = br.readLine()) != null) {
                importEntityData(line, conflictList);
            }
            int modifiedGrammar = GrammarBuilder
                    .setBit(appBean.getModifiedGrammar(),
                    GrammarBuilder.MASK_ENTITIES_GRAMMAR);
            appBean.setModifiedGrammar(modifiedGrammar);
        }
        
        ApplicationManager.updateApplicationBean(appBean);
        
        req.setAttribute("type", importType);
        req.setAttribute("conflictList", conflictList);
        displayHtmlJsp(req, res, "batchReport.jsp");
    }
    
    //
    // Helper function
    //
    /**
     * Imports Contacts
     *
     * @param data
     *            one line of contact entry
     * @param conflictList
     *            list of conflicting entries
     */
    protected boolean importContactData(String data, BeanCollection conflictList) {
        // //
        System.out.println("Data:" + data);
        // //
        StringTokenizer nizer = new StringTokenizer(data, ",");
        if (nizer.countTokens() != 5)
            return false;
        
        // Must have FOUR entries...
        String firstname = nizer.nextToken();
        String lastname = nizer.nextToken();
        String workPhoneNumber = nizer.nextToken();
        String mobilePhoneNumber = nizer.nextToken();
        String distinctInfo = nizer.nextToken();
        // //
        System.out.println("Firstname:" + firstname);
        System.out.println("Lastname:" + lastname);
        System.out.println("Office:" + workPhoneNumber);
        System.out.println("Mobile:" + mobilePhoneNumber);
        System.out.println("DistinctInfo:" + distinctInfo);
        // //
        
        // put values in a ContactBean
        ContactBean contact = new ContactBean();
        contact.setFirstname(firstname.trim());
        contact.setLastname(lastname.trim());
        if (!"N/A".equalsIgnoreCase(workPhoneNumber.trim()))
            contact.setWorkPhoneNumber(stripWhiteSpace(workPhoneNumber));
        if (!"N/A".equalsIgnoreCase(mobilePhoneNumber.trim()))
            contact.setMobilePhoneNumber(stripWhiteSpace(mobilePhoneNumber));
        contact.setPhoneticCode();
        if (!"N/A".equalsIgnoreCase(distinctInfo.trim()))
            contact.setDistinctInfo(distinctInfo);
        contact.setActive(true);
        
        if (contact.getWorkPhoneNumber().length() == 0
                && contact.getMobilePhoneNumber().length() == 0) {
            contact.setActive(false);
            conflictList.addItem(contact);
        } else if (contact.getDistinctInfo().length() == 0) {
            // We need to check for conflicts? If so, how do we handle them,
            // since this is a batch entry...
            BeanCollection contacts = new BeanCollection();
            ContactManager.lookupContactsByPhoneticCode(contact
                    .getPhoneticCode(), contacts);
            if (contacts.size() > 0) {
                // Has conflict, set the current contact as inactive
                contact.setActive(false);
                conflictList.addItem(contact);
            }
        }
        // add the new contact
        return ContactManager.addContact(contact);
    }
    
    /**
     * Imports Entities
     *
     * @param data
     *            one line of entity entry
     * @param conflictList
     *            list of conflicting entries
     */
    protected boolean importEntityData(String data, BeanCollection conflictList) {
        // //
        System.out.println("Data:" + data);
        // //
        StringTokenizer nizer = new StringTokenizer(data, ",");
        if (nizer.countTokens() != 2)
            return false;
        
        // Must have TWO entries...
        String name = nizer.nextToken();
        String phoneNumber = nizer.nextToken();
        // //
        System.out.println("Name:" + name);
        System.out.println("PhoneNumber:" + phoneNumber);
        // //
        
        // put values in a ContactBean
        EntityBean entity = new EntityBean();
        entity.setName(name);
        if (!phoneNumber.equalsIgnoreCase("N/A")) {
            entity.setPhoneNumber(stripWhiteSpace(phoneNumber));
            entity.setActive(true);
        } else {
            entity.setActive(false);
            conflictList.addItem(entity);
        }
        // add the new contact
        return EntityManager.addEntity(entity);
    }
    
    /**
     * Removes all the potential white spaces.
     *
     * @param input
     *            the text to perform the action on
     */
    protected String stripWhiteSpace(String input) {
        StringTokenizer nizer = new StringTokenizer(input, " \n-()");
        StringBuffer buffer = new StringBuffer();
        while (nizer.hasMoreTokens()) {
            buffer.append(nizer.nextToken());
        }
        return buffer.toString();
    }
}
