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
   List contactList = (List) request.getAttribute("contactList");
   ErrorBean errorBean = (ErrorBean)request.getAttribute("errorBean");

   Iterator li;
   ContactBean contact;
%>
<html>

<head>
   <title>Contact | Edit All</title>
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

      for(var i = 2; i < form.elements.length ; ) {
         //alert(form.elements[i].value);
         element1 = form.elements[i];
         element2 = form.elem/ents[i+1];
         element3 = form.elements[i+2];
         element4 = form.elements[i+3];
         if(element1.type == "text") {
            if(((trim(element1.value) != "") && !isValidPhoneNumber(element1.value)) ||
               ((trim(element2.value) != "") && !isValidPhoneNumber(element2.value)) ||
               (!isValidPhoneNumber(element1.value) && !isValidPhoneNumber(element2.value)))
            {
               alert(ERROR_PHONENUMBERS_ONLY_DIGITS + ERROR_PHONE_NON_0_1);
               element1.focus();
               return false;
            }
         }
         i += 4;
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
      submenu="editAll";
   %>
   <%@ include file="menuContacts.jsp" %>
   </td></tr>

   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>
         <table width="700">

            <tr><td>&nbsp;</td></tr>

         <%
            if(errorBean != null) {
         %>
            <!----- ERROR ---->
            <tr><td>&nbsp;</td></tr>
            <tr>
               <td>
                  <font class="heading">Error:</font>
                  <br>
                  <p class="error"><%= errorBean.getErrorAt(0).getErrorMessage() %></p>
                  <br>
                  <font class="plaintext">
                     Please click the "View" tab to get the updated listing or click
                     <a href="<%=response.encodeURL(ContactController
                              + "?reqState=htmlShowContacts")%>">
                        here
                     </a>
                  </font>
               </td>
            </tr>
         <%
            } else {
            //check to see if there are contacts
            if(contactList == null || contactList.size() < 1) {
         %>
            <!---- NO CONTACTS ---->
            <tr>
               <td class="error">There are no contacts in the system</td>
            </tr>
         <%
            } else {
            //display contacts
            li = contactList.iterator();
            String linked = "";
            while(li.hasNext()) {
               contact = (ContactBean) li.next();
               linked = linked.concat(contact.getLastname().substring(0,1));
            }
         %>
            <!---- CONTACTS ---->
            <tr>
               <td class="heading">
                  Edit All
               </td>
            </tr>
            <tr>
               <td class="plaintext">
                  Below are the phone numbers for all employee contacts in the
                  AutoAttendant.  Make all appropriate changes (i.e., changes,
                  deletions) directly into the fields and then save the changes.
               </td>
            </tr>

            <tr><td>&nbsp;</td></tr>

            <tr><td align="center" class="heading2">
               <attendant:alphabetlink linked="<%=linked%>"/><br>
            </td></tr>

            <tr><td>
            <form method="post" action="<%=response.encodeURL(ContactController)%>" onSubmit="return validateForm(this);">
               <table border="0" width="700">
         <%
            if (contactList == null) {
         %>
               <tr><td>There are no contacts in the system.</td></tr>
         <%
            } else {
         %>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="2" align="right">
                     <input type="submit" name="save" value="save">
                     <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td colspan="3" class="plaintext">
                        Phone numbers must be 10-digit North American numbers (with no spaces in between digits), or a 
                        SIP address in the following format: <b>sip:youraddress</b>
                  </td>
               <tr>

         <%
               li = contactList.listIterator();
               String prevFirstLetter = "";
               String firstLetter = "";
               while (li.hasNext()) {
                  contact = (ContactBean) li.next();
                  firstLetter = contact.getLastname().substring(0,1);
         %>
         <%
                  if(!firstLetter.equals(prevFirstLetter)) {
         %>
               <tr><td>&nbsp</td></tr>
                  <td class="heading">
                     <a name="<%=firstLetter%>"><%=firstLetter%>
                  </td>
                  <td class="plaintext">Work Phone</td>
                  <td class="plaintext">Cell Phone</td>
                  <td class="plaintext">Home Phone</td>
                  <td class="plaintext">Audio File</td>
               </tr>
         <%
                     prevFirstLetter = firstLetter;
                  }
         %>
               <tr>
                  <td class="heading2" nowrap>
                  <!-- Lastname, Firstname -->
                  &nbsp;&nbsp;
                  <a href="<%=response.encodeURL(ContactController +
                                                 "?reqState=htmlShowContactDetail&amp;contactId=" +
                                                 String.valueOf(contact.getContactId()))%>">
                  <%=contact.getFirstname() %> <%=contact.getLastname() %>
                  </a>
                  </td>
                  <td>
                  <!-- Work Phone -->
                  <input type="text" name="wn<%=contact.getContactId()%>" value="<%=contact.getWorkPhoneNumber() %>">
                  </td>
                  <td>
                  <!-- Mobile Phone -->
                  <input type="text" name="mn<%=contact.getContactId()%>" value="<%=contact.getMobilePhoneNumber() %>">
                  </td>
                  <td>
                  <!-- Home Phone -->
                  <input type="text" name="hn<%=contact.getContactId()%>" value="<%=contact.getHomePhoneNumber() %>">
                  </td>
                  <td>
                  <!-- Audio Filename -->
                  <input name="af<%=contact.getContactId()%>" value="<%=contact.getAudioName()%>">
                  </td>
               </tr>
         <%    } %>

            <tr><td>&nbsp;</td></tr>
            <tr>
               <td>&nbsp;</td>
               <td colspan="2" align="right">
                  <input type="hidden" name="reqState" value="htmlProcessContactEditAll">
                  <input type="submit" name="save" value="save">
                  <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">
               </td>
            </tr>
         <% } %>
            </table>
            </td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td class="heading2" align="center">
               <attendant:alphabetlink linked="<%=linked%>"/><br>
            </td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
         <%  } %>
         <% } %>

         </table>
      </td>
      <!-- end page content -->

   </tr>
</table>

</body>
</html>
