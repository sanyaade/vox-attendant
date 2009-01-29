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

String greetingAudio = (String)request.getAttribute("greetingAudio");
boolean playDirectionAudio = (request.getAttribute("playDirectionAudio") != null);
List skipList = (List)request.getAttribute("skipList");
System.out.println("Got to main.jsp");
%>

<meta name="maintainer" content="shawn@rhoads.com"/>

<var name="nbest"/>
<var name="nbestConf"/>
<var name="isDtmf"/>
<var name="confirmation"/>

<var name="playedCompanyInfo" expr="false"/>
<var name="playedRepeat" expr="false"/>
<var name="errorCount" expr="0"/>
<var name="niCount" expr="0"/>
<var name="nmCount" expr="0"/>

<var name="callerId" expr="session.callerid"/>

<%
if(greetingAudio != null) {
%>
<form id="Greeting">
    <block>
        <voxeo:recordcall value="100" info="AutoAttendant" /> 
        <prompt bargein="true">
            <audio src="<%=audioDir%>/Welcome.wav">
                Thanks for calling Vox Attendant.
            </audio>
        </prompt>
        <goto next="#MainMenu"/>
    </block>
</form>
<% } %>

<form id="MainMenu">
    <block>
        <assign name="nbest" expr="''"/>
        <assign name="nbestConf" expr="''"/>
    </block>
    
    <field name="mainmenu">
        <property name="nuance.core.rec.ConfidenceRejectionThreshold" value="<%=confRejectionThreshold%>"/>
        <property name="maxnbest" value="<%=maxNBest%>" />
        <property name="nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>
        
        <grammar src="<%=grammarDir%>/MainMenu.xml" type="application/grammar-xml"/>
        <grammar src="<%=grammarDir%>/Entities.xml" type="application/grammar-xml"/>
        <grammar src="<%=grammarDir%>/Contacts.xml" type="application/grammar-xml"/>
        
        <prompt timeout="<%=timeout%>s" cond="!playedCompanyInfo">
            <audio src="<%=audioDir%>/Main.wav">
                Say the name of the person or department you want.  Or say company info.
            </audio>
        </prompt>   
        
        <prompt cond="playedCompanyInfo">
            <audio src="<%=audioDir%>/After-Company-Info.wav">
                You can say "driving directions", or say the name of the person or department you want.
            </audio>
            
        </prompt>
        
        <!-- *** NOINPUT EVENTS *** -->
        <noinput>
            <script language="Javascript">
            <![CDATA[
              errorCount = errorCount+1;
              niCount = niCount+1;
            ]]>
            </script>
            <if cond="errorCount==3">
                <throw eventexpr="'NoInput'+errorCount"/>
            <else/>
                <throw eventexpr="'NoInput'+niCount"/>
            </if>
        </noinput>
        <nomatch>
            <script language="Javascript">
            <![CDATA[
              errorCount = errorCount+1;
              nmCount = nmCount+1;
            ]]>
            </script>
            <if cond="errorCount==3">
                <throw eventexpr="'NoMatch'+errorCount"/>
            <else/>
                <throw eventexpr="'NoMatch'+nmCount"/>
            </if>
        </nomatch>
        
        <catch event="NoInput1"> 
            <prompt>
                <audio src="<%=audioDir%>/MM-Noinput-1.wav">
                    Just say the person or department you wanna call, or say company info.
                </audio>
            </prompt>
        </catch>
        <catch event="NoInput2"> 
            <prompt>
                <audio src="<%=audioDir%>/MM-Noinput-2.wav">
                    I'm sorry, I didn't hear you. Please say a person's name or one of these departments:
                    Support, Sales, Engineering, NetOps, Finance, or Front Desk. You can also enter an extension.
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoInput3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** NOMATCH EVENTS *** -->
        <catch event="NoMatch1"> 
            <prompt>
                <audio src="<%=audioDir%>/MM-Voice-Nomatch-1.wav">
                    Sorry, I didn't get that. Just say a person or department, or say company info. You can also enter an extension.
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoMatch2"> 
            <prompt>
                <audio src="<%=audioDir%>/MM-Voice-Nomatch-2.wav">
                    I'm sorry, I still didn't get that. Please say a person's name or one of these departments: 
                    Support, Sales, Engineering, NetOps, Finance, or Front Desk.
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoMatch3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** HELP *** -->
        <help>
            <prompt>
                <audio src="<%=audioDir%>/MM-Help.wav">
                    Here's some help. Just say the first and last name of the person
                    you wanna call, or say the name of a department, like Sales. 
                    You can also say company info or front desk, or enter an extension.
                </audio>
            </prompt>
        </help>
        
        <filled>
            <var name="MyArray" expr="lastresult$"/>
            <assign name="isDtmf" expr="MyArray[0].inputmode"/>
            <log expr="'LENGTH = ' + MyArray.length"/>
            <log expr="'****LAST RESULT = ' + MyArray[0].interpretation.mainmenu"/>
            
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
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.MainMenu + ';'
                + MyArray[2].interpretation.mainmenu + ';'"/>
                
                <assign name="confirmation" expr="'0'"/>
                
            <elseif cond="MyArray.length= 4"/>
                <assign name="nbestConf" expr="MyArray[0].confidence + ':'
                + MyArray[0].interpretation.mainmenu + '(' + MyArray[0].confidence + ');'  
                + MyArray[1].interpretation.mainmenu + '(' + MyArray[1].confidence + ');'
                + MyArray[2].interpretation.mainmenu + '(' + MyArray[2].confidence + ');
                + MyArray[3].interpretation.mainmenu + '(' + MyArray[3].confidence + ');'"/>
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.MainMenu + ';'
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
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.MainMenu + ';'
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
                
                <assign name="nbest" expr="MyArray[0].interpretation.mainmenu + ';'  				     				           + MyArray[1].interpretation.MainMenu + ';'
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
        
        <if cond="mainmenu == 'companyinfo'">
            <goto next="#CompanyInfo"/>
            
        <elseif cond="mainmenu == 'listentities'"/>
            <goto next="<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowEntities") %>"
                  fetchaudio="<%=voxfxAudioDir%>/Pause1.wav"/>
            
        <elseif cond="mainmenu == 'directions'"/>
            <goto next="<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowDirections") %>"
                  fetchaudio="<%=voxfxAudioDir%>/Pause1.wav"/>
            
        <elseif cond="nbest == ''"/>
            <goto nextitem="mainmenu"/>
            
        <!-- they have either an entity or contact match... -->
        <else/>
            
            <log expr="' ******ENTITY OR CONTACT MATCH ****** '"/>
            <submit expr="'<%=response.encodeURL(appDir+"/MainController?reqState=vxmlShowConfirmCall&amp;confirmation=")%>' + confirmation"
                    fetchaudio="<%=voxfxAudioDir%>/Pause1.wav" method="post" namelist="nbest nbestConf isDtmf callerId"/>
            
        </if>
    </block>
