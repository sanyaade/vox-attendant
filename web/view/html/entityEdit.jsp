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
   boolean newEntity = false;
   if(entity == null) {
     newEntity = true;
     entity = new EntityBean();
   }

   String navigate = (String) request.getAttribute("navigate");
   if(navigate == null)
      navigate = "";
%>
<html>

<head>
   <title>Departments | Edit</title>
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
         if((element.name == "name") || (element.name == "phoneNumber")) {
            if(trim(element.value) == "") {
               errorMessages = ERROR_REQUIRED_FIELDS;
               element.focus();
               break;
            }
            if((element.name == "name") && !validName(trim(element.value))) {
               errorMessages = ERROR_NAME_ONLY_LETTERS;
               element.focus();
               break;
            }
         }
      }
      if(errorMessages == "") {
         var phoneNumber = trim(form.phoneNumber.value);

         if(!isValidPhoneNumber(phoneNumber)) {
            errorMessages = ERROR_PHONENUMBER_ONLY_DIGITS + ERROR_PHONE_NON_0_1;
            form.phoneNumber.focus();
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
      if(newEntity) {
         submenu="new";
      }
   %>
   <%@ include file="menuEntities.jsp" %>
   </td></tr>
   <tr><td>&nbsp;</td></tr>

   <form method="get" action="<%=response.encodeURL(EntityController)%>" onSubmit="return validateForm(this);">
   <tr>
<%
   if(!newEntity) {
%>
      <td align="right">
         <input type="hidden" name="buttonPressed" value="">
         <attendant:navigate value="<%=navigate%>" type="button"
                      onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)"/>
      </td>
<% } %>
   </tr>

   <tr><td>
   <table>
   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>


   <table width="700">
   <%-- this section is only displayed if we are editing an existing entity --%>
<%
   if(!newEntity) {
%>
   <tr>
      <td>
<%    if(parentEntity != null) { %>
         <font class="heading">
         Sub-Department:
         </font>
         <font class="heading2">
         <attendant:displayText format="title" text="<%=entity.getName()%>"/>
         </font>
         <br>
         <font class="heading">
         Department:
         </font>
         <font class="heading2">
         <attendant:displayText format="title" text="<%=parentEntity.getName()%>"/>
         </font>
<%
      } else {
%>
         <font class="heading">
         Department:
         </font>
         <font class="heading2">
         <attendant:displayText format="title" text="<%=entity.getName()%>"/>
         </font>
<%    } %>
      </td>
   <tr>

   <tr>
     <td colspan="2" class="plaintext">
<%    if(parentEntity != null) { %>
        To update the sub-department information below, make all appropriate edits
        or changes directly into the fields and then save the changes.
<%
      } else {
%>
        To update the department information below, make all appropriate edits
        or changes directly into the fields and then save the changes.
<%    } %>
     </td>
   </tr>

   <tr><td>&nbsp;</td></tr>
   <tr>
      <td class="heading2">
      <% if (parentEntity != null) { %>
         Sub-Department Information
      <% } else { %>
         Department Information
      <% } %>
      </td>
   </tr>
   <tr><td colspan="2"><hr></td></tr>
<%
   } else {
%>
   <%-- this section is only displayed if we are adding a new entity --%>
<%
    if(parentEntity == null) {
%>
   <tr>
      <td class="heading">New Department</td>
   </tr>
   <tr>
      <td class="plaintext">
         Please fill in the information below to add a new department to the
         AutoAttendant.
      </td>
   </tr>
<%
      } else {
%>
   <tr>
      <td>
         <font class="heading2">
            <attendant:displayText format="title" text="<%=parentEntity.getName()%>"/>
         </font>
         <br>
         <font class="heading">New Sub-Department</font>
         <input type="hidden" name="entityId" value="<%=parentEntity.getEntityId()%>">
      </td>
   </tr>
   <tr>
      <td class="plaintext">
         Please fill in the information below to add a new sub-department to the
         AutoAttendant.
      </td>
   </tr>
<%    } %>
<% } %>
   <tr>
      <td><ul><table>
         <tr><td>&nbsp;</td></tr>
         <tr>
            <td class="heading2">
               Name:
            </td>
            <td class="plaintext">
               <input type="text" value="<%=entity.getName() %>" name="name" maxlength="25">
            </td>
         </tr>
      <%
         if (!newEntity) {
      %>
         <tr>
            <td class="heading2" nowrap>
               Audio Filename:
            </td>
            <td class="plaintext">
               <input type="text" value="<%=entity.getAudioName() %>" name="audioName" maxlength="45">&nbsp
               <%if (entity.getAudioName() != null && entity.getAudioName().length() > 0) {%>
               <attendant:playAudio name="<%=entity.getAudioName()%>" audiodir="<%=contactAudioDir%>">
                  Listen >>
               </attendant:playAudio>
               <%}%>
               
            </td>
         </tr>
         <tr>
          <td>&nbsp;</td>
          <td colspan="2" class="plaintext">
             <font size="1">
                     These .wav files should be dropped in the "\web\view\audio\entity" directory of the deployment.
                 </font>
          </td>
       </tr>
      <% } else {%>
        <tr>
            <td class="heading2">
               Audio Filename:
            </td>
            <td class="plaintext">
               <input type="text" value="<%=entity.getAudioName() %>" name="audioName" maxlength="45">&nbsp
            </td>
         </tr>
      <%}%>
         <tr>
            <td class="heading2">Active:</td>
            <td class="plaintext">
      <%
            if(entity.isActive()) {
      %>
               <input type="checkbox" name="isActive" value="true" CHECKED>
      <%
            } else {
      %>
               <input type="checkbox" name="isActive" value="true">
      <%    } %>
            </td>

         </tr>
         <tr><td>&nbsp;</td></tr>
         <tr>
            <td class="heading2">
               Phone Number:
            </td>
            <td class="plaintext">
               <input type="text" value="<%=entity.getPhoneNumber() %>" name="phoneNumber" maxlength="10">
            </td>
         </tr>
         <tr>
            <td>&nbsp;</td>
            <td colspan="2" class="plaintext">
               <font size="1">
                  Phone numbers must be 10-digit North American numbers (with no spaces inbetween digits).
               </font>
            </td>
         </tr>

         <tr><td>&nbsp;</td></tr>

         <tr>
            <td class="heading2">
               Notes:
            </td>
            <td class="plaintext">
               <input type="text" value="<%=entity.getNote() %>" name="note" maxlength="100">
            </td>
         </tr>

         <tr>
            <td>&nbsp;</td>
            <td colspan="2" class="plaintext">
               <font size="1">
                  This field will help provide additional information
                  (e.g., Calls going to Mary Smith))
               </font>
            </td>
         </tr>

   <tr><td>&nbsp;</td></tr>
   <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>
<%
   if (newEntity) {
%>
          <input type="hidden" name="reqState" value="htmlProcessEntityAdd">
<%
   } else {
%>
          <input type="hidden" name="reqState" value="htmlProcessEntityEdit">
          <input type="hidden" name="entityId" value="<%=entity.getEntityId()%>">
<% } %>
          <input type="submit" name="save" value="save">
          <input type="submit" name="cancel" value="cancel" onClick="setCancelled()">
          &nbsp;
      </td>
   </tr>
      </table>
      </ul>
      </td>
    </tr>

   </table>

   </td></tr>
   </form>

         </td>
      <!-- end page content -->

   </tr>

</table>

</body>
</html>
