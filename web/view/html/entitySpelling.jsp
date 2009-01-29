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

   String navigate = (String) request.getAttribute("navigate");
   if(navigate == null) navigate = "";
%>
<html>
<head>
   <title>Departments | Alternate Spellings</title>
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
   <%@ include file="menuEntities.jsp" %>
   </td></tr>

   <tr><td>
   <table>
   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>

      <!-- start page content -->
      <td>

         <table>

            <tr><td>&nbsp;</td></tr>

            <form method="post" action="<%=response.encodeURL(EntityController)%>" onSubmit="return validateForm(this);">
            <tr><td align="right">
               <attendant:navigate value="<%=navigate%>" type="button"
                     onClick="this.form.buttonPressed.value=this.value;validateAndSubmitForm(this.form)"/>
            </td><tr>

            <tr><td>
            <form method="get" action="<%=response.encodeURL(EntityController)%>">
               <input type="hidden" name="entityId" value="<%=entity.getEntityId()%>">
               <input type="hidden" name="buttonPressed" value="none">
               <table width="700">
               <tr>
               <td>
         <%
                if(parentEntity != null) {
         %>
                  <font class="heading">Sub-Department:</font>
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
         <% } %>
             </td>
               </tr>
               <tr>
                  <td class="plaintext">
                     Below are the alternate spellings and phonetic
                     pronunciations for this department.  You can add
                     information to make it easier for callers to reach
                     employees.  For example, you can add in department
                     monikers (e.g., QA for Quality Assurance), as well as
                     name variations (e.g., Biz Dev for Business Development,
                     Alliances for Partnerships and Alliances).
                  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr><td class="heading2">Spellings</td></tr>
               <tr><td colspan="2"><hr></td></tr>
               <tr>
                  <td colspan="2" class="plaintext">
                     You can add a spelling, modify a spelling or delete a
                     spelling.  Make all appropriate changes below.
                 </td>
               </tr>
               <tr>
                  <td colspan="2" align="center">
                     <table>
                        <tr><td>&nbsp;</td></tr>
                        <tr>
                           <td class="plaintext">
                              <attendant:displayText format="title" text="<%=entity.getName()%>"/>
                           </td>
                        </tr>
                        <tr><td valign="top">
                           <table>
         <%
            BeanCollection altSpellings = entity.getAltSpellings();

            if(altSpellings != null) {
               for(int i = 0; i < altSpellings.size(); i ++) {
                  AltSpellingBean altSpellingBean = (AltSpellingBean)altSpellings.getItem(i);
         %>
                           <tr>
                           <td valign="top">
                              <input type="text" value="<%=altSpellingBean.getSpelling() %>"
                                        name="spl<%=altSpellingBean.getAltSpellingId() %>" maxlength="25">
                           </td>
                           <td valign="top">
                              <input type="button" value="x" name="del<%=altSpellingBean.getAltSpellingId()%>"
                                    onClick="this.form.buttonPressed.value=this.name;this.form.submit()"/>
                           </td>
                           </tr>
         <%    }
            }
         %>
                           </table>
                        </td></tr>

                        <tr>
                           <td class="heading2"><p>New Spelling:</td>
                        </tr>
                        <tr>
                           <td><input type="text" name="newSpelling" value="" maxlength="25"></td>
                        </tr>
                        <tr>
                          <td align="right" colspan="4">
                             <input type="hidden" name="reqState" value="htmlProcessEntitySpellingEdit">
                             <input type="submit" name="apply" value="Apply">&nbsp;

                             <input type="button" name="save" value="Save"
                                onClick="this.form.buttonPressed.value=this.name;validateAndSubmitForm(this.form)">&nbsp;

                             <input type="button" name="cancel" value="Cancel"
                                onClick="this.form.buttonPressed.value=this.name;this.form.submit()">
                          </td>
                        </tr>
                     </table>
                  </td>
               </tr>
            </form>

            </td></tr>
         </table>

      </td>
      <!-- end page content -->

   </tr>
   </form>


</table>

</body>
</html>
