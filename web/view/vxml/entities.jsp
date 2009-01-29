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
String confRejectionThreshold = Config.getString("confRejectionThreshold");
String maxNBest = Config.getString("maxNBest");
String confimationThreshold = Config.getString("confimationThreshold");
String delta = Config.getString("delta");

EntityBean parent = (EntityBean)request.getAttribute("parent");
List entityBeans = (List)request.getAttribute("entityBeans");
EntityBean entityBean = null;
String inlineGrammar = (String)request.getAttribute("inlineGrammar");
%>

<link event="repeat">
    <grammar src="<%= grammarDir %>/Repeat.xml" type="application/grammar-xml"/>
</link>

<var name="nbest"/>
<var name="nbestConf"/>
<%
if(entityBeans == null) {
%>
<form id="nolist">
    <block>
        <audio src="<%=audioDir%>/?.wav">
            I'm sorry, I don't have a list  of departments available.
            I'll take you back to the Main Menu now.
        </audio>
        <goto next="<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowMainMenu")%>"/>
    </block>
</form>
<%
} else {
%>
<form id="init">
    <block>
        <%
        if(parent == null) {
        %>
        <audio src="<%=audioDir%>/015.wav">OK</audio>
        <audio src="<%=voxfxAudioDir%>/silence0_5.wav"/>
        <audio src="<%=audioDir%>/031.wav">
            When you hear the department you want, just say it.
        </audio>
        <%
        } else {
        %>
        <audio src="<%=audioDir%>/051.wav">
            There are several groups within...
        </audio>
        <attendant:audio type="entity" id="<%=parent.getEntityId()%>" audiodir="<%=entityAudioDir%>">
            <%=parent.getName()%>
        </attendant:audio>
        <audio src="<%=audioDir%>/052.wav">
            When you hear the one you want, just say it.
        </audio>
        <%    } %>
        <goto next="#listing"/>
    </block>
</form>

<form id="listing">
    <block>
        <assign name="nbest" expr="''"/>
        <assign name="nbestConf" expr="''"/>
    </block>
    
    <field name="selection" slot="mainmenu">
        <property name="com.nuance.core.rec.ConfidenceRejectionThreshold" value="<%=confRejectionThreshold%>" />
        <property name="maxnbest" value="<%=maxNBest%>" />
        
        <grammar src="<%= grammarDir %>/Entities.xml" type="application/grammar-xml"/>
        
        <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <prompt timeout="<%=timeout%>s" count="1">
            <audio src="<%=voxfxAudioDir%>/silence0_5.wav"/>
            <%
            for(int i = 0; i < entityBeans.size(); i ++) {
            entityBean = (EntityBean)entityBeans.get(i);
            %>
            <attendant:audio type="entity" id="<%=entityBean.getEntityId()%>" audiodir="<%=entityAudioDir%>">
                <%=entityBean.getName()%>
            </attendant:audio>
            <audio src="<%=voxfxAudioDir%>/silence0_5.wav"/>
            <%    } %>
            <audio src="<%=audioDir%>/092.wav">
                To hear the list again, say repeat.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting3_0.wav"/>
        </prompt>
        <!-- This second set of prompts is purely to get around the fact that
           we don't want to reprompt for Repeat! -->
        <prompt timeout="<%=timeout%>s" count="2">
            <%
            if(parent == null) {
            %>
            <audio src="<%=audioDir%>/031.wav">
                When you hear the department you want, just say it.
            </audio>
            <%
            } else {
            %>
            <audio src="<%=audioDir%>/051.wav">
                There are several groups within...
            </audio>
            <attendant:audio type="entity" id="<%=parent.getEntityId()%>" audiodir="<%=entityAudioDir%>">
                <%=parent.getName()%>
            </attendant:audio>
            <audio src="<%=audioDir%>/052.wav">
                When you hear the one you want, just say it.
            </audio>
            <%    } %>
            <audio src="<%=voxfxAudioDir%>/silence0_5.wav"/>
            <%
            for(int i = 0; i < entityBeans.size(); i ++) {
            entityBean = (EntityBean)entityBeans.get(i);
            %>
            <attendant:audio type="entity" id="<%=entityBean.getEntityId()%>" audiodir="<%=entityAudioDir%>">
                <%=entityBean.getName()%>
            </attendant:audio>
            <audio src="<%=voxfxAudioDir%>/silence0_5.wav"/>
            <%    } %>
            <audio src="<%=audioDir%>/092.wav">
                To hear the list again, say repeat.
            </audio>
            <audio src="<%=voxfxAudioDir%>/waiting3_0.wav"/>
        </prompt>
        <%
        promptSet = audioDir + "/044.wav,"  + voxfxAudioDir + "/silence0_5.wav;" +
                audioDir + "/046.wav," + audioDir + "/191.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        <%
        promptSet = audioDir + "/041.wav,"  + voxfxAudioDir + "/silence0_5.wav;" +
                audioDir + "/043.wav," + audioDir + "/191.wav";
        %>
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        <%
        promptSet = voxfxAudioDir + "/helpin.wav," + audioDir;
        if(parent == null)
            promptSet += "/047.wav,";
        else
            promptSet += "/061.wav,";
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
            finalURI="#listing"/>
        <filled>
            
            <var name="MyArray" expr="application.lastresult$"/>
            <log expr="'LENGTH = ' + MyArray.length"/>
            
            <if cond="MyArray.length== 1">
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <elseif cond="MyArray.length== 2"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');' 
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + 
                ';' + MyArray[1].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <elseif cond="MyArray.length= 3"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'  	
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'
                + MyArray[2].interpretation.mainmenu + '(' + MyArray[2].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.mainmenu + ';'
                + MyArray[2].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <elseif cond="MyArray.length= 4"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'  
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'
                + MyArray[2].interpretation.mainmenu + '(' + MyArray[2].confidence + ');
                + MyArray[3].interpretation.mainmenu + '(' + MyArray[3].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.mainmenu + ';'
                + MyArray[2].interpretation.mainmenu + ';'
                + MyArray[3].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <elseif cond="MyArray.length= 5"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'  
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'
                + MyArray[2].interpretation.mainmenu + '(' + MyArray[2].confidence + ');
                + MyArray[3].interpretation.mainmenu + '(' + MyArray[3].confidence + ');'
                + MyArray[4].interpretation.mainmenu + '(' + MyArray[4].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.mainmenu + ';'
                + MyArray[2].interpretation.mainmenu + ';'
                + MyArray[3].interpretation.mainmenu + ';'
                + MyArray[4].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <elseif cond="MyArray.length= 6"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'  	
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'
                + MyArray[2].interpretation.mainmenu + '(' + MyArray[2].confidence + ');
                + MyArray[3].interpretation.mainmenu + '(' + MyArray[3].confidence + ');'
                + MyArray[4].interpretation.mainmenu + '(' + MyArray[4].confidence + ');'
                + MyArray[5].interpretation.mainmenu + '(' + MyArray[5].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.mainmenu + ';'
                + MyArray[2].interpretation.mainmenu + ';'
                + MyArray[3].interpretation.mainmenu + ';'
                + MyArray[4].interpretation.mainmenu + ';'
                + MyArray[5].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
                <else/>
                
                <log expr="' ^^^^^ MORE THAN SIX MATCHES, THAT IS A BAD THING ^^^^^ '"/>
                
            </if>
            
            
            <goto nextitem="BlockSelection"/>
        </filled>
    </field>
    
    <block name="BlockSelection">
        <if cond="selection == 'repeat'">
            <throw event="repeat"/>
            
            <elseif cond="nbest == ''"/>
            <audio src="<%=audioDir%>/021.wav"/>
            <goto nextitem="selection"/>
            
            <!-- they have either an entity or contact match... -->
            <else/>
            <assign name="nbest" expr="selection"/>			  
            <submit expr="'<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=")%>' + confirmation"
                    fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest nbestConf"/>
        </if>
    </block>
