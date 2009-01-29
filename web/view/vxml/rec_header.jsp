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
<?xml version="1.0"?>


<%@ include file="../imports.jsp" %>
<%@ taglib uri="autoattendant" prefix="attendant" %>
<%
String oogni            = null;
String promptSet         = null;
String appDir           = request.getContextPath();
String viewDir          = appDir + "/view";
String audioDir         = viewDir + "/audio";
String contactAudioDir  = audioDir + "/contact";
//String contactAudioDir  = appDir + "/AudioControl/playAudio";
String entityAudioDir   = audioDir + "/entity";
//String entityAudioDir   = appDir + "/AudioControl/playAudio";
String voxfxAudioDir    = audioDir + "/voxfx";
String recordAudioDir   = audioDir + "/record";
String grammarDir       = viewDir + "/vxml/grammar";
String appRoot          = viewDir + "/vxml/root.jsp";
String appRecordRoot    = viewDir + "/vxml/recordRoot.jsp";
String timeout          = "0.01";
%>

<vxml version="2.0" application="<%=appRecordRoot%>" xmlns="http://www.w3.org/2001/vxml" xmlns:voxeo="http://community.voxeo.com/xmlns/vxml">

<link next="<%= response.encodeURL(appDir + "/MainController?reqState=vxmlShowMainMenu")%>"
      fetchaudio="<%=voxfxAudioDir%>/fetching.wav">
    <grammar src="<%=grammarDir%>/Universals_MainMenu.xml" type="application/grammar-xml"/>
</link>

<link next="<%= response.encodeURL(appDir + "/MainController?reqState=vxmlShowOperator&amp;state=user")%>"
      fetchaudio="<%=voxfxAudioDir%>/fetching.wav">
    <grammar src="<%=grammarDir%>/Universals_Operator.xml" type="application/grammar-xml"/>
</link>
