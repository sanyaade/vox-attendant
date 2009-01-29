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
<html>

<head>
<title>Batch Import Tool</title>
</head>

<body>
<h1>Batch Import Tool</h1>

<form action="UtilController?reqState=htmlProcessImport" method="post">
   <table width="600">
      <tr>
      <td colspan="5">
         <h4>
         <p>
         Formats<br>
         <em>(for contacts): Firstname, Lastname, Work Number, Cell Number, Distinct Info</em>
         <br>
         <em>(for departments): Department name, Phone Number</em>
         <br>
         <br>
         White spaces in phone numbers will be strip out automatically.
         If a particular phone number is not available, please put down 'N/A'. There
         must be at least one phone number available, otherwise that entry will not be added.
         <br>
         <br>
         DO NOT leave an entry blank.
         <p>
         </h4>
      </td>
      </tr>
      <tr>
      <td align="center">
         <input type="radio" NAME="importType" VALUE="contact" CHECKED>Contacts
         <input type="radio" NAME="importType" VALUE="entity">Departments
      </td>
      </tr>
      <tr>
      <td align="center">
         <textarea name="csvData" cols="40" rows="10" wrap="VIRTUAL" style="width: 500px"></textarea>
      </td>
      </tr>
      <tr>
      <td align="center">
         <input type="submit" value="Import">
      </td>
      </tr>
   </table>
</form>

</body>
</html>
