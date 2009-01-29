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
//be sure to copy changes over to header.jsp

String oogni            = null;
String promptSet         = null;
String appDir           = request.getContextPath();
String viewDir          = appDir + "/view";
String audioDir         = viewDir + "/audio";
String contactAudioDir  = audioDir + "/contact";
String entityAudioDir   = audioDir + "/entity";
String voxfxAudioDir    = audioDir + "/voxfx";
String recordAudioDir   = audioDir + "/record";
String grammarDir       = viewDir + "/vxml/grammar";
String appRoot          = viewDir + "/vxml/root.jsp";
String appRecordRoot    = viewDir + "/vxml/recordRoot.jsp";

String timeout          = "0.01";
%>

<vxml version="2.0" xmlns="http://www.w3.org/2001/vxml" xmlns:voxeo="http://community.voxeo.com/xmlns/vxml">
    
    <meta name="maintainer" content="shawn@rhoads.com"/>
    
    <!-- speed up DTMF return time-->
    <property name="interdigittimeout" value="2s"/>
    
    <!-- record utterances -->
    <property name="com.nuance.client.WriteWaveforms" value="1"/>
    
    <!-- defines which built in utterences are allowed  -->
    <!-- note that unless we have defined help events, then the nuance default will be executed -->
    <!-- that is Bad -->
    <property name="universals" value="none"/>
    
    <!-- allows a bit more room for grammar matches -->
    <property name="confidencelevel" value="0.4"/>
    
    <!--  set the fetchaudio delay to avoid trunctuated fetchaudio filler-->
    <property name="fetchaudiodelay" value="2s"/>
    
    <!-- set the minimum duration a fetchaudio file should play-->
    <property name="fetchaudiominimum" value="3s"/>
    
    <!--  sets some 'filler' music in the event of a long fetch time -->
    <property name="fetchaudio" value="<%=voxfxAudioDir%>/Pause1.wav"/>
    
    <!--  set all fetching parameters to 'prefetch' for speed -->
    <property name="grammarfetchhint" value="prefetch"/>
    <property name="documentfetchhint" value="prefetch"/>
    <property name="audiofetchhint" value="prefetch"/>
    <property name="scriptfetchhint" value="prefetch"/>
    
    <!-- set the amount of time a fetch is allowed before throwing a badfetch -->
    <property name="fetchtimeout" value="10s"/>
    
    <!-- FOR SOME UNFATHOMABLE REASON, THESE LINKS WILL NOT WORK -->
    <!-- WHILE IN root.jsp, SO WE WILL PUT THEM IN header.jsp -->
    <link next="<%= response.encodeURL(appDir + "/MainController?reqState=vxmlShowMainMenu")%>"
          fetchaudio="<%=voxfxAudioDir%>/Pause1.wav">
        <grammar src="<%=grammarDir%>/Universals_MainMenu.xml" type="application/grammar-xml"/>
    </link>
    
    <link next="<%= response.encodeURL(appDir + "/MainController?reqState=vxmlShowOperator&amp;state=user")%>"
          fetchaudio="<%=voxfxAudioDir%>/Pause1.wav">
        <grammar src="<%=grammarDir%>/Universals_Operator.xml" type="application/grammar-xml"/>
    </link>
    
    <link event="help">
        <grammar src="<%=grammarDir%>/Universals_Help.xml" type="application/grammar-xml" />
    </link>
    
    <!-- Universal Error-Out -->
    <noinput count="3">
        <prompt>
            <audio src="<%=audioDir%>/System_Error_With Agent.wav">
                Just a moment, I'll get someone on the line to help you.
            </audio>
        </prompt>
        <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
    </noinput>
    <nomatch count="3">
        <prompt>
            <audio src="<%=audioDir%>/System_Error_With Agent.wav">
                Just a moment, I'll get someone on the line to help you.
            </audio>
        </prompt>   
        <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
    </nomatch>
    
    <catch event="help">
        
    </catch>
    
    <catch event="cancel">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="exit">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="repeat">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="gototop">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="UNEXPECTED_KEY">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="SPEECH_TOO_EARLY">
        <throw event="nomatch"/>
    </catch>
    
    <catch event="NO_SPEECH_TIMEOUT">
        <throw event="nomatch"/>
    </catch>
    
    <error>
        <goto next="<%=appDir%>/MainController?reqState=vxmlShowMainMenu"/>
    </error>
    
</vxml>
