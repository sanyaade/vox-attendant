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
   OperatorBean operatorBean = (OperatorBean)request.getAttribute("operatorBean");
%>
<html>

<head>
   <title>Settings | Operator Edit</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   var cancelPressed = false;
   function setCancel() {
      cancelPressed = true;
   }
   var deletePressed = false;
   function setDelete() {
      deletePressed = true;
   }
   function validateForm(form) {
      if(cancelPressed || deletePressed)
         return true;

      for(var i = 0; i < form.elements.length;) {
         element1 = form.elements[i];
         if(element1.type == "text")
            element2 = form.elements[i+1];
         else {
            i ++;
            continue;
         }
         if(element1.name.indexOf("note-") == 0) {
            if(trim(element1.value) == "") {
               alert(ERROR_OPERATOR_NON_BLANK);
               element1.focus();
               return false;
            }
            if(trim(element2.value) == "" || !isValidPhoneNumber(trim(element2.value))) {
               alert(ERROR_PHONENUMBER_ONLY_DIGITS + ERROR_PHONE_NON_0_1);
               element2.focus();
               return false;
            }
         }
         else if(element1.name == "newNote") {
            if((trim(element1.value) == "") && (element2.value != "")) {
               alert(ERROR_OPERATOR_NON_BLANK);
               element1.focus();
               return false;
            }
            if((trim(element1.value) != "") &&
               ((trim(element2.value) == "") || !isValidPhoneNumber(trim(element2.value)))) {
               alert(ERROR_PHONENUMBER_ONLY_DIGITS + ERROR_PHONE_NON_0_1);
               element2.focus();
               return false;
            }
         }
         i += 2;
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
            <table width="700">
               <tr>
                  <td class="heading">
                  Edit Operator Numbers
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">
                  Add or edit the Operator phone numbers. The phone numbers must be 10-digit North American numbers
                  (with no spaces in-between digits)
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
            </table>
            </td></tr>

            <tr><td>
            <form method="post" action="<%=response.encodeURL(SettingsController)%>" onSubmit="return validateForm(this);">
            <input type="hidden" name="reqState" value="htmlProcessOperatorEdit">
            <input type="hidden" name="deletePressed" value="none">
            <table>
               <tr>
                  <td class="plaintext">Operator names<hr></td>
                  <td class="plaintext">Phone numbers<hr></td>
                  <td>&nbsp;</td>
               </tr>
         <%
            BeanCollection phoneNumbers = operatorBean.getPhoneNumbers();
            for(int i = 0; i < phoneNumbers.size(); i++) {
               PhoneNumberBean phoneNumberBean = (PhoneNumberBean)phoneNumbers.getItem(i);
         %>
               <tr>
                  <td valign="top">
                     <input type="text" value="<%=phoneNumberBean.getNote()%>" name="note-<%=phoneNumberBean.getPhoneNumberId()%>" maxlength="25">
                  </td>
                  <td valign="top">
                     <input type="text" value="<%=phoneNumberBean.getNumber()%>" name="number-<%=phoneNumberBean.getPhoneNumberId()%>" maxlength="100">
                  </td>
                  <td valign="top">
                     <input type="button" value="x" name="delete-<%=phoneNumberBean.getPhoneNumberId()%>"
                            onClick="this.form.deletePressed.value=this.name;this.form.submit()">
                  </td>
               </tr>
         <% } %>
               <tr><td>&nbsp;</td></tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td class="plaintext">New Operator Number<hr></td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td><input type="text" name="newNote" value="" maxlength="25"></td>
                  <td><input type="text" name="newNumber" value="" maxlength="100"></td>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td align="right" colspan="3">
                     <input type="submit" name="apply" value="Apply">&nbsp;
                     <input type="submit" name="save" value="Save">&nbsp;
                     <input type="submit" name="cancel" value="Cancel" onClick="setCancel()">
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
