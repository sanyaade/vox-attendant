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
   
   String errorType = (String) request.getAttribute("errorType");
   boolean displayError = false;
   String error = "";
   if(errorType != null) {
      if(errorType.equals("db")) {
         error = "Problem accessing database. Please contact system support.";
         displayError = true;
      }
      else if (errorType.equals("invalidPasscode")) {
         error = "Invalid passcode.";
         displayError = true;
      }
   }
%>
<html>
<head>
   <title>Home VoxAttendant.org</title>
   <%@ include file="scripts.jsp" %>

   <script LANGUAGE="JavaScript">
   <!-- //
   function validateForm(form) {
      for(var i = 0; i < form.elements.length ; i++) {
         element = form.elements[i];
         if(element.type == "password") {
            if(!isValidPasscode(element.value)) {
               alert(ERROR_LOGIN);
               element.focus();
               return false;
            }
         }
      }
      return true;
   }
   //  -->
   </script>
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0"
      background="<%=imageDir%>/cheat_2.gif">
<table border="0" cellspacing="0" cellpadding="0" align="left">
   <tr><td colspan="2">
   <table width="98%" border="0" cellspacing="0" cellpadding="0" align="left">
     <tr>
       <td width="721"><img src="<%=imageDir%>/head.jpg" width="267" height="177"><img src="<%=imageDir%>/head-03.jpg" width="292" height="177"></td>
       <td rowspan="3" valign="bottom">&nbsp;</td>
     </tr>
     <tr><td><img name="home" border="0" src="<%=imageDir%>/dark.jpg" width="128" height="33"></td></tr>
     <tr>
       <td><img src="<%=imageDir%>/lt_blue_blank_1.jpg" width="128" height="42"><img src="<%=imageDir%>/lt_blue_blank_2.jpg" width="139" height="42"><img src="<%=imageDir%>/lt_blue_blank_3.jpg" width="113" height="42"><img src="<%=imageDir%>/lt_blue_blank_4.jpg" width="194" height="42"><img src="<%=imageDir%>/lt_blue_blank_5.jpg" width="147" height="42"></td>
     </tr>
   </table>
   </td></tr>

   <tr>
      <!-- margin -->
      <td width="<%=margin%>">&nbsp;</td>
      <!------------>

      <td>
         <table>

   <tr><td>&nbsp;</td></tr>

   <tr><td>
         <!-- start page content -->

         <table>

         <tr><td>
         <p class="heading">AutoAttendant Admin Page</p>
   <%
      if(displayError) {
   %>
         <p class="error"><%=error%></p>
   <%
      }
   %>
         <form method="post" action="<%=LoginController%>">
         <table>
            <tr>
               <td>
                  <p class="plaintext">To login, please enter your passcode below:</p>
               </td>
            </tr>
            <tr>
               <td>
               <input type="password" name="passcode" maxlength="4">
               <input type="hidden" name="reqState" value="htmlProcessLogin">
               <input type="submit" name="submit" value="Login">
               </td>
            </tr>
         </table>
         </form>
        </td></tr>
        <tr>
           <td>
              <font size="1">
              If you are having trouble logging in, send an email to
              <a href="mailto:voxattendant@icoa.com">voxattendant@icoa.com</a>
              </font>
           </td>
        </tr>
            </table>

         <!-- end page content -->

      </td>
   </tr>
</table>
</body>
</html>
