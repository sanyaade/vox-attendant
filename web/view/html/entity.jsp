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
   EntityBean entity = (EntityBean) request.getAttribute("entity");
   EntityBean parentEntity = (EntityBean) request.getAttribute("parentEntity");
   List subEntityList = null;
   ErrorBean errorBean = (ErrorBean)request.getAttribute("errorBean");

   String navigate = (String) request.getAttribute("navigate");
   if(navigate == null)
      navigate = "";

   String active = "";
   if(entity.isActive()) {
      active = "Active";
   } else {
      active = "Inactive";
   }
%>
<html>
<head>
   <title>Departments | Details</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function checkForm(form){
      return confirm(INFO_DELETE_ENTITY);
   }
   //  -->
   </script>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      onLoad="MM_preloadImages('<%=imageDir%>/home_over.jpg','<%=imageDir%>/contacts_over.jpg','<%=imageDir%>/department_over.jpg','<%=imageDir%>/settings_over.jpg','<%=imageDir%>/logout_over.jpg')"
      background="<%=imageDir%>/cheat_2.gif">

<table border="0" cellspacing="0" cellpadding="0" align="left">
   <tr><td colspan="2">
   <%@ include file="menuEntities.jsp" %>
   </td></tr>

   <tr><td>
   <table>
   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>

   <table width="700">

   <tr><td>&nbsp;</td></tr>

   <tr><td>
