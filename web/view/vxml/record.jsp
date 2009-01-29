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
String audioName = (String)request.getAttribute("audioName");
String audioTTS = (String)request.getAttribute("audioTTS");
String subdialogURL = appDir + "/AudioControl?reqState=vxmlDisplayRecord&amp;name=" +
        audioName + "&amp;tts=" + URLEncoder.encode(audioTTS);
boolean continueContact = ((String)request.getAttribute("recCategory")).equals("contact");
%>


<form id="subdialog">
    <subdialog name="result" src="<%=subdialogURL%>" method="get">
        <filled>
            <goto next="#continue"/>
        </filled>
    </subdialog>
</form>

<form id="continue">
    <field name="selection" slot="confirm">
        <grammar src="<%= grammarDir %>/Yes_No.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <%
            if(continueContact) {
            %>
            <audio src="<%=recordAudioDir%>/continue1.wav">
                Do you want to update another contact?
            </audio>
            <%
            } else {
            %>
            <audio src="<%=recordAudioDir%>/continue2.wav">
                Do you want to update another department?
            </audio>
            <% } %>
            <audio src="<%=voxfxAudioDir%>/waiting3_5.wav"/>
        </prompt>
        <%
        promptSet = recordAudioDir + "/oogni1.wav;" +
                recordAudioDir + "/oogni2.wav;" +
                recordAudioDir + "/oogni3.wav," + recordAudioDir + "/oogni14.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%=response.encodeURL("RecordController?reqState=vxmlShowRecord")%>"/>
        <%
        promptSet = recordAudioDir + "/oogni4.wav;" +
                recordAudioDir + "/oogni5.wav;" +
                recordAudioDir + "/oogni6.wav," + recordAudioDir + "/oogni14.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%=response.encodeURL("RecordController?reqState=vxmlShowRecord")%>"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                recordAudioDir + "/continueHelp.wav," +
                voxfxAudioDir + "/helpout.wav," +
                voxfxAudioDir + "/silence1_0.wav";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
        
        <filled>
            <if cond="selection == 'yes'">
                <%
                if(continueContact) {
                %>
                <goto next="<%=response.encodeURL("RecordController?reqState=vxmlShowRecordContact")%>"
                      fetchaudio="<%=voxfxAudioDir%>/fetching.wav"/>
                <%
                } else {
                %>
                <goto next="<%=response.encodeURL("RecordController?reqState=vxmlShowRecordEntity")%>"
                      fetchaudio="<%=voxfxAudioDir%>/fetching.wav"/>
                <% } %>
                <else/>
                <audio src="<%=recordAudioDir%>/continue3.wav">
                    OK, I'll take you back to the Main Menu now.
                </audio>
                <goto next="<%=response.encodeURL("RecordController?reqState=vxmlShowRecord")%>"
                      fetchaudio="<%=voxfxAudioDir%>/fetching.wav"/>
            </if>
        </filled>
    </field>
</form>

</vxml>
