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
   List entityList = (List) request.getAttribute("entityList");
   List subEntityList = null;
   Iterator li;
   EntityBean entity = null;
   String linked = "";
   ErrorBean errorBean = (ErrorBean)request.getAttribute("errorBean");

%>
<html>
<head>
   <title>Departments</title>
   <%@ include file="scripts.jsp" %>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      onLoad="MM_preloadImages('<%=imageDir%>/home_over.jpg','<%=imageDir%>/contacts_over.jpg','<%=imageDir%>/department_over.jpg','<%=imageDir%>/settings_over.jpg','<%=imageDir%>/logout_over.jpg')"
      background="<%=imageDir%>/cheat_2.gif">

<table border="0" cellspacing="0" cellpadding="0" align="left">
   <tr><td>
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
<%
   if(errorBean != null) {
%>
   <tr><td>&nbsp;</td></tr>
   <tr>
      <td class="heading">Error</td>
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
   } else if(entityList == null || entityList.size() < 1) {
%>
      <tr><td>&nbsp;<td></tr>
      <tr>
         <td class="error">There are no departments in the system</td>
      </tr>
<%
   } else {
%>

      <tr><td>&nbsp;</td></tr>
      <tr>
         <td class="heading" colspan="2">Departments</td>
      <tr>
      <tr>
         <td colspan="3" class="plaintext">
            Below are all the department and sub-department names that have
            been entered into the AutoAttendant.  To edit a department or
            sub-department, just click on the name below.  If you want to
            add a new department, select New Department from the navigation
            bar above.  Edit All allows you to view and edit the phone numbers
            for all departments.
         </td>
      </tr>
      <tr><td>&nbsp;</td></tr>

<%
         //generate a string of all the first letters of the entities

         li = entityList.iterator();
         while(li.hasNext()) {
            entity = (EntityBean) li.next();
            linked = linked.concat(entity.getName().substring(0,1));
         }
%>
      <tr>
         <td class="heading2" align="center" colspan="2">
            <attendant:alphabetlink linked="<%=linked%>"/><br>
         </td>
      </tr>
   <tr>
   <td width="45">&nbsp;</td>
   <td><table>

<%
      li = entityList.iterator();
      String prevFirstLetter = "";
      String firstLetter = "";
      boolean isActiveParent = false;
      boolean colHeadings = true;

      while (li.hasNext()) {
         entity = (EntityBean)li.next();
         firstLetter = entity.getName().substring(0,1);

         if(!firstLetter.equals(prevFirstLetter)) {
%>
      <tr><td>&nbsp;</td></tr>
      <tr><td class="heading">
         <a name="<%=firstLetter%>"><%=firstLetter%></a>
      </td>

<%          if(colHeadings) { %>
               <td align="center" class="heading2">Status</td>
               <td align="center" class="heading2">ID #</td>
<%
               colHeadings = false;
            }
            prevFirstLetter = firstLetter;
%>
      </tr>
<%
         }
%>


      <tr>
         <td class="plaintext">
            <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntity&amp;entityId=" +
                                           String.valueOf(entity.getEntityId()))%>">
               <attendant:displayText format="title" text="<%=entity.getName()%>"/>
            </a>
         </td>
         <td class="plaintext" align="center" width="100">
<%
       isActiveParent = entity.isActive();
       if(!isActiveParent) {
%>
            Inactive
<%     } else { %>
            Active
<%     } %>
         </td>
         <td width="50" align="right" class="plaintext">
            <!-- Audio -->
            <%=entity.getEntityId()%>
         </td>

<%
         // display subEntities
         if(entity.getSubEntities() != null && entity.getSubEntities().size() > 0) {
            subEntityList = entity.getSubEntities().getSortedList("name");
            for(int i=0; i < subEntityList.size(); i++) {
               entity = (EntityBean)subEntityList.get(i);
%>
      </tr>
      <tr>
         <td class="plaintext">
             &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntity&amp;entityId=" +
                                           String.valueOf(entity.getEntityId()))%>">
            <attendant:displayText format="title" text="<%=entity.getName()%>"/>
             </a>
         </td>
         <td class="plaintext" align="center" width="100">
<%   if(isActiveParent) { %>
         <!-- Active/Inactive -->
   <%    if(entity.isActive()) { %>
            Active
   <%    } else { %>
            Inactive
   <%    } %>
<%   } %>
         </td>
         <td width="50" align="right" class="plaintext">
            <!-- Audio -->
            <%=entity.getEntityId()%>
         </td>
      </tr>
<%          } %>
<%
         }
%>
<%    }  %>

   </table>
   <tr><td>&nbsp;</td></tr>
   <tr>
      <td colspan="3" align="center" class="heading2">
         <attendant:alphabetlink linked="<%=linked%>"/>
      </td>
   </tr>

   </td></tr>
   </table>
   </td></tr>
   <tr><td>&nbsp;</td></tr>
   <tr><td>&nbsp;</td></tr>
          </table>
      </td>
      <!-- end page content -->

   </tr>

<% } %>

</table>

</body>
</html>