<%
   if(errorBean != null) {
%>
   <tr>
      <td class="heading2">Error</td>
   </tr>
   <tr><td>&nbsp;</td></tr>
   <tr><td class="error"><%= errorBean.getErrorAt(0).getErrorMessage() %></td></tr>
   <tr><td>&nbsp;</td></tr>
   <tr>
      <td class="plaintext">Please click the "View" tab to get the updated listing or click
         <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntities")%>">here</a>
      </td>
   </tr>
<%
   } else {
%>
   <tr><td width="450">
<%
   if(parentEntity != null) {
%>
         <table>
            <tr>
               <td>
                  <font class="heading">Sub-Department:</font>
               </td>
               <td>
                  <font class="heading2">
                     <attendant:displayText format="title" text="<%=entity.getName()%>"/>
                  </font>
               </td>
            </tr>
            <tr>
               <td>
                  <font class="heading">Department:</font>
               </td>
               <td>
                  <font class="heading2">
                     <attendant:displayText format="title" text="<%=parentEntity.getName()%>"/>
                  </font>
               </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
         </table>
<%
   } else {
%>
         <font class="heading">Department:</font>
         <font class="heading2">
            <attendant:displayText format="title" text="<%=entity.getName()%>"/>
         </font>
<% } %>
     </td>
     <td>
         <form method="get" action="<%=response.encodeURL(EntityController)%>" onSubmit="return checkForm(this);">
            <input type="hidden" name="reqState" value="htmlProcessEntityDelete">
            <input type="hidden" name="entityId" value="<%=entity.getEntityId()%>">
            <input type="submit" name="delete" value="Delete">
         </form>

      </td>
      <td align="right">
         <form method="get" action="<%=response.encodeURL(EntityController)%>">
            <input type="hidden" name="reqState" value="htmlShowEntityNavigate">
            <input type="hidden" name="currentEntityId" value="<%=entity.getEntityId()%>">
            <attendant:navigate value="<%=navigate%>" type="submit"/>
         </form>
      </td>
   </tr>

   <tr>
   <% if(parentEntity != null) { %>
      <td colspan="3" class="plaintext">
         Below is the AutoAttendant information for this sub-department.<br>
         You can edit the Sub-Department information and alternate spellings.
      </td>
   <% } else { %>
      <td colspan="3" class="plaintext">
         Below is the AutoAttendant information for this department.<br>
         You can edit department information, alternate spellings, and sub-departments.
      </td>
   <% } %>
   </tr>
   <tr><td>&nbsp;</td></tr>
   <tr>
      <td colspan="3" class="heading2">
      <% if (parentEntity != null) { %>
         Sub-Department Information
      <% } else { %>
         Department Information
      <% } %>
      (<a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntityEdit&amp;" +
                                        "entityId=" + entity.getEntityId())%>">edit</a>)
      </td>
   </tr>
   <tr>
      <td colspan="3"><hr></td>
   </tr>
   <tr>
      <td colspan="1">
         <ul>
            <table>
               <tr>
                  <td class="heading2">Name:</td>
                  <td class="plaintext">
                     <attendant:displayText format="title" text="<%=entity.getName()%>"/>
                  </td>
              </tr>
               <tr>
                  <td class="heading2">Status:</td>
                  <td class="plaintext">
                     <attendant:displayText format="title" text="<%=active%>"/>
                  </td>
              </tr>
              <tr>
                 <td class="heading2">Phone Number:</td>
                 <td class="plaintext">
                    <attendant:displayText format="phonenumber" text="<%=entity.getPhoneNumber()%>"/>
                 </td>
              </tr>
              <tr>
                 <td class="heading2">Audio Filename:</td>
                 <td class="plaintext">
                       <%=entity.getAudioName()%> &nbsp;
                       <%if (entity.getAudioName() != null && entity.getAudioName().length() > 0) {%>
                       <attendant:playAudio name="<%=entity.getAudioName()%>" audiodir="<%=entityAudioDir%>">
                          Listen >>
                       </attendant:playAudio>
                       <%}%>
                 </td>
              </tr>
              <tr>
                 <td class="heading2">Notes:</td>&nbsp;
                 <td class="plaintext"><%=entity.getNote()%></td>
              </tr>
              <tr><td>&nbsp;</td></tr>
            </table>
         </ul>
      </td>
   <tr>
      <td colspan="3" class="heading2">Spellings
         (<a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntitySpelling&amp;" +
                                        "entityId=" + entity.getEntityId())%>">edit</a>)
          <hr>
      </td>
   </tr>
   <tr><td class="plaintext">
      <ul>
         <attendant:displayText format="title" text="<%=entity.getName()%>"/>
         <br>
<%
   BeanCollection altSpellings = entity.getAltSpellings();
   if(altSpellings != null) {
      for(int i = 0; i < altSpellings.size(); i ++) {
         AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
%>
               <%=altSpellingBean.getSpelling() %><br>
<%     }
     }
%>
      </ul>
   </td></tr>
<%
   // only show subentity section if this is a top level entity
   if(entity.getParentId() == 0) {
%>
   <form method="post" action="<%=response.encodeURL(EntityController)%>">
      <input type="hidden" name="reqState" value="htmlShowSubEntityAdd">
      <input type="hidden" name="entityId" value="<%=entity.getEntityId()%>">
      <tr>
         <td colspan="3" valign="top" class="heading2">
            Sub-Department Information &nbsp;
            <input type="submit" name="add" value="Add">
            <hr>
         </td>
      </tr>
   </form>

   <tr><td>
      <ul>
      <table>
<%
   // display subEntities
   if(entity.getSubEntities() != null && entity.getSubEntities().size() > 0) {
      subEntityList = entity.getSubEntities().getSortedList("name");
%>
      <form method="get" action="<%=response.encodeURL(EntityController)%>" onSubmit="return checkForm(this);">
         <input type="hidden" name="reqState" value="htmlProcessSubEntityDelete">
         <%-- this is the parent entityId --%>
         <input type="hidden" name="entityId" value="<%=entity.getEntityId()%>">
         <tr>
            <td class="heading2">Name</td><td class="heading2">Contact Number</td>
         </tr>
<%
      for(int i=0; i < subEntityList.size(); i++) {
         entity = (EntityBean)subEntityList.get(i);
%>
         <tr>
            <td class="plaintext">
               <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntity&amp;entityId=" +
                                              String.valueOf(entity.getEntityId()))%>">
                <attendant:displayText format="title" text="<%=entity.getName()%>"/>
                </a>
            </td>
            <td align="center" class="plaintext">
               <attendant:displayText format="phonenumber" text="<%=entity.getPhoneNumber()%>"/>
            </td>
            <td>&nbsp;</td>
            <td valign="top" class="plaintext">
               <%-- this is the sub entityId --%>
               <input type="submit" value="x" name="del<%=entity.getEntityId()%>">
            </td>
         </tr>
<%    } %>
      </form>
<%
   } else {
%>
         <tr>
            <td class="plaintext">
               There are currently no Sub-Departments.
            </td>
         </tr>
<%
   }
%>
      </table>
      </ul>
   </td></tr>
<% } %>
<% } %>
   </table>
   </td></tr>

          </table>
      </td>
      <!-- end page content -->

   </tr>


</table>

</body>
</html>
