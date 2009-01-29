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
   List contactList = (List) request.getAttribute("contacts");
   ListIterator li = contactList.listIterator();
   ContactBean contact;
   String updateContactId = (String) request.getAttribute("updateContactId");
   if(updateContactId == null)
      updateContactId = "-1";
%>
<html>
<head>
   <title>Contacts | Distinct Information</title>
   <%@ include file="scripts.jsp" %>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      onLoad="MM_preloadImages('<%=imageDir%>/home_over.jpg','<%=imageDir%>/contacts_over.jpg','<%=imageDir%>/department_over.jpg','<%=imageDir%>/settings_over.jpg','<%=imageDir%>/logout_over.jpg')"
      background="<%=imageDir%>/cheat_2.gif">

<table border="0" cellspacing="0" cellpadding="0" align="left">
   <tr><td colspan="2">
   <%
      if(updateContactId.equals("-1")) {
         submenu="new";
      }
   %>
   <%@ include file="menuContacts.jsp" %>
   </td></tr>

   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start of content -->
      <td>
         <table width="700">


            <tr><td>&nbsp;</td></tr>
            <tr>
               <td>
                  <font class="heading">Update Distinct Information</font>
                  <br>
                  <font class="heading2">
         <%          if(!updateContactId.equals("-1")) { %>
                        The contact you are trying to modify now sounds very similar to
                        existing contacts in the system.
         <%          } else { %>
                        The new contact you are trying to add sounds very similar to
                        existing contacts in the system.
         <%          } %>
                  <br>
                  Please add information that callers
                  will hear that will distinguish each of the contacts.
                  </font>
               </td>
            </tr>
            <tr>
            <td>
            <form method="post" action="<%=response.encodeURL(ContactController)%>">
            <table width="100%">
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td colspan="2">
                     <font class="heading2">Existing contacts</font>
                     <hr>
                  </td>
               </tr>
         <%
            String prevFirstLetter = "";
            String firstLetter = "";
            while (li.hasNext()) {

               contact = (ContactBean) li.next();
               firstLetter = contact.getLastname().substring(0,1);
         %>
               <tr>

               <tr><td colspan="2" class="heading2">
         <%    if(contact.getContactId() == -1) { %>
                  <br>
                  New contact
                  <hr>
         <%    } else if(contact.getContactId() == Integer.parseInt(updateContactId)) { %>
                  <br>
                  Current Modified Contact<br>
                  <hr>
         <%    } %>
               </td></tr>

               <tr>
               <td>
               <ul>
               <table>
                  <tr>
                     <td class="heading2" colspan="2">
                        <%=contact.getFirstname() %> <%=contact.getLastname() %>
                     </td>
                  </tr>
                  <tr>
               <td class="plaintext">
                  Distinct Information:
               </td>
               <td>
         <%    if(contact.getContactId() == -1 ||
                  contact.getContactId() == Integer.parseInt(updateContactId)) {
         %>
               <%-- for the new contact, put all the info in hidden fields so that we can
                    pass it to the next request --%>
                  <input type="text" name="distinctInfo" value="<%=contact.getDistinctInfo() %>"/>
                  <input type="hidden" name="contactId" value="<%=contact.getContactId() %>"/>
                  <input type="hidden" name="firstname" value="<%=contact.getFirstname()%>">
                  <input type="hidden" name="lastname" value="<%=contact.getLastname()%>">
                  <input type="hidden" name="nickname" value="<%=contact.getNickname()%>">
                  <input type="hidden" name="note" value="<%=contact.getNote()%>">
                  <input type="hidden" name="workPhoneNumber" value="<%=contact.getWorkPhoneNumber()%>">
                  <input type="hidden" name="mobilePhoneNumber" value="<%=contact.getMobilePhoneNumber()%>">
                  <% if(contact.isActive()) { %>
                     <input type="hidden" name="isActive" value="true">
                  <% } else { %>
                     <input type="hidden" name="isActive" value="false">
                  <% } %>

               <%-- -----------end of fields -----------------%>
         <%    } else { %>
                  <input type="text" value="<%=contact.getDistinctInfo() %>" name="info<%=contact.getContactId()%>"/>
         <%    } %>

         <%    if(contact.getContactId() > 0) { %>
                  <font class="plaintext">
                  <attendant:playAudio type="distinctinfo" id="<%=contact.getContactId()%>" audiodir="<%=contactAudioDir%>">
                     play
                  </attendant:playAudio>
                  </font>
         <%    } %>
               </td>

                  </tr>
                  <tr>
                     <td class="plaintext">Office Phone:</td>
                     <td class="plaintext">
                        <attendant:displayText format="phonenumber" text="<%=contact.getWorkPhoneNumber() %>"/>
               </td>
               </tr>
               <tr>
               <td class="plaintext">Cell Phone:</td>
               <td class="plaintext">
                  <attendant:displayText format="phonenumber" text="<%=contact.getMobilePhoneNumber() %>"/>
               </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                    </ul>
                  </table>
               </td>
              </tr>
         <% } %>
            <tr>
            <td align="right" colspan="2">
               <input type="hidden" name="reqState" value="htmlProcessConflicts">
               <input type="submit" name="save" value="save">
               <input type="submit" name="cancel" value="cancel">
            </td>
            </table>
            </form>
            </td></tr>

         </table>
      </td>
      <!-- end content -->
   </tr>


</table>

</body>
</html>
