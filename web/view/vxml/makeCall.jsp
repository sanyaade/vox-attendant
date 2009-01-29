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
String phoneNumber = (String)request.getAttribute("phoneNumber");
String aniNumber = (String)request.getAttribute("aniNumber");
String cancelURL = response.encodeURL(appDir+"/MainController?reqState=vxmlProcessSkipName");
System.out.println("makeCall.jsp: calling " + phoneNumber );
%>


<form id="makeCall">
    
    <block id="KillCallRec">
        <voxeo:recordcall value="0" info="AutoAttendant" />
        <goto nextitem="callee"/>
    </block>
    
    <%
    if (!(phoneNumber.startsWith("sip:"))) {
    phoneNumber = "tel:+1" + phoneNumber;
    }
    %>
    
    
    
    <transfer name="callee" destexpr="'<%=phoneNumber%>'" connecttimeout="60s" bridge="true" transferaudio="<%=voxfxAudioDir%>/Hold-Music-Long.wav">
        <filled>
            <log expr="'*** TRANSFER RESULT =' + callee + '***' "/>
            <if cond="callee == 'busy'">
                <goto next="#xfer2Operator"/>
            <elseif cond="callee == 'noanswer'"/>
                <goto next="#xfer2Operator"/>
            <elseif cond="callee == 'network_busy'"/>
                <goto next="#xfer2Operator"/>
            <elseif cond="callee == 'near_end_disconnect'"/>
                <log expr="'***** Near End Disconnect *****' + callee"/>
                <audio src="<%=voxfxAudioDir%>/Stop-Tone.wav"/>
                <goto next="<%=cancelURL%>"/>
            <elseif cond="callee == 'far_end_disconnect'"/>
                <log expr="'***** Far End Disconnect *****' + callee"/>
                <disconnect/>
            <elseif cond="callee == 'network_disconnect'"/>
                <log expr="'***** Network Disconnect *****' + callee"/>
                <disconnect/>
            <else/>
                <log expr="'***** Not one of the events *****' + callee"/>
                <disconnect/>
            </if>
        </filled>
    </transfer>

</form>

<form id="xfer2Operator">
    <block>
        <audio src="<%=audioDir%>/System_Error_With Agent.wav">
                Just a moment, I'll get someone on the line to help you.
        </audio>
        <goto next="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator")%>"/>
    </block>
</form>

</vxml>
