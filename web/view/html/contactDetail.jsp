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
      
      System.out.println("got to contactDetail.jsp");
%>
<html>
<head>
   <title>Contact | Details</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function checkForm(form){
      return confirm(INFO_DELETE_CONTACT);
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

            <tr><td>
            <table border="0" bordercolor="red" width="700">
            <tr>
               <td width="450">
                  <font class="heading">View Contact:</font>
                  <font class="heading2"><%=contact.getFirstname()%> <%=contact.getLastname()%></font>
               </td>
               <td>
                  <form method="get" action="<%=response.encodeURL(ContactController)%>" onSubmit="return checkForm(this);">
                     <input type="hidden" name="reqState" value="htmlProcessContactDelete">
                     <input type="hidden" name="contactId" value="<%=contact.getContactId()%>">
                     <input type="submit" name="delete" value="Delete">
                  </form>
               </td>

               <td align="right">
                    <form method="get" action="<%=response.encodeURL(ContactController)%>">
                       <input type="hidden" name="reqState" value="htmlShowContactDetailNavigate">
                       <input type="hidden" name="currentContactId" value="<%=contact.getContactId()%>">
                       <attendant:navigate value="<%=navigate%>" type="submit"/>
                    </form>
               </td>
            </tr>
            <tr>
                <td colspan="3" class="plaintext">
                   Below is the AutoAttendant information for this employee contact.
                   You can edit both the contact information and the alternate spellings.
                   <p>
                </td>
            </tr>

               <tr>
                  <td class="heading2">Contact Information
                     (<a href="<%=response.encodeURL(ContactController +
                                                       "?reqState=htmlShowContactEdit&amp;" +
                                                       "contactId=" +
                                                       contact.getContactId())%>">edit</a>)
                  </td>
               </tr>
               <tr><td colspan="3"><hr></td></tr>
               <tr>
                  <td>
                     <ul>
                     <table border="0">
                        <tr>
                           <td width="150" class="heading2">Name:</td>
                           <td class="plaintext">
                              <%=contact.getFirstname() %> <%=contact.getLastname() %>
                           </td>
                        </tr>
                        <tr>
                           <td class="heading2">Audio File Name:</td>
                           <td class="plaintext">
                              <%=contact.getAudioName() %>&nbsp;
                              <%if (contact.getAudioName() != null && contact.getAudioName().length() > 0) {%>
                              <attendant:playAudio name="<%=contact.getAudioName()%>" audiodir="<%=contactAudioDir%>">
                                 Listen >>
                              </attendant:playAudio>
                              <%}%>
                           </td>
                        </tr>

                        <tr>
                           <td class="heading2">Status:</td>
                           <td class="plaintext">
                              <%  if(contact.isActive()) { %>
                                 Active
                              <%  } else { %>
                                 Inactive
                              <%  } %>
                           </td>
                        </tr>
                        <tr>
                           <td class="heading2">Voicemail Enabled:</td>
                           <td class="plaintext">
                              <%  if(contact.isVoicemailEnabled()) { %>
                              Yes &nbsp; <a href="/vox-mail/inbox.do?cmd=doLogin&phone=<%=contact.getWorkPhoneNumber()%>&pin=1234">Vox-mail Mailbox</a>
                              <%  } else { %>
                                 No
                              <%  } %>
                           </td>
                        </tr>
                       <tr><td>&nbsp</td></tr>
                       <tr>
                           <td class="heading2">Extension:</td>
                           <td class="plaintext">
                              <%
                                String phoneNumber = "";

                                phoneNumber = contact.getExtension();
                                if (phoneNumber != null && phoneNumber.toLowerCase().startsWith("sip:")) {
                                    out.println(phoneNumber);
                                } else {
                                %>
                                  <attendant:displayText format="phonenumber" text="<%=phoneNumber%>"/>
                             <% } %>
                           </td>
                       </tr>
                       <tr>
                           <td class="heading2">Office Phone:</td>
                           <td class="plaintext">
                              <%
                                phoneNumber = contact.getWorkPhoneNumber();
                                if (phoneNumber != null && phoneNumber.toLowerCase().startsWith("sip:")) {
                                    out.println(phoneNumber);
                                } else {
                                %>
                                  <attendant:displayText format="phonenumber" text="<%=phoneNumber%>"/>
                             <% } %>
                           </td>
                       </tr>
                       
                       <tr>
                           <td class="heading2">Mobile Phone:</td>
                           <td class="plaintext">
                              <%
                                phoneNumber = contact.getMobilePhoneNumber();
                                if (phoneNumber != null && phoneNumber.toLowerCase().startsWith("sip:")) {
                                    out.println(phoneNumber);
                                } else {
                                %>
                                  <attendant:displayText format="phonenumber" text="<%=phoneNumber%>"/>
                             <% } %>
                           </td>
                       </tr>
                       <tr>
                           <td class="heading2">Home Phone:</td>
                           <td class="plaintext">
                              <%
                                phoneNumber = contact.getHomePhoneNumber();
                                if (phoneNumber != null && phoneNumber.toLowerCase().startsWith("sip:")) {
                                    out.println(phoneNumber);
                                } else {
                                %>
                                  <attendant:displayText format="phonenumber" text="<%=phoneNumber%>"/>
                             <% } %>
                           </td>
                       </tr>
                       <tr>
                          <td class="heading2">
                             Email:
                          </td>
                          <td class="plaintext">
                             <%=contact.getEmail() %>
                          </td>
                       </tr>
                       
                        <tr><td>&nbsp;</td></tr>
                        <tr>
                           <td class="heading2">Distinguishing<br>&nbsp;&nbsp; Information:</td>
                           <td class="plaintext"><%=contact.getDistinctInfo() %>&nbsp;
                               
                            </td>
                         </tr>

                        <tr>
                           <td class="heading2">Notes:</td>
                           <td class="plaintext"><%=contact.getNote() %></td>
                        </tr>
                      </table>
                   </td>
               </tr>
               <tr><td>&nbsp</td></tr>
               <tr>
                  <td class="heading2" colspan="3">
                     Spellings:
                     (<a href="<%=response.encodeURL(ContactController + "?reqState=htmlShowContactSpelling&amp;" +
                                                    "contactId=" + contact.getContactId())%>">edit</a>)
                     <br>
                     <hr>
                  </td>
               </tr>
               <tr>
                  <td colspan="2">
                  <ul>
                  <table border="0" bordercolor="blue">
                     <tr>
                        <td width="120" class="heading2">First names<hr></td>
                        <td width="120" class="heading2">Last names<hr></td>
                     </tr>
                     <tr>
                        <td class="plaintext"><%=contact.getFirstname() %></td>
                        <td class="plaintext"><%=contact.getLastname() %></td>
                     </tr>
                     <tr>
                        <td valign="top">
                        <table bordercolor="orange" border="0">
         <%
            //Firstnames
            BeanCollection altSpellings = contact.getAltSpellings();
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  System.out.println(altSpellingBean.getSpelling() + " " + altSpellingBean.getAltspellingTypeId());
                  if(altSpellingBean.getAltspellingTypeId() == 1) {
         %>
                           <tr>
                              <td class="plaintext"><%=altSpellingBean.getSpelling() %></td>
                           </tr>
         <%       }
               }
            }
         %>
                        </table>
                        </td>
                        <td valign="top">
                        <table>
         <%
            // Lastnames
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  System.out.println(altSpellingBean.getSpelling() + " " + altSpellingBean.getAltspellingTypeId());
                  if(altSpellingBean.getAltspellingTypeId() == 2) {
         %>
                           <tr>
                              <td class="plaintext"><%=altSpellingBean.getSpelling() %></td>
                           </tr>
         <%       }
               }
            }
         %>
                        </table>
                        </td>
                     </tr>
                  </table>
                  </td>
               </tr>
            </table>
         </td></tr>
         <tr><td>&nbsp</td></tr>
               <tr>
                  <td class="heading2" colspan="3">
                     Contact Aliases:
                     (<a href="<%=response.encodeURL(ContactController + "?reqState=htmlShowContactSpelling&amp;" +
                                                    "contactId=" + contact.getContactId())%>">edit</a>)
                     <br>
                     <hr>
                  </td>
               </tr>
               <tr>
                  <td colspan="2">
                  <ul>
                  <table border="0" bordercolor="blue">
                     <tr>
                        <td width="120" class="heading2">Aliases<hr></td>
                        <td width="120" class="heading2">&nbsp;</td>
                     </tr>
                     
                     <tr>
                        <td valign="top">
                        <table bordercolor="orange" border="0">
         <%
            //Aliases
            altSpellings = contact.getAltSpellings();
            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
                  System.out.println(altSpellingBean.getSpelling() + " " + altSpellingBean.getAltspellingTypeId());
                  if(altSpellingBean.getAltspellingTypeId() == 4) {
         %>
                           <tr>
                              <td class="plaintext"><%=altSpellingBean.getSpelling() %></td>
                           </tr>
         <%       }
               }
            }
         %>
                        </table>
                        </td>
                        <td valign="top">
                        &nbsp;
                        </td>
                     </tr>
                  </table>
                  </td>
               </tr>
            </table>
         </td></tr>
         <tr><td>&nbsp;</td></tr>
         <tr><td>&nbsp;</td></tr>

         </table>
      </td>
      <!-- end page content -->
   </tr>
</table>

</body>
</html>
