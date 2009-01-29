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
boolean jumpToContact = ((String)request.getAttribute("jumpToContact") != null);
boolean jumpToEntity = ((String)request.getAttribute("jumpToEntity") != null);

// For error handling only...
boolean error = ((String)request.getAttribute("error") != null);
String category = (String)request.getAttribute("category");
String type = (String)request.getAttribute("type");
%>


<var name="recCategory"/>  <!-- contact or entity -->
<var name="recType"/>      <!-- name or distinctinfo -->
<%
if(error) {
%>
<form id="error">
    <block>
        <assign name="recCategory" expr="'<%=category%>'"/>
        <assign name="recType" expr="'<%=type%>'"/>
        
        <audio src="<%=recordAudioDir%>/error.wav">
            The id you provide is invalid. Please try again.
        </audio>
        <goto next="#getFileIdRetry"/>
    </block>
</form>
<% } %>
<%
if(jumpToContact) {
%>
<form id="jump">
    <block>
        <assign name="recCategory" expr="'contact'"/>
        <goto next="#contact"/>
    </block>
</form>
<%
} else if(jumpToEntity) {
%>
<form id="jump">
    <block>
        <assign name="recCategory" expr="'entity'"/>
        <goto next="#getFileId"/>
    </block>
</form>
<% } %>

<form id="main">
    <field name="selection" slot="main">
        <grammar src="<%=grammarDir%>/Main.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <audio src="<%=recordAudioDir%>/main1.wav">
                Would you like to update a Contact or Department?
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting4_0.wav"/>
        </prompt>
        <%
        promptSet = recordAudioDir + "/oogni1.wav;" +
                recordAudioDir + "/oogni2.wav;" +
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
                recordAudioDir + "/mainHelp.wav," +
                voxfxAudioDir + "/helpout.wav;";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="true"/>
        
        <filled>
            <assign name="recCategory" expr="selection"/>
            <if cond="selection == 'contact'">
                <goto next="#contact"/>
                
                <elseif cond="selection == 'entity'"/>
                <goto next="#getFileId"/>
                
                <else/>
                <clear namelist="recCategory"/>
                <goto nextitem="selection"/>
            </if>
        </filled>
    </field>
</form>

<form id="contact">
    <field name="selection" slot="contact">
        <grammar src="<%=grammarDir%>/Contact.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <audio src="<%=recordAudioDir%>/contact1.wav">
                Okay, would you like to update a contact's name or their unique distinct information?
                Say Name or Distinct Info.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting4_0.wav"/>
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
            finalURI="#main"/>
        <%
        promptSet = recordAudioDir + "/oogni4.wav;" +
                recordAudioDir + "/oogni5.wav;" +
                recordAudioDir + "/oogni6.wav," + recordAudioDir + "/oogni14.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="#main"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                recordAudioDir + "/contactHelp.wav," +
                voxfxAudioDir + "/helpout.wav," +
                voxfxAudioDir + "/silence1_0.wav";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
        
        <filled>
            <assign name="recType" expr="selection"/>
            <if cond="selection == 'name'">
                <goto next="#getFileId"/>
                
                <elseif cond="selection == 'distinctinfo'"/>
                <goto next="#getFileId"/>
                
                <else/>
                <goto next="#contact"/>
            </if>
        </filled>
    </field>
</form>

<form id="getFileId">
    <var name="reqState" expr="'vxmlStartRecord'"/>
    
    <field name="fileid" type="digits?maxlength=6">
        
        <!--
  <field name="fileid" slot="number" modal="true" type="digits?maxlength=6">
        <grammar src="<%=grammarDir%>/Capture_Number.xml" type="application/grammar-xml"/>
-->
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <audio src="<%=recordAudioDir%>/fileNumber1.wav">
                Next, key in the audio file number followed by the Pound Key.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting5_0.wav"/>
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
            finalURI="#main"/>
        <%
        promptSet = recordAudioDir + "/oogni4.wav;" +
                recordAudioDir + "/oogni5.wav;" +
                recordAudioDir + "/oogni6.wav," + recordAudioDir + "/oogni14.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="#main"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                recordAudioDir + "/fileNumberHelp.wav," +
                voxfxAudioDir + "/helpout.wav," +
                voxfxAudioDir + "/silence1_0.wav";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
        
        <filled>
            <submit next="<%=response.encodeURL("RecordController")%>"
                    fetchaudio="<%=voxfxAudioDir%>/fetching.wav" method="post" namelist="reqState fileid recCategory recType"/>
        </filled>
    </field>
</form>

<form id="getFileIdRetry">
    <var name="reqState" expr="'vxmlStartRecord'"/>
    <field name="fileid" slot="number"  modal="true">
        <grammar src="<%=grammarDir%>/Capture_Number.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <audio src="<%=recordAudioDir%>/fileNumber1.wav">
                Key in the audio file number followed by the Star Key.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting5_0.wav"/>
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
            finalURI="#main"/>
        <%
        promptSet = recordAudioDir + "/oogni4.wav;" +
                recordAudioDir + "/oogni5.wav;" +
                recordAudioDir + "/oogni6.wav," + recordAudioDir + "/oogni14.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="#main"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                recordAudioDir + "/fileNumberHelp.wav," +
                voxfxAudioDir + "/helpout.wav," +
                voxfxAudioDir + "/silence1_0.wav";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
        
        <filled>
            <submit next="<%=response.encodeURL("RecordController")%>"
                    fetchaudio="<%=voxfxAudioDir%>/fetching.wav" method="get" namelist="reqState fileid recCategory recType"/>
        </filled>
    </field>
</form>

<form id="disconnect">
    <block>
        <audio src="<%=recordAudioDir%>/oogni7.wav">
            There seems to be a problem with our connection. Please initiate this call again soon.
        </audio>
        <disconnect/>
    </block>
</form>
</vxml>
