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
   ContactBean contact = (ContactBean) request.getAttribute("contact");
   String navigate = (String) request.getAttribute("navigate");
   if(navigate == null) navigate = "";

   boolean newContact = false;
   if(contact == null) {
     newContact = true;
     contact = new ContactBean();
   }
%>
<html>
<head>
   <title>Contact | Edit</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   var cancelled = false;
   function setCancelled() {
      cancelled = true;
   }
   function validateAndSubmitForm(form) {
      if(validateForm(form))
         form.submit();
      else
         return;
   }
   function validateForm(form) {
      if(cancelled)
         return true;

      var errorMessages = "" ;
      var hasPhoneNumber = false;

      for(var i = 0; i < form.elements.length; i++) {
         element = form.elements[i];
         if((element.name == "firstname") || (element.name == "lastname")) {
            if(trim(element.value) == "") {
               errorMessages = ERROR_NAMES_NON_BLANK;
               element.focus();
               break;
            }
            if(!validName(trim(element.value))) {
               errorMessages = ERROR_NAMES_ONLY_LETTERS;
               element.focus();
               break;
            }
         }
      }
      if(errorMessages == "") {
         var workPhoneNumber = trim(form.workPhoneNumber.value);
         var mobilePhoneNumber = trim(form.mobilePhoneNumber.value);
         var homePhoneNumber = trim(form.homePhoneNumber.value);
         var extension = trim(form.extension.value);

         if(workPhoneNumber == "") {
            errorMessages = ERROR_PHONENUMBERS_ONLY_DIGITS + ERROR_PHONE_NON_0_1;
            form.workPhoneNumber.focus();
         }
         else {
            if(!isValidPhoneNumber(workPhoneNumber)) {
               errorMessages = ERROR_WORKPHONE_ONLY_DIGITS + ERROR_PHONE_NON_0_1;
               form.workPhoneNumber.focus();
            }
            else {
               if((mobilePhoneNumber != "") &&
                  !isValidPhoneNumber(mobilePhoneNumber)) {
                  errorMessages = ERROR_CELLPHONE_ONLY_DIGITS + ERROR_PHONE_NON_0_1;
                  form.mobilePhoneNumber.focus();
               }
            }
         }
      }
      if(errorMessages != "") {
         alert(errorMessages);
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
   <%
      if(newContact) {
         submenu="new";
      }
   %>
   <%@ include file="menuContacts.jsp" %>
   </td></tr>

   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>
         <table>

            <tr><td>&nbsp;</td></tr>

            <form method="post" action="<%=response.encodeURL(ContactController)%>" onSubmit="return validateForm(this);">
            <tr>
         <%
            if(!newContact) {
         %>
               <td align="right">
                  <input type="hidden" name="buttonPressed" value="none">
                  <attendant:navigate value="<%=navigate%>" type="button" onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)"/>
               </td>
         <% }%>
            </tr>

            <tr><td>
            <table border="0" bordercolor="green" width="700">

         <%-- this section is only displayed if we are editing an existing contact --%>
         <% if(!newContact) { %>
               <tr>
                  <td><font class="heading">Contact Profile:</font>
                      <font class="heading2"> <%=contact.getFirstname() %> <%=contact.getLastname() %></font>
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">
                     To update the employee contact below, make all appropriate
                     edits or changes directly into the fields and then save the changes.
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr><td class="heading2">Contact Information</td></tr>
               <tr><td colspan="2"><hr></td></tr>
         <%-- -------------------------------------------------------------------- --%>
         <% } else { %>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td class="heading">
                     New Contact
                  </td>
               </tr>
               <tr>
                  <td class="plaintext">
                     Please fill in the information below to add a new contact to the AutoAttendant.
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
         <% } %>
               <tr><td><ul><table border="0" bordercolor="blue">
               <tr>
                  <td class="heading2">
                     First Name:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getFirstname() %>" name="firstname" maxlength="25">
                  </td>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td class="heading2">
                  Last Name:
                  </td>
                  <td class="plaintext">
                  <input type="text" value="<%=contact.getLastname() %>" name="lastname" maxlength="25">
                  </td>
               </tr>
               
               <tr>
                  <td class="heading2">
                 Active:

                  </td>
                  <td>
            <% if(contact.isActive()) { %>
                     <input type="checkbox" name="isActive" value="true" CHECKED>
            <% } else { %>
                     <input type="checkbox" name="isActive" value="true">
            <% } %>
                  </td>
               </tr>
               <tr>
                  <td class="heading2" nowrap>
                 Voicemail Enabled

                  </td>
                  <td>
            <% if(contact.isVoicemailEnabled()) { %>
                     <input type="checkbox" name="isVoicemailEnabled" value="true" CHECKED>
            <% } else { %>
                     <input type="checkbox" name="isVoicemailEnabled" value="true">
            <% } %>
                  </td>
               </tr>

         <%
            if (!newContact) {
         %>
               <tr>
                  <td class="heading2">
                  Audio Filename:
                  </td>
                  <td class="plaintext">
                  <input type="text" value="<%=contact.getAudioName() %>" name="audioName" maxlength="45">
                  <%if (contact.getAudioName() != null && contact.getAudioName().length() > 0) {%>
                     <attendant:playAudio name="<%=contact.getAudioName()%>" audiodir="<%=contactAudioDir%>">
                        Listen >>
                     </attendant:playAudio>
                     <%}%>
                  </td>
               </tr>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="2" class="plaintext">
                     <font size="1">
                         These .wav files should be dropped in the "\web\view\audio\contact" directory of the deployment.
                     </font>
                  </td>
               </tr>
         <%
            } else {
         %>
            <tr>
                  <td class="heading2">
                  Audio Filename:
                  </td>
                  <td class="plaintext">
                  <input type="text" value="<%=contact.getAudioName() %>" name="audioName" maxlength="25">
                     
                  </td>
                 
               </tr>
         <%}%>
               <tr><td>&nbsp</td></tr>
               <tr>
                  <td class="heading2">
                     Extension:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getExtension() %>" name="extension">
                  </td>
               </tr>
               <tr>
                  <td class="heading2">
                     Office Phone:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getWorkPhoneNumber() %>" name="workPhoneNumber">
                  </td>
               </tr>
               <tr>
                  <td class="heading2">
                     Mobile Phone:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getMobilePhoneNumber() %>" name="mobilePhoneNumber">
                  </td>
               </tr>
               <tr>
                  <td class="heading2">
                     Home Phone:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getHomePhoneNumber() %>" name="homePhoneNumber">
                  </td>
               </tr>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="2" class="plaintext">
                     <font size="1">
                        Phone numbers must be 10-digit North American numbers (with no spaces in between digits), or a 
                        SIP address in the following format: <b>sip:youraddress</b>
                     </font>
                  </td>
               </tr>

               <tr>
                  <td class="heading2">
                     Email:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getEmail() %>" name="email">
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>

               <tr>
                  <td class="heading2" nowrap>
                     Distinct Information:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getDistinctInfo() %>" name="distinctInfo" maxlength="100">
         
                  </td>
               </tr>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="2" class="plaintext">
                     <font size="1">
                        This field will help distinguish between similar
                        sounding names (e.g., Department Name, Office Location, Job Title)
                     </font>
                  </td>
               </tr>

               <tr><td>&nbsp;</td></tr>

               <tr>
                  <td class="heading2">
                     Notes:
                  </td>
                  <td class="plaintext">
                     <input type="text" value="<%=contact.getNote() %>" name="note" maxlength="100">
                  </td>
               </tr>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="2" class="plaintext">
                     <font size="1">
                        This field will help track employees (e.g., Employee ID Number, Start Date)
                     </font>
                  </td>
               </tr>

               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>
            <% if (newContact) { %>
                     <input type="hidden" name="reqState" value="htmlProcessContactAdd">
            <% } else { %>
                     <input type="hidden" name="reqState" value="htmlProcessContactEdit">
                     <input type="hidden" name="contactId" value="<%=contact.getContactId()%>">
            <% } %>
                     <input type="submit" name="save" value="save">
                     <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">&nbsp;
                  </td>
               </tr>
               </table>
               </ul>
               </td></tr>
            </table>
            </td></tr>
            </form>

         </table>
      </td>
      <!-- end page content -->

   </tr>
</table>

</body>
</html>
