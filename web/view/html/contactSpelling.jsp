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
   if(navigate == null)
      navigate = "";
%>
<html>
<head>
   <title>Contact | Alternate Spellings</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function validateAndSubmitForm(form) {
      if(validateForm(form))
         form.submit();
      else
         return;
   }
   function validateForm(form) {
      var errorMessages = "" ;

      for(var i = 0; i < form.elements.length; i++) {
         element = form.elements[i];
         if(element.type == "text") {
            if((trim(element.value) != "") && !validName(trim(element.value))) {
               errorMessages = ERROR_NAMES_ONLY_LETTERS;
               element.focus();
               break;
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
               <td align="right">
                  <attendant:navigate value="<%=navigate%>" type="button"
                               onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)"/>
               </td>
            <tr>

            <tr><td>
                
            <form method="post" action="<%=response.encodeURL(ContactController)%>">
               <input type="hidden" name="contactId" value="<%=contact.getContactId()%>">
               <input type="hidden" name="buttonPressed" value="none">
               <table width="700">
               <tr>
                  <td><font class="heading">Contact:</font>
                      <font class="heading2"><%=contact.getFirstname() %> <%=contact.getLastname() %></font>
                  </td>
               <tr>
               <tr>
                  <td class="plaintext">
                     Below are the alternate spellings and phonetic
                     pronunciations for this employee contact.  You can add
                     information for both the first and last name to make it
                     easier for callers to reach employees.  For example, you
                     can add in preferred first names (e.g., Mike for Michael,
                     Kathy for Katherine), as well as phonetic pronunciations
                     for hard-to-pronounce and often-mispronounced names.
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr><td class="heading2">Spellings</td></tr>
               <tr><td colspan="2"><hr></td></tr>
               <tr>
                  <td colspan="2" class="plaintext">
                     You can add or delete a spelling.
                     Make all appropriate changes below.

                  </td>
               </tr>
               <tr>
                  <td colspan="2" align="center">
                     <table border="0">
                        <tr>
                           <td class="heading2">First names<hr></td>
                           <td class="heading2">Last names<hr></td>
                        </tr>
                        <tr>
                           <td class="plaintext"><%=contact.getFirstname() %></td>
                           <td class="plaintext"><%=contact.getLastname() %></td>
                        </tr>
                        <tr>
                        <td valign="top"><table border="0" bordercolor="yellow">
         <%
            BeanCollection altSpellings = contact.getAltSpellings();
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  if(altSpellingBean.getAltspellingTypeId() == 1) {
                  %>
                        <tr>
                           <td valign="top" class="plaintext">
                              <input type="text" value="<%=altSpellingBean.getSpelling() %>"
                                     name="fst<%=altSpellingBean.getAltSpellingId() %>" maxlength="25">
                           </td>
                           <td valign="top" class="plaintext">
                              <!-- input type="submit" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>" -->
                              <input type="button" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>"
                               onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                           </td>
                        </tr>
                <%}
               }
            }
         %>
                           </table>
                        </td>
                        <td valign="top"><table border="0" bordercolor="green">
         <%
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  if(altSpellingBean.getAltspellingTypeId() == 2) {
         %>
                        <tr>
                           <td>
                              <input type="text" value="<%=altSpellingBean.getSpelling() %>"
                                     name="lst<%=altSpellingBean.getAltSpellingId() %>"  maxlength="25">
                           </td>
                           <td valign="top">
                              <!-- input type="submit" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>" -->
                              <input type="button" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>"
                               onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                           </td>
                        </tr>
         <%       }
               }
            }
         %>
                            </table>
                        </td>
                        </tr>
                        <tr>
                           <td class="heading2"><p>New Spelling:</td>
                        </tr>
                        <tr>
                           <td><input type="text" name="newFirst" value="" maxlength="25"></td>
                           <td><input type="text" name="newLast" value="" maxlength="25"></td>
                        </tr>
                        <tr>
                          <td align="right" colspan="4">
                             <input type="hidden" name="reqState" value="htmlProcessContactSpellingEdit">
                             <input type="submit" name="apply" value="Apply">&nbsp;

                             <input type="button" name="save" value="Save"
                                onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)">&nbsp;

                             <input type="button" name="cancel" value="Cancel"
                                onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                          </td>
                     </table>
                  </td>
               </tr>
         </form>
         <br>
         
         <form method="post" action="<%=response.encodeURL(ContactController)%>">
               <input type="hidden" name="contactId" value="<%=contact.getContactId()%>">
               <input type="hidden" name="buttonPressed" value="none">
               <table width="700">
               
               
               <tr><td>&nbsp;</td></tr>
               <tr><td class="heading2">Contact Aliases</td></tr>
               <tr>
                  <td class="plaintext">
                     Below are aliases for this employee contact.  You can
                     add a short descriptive name for how you want to identify
                     this contact.  For example, for John Smith, you can add
                     "Johnny" as an alias.  The application will match on just this 
                     name versus a first and last name.
                  </td>
               </tr>
               <tr><td colspan="2"><hr></td></tr>
               <tr>
                  <td colspan="2" class="plaintext">
                     You can add or delete an alias below.
                  </td>
               </tr>
               <tr>
                  <td colspan="2" align="center">
                     <table border="0">
                        <tr>
                           <td class="heading2">Alias or Descriptive Name<hr></td>
                        </tr>
                        <tr>
                        <td valign="top"><table border="0" bordercolor="yellow">
         <%
            altSpellings = contact.getAltSpellings();
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  if(altSpellingBean.getAltspellingTypeId() == 4) {
                  %>
                        <tr>
                           <td valign="top" class="plaintext">
                              <input type="text" value="<%=altSpellingBean.getSpelling() %>"
                                     name="als<%=altSpellingBean.getAltSpellingId() %>" maxlength="25">
                           </td>
                           <td valign="top" class="plaintext">
                              <!-- input type="submit" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>" -->
                              <input type="button" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>"
                               onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                           </td>
                        </tr>
                <%}
               }
            }
         %>
                           </table>
                        </td>
                        <td valign="top">
                        </td>
                        </tr>
                        <tr>
                           <td class="heading2"><p>New Alias:</td>
                        </tr>
                        <tr>
                           <td><input type="text" name="newAlias" value="" maxlength="25"></td>
                           <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td align="right" colspan="4">
                             <input type="hidden" name="reqState" value="htmlProcessContactSpellingEdit">
                             <input type="submit" name="apply" value="Apply">&nbsp;

                             <input type="button" name="save" value="Save"
                                onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)">&nbsp;

                             <input type="button" name="cancel" value="Cancel"
                                onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                          </td>
                     </table>
                  </td>
               </tr>
         </form>
         </table>
      </td>
      <!-- end page content -->
   </tr>

</table>

</body>
</html>
