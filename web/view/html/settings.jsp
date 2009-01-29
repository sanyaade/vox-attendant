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
   ApplicationBean appBean = (ApplicationBean)request.getAttribute("appBean");
   OperatorBean operatorBean = (OperatorBean)request.getAttribute("operatorBean");
%>
<html>

<head>
   <title>Settings</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function sendChange(form) {
      form.submit();
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

         <%
            if(errorBean != null) {
         %>
            <tr><td>
               <p class="error"><%= errorBean.getErrorAt(0).getErrorMessage() %></p>
            </td></tr>
         <%
            } else {
         %>
            <tr><td>&nbsp;</td></tr>
         <% } %>
            <tr><td>
               <table>
               <tr>
                  <td class="heading">
                     Settings
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">
                     This page allows you to change the phone number settings
                     for the application and operator.
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td class="heading2">Application Settings
                  <font class="plaintext">
                  (<a class="plaintext" href="<%=response.encodeURL(SettingsController + "?reqState=htmlShowApplicationEdit")%>">edit</a>)
                  </td>
                  </font>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td class="plaintext">The current phone number for this application is:
                     <font class="plaintext">
                     &nbsp;<attendant:displayText format="phonenumber" text="<%=appBean.getApplicationNumber()%>"/>
                     </font>
                  </td>
               </tr>
               </table>
            </td></tr>
            <tr>
                  <td>&nbsp;</td>
            </tr>
            <tr>
                  <td>&nbsp;</td>
            </tr>
            <tr><td>
            <form method="get" action="<%=response.encodeURL(SettingsController)%>">
               <input type="hidden" name="reqState" value="htmlProcessOperatorUpdate">
               <table>
               <tr>
                  <td class="heading2">Operator Settings
                  <font class="plaintext">
                  (<a class="plaintext" href="<%=response.encodeURL(SettingsController + "?reqState=htmlShowOperatorEdit")%>">edit</a>)
                  </font>
                  </td>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td class="plaintext">
                     Select an Operator choice from the pull-down list below,
                     or select Edit to add a new Operator or edit an existing
                     Operator.  Only one Operator can be selected at a time.
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">Current Selected Operator: &nbsp;
                  <!-- Good candidate for Taglib -->
                  <select name="selectedId" onChange="sendChange(this.form)">
                     <option value="-1">(none)</option>
         <%
               if(operatorBean != null) {
                  BeanCollection phoneNumbers = (BeanCollection)operatorBean.getPhoneNumbers();
                  if(phoneNumbers.size() > 0) {
                     for(int i = 0; i < phoneNumbers.size(); i++) {
                        PhoneNumberBean phoneNumberBean = (PhoneNumberBean)phoneNumbers.getItem(i);
         %>
                     <option value="<%=phoneNumberBean.getPhoneNumberId()%>"
         <%             if(phoneNumberBean.getPhoneNumberId() == appBean.getCurrentOperatorNumberId()) { %>
                           SELECTED
         <%             } %>
                     ><%=phoneNumberBean.getNote()%></option>
         <%          }
                  }
               }
         %>
                  </select>
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
