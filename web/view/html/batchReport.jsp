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
   BeanCollection conflictList = (BeanCollection)request.getAttribute("conflictList");
   boolean isContactType = ((String)request.getAttribute("type")).equalsIgnoreCase("contact");
   ContactBean contact = null;
   EntityBean entity = null;
%>
<html>

<head>
<title>Report from Batch Import</title>
</head>

<body>
<h1>Report from Batch Import </h1>

<% if(isContactType) { %>

<% //////////////////// CONTACT ///////////////////////////////////////////////
   if(conflictList.size() == 0) {
%>
   <h4>
   There is no conflict in the data. All data have been successfully added into the database.
   </h4>
<%
   } else {
%>
   <table border="1" width="700">
      <tr>
      <td colspan="5">
         <h4>
         <p>
         The following items xhave been added into the database but are set to inactive.
         Either they are in conflict with one or more data in the database or no phone
         number has been specified. Please add the necessary distinct information and/or
         phone number through the Administration page.
         <p>
         </h4>
      </td>
      </tr>
      <tr>
         <td><b>First Name</b></td>
         <td><b>Last Name</b></td>
         <td><b>Office Number</b></td>
         <td><b>Cell Number</b></td>
         <td><b>Distinct Information</b></td>
      </tr>
<%
      for(int i = 0; i < conflictList.size(); i++) {
         contact = (ContactBean)conflictList.getItem(i);
%>
      <tr>
         <td><%=contact.getFirstname()%></td>
         <td><%=contact.getLastname()%></td>
         <td><%=contact.getWorkPhoneNumber()%></td>
         <td><%=contact.getMobilePhoneNumber()%></td>
         <td><%=contact.getDistinctInfo()%></td>
      </tr>
<%    } %>
   </table>
<% } %>

<% } else { %>

<% //////////////////// ENTITY ///////////////////////////////////////////////
   if(conflictList.size() == 0) {
%>
   <h4>
   All data have been successfully added into the database.
   </h4>
<%
   } else {
%>
   <table border="1" width="700">
      <tr>
      <td colspan="5">
         <h4>
         <p>
         The following items have been added into the database but are set to inactive.
         There is no phone number specified. Please add the phone number through the Administration page.
         <p>
         </h4>
      </td>
      </tr>
      <tr>
         <td><b>Department Name</b></td>
         <td><b>Phone Number</b></td>
      </tr>
<%
      for(int i = 0; i < conflictList.size(); i++) {
         entity = (EntityBean)conflictList.getItem(i);
%>
      <tr>
         <td><%=entity.getName()%></td>
         <td><%=entity.getPhoneNumber()%></td>
      </tr>
<%    } %>
   </table>
<% } %>

<% } %>
   <br>
   <br>
   <a href="UtilController">Import another file</a>
</body>

</html>