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
List contactBeans = (List)request.getAttribute("contactBeans");
ContactBean contactBean = null;
List entityBeans = (List)request.getAttribute("entityBeans");
EntityBean entityBean = null;
%>
<%!
private String buildCountAudio(int index) {
    return ("07" + index + ".wav");
}

private boolean hasConflictInList(ContactBean contactBean, List contactBeans) {
    Iterator it = contactBeans.iterator();
    while(it.hasNext()) {
        ContactBean c = (ContactBean)it.next();
        if((c.getContactId() != contactBean.getContactId()) &&
                c.getPhoneticCode().equals(contactBean.getPhoneticCode())) {
            return true;
        }
    }
    return false;
}
%>


<var name="nbest"/>
<%
if(contactBeans != null) {
%>
<form id="disambiguateContacts">
    <field name="selection" slot="samename">
        <grammar src="<%= grammarDir %>/SameName.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s" count="1">
            <%
            for(int i = 0; i < contactBeans.size(); i ++) {
                contactBean = (ContactBean)contactBeans.get(i);
            %>
            <audio src="<%=audioDir%>/<%=buildCountAudio(i+1)%>">Press or say...</audio>
            <attendant:audio type="contact" id="<%=contactBean.getContactId()%>" audiodir="<%=contactAudioDir%>">
                <%=contactBean.getFirstname()%> <%=contactBean.getLastname()%>
            </attendant:audio>
            <%-- if(hasConflictInList(contactBean, contactBeans)) { --%>
            <%
            if(contactBean.getDistinctInfo() != null &&
                        contactBean.getDistinctInfo().length() != 0) {
            %>
            <attendant:audio type="distinctinfo" id="<%=contactBean.getContactId()%>" audiodir="<%=contactAudioDir%>">
                <%=contactBean.getDistinctInfo()%>
            </attendant:audio>
            <%    } %>
            <audio src="<%=audioDir%>/voxfx/silence0_5.wav"/>
            <% } %>
            <audio src="<%=audioDir%>/092.wav">
                To hear the list again, say repeat.
            </audio>
            <audio src="<%=audioDir%>/voxfx/waiting3_0.wav"/>
        </prompt>
        <%
        promptSet = audioDir + "/044.wav;" +
                audioDir + "/046.wav," + audioDir + "/192.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowMainMenu") %>"/>
        <%
        promptSet = audioDir + "/041.wav;" +
                audioDir + "/042.wav;" +
                audioDir + "/043.wav," + audioDir + "/191.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," +
                audioDir + "/081.wav," +
                voxfxAudioDir + "/helpout.wav;";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="true"/>
        <attendant:vxmlEvent
            type="repeat"
            promptSet="<%=audioDir+"/033.wav"%>"
            reprompt="false"
            finalURI="#disambiguateContacts"/>
        
        <filled>
            <if cond="selection == 'repeat'">
                <throw event="repeat"/>
                <%
                for(int i = 0; i < contactBeans.size(); i ++) {
            contactBean = (ContactBean)contactBeans.get(i);
                %>
                <elseif cond="selection == '<%=i + 1%>'"/>
                <assign name="nbest" expr="'contact-' + <%=contactBean.getContactId()%>"/>
                <submit next="<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=0")%>"
                        fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest"/>
                <% } %>
                <else/>
                <throw event="repeat"/>
            </if>
        </filled>
    </field>
</form>
<%
} else {
%>
<form id="disambiguateEntities">
    <field name="selection" slot="choice">
        <grammar src="<%= grammarDir %>/SameName.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s">
            <%
            for(int i = 0; i < entityBeans.size(); i ++) {
                entityBean = (EntityBean)entityBeans.get(i);
            %>
            <audio src="<%=audioDir%>/<%=buildCountAudio(i+1)%>">Press or say...</audio>
            <attendant:audio type="entity" id="<%=entityBean.getEntityId()%>" audiodir="<%=entityAudioDir%>">
                <%=entityBean.getName()%>
            </attendant:audio>
            <%
            if(entityBean.isSubEntity()) {
            %>
            <audio src="<%=audioDir%>/091.wav">in</audio>
            <attendant:audio type="entity" id="<%=entityBean.getParentId()%>" audiodir="<%=entityAudioDir%>">
                <%-- No access to the Parent Entity's name! Maybe later... says Simon --%>
            </attendant:audio>
            <%    } %>
            <audio src="<%=audioDir%>/voxfx/silence0_5.wav"/>
            <% } %>
            <audio src="<%=audioDir%>/092.wav">
                To hear the list again, say repeat.
            </audio>
            <audio src="<%=audioDir%>/voxfx/waiting3_0.wav"/>
        </prompt>
        <%
        promptSet = audioDir + "/044.wav;" +
                audioDir + "/046.wav," + audioDir + "/192.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowMainMenu") %>"/>
        <%
        promptSet = audioDir + "/041.wav;" +
                audioDir + "/042.wav;" +
                audioDir + "/043.wav," + audioDir + "/191.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," + audioDir;
        if(!((EntityBean)entityBeans.get(0)).isSubEntity())
            promptSet += "/082.wav,";
        else
            promptSet += "/101.wav,";
        promptSet += voxfxAudioDir + "/helpout.wav;";
        %>
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="true"/>
        <attendant:vxmlEvent
            type="repeat"
            promptSet="<%=audioDir+"/033.wav"%>"
            reprompt="false"
            finalURI="#disambiguateEntities"/>
        
        <filled>
            <if cond="selection == 'repeat'">
                <throw event="repeat"/>
                <%
                for(int i = 0; i < entityBeans.size(); i ++) {
            entityBean = (EntityBean)entityBeans.get(i);
                %>
                <elseif cond="selection == '<%=i + 1%>'"/>
                <assign name="nbest" expr="'entity-' + <%=entityBean.getEntityId()%>"/>
                <submit next="<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=0")%>"
                        fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest"/>
                <% } %>
                <else/>
                <throw event="repeat"/>
            </if>
        </filled>
    </field>
</form>
<%
}
%>
</vxml>
