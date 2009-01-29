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
   BeanCollection promptBeanList = (BeanCollection)request.getAttribute("promptBeanList");
   boolean drivingDirOn = (request.getAttribute("drivingDir") != null);
%>
<html>

<head>
   <title>Settings | Prompts</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function validateForm(form) {
      var count = 0;
      for(var i = 0; i < form.elements.length; i++) {
         element = form.elements[i];
         if(element.type == "checkbox") {
            if(element.checked) {
               count ++;
            }
         }
      }
      if(count == 0) {
         alert(ERROR_GREETINGS);
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

<table border="0" bordercolor="red" cellspacing="0" cellpadding="0" align="left">
   <tr><td colspan="2">
   <%
      submenu="prompts";
   %>
   <%@ include file="menuSettings.jsp" %>
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
   <table border="0" width="700">
      <tr>
         <td class="heading">
         Prompts
         </td>
      </tr>
      <tr>
         <td class="plaintext">
            This page allows you to change some of the prompts
            that are played in the AutoAttendant application.
         </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
   </table>
   </td></tr>

   <tr><td>
   <form method="get" action="<%=response.encodeURL(SettingsController)%>">
      <input type="hidden" name="reqState" value="htmlProcessPromptDirections">
      <table border="0">
      <tr>
         <td class="heading2">Driving Directions&nbsp;
            <font class="plaintext">
            <a href="<%=audio%>/172.wav">play</a>
            </font>
         </td>
      </tr>
      <tr><td><hr></td></tr>
      <tr>
         <td colspan="2" class="plaintext">
            To change the status, just click the button below. 'On' means
            that callers can ask to hear driving directions to your office
            locations. 'Off' means that will not be able to ask for
            driving directions.
         </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
         <td class="plaintext">Driving Directions are currently:
            <b>
      <% if(drivingDirOn) { %>
                  ON
      <% } else { %>
                  OFF
      <% } %>
            </b>
         </td>
         <td>&nbsp;</td>
      </tr>
      <tr>
         <td class="plaintext">To change it, just click the button.&nbsp;
         <input type="submit" name="toggle"
      <% if(drivingDirOn) { %>
                  value="Turn it Off"
      <% } else { %>
                  value="Turn it On"
      <% } %>
         ></td>
      </tr>
      </table>
   </form>
   </td></tr>

   <tr><td>
   <form method="post" action="<%=response.encodeURL(SettingsController)%>" onSubmit="return validateForm(this);">
      <input type="hidden" name="reqState" value="htmlProcessPromptGreetings">
      <table border="0">
      <tr>
         <td colspan="2" class="heading2">Greeting Prompts</td>
      </tr>

      <tr><td><hr></td></tr>

      <tr>
         <td colspan="2" class="plaintext">
            You can select to play up to four pre-recorded greetings.  The
            selected greetings will rotate each time a call reaches the
            AutoAttendant.  At least one greeting must be selected.
         </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
<%
   PromptBean promptBean = null;
   for(int i = 0; i < promptBeanList.size(); i++) {
      promptBean = (PromptBean)promptBeanList.getItem(i);
%>
      <tr>
         <td class="plaintext">
            <input type="checkbox" name="promptId" value="<%=promptBean.getPromptId()%>"
<%    if(promptBean.isActive()) { %>
            CHECKED
<%    } %>
            ><%=promptBean.getName()%>
            &nbsp;<a href="<%=audio%>/<%=promptBean.getAudio()%>">play</a>
         </td>
      </tr>
<% } %>
      <tr>
         <td align="center"><input type="submit" name="save" value="Save"></td>
      </tr>
      </table>
   </form>
   </td></tr>

          </table>
      </td>
      <!-- end page content -->

   </tr>


</table>

</body>
</html>
