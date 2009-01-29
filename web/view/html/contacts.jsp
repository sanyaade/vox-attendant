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
   ContactBean contact = null;
   Iterator it = null;
   String linked = "";

   ErrorBean errorBean = (ErrorBean)request.getAttribute("errorBean");

%>
<html>
<head>
   <title>Contacts</title>
   <%@ include file="scripts.jsp" %>
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

      <!-- start of content -->
      <td>
         <table width="700">

         <%
            if(errorBean != null) {
         %>
                  <!----- ERROR ----->

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
            } else if(contactList == null || contactList.size() < 1) {
         %>
                  <!----- NO CONTACTS ----->
            <tr><td>&nbsp;</td></tr>
            <tr>
               <td class="error">There are no contacts in the system</td>
            </tr>

         <%
            } else {
         %>
                  <!----- CONTACTS ----->

                     <tr><td>&nbsp;</td></tr>
                     <tr>
                        <td class="heading">Contacts</td>
                     <tr>
                     <tr>
                        <td class="plaintext">
                           Below are all the employee contacts that have been entered into the AutoAttendant.
                           To edit a contact, just click on the contact name below.  If you want to add a
                           new contact, select New Contact from the navigation bar above.  Edit
                           All allows you to view and edit the phone numbers for all contacts.
                        </td>
                     </tr>
                     <tr><td>&nbsp;</td></tr>
                     <tr>
                  <%
                     it = contactList.iterator();
                     while(it.hasNext()) {
                        contact = (ContactBean) it.next();
                        linked = linked.concat(contact.getLastname().substring(0,1));
                     }
                  %>
                        <td align="center" class="heading2">
                           <attendant:alphabetlink linked="<%=linked%>"/><br>
                        </td>
                     </tr>

                     <tr><td>
                     <table width="700">
                     <tr>
                     <td width="65">&nbsp;</td>
                     <td><table>
                  <%
                     if(contactList == null || contactList.size() < 1) {
                  %>
                     <tr><td>&nbsp;</td></tr>
                     <tr>
                        <td class="error">There are no contacts in the system</td>
                     </tr>
                  <%
                     } else {
                        String prevFirstLetter = "";
                        String firstLetter = "";
                        boolean colHeadings = true;
                        it = contactList.iterator();
                        while (it.hasNext()) {
                           contact = (ContactBean)it.next();
                           firstLetter = contact.getLastname().substring(0,1);

                           if(!firstLetter.equals(prevFirstLetter)) {
                  %>
                        <tr><td>&nbsp;</td></tr>
                         <tr>
                           <td class="heading">
                              <a name="<%=firstLetter%>"><%=firstLetter%></a>
                           </td>
                   <%    if(colHeadings) { %>
                           <td align="center" class="heading2">Status</td>
                           <td align="center" class="heading2">ID #</td>
                   <%
                           colHeadings = false;
                          }
                   %>

                        </tr>
                  <%
                              prevFirstLetter = firstLetter;
                           }
                  %>
                        <tr>
                        <td width="150" class="plaintext">
                           <!-- Lastname, Firstname -->
                           &nbsp;
                           <a href="<%=response.encodeURL(ContactController + "?reqState=htmlShowContactDetail&amp;contactId=" +
                                                          String.valueOf(contact.getContactId()))%>">
                          <%=contact.getFirstname() %> <%=contact.getLastname() %></a>
                        </td>

                        <td width="100" class="plaintext" align="center">
                        <!-- Active/Inactive -->
                  <%    if(contact.isActive()) { %>
                           Active
                  <%    } else { %>
                           Inactive
                  <%    } %>
                        </td>

                        <td align="right" class="plaintext">
                           <!-- Audio -->
                           <%=contact.getContactId() %>
                        </td>
                        </tr>
                  <%    }
                     }
                  %>
                     </table>
                  </td>
                  </tr>
                  </table>
                  </td></tr>
                  <tr>
                     <td>&nbsp;</td>
                  </tr>
                  <tr>
                     <td align="center" class="heading2">
                        <attendant:alphabetlink linked="<%=linked%>"/>
                     </td>
                  </tr>
                  <tr>
                     <td><p>&nbsp;<p></td>
                  </tr>
               <% } %>

         </table>
      </td>
   </tr>
</table>

</body>
</html>
