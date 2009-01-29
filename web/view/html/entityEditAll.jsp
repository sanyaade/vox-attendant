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
%>
<html>

<head>
   <title>Departments | Edit All</title>
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

      for(var i = 3; i < form.elements.length ; ) {
         //alert(form.elements[i].value);
         element1 = form.elements[i];
         if(element1.type == "text") {
            if(!isValidPhoneNumber(element1.value)) {
               alert(ERROR_PHONENUMBER_ONLY_DIGITS + ERROR_PHONE_NON_0_1);
               element1.focus();
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
   <%
      submenu = "editAll";
   %>
   <%@ include file="menuEntities.jsp" %>
   </td></tr>
   <tr><td>&nbsp;</td></tr>

   <tr><td><ul>
   <form method="post" action="<%=response.encodeURL(EntityController)%>" onSubmit="return validateForm(this);">
   <table width="700">
<%
   if(entityList == null || entityList.size() < 1) {
%>
   <tr>
      <td class="error">There are no departments in the system</td>
   </tr>
<%
   } else {
%>
   <tr>
      <td class="heading">
         Edit All
      </td>
   </tr>
   <tr>
      <td colspan="4" class="plaintext">
         Below are the phone numbers for all departments and sub-departments
         in the AutoAttendant.  Make all appropriate changes (i.e., changes,
         deletions) directly into the fields and then save the changes.
      </td>
   </tr>
   <tr><td colspan="4" >&nbsp;</td></tr>

      <%
         //get the letters that will need to have links
         li = entityList.iterator();
         while(li.hasNext()) {
            entity = (EntityBean) li.next();
            linked = linked.concat(entity.getName().substring(0,1));
         }
      %>

   <tr>
      <td colspan="4" align="center" class="heading2">
         <attendant:alphabetlink linked="<%=linked%>"/><br>
      </td>
   </tr>

   <tr>
       <td>&nbsp;</td>
       <td colspan="3" align="right">
          <input type="hidden" name="reqState" value="htmlProcessEntityEditAll">
          <input type="submit" name="save" value="save">
          <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">
       </td>
   </tr>
   <tr><td>&nbsp;</td></tr>
   <tr>
      <td colspan="4" class="plaintext" >
         <table>
            <tr>
               <td class="plaintext">
                  Phone numbers must be 10-digit North American numbers<br>
                  (with no spaces in between digits).
               </td>
            </tr>
         </table>
      </td>
   </tr>

<%
      li = entityList.iterator();
      String prevFirstLetter = "";
      String firstLetter = "";

      while (li.hasNext()) {
         entity = (EntityBean)li.next();
         firstLetter = entity.getName().substring(0,1);

         if(!firstLetter.equals(prevFirstLetter)) {

      // display first letter
%>
      <tr><td class="heading">
         <a name="<%=firstLetter%>"><%=firstLetter%></a>
      </td></tr>
      <tr><td>&nbsp</td></tr>
          <td class="heading">
             <a name="<%=firstLetter%>"><%=firstLetter%></a>
          </td>
          <td class="plaintext">Phone</td>
          <td class="plaintext">Audio File</td>
       </tr>
<%
            prevFirstLetter = firstLetter;
         }
%>
<%
      // display Entity
%>
      <tr>
         <td class="heading2">
            &nbsp;&nbsp;
            <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntity&amp;entityId=" +
                                           String.valueOf(entity.getEntityId()))%>">
            <attendant:displayText format="title" text="<%=entity.getName()%>"/>
         </td>
         <td>
            <input type="text" name="en<%=entity.getEntityId()%>" value="<%=entity.getPhoneNumber()%>" >
         </td>
         <td>
            <input name="af<%=entity.getEntityId()%>" value="<%=entity.getAudioName()%>" >
         </td>
      </tr>

<%
   // display subEntities
   if(entity.getSubEntities() != null && entity.getSubEntities().size() > 0) {
      subEntityList = entity.getSubEntities().getSortedList("name");
      for(int i=0; i < subEntityList.size(); i++) {
         entity = (EntityBean)subEntityList.get(i);
%>
      <tr>
      <td class="plaintext">
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <a href="<%=response.encodeURL(EntityController + "?reqState=htmlShowEntity&amp;entityId=" +
                                        String.valueOf(entity.getEntityId()))%>">
          <attendant:displayText format="title" text="<%=entity.getName()%>"/>
          </a>
      </td>
      <td>
         <input type="text" name="en<%=entity.getEntityId()%>" value="<%=entity.getPhoneNumber()%>" maxlength="10">
      </td>

      </tr>
<%
      }
   }
%>
<%    } %>
   <tr>
      <td>&nbsp;</td>
   </tr>
   <tr>
       <td>&nbsp;</td>
       <td colspan="2" align="right">
          <input type="hidden" name="reqState" value="htmlProcessContactEditAll">
          <input type="submit" name="save" value="save">
          <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">
       </td>
   </tr>
   </table>
   </form>
   </ul>
   </td></tr>
   <tr>
      <td colspan="2" align="center" class="heading2">
         <attendant:alphabetlink linked="<%=linked%>"/><br>
      </td>
   </tr>

<% } %>

</table>

</body>
</html>
