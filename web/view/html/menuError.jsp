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
<!-- Begin Contacts Menu -->
   <table width="98%" border="0" cellspacing="0" cellpadding="0" align="left">
     <tr>
       <td width="721"><img src="<%=imageDir%>/head.jpg" width="267" height="177"><img src="<%=imageDir%>/head-03.jpg" width="292" height="177"></td>
       <td rowspan="3" valign="bottom">&nbsp;</td>
     </tr>
     <tr>
       <td><a href="<%=LoginController + "?reqState=htmlShowHome"%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('home','','<%=imageDir%>/home_over.jpg',1)"><img name="home" border="0" src="<%=imageDir%>/home.jpg" width="128" height="33"></a><a href="<%=ContactController%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('contacts','','<%=imageDir%>/contacts_over.jpg',1)"><img name="contacts" border="0" src="<%=imageDir%>/contacts.jpg" width="139" height="33"></a><a href="<%=EntityController%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('departments','','<%=imageDir%>/department_over.jpg',1)"><img name="departments" border="0" src="<%=imageDir%>/department.jpg" width="162" height="33"></a><a href="<%=SettingsController%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('settings','','<%=imageDir%>/settings_over.jpg',1)"><img name="settings" border="0" src="<%=imageDir%>/settings.jpg" width="145" height="33"></a><a href="<%=LoginController + "?reqState=htmlProcessLogout"%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('logout','','<%=imageDir%>/logout_over.jpg',1)"><img name="logout" border="0" src="<%=imageDir%>/logout.jpg" width="147" height="33"></a></td>
     </tr>
     <tr>
       <td><img src="<%=imageDir%>/lt_blue_blank_1.jpg" width="128" height="42"><img src="<%=imageDir%>/lt_blue_blank_2.jpg" width="139" height="42"><img src="<%=imageDir%>/lt_blue_blank_3.jpg" width="113" height="42"><img src="<%=imageDir%>/lt_blue_blank_4.jpg" width="194" height="42"><img src="<%=imageDir%>/lt_blue_blank_5.jpg" width="147" height="42"></td>
     </tr>
   </table>
<!-- End Contacts Menu -->
