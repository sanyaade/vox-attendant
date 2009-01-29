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
   ApplicationBean appBean = (ApplicationBean) application.getAttribute("ApplicationBean");
%>
<html>
<head>
   <title>Home VoxAttendant.org</title>
   <%@ include file="scripts.jsp" %>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      onLoad="MM_preloadImages('<%=imageDir%>/home_over.jpg','<%=imageDir%>/contacts_over.jpg','<%=imageDir%>/department_over.jpg','<%=imageDir%>/settings_over.jpg','<%=imageDir%>/logout_over.jpg')"
      background="<%=imageDir%>/cheat_2.gif">

<table border="0" cellspacing="0" cellpadding="0" align="left">
   <!-- image & menu -->
   <tr><td colspan="2">
   <%@ include file="menuHome.jsp" %>
   </td></tr>
   <!------------------>

   <tr>
      <!-- margin indent -->
      <td width="<%=margin%>">&nbsp;</td>

      <td>
         <!-- start page content -->
         <table width="700">

            <tr><td>&nbsp;</td></tr>
            <tr>
               <td class="plaintext"><p class="heading">Welcome to the AutoAttendant</p>
                   This administration site allows you to manage all the employee contacts,
                   departments and settings for your AutoAttendant application.
                   If you have any questions about this application, send an email to
                   <a href="mailto:voxattendant@icoa.com">voxattendant@icoa.com</a>
               </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
               <td >
                  <p class="heading2">
                     The current phone number for this application is:
                  </p>
               </td>
            </tr>
            <tr>
               <td class="plaintext">
                  <p><attendant:displayText format="phonenumber" text="<%=appBean.getApplicationNumber()%>"/></p>
               </td>
            </tr>

         </table>
         <!-- end page content-->
      </td>
   </tr>

</table>

</body>
</html>