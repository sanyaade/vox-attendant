<%--
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
--%>
<%@ include file="header.jsp" %>
<%
   ErrorBean errorBean = (ErrorBean)request.getAttribute("errorBean");
%>
<html>

<head>
   <title>Settings | Passcode</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   var cancelled = false;
   function setCancelled() {
      cancelled = true;
   }
   function validateForm(form) {
      if(cancelled)
         return true;

      for(var i = 0; i < form.elements.length; i++) {
         element = form.elements[i];
         if(element.type == "password") {
            if(trim(element.value) == "") {
               alert(ERROR_PASSCODE_NON_BLANK);
               element.focus();
               return false;
            }
            else if(!isValidPasscode(trim(element.value))) {
               alert(ERROR_LOGIN);
               element.focus();
               return false;
            }
         }
      }
      if(form.newPasscode.value != form.newPasscode2.value) {
         alert(ERROR_CONFIRM_PASSCODE);
         form.newPasscode2.focus();
         return false;
      }
      return true;
   }
   //  -->
   </script>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      onLoad="MM_preloadImages('<%=imageDir%>/home_over.jpg','<%=imageDir%>/contacts_over.jpg','<%=imageDir%>/department_over.jpg','<%=imageDir%>/settings_over.jpg','<%=imageDir%>/logout_over.jpg')"
      background="<%=imageDir%>/cheat_2.gif">

<table border="0" cellspacing="0" cellpadding="0" align="left">
   <tr><td>
   <%
      submenu="passcode";
   %>
   <%@ include file="menuSettings.jsp" %>
   </td></tr>

   <tr><td>
   <table>
   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>

            <table width="700">


            <tr><td>&nbsp;</td></tr>

            <tr><td>
            <table width="700">
               <tr>
                  <td class="heading">
                  Change Passcode
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">
                     This page allows you to change the passcode to this
                     Administration site.  Please enter your new passcode
                     information below.
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
            </table>
            </td></tr>
         <%
            if(errorBean != null) {
         %>
            <tr><td>
               <p class="error"><%= errorBean.getErrorAt(0).getErrorMessage() %></p>
            </td></tr>
            <tr><td>&nbsp;</td></tr>
         <%
            }
         %>
            <tr><td>
            <form method="get" action="<%=response.encodeURL(SettingsController)%>" onSubmit="return validateForm(this);">
               <input type="hidden" name="reqState" value="htmlProcessPasscodeEdit">
               <table>
                  <tr>
                     <td class="plaintext">Old Passcode:</td>
                     <td><input type="password" name="oldPasscode" value="" maxlength="4"></td>
                  </tr>
                  <tr>
                     <td class="plaintext">New Passcode:</td>
                     <td><input type="password" name="newPasscode" value="" maxlength="4"></td>
                     <td><font size="1">Passcodes must be 4-digits.</font></td>
                  </tr>
                  <tr>
                     <td class="plaintext">Confirm Passcode:</td>
                     <td><input type="password" name="newPasscode2" value="" maxlength="4"></td>
                  </tr>
                  <tr>
                     <td>&nbsp;</td>
                     <td>
                     <input type="submit" name="save" value="Save">
                     <input type="submit" name="cancel" value="Cancel" onClick="setCancelled()">
                     </td>
                  </tr>
               </table>
            </form>
            </td></tr>

         </table>
      </td>
      <!-- end page content -->

   </tr>

</table>


</body>
</html>