</form>

<form id="CompanyInfo">
    <block>
    <assign name="playedCompanyInfo" expr="true"/>
    <prompt>
        <audio src="<%=audioDir%>/Co_Info_Main.wav">
            OK, Vox Attendant is located on the web at vox attendant dot org.  Vox attendant is part of rocket source dot org.  
            From your browser, just type H T T P : vox attendant dot org or H T T P : rocket source dot org.
        </audio>
        <break time="2000"/>
    </prompt>
    <if cond="!playedRepeat">
        <goto next="#Repeat"/>
    <else/>
        <goto next="#MainMenu"/>
    </if>
    </block>
</form>

<form id="Repeat">
    <field name="confirm" >
        <grammar src="<%=grammarDir%>/Yes_No.xml" type="application/grammar-xml"/> 
        <prompt>
            <audio src="<%=audioDir%>/Co_Info_Repeat_Main.wav">
                Do you need to hear that again?
            </audio>
        </prompt>
        <filled>
            <assign name="playedRepeat" expr="true"/>
            <if cond="confirm=='yes'">
                <goto next="#CompanyInfo"/>
            <else/>
                <goto next="#MainMenu"/>
            </if>
        </filled>
        
        <!--***  NOINPUT EVENTS *** -->
        <noinput count="1">
            <prompt>
                <audio src="<%=audioDir%>/Co_Info_Repeat_NoInput-1.wav">
                    Would you like to hear that again? Say yes or no.
                </audio>
            </prompt>
        </noinput>
        <noinput count="2">
            <prompt>
                <audio src="<%=audioDir%>/Co_Info_Repeat_NoInput-2.wav">
                    Sorry, I didn't hear you. Do you need to hear our company info again? 
                    Press 1 for yes or 2 for no.
                </audio>
            </prompt>
        </noinput>
        
        <!-- *** NOMATCH EVENTS *** -->
        <nomatch count="1">
            <prompt>
                <audio src="<%=audioDir%>/Co_Info_Repeat_Nomatch-1.wav">
                    Sorry, I didn't get that. Do you need the company info again? Just say yes or no. 
                </audio>
            </prompt>
        </nomatch>
        <nomatch count="2">
            <prompt>
                <audio src="<%=audioDir%>/Repeat.wav">
                    I'm sorry, I still didn't get that. Would you like to hear 
                    the company info again? Press 1 for yes or 2 for no.
                </audio>
            </prompt>
        </nomatch>
        
        <!-- *** HELP EVENTS *** -->
        <help>
            <prompt>
                <audio src="<%=audioDir%>/Co_Info_Repeat-Help.wav">
                    Here's some help. Would you like to hear the company info again? 
                    Just say yes or no. Or press 0 if you'd like to talk to someone at the front desk.
                </audio>
            </prompt>
        </help>
    </field>
</form>
</vxml>
