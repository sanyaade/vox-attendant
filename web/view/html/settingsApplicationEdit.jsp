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
   ApplicationBean appBean = (ApplicationBean)request.getAttribute("appBean");
%>
<html>
<head>
   <title>Settings | Application Edit</title>
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

      var element = form.appNumber;

      if(element.value == "" || !isValidPhoneNumber(trim(element.value))) {
         alert(ERROR_PHONENUMBER_ONLY_DIGITS + ERROR_PHONE_NON_0_1);
         element.focus();
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
   <tr><td colspan="2">
   <%@ include file="menuSettings.jsp" %>
   </td></tr>

   <tr><td>
   <table>
   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>
         <table>


            <tr><td>&nbsp;</td></tr>

            <tr><td>
            <form method="get" action="<%=response.encodeURL(SettingsController)%>" onSubmit="return validateForm(this);">
               <input type="hidden" name="reqState" value="htmlProcessApplicationEdit">
               <table border="0">
                  <tr>
                     <td colspan="2" class="heading">
                     Edit Main Application Number
                     </td>
                  </tr>
                  <tr>
                     <td colspan="2" class="plaintext">
                        Add or edit the phone number for the AutoAttendant application.
                     </td>
                  </tr>
                  <tr><td>&nbsp;</td></tr>
                  <tr>
                     <td class="plaintext">
                     Phone Number:
                     </td>
                     <td>
                     <input type="text" name="appNumber" value="<%=appBean.getApplicationNumber()%>" maxlength="50">
                     </td>
                  </tr>

                  <tr>
                     <td>&nbsp;</td>
                     <td class="plaintext">
                        <font size="1">
                           Main Application number must be a 10-digit North American number<br>
                           (with no spaces in-between digits) or a sip address<br/>
                           ex: sip:aa@127.0.0.1
                        </font>
                     </td>
                  </tr>
                  <tr>
                     <td>&nbsp;</td>
                     <td align="right">
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
