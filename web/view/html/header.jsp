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
<%@ include file="../imports.jsp" %>
<%@ taglib uri="autoattendant" prefix="attendant" %>
<%
   String appDir           = request.getContextPath();
   String view             = appDir + "/view";
   String host             = (String) request.getServerName();
   String imageDir         = view + "/html/images";
   String audio            = view + "/audio";
   String contactAudioDir  = audio + "/contact";
   //String contactAudioDir  = appDir + "/PlayAudioController/playAudio";
   String entityAudioDir   = audio + "/entity";
   //String entityAudioDir   = appDir + "/PlayAudioController/playAudio";

   String LoginController  = request.getContextPath() + "/LoginController";
   String ContactController = request.getContextPath() + "/ContactController";
   String EntityController = request.getContextPath() + "/EntityController";
   String SettingsController = request.getContextPath() + "/SettingsController";

   String submenu = "";

   //
   String margin = "35";
%>
