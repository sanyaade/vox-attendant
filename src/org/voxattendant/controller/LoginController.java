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
import javax.servlet.http.HttpSession;

import org.voxattendant.model.ApplicationBean;
import org.voxattendant.util.UtilBundle;

public class LoginController extends BaseController {
    
    private static final long serialVersionUID = 3225775823358574099L;
    
    public static final String CONTROLLER = "/LoginController";
    
    public void init(javax.servlet.ServletConfig config)
    throws javax.servlet.ServletException {
        
        super.init(config);
    }
    
    
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }
    
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String reqState = req.getParameter("reqState");
            if (reqState == null || reqState.length() == 0) {
                reqState = "htmlShowLogin";
            }
            // //
            UtilBundle.log.logMessage("LoginController", "miloService", "ReqState is: " + reqState, 3);
            System.out.println("LoginController ReqState is: " + reqState);
            // //
            if (reqState.equals("htmlShowLogin")) {
                htmlShowLogin(req, res);
            } else if (reqState.equals("htmlProcessLogin")) {
                htmlProcessLogin(req, res);
            } else if (reqState.equals("htmlProcessLogout")) {
                htmlProcessLogout(req, res);
            } else {
                if (!isUserSessionValid(req)) {
                    htmlShowLogin(req, res);
                    return;
                }
                if (reqState.equals("htmlShowHome")) {
                    htmlShowHome(req, res);
                } else {
                    // //
                    System.err.println("LoginController: "
                            + reqState
                            + " does not match a valid request state");
                    // //
                    UtilBundle.log.logMessage("LoginController", "miloService", reqState + " does not match a valid request state", 3);
                    htmlShowLogin(req, res);
                }
            }
        } catch (Exception e) {
            // //
            System.err.println("Error: LoginController");
            // //
            UtilBundle.log.logMessage("LoginController", "miloService", "Error" + e.toString(), 3);
            e.printStackTrace();
        }
    }
    
    /**
     * display the html login page
     */
    private void htmlShowLogin(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        // //
        System.err.println("showing login");
        UtilBundle.log.logMessage("LoginController", "htmlShowLogin", "show login", 3);
        // //
        displayHtmlJsp(req, res, "login.jsp");
    }
    
    /**
     * process the login request verifies the passcode with the passcode stored
     * in the database if the user submits valid passcode, log the user in, and
     * redirect to the home page if not, redirect to the login page and display
     * an error message
     */
    private void htmlProcessLogin(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        // //
        System.err.println("processing login");
        UtilBundle.log.logMessage("LoginController", "htmlProcessLogin", "processing login", 3);
        // //
        String errorType;
        String passcode = req.getParameter("passcode");
        
        ApplicationBean appBean = retrieveApplicationBean();
        System.err.println("retrieved retrieveApplicationBean");
        // check if passcode matches
        if (appBean.getPasscode().equals(passcode)) {
            storeUserSession(req);
            displayHtmlJsp(req, res, "home.jsp");
            return;
        } else {
            errorType = "invalidPasscode";
            // if reached this point, there was an error,
            // redirect back to login, with errorType
            req.setAttribute("errorType", errorType);
            displayHtmlJsp(req, res, "login.jsp");
        }
    }
    
    /**
     * process logout of the user clean up session variables and redirect to the
     * login page
     */
    private void htmlProcessLogout(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        // //
        System.err.println("processing logout");
        UtilBundle.log.logMessage("LoginController", "htmlProcessLogin", "processing logout", 3);
        // //
        // get rid of the session
        HttpSession session = req.getSession();
        session.invalidate();
        
        displayHtmlJsp(req, res, "login.jsp");
    }
    
    /**
     * process logout of the user clean up session variables and redirect to the
     * login page
     */
    private void htmlShowHome(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        displayHtmlJsp(req, res, "home.jsp");
    }
}
