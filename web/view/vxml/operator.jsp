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
String operatorNum = (String)request.getAttribute("operatorNum");
if (operatorNum.indexOf("sip") < 0)
{
    operatorNum = "tel:+1" + operatorNum;
}
boolean userInitiated = (request.getAttribute("userInitiated") != null);
boolean failedToProcess = (request.getAttribute("failedToProcess") != null);

System.out.println("Transferring to Operator num: " + operatorNum);
//System.out.println("userInitiated: " + userInitiated);
//System.out.println("failedToProcess: " + failedToProcess);
%>

<%
//Office open error message
if(failedToProcess) {
%>
<form id="call">
    <block>
        <prompt>
            <audio src="<%=audioDir%>/System_Error_With Agent.wav">
                Just a moment, I'll get someone on the line to help you.
            </audio>
        </prompt>
        <goto next="#operatorTransfer"/>
    </block>
</form>
<% } %>


<form id="operator">
    <%
    if(operatorNum == null) {
    %>
    <block>
        <goto next="#no_operator"/>
    </block>
    <%} else {%>
    
    <% if (userInitiated) { %>
    
    <field name="cancel" >
        <grammar src="<%= grammarDir %>/Cancel.xml" type="application/grammar-xml" />
        <property name="nuance.core.client.NoSpeechTimeoutSecs" value="2.0s"/>
        
        <prompt timeout="2.0s">
            <audio src="<%=audioDir%>/Front_Desk-Main.wav">
                OK, connecting you to the front desk.  Say stop to
                cancel.
            </audio>
        </prompt>
        <filled>
            <if cond="cancel == 'cancel'">
                <audio src="<%=voxfxAudioDir%>/Stop-Tone.wav"/>
                <goto next="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowMainMenu")%>"/>
            <else/>
                <goto nextitem="operatorTransfer"/>
            </if>
        </filled>
        
        <noinput>
            <goto nextitem="operatorTransfer"/>
        </noinput>
        <nomatch>
            <goto nextitem="operatorTransfer"/>
        </nomatch>
    </field>
    <%    } %>

    <transfer name="operatorTransfer" dest="<%= operatorNum %>" connecttimeout="60s" bridge="true" transferaudio="<%=voxfxAudioDir%>/Hold-Music-Long.wav">
        <filled>
            <if cond="operatorTransfer == 'busy'">
                <goto next="#no_operator"/>
            <elseif cond="operatorTransfer == 'noanswer'"/>
                <goto next="#no_operator"/>
            <elseif cond="operatorTransfer == 'network_busy'"/>
                <goto next="#no_operator"/>
            <elseif cond="operatorTransfer == 'near_end_disconnect'"/>
                <disconnect/>
            <elseif cond="operatorTransfer == 'far_end_disconnect'"/>
                <disconnect/>
            <elseif cond="operatorTransfer == 'network_disconnect'"/>
                <goto next="#no_operator"/>
            <else/>
                <disconnect/>
            </if>
        </filled>
    </transfer>
    <% } %>
</form>

<form id="no_operator">
    <block>
       
        <goto next="#return"/>
    </block>
</form>

<form id="return">
    <block>
        <goto next="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowMainMenu")%>"/>
    </block>
</form>
</vxml>