</form>

<%
promptSet = audioDir + "/striva-og-3.wav," + voxfxAudioDir + "/waiting4_0.wav;" +
        audioDir + "/striva-og-4.wav";
%>
<attendant:vxmlEvent
    type="noinput"
    promptSet="<%=promptSet%>"
    reprompt="false"
    finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>

<attendant:vxmlEvent
    type="nomatch"
    promptSet="<%=promptSet%>"
    reprompt="false"
    finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>

<attendant:vxmlEvent
    type="help"
    promptSet="<%=promptSet%>"
    reprompt="false"
    finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>

<filled>
    <assign name="nbest" expr="selection"/>
    <submit expr="'<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=")%>'+ confirmation" fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest nbestConf"/>
</filled>
</field>
</form>

<form id="whichSalesRegion">
    <block>
        <assign name="confirmation" expr="'0'"/>
        <assign name="nbestConf" expr="100"/>
    </block>
    
    <field name="selection" slot="mainmenu">
        <grammar src = "<%= grammarDir %>/Striva_Regions.xml" type="application/grammar-xml"/>
        <prompt timeout="<%=timeout%>s" count="1">
            <audio src="<%=audioDir%>/striva-1.wav">Our sales and partnering group is divided into several regions.  When you hear the correct one, just say it.  United States, Canada, Latin America, Asia Pacific, Europe, Africa.</audio>
            <audio src = "<%=voxfxAudioDir%>/waiting4_0.wav" />
        </prompt>
        <%
        promptSet = audioDir + "/striva-og-1.wav," + voxfxAudioDir + "/waiting4_0.wav;" +
                audioDir + "/striva-og-2.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <filled>
            <if cond="selection == 'entity-7'">
                <goto next="#whichState"/>
                <elseif cond="selection== 'entity-6'" />
                <goto next ="#canadaLocation" />
                <elseif cond="selection== 'entity-13'" />
                <goto next ="#canadaLocation" />
                <elseif cond="selection== 'entity-14'" />
                <goto next ="#canadaLocation" />
                <else/>
                <assign name="nbest" expr="selection"/>
                <submit expr="'<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=")%>'+ confirmation" fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest nbestConf"/>
            </if>
        </filled>
    </field>
</form>


<form id="canadaLocation" >
    <block>
        <assign name="confirmation" expr="'0'"/>
        <assign name="nbestConf" expr="100"/>
    </block>
    
    <field name="selection" slot="mainmenu">
        <grammar src = "<%= grammarDir %>/Striva_Canada.xml" type="application/grammar-xml"/>
        <prompt timeout="<%=timeout%>s" count="1">
            <audio src="<%=audioDir%>/striva-3.wav">Are you calling from eastern or western Canada?</audio>
        </prompt>
        <%
        promptSet = audioDir + "/striva-og-5.wav," + voxfxAudioDir + "/waiting4_0.wav;" +
                audioDir + "/striva-og-6.wav";
        %>
        <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowOperator") %>"/>
        
        <filled>
            <assign name="nbest" expr="selection"/>
            <submit expr="'<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=")%>'+ confirmation" fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest nbestConf"/>
        </filled>
    </field>
</form>

<% } %>
</vxml>
