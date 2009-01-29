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
<%@ include file="rec_header.jsp" %>
<%
boolean error = ((String)request.getAttribute("error") != null);
%>

<%
if(error) {
%>
<form id="error">
    <block>
        <audio src="<%=recordAudioDir%>/oogni8.wav">
            I'm sorry, that's not the right passcode.  Please try again.
        </audio>
        <goto next="#login"/>
    </block>
</form>
<% } %>

<form id="greeting">
    <block>
        <prompt bargein="false">
            <audio>
                Hi.  This the Vox Attendant Record Tool.
            </audio>
        </prompt>
        <goto next="#login"/>
    </block>
</form>

<form id="login">
    <var name="reqState" expr="'vxmlProcessLogin'"/>
    <field name="passcode" type="digits?length=4">
        
        <!--
        <grammar src="<%=grammarDir%>/Passcode.xml" type="application/grammar-xml"/>
        -->
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <audio src="<%=recordAudioDir%>/login1.wav">
                Key in your 4 digit passcode followed by the Pound key.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting5_0.wav"/>
        </prompt>
        <%
        promptSet = recordAudioDir + "/oogni1.wav;" +
                recordAudioDir + "/oogni3.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="#disconnect"/>
        <%
        promptSet = recordAudioDir + "/oogni4.wav;" +
                recordAudioDir + "/oogni5.wav;" +
                recordAudioDir + "/oogni6.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="#disconnect"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                recordAudioDir + "/loginHelp.wav," +
                voxfxAudioDir + "/helpout.wav;";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="true"/>
        
        <filled>
            <log expr="' ***************** PASSCODE = ' + passcode +'**********************'"/>
            
            <log expr="' ***************** REQSTATE =' + reqState +'**********************'"/>
            
            
            <submit next="<%=response.encodeURL(appDir+"/RecordController")%>"
                    fetchaudio="<%=voxfxAudioDir%>/fetching.wav" method="get" namelist="reqState passcode"/>
        </filled>
    </field>
</form>

<form id="disconnect">
    <block>
        <audio src="<%=recordAudioDir%>/oogni7.wav">
            There seems to be a problem with our connection. Please initiate this call again soon.
        </audio>
        <exit/>
    </block>
</form>
</vxml>
