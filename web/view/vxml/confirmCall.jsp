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
CalleeBean calleeBean = (CalleeBean)request.getAttribute("calleeBean");
String activeId = (String)request.getAttribute("activeId");
String isDtmf = (String) request.getAttribute("isDtmf");
boolean offerHomeNumber = false;
String aniRecognized = (String) request.getSession().getAttribute("aniRecognized");
if (aniRecognized != null && aniRecognized.equals("true"))
{
    offerHomeNumber=true;
}

//This is used to drive audio, grammars, and TTS for the 8 permutations of 
//destinations:  work, home, mobile, voicemail where work is always required.
int destType = calleeBean.getDestinationsType(offerHomeNumber);
System.out.println("destType: " + destType);
//This adds the cancelled name to the skip list
String cancelURL = response.encodeURL(appDir+"/MainController?reqState=vxmlProcessSkipName");
cancelURL += "&amp;skip=" + activeId;

String voicemailUrl = (String) request.getAttribute("voicemailUrl");
String contactId = (String) request.getAttribute("contactId");

String aniNumber = calleeBean.getWorkPhoneNumber();
if (aniNumber == null || aniNumber.equals(""))
{
    aniNumber = calleeBean.getPhoneNumber();
}
%>

<var name="phoneNumber"/>
<var name="errorCount" expr="0"/>
<var name="niCount" expr="0"/>
<var name="nmCount" expr="0"/>
<var name="contactId" expr="'<%=contactId%>'"/>

<var name="useType" expr="true"/>
<var name="typeSelected" expr="''"/>

<form id="ChoosePhoneType">
    <%if (isDtmf != null && isDtmf.equals("dtmf") && calleeBean.getWorkPhoneNumber().length() > 0) {%>
        <block>
            <assign name="phoneNumber" expr="'<%= calleeBean.getWorkPhoneNumber() %>'"/>
            <assign name="useType" expr="false"/>
            <goto next="#LastChance"/>
        </block>
    <%} else if(destType == CalleeBean.DEST_WORK) {%>
        <block>
            <assign name="phoneNumber" expr="'<%= calleeBean.getWorkPhoneNumber() %>'"/>
            <assign name="useType" expr="false"/>
            <goto next="#LastChance"/>
        </block>
    <%} else if(calleeBean.hasMultiplePhoneNumbers()) {%>
    <field name="phonetype">
        <grammar src="<%= grammarDir %>/PhoneType<%=destType%>.xml?t=1" type="application/grammar-xml"/>
        <prompt timeout="3s">
            <audio src="<%=audioDir%>/OK.wav">
                OK,
            </audio>
            
            <audio src="<%=contactAudioDir%>/<%= calleeBean.getAudioName() %>">
                <%= calleeBean.getName() %>
                <break time="500"/>
            </audio>
            
            <audio src="<%=audioDir%>/<%=calleeBean.getMainOptionPrompt(destType)%>">
                <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                Work or home.
                <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                Work, home, or mobile.
                <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                Work or mobile.
                <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                Work or voicemail.
                <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                Work, home, or voicemail.
                <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                Work, home, mobile, or voicemail.
                <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                Work, mobile, or voicemail.
                <%}%>
            </audio>
        </prompt>
        
        <filled>
            <if cond="phonetype == 'work'">
                <assign name="typeSelected" expr="'work'"/>
                <assign name="phoneNumber" expr="'<%= calleeBean.getWorkPhoneNumber() %>'"/>
                <goto next="#LastChance"/>
                
            <elseif cond="phonetype == 'mobile'"/>
                <assign name="typeSelected" expr="'mobile'"/>
                <assign name="phoneNumber" expr="'<%= calleeBean.getMobilePhoneNumber() %>'"/>
                <goto next="#LastChance"/>
                
            <elseif cond="phonetype == 'home'"/>
                <assign name="typeSelected" expr="'home'"/>
                <assign name="phoneNumber" expr="'<%= calleeBean.getHomePhoneNumber() %>'"/>
                <goto next="#LastChance"/>
                
            <elseif cond="phonetype == 'voicemail'"/>
                <assign name="typeSelected" expr="'voicemail'"/>
                <assign name="phoneNumber" expr="'voicemail'"/>
                <goto next="#LastChance"/>
                
            <else/>
                <goto next="<%=cancelURL%>"/>
            </if>
        </filled>
        
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
        
        
        <!-- *** NOINPUT EVENTS *** -->
        <catch event="NoInput1"> 
            <prompt>
                <audio src="<%=audioDir%>/<%=calleeBean.getNoInput1Prompt(destType)%>">
                    <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                    Please say work or home.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                    Please say work, home or mobile.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                    Please say work or mobile.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                    Please say work or voicemail.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                    Please say work, home or voicemail.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                    Please say work, home, mobile, or voicemail.  You can also say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                    Please say work, mobile, or voicemail.  You can also say "go back".
                    <%}%>
                </audio>
            </prompt>
        </catch>
        <catch event="NoInput2"> 
            <prompt>
                <audio src="<%=audioDir%>/<%=calleeBean.getNoInput2Prompt(destType)%>">
                    <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone,
                    press 1.  Or for home, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone, press 1.
                    For home, press 2.  Or for mobile, press 3.
                    <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone,
                    press 1.  Or for mobile, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone,
                    press 1.  Or for voicemail, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone, press 1.
                    For home, press 2.  Or for voicemail, press 3.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone, press 1.
                    For home, press 2.  For mobile, press 3.  Or for voicemail, press 4.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                    Sorry, I didn't hear you.  To connect to this person's work phone, press 1.
                    For mobile, press 2.  Or for voicemail, press 3.
                    <%}%>
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoInput3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** NOMATCH EVENTS *** -->
        <catch event="NoMatch1"> 
            <prompt>
                <audio src="<%=audioDir%>/<%=calleeBean.getNoMatch1Prompt(destType)%>">
                    <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                    Sorry, I didn't get that.  Please say work or home.  Or
                    to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                    Sorry, I didn't get that.  Please say work, home or mobile.
                    Or to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                    Sorry, I didn't get that.  Please say work or mobile.  Or
                    to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                    Sorry, I didn't get that.  Please say work or voicemail.  Or
                    to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                    Sorry, I didn't get that.  Please say work, home or voicemail.
                    Or to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                    Sorry, I didn't get that.  Please say work, home, mobile, or voicemail.
                    Or to call someone else, say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                    Sorry, I didn't get that.  Please say work, mobile, or voicemail.
                    Or to call someone else, say "go back".
                    <%}%>
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoMatch2"> 
            <prompt>
                <audio src="<%=audioDir%>/<%=calleeBean.getNoMatch2Prompt(destType)%>">
                    <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                    I'm sorry, I still didn't get that.  To connect to this person's
                    work phone, press 1.  Or for home, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                    I'm sorry, I still didn't get that.  To connect to this person's work
                    phone, press 1.  For home, press 2.  Or for mobile, press 3.
                    <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                    I'm sorry, I still didn't get that.  To connect to this person's
                    work phone, press 1.  Or for mobile, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                    I'm sorry, I still didn't get that.  To connect to this person's
                    work phone, press 1.  Or for voicemail, press 2.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                    I'm sorry, I still didn't get that.  To connect to this person's work
                    phone, press 1.  For home, press 2.  Or for voicemail, press 3.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                    I'm sorry.  I still didn't get that.  To connect to this person's work phone,
                    press 1.  For home, press 2.  For mobile, press 3.  Or for voicemail, press 4.
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                    I'm sorry.  I still didn't get that.  To connect to this person's work phone,
                    press 1.  For mobile, press 2. Or for voicemail, press 3.
                    <%}%>
                </audio>
            </prompt>
        </catch>
        
        <catch event="NoMatch3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** HELP *** -->
        <help>
            <prompt>
                <audio src="<%=audioDir%>/<%=calleeBean.getHelpPrompt(destType)%>">
                    <% if (destType == CalleeBean.DEST_WORK) {%>
                        
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME) {%>
                    Here's some help.  I just need to know where to connect you.  You
                    can say work or press 1, or home or press 2.  If you want to 
                    call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_HOME_MOBILE) {%>
                    Here's some help.  I just need to know where to connect you.  You 
                    can say work or press 1, home or press 2, or mobile or press 3.
                    If you want to call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_MOBILE) {%>
                    Here's some help.  I just need to know where to connect you.  You
                    can say work or press 1, or mobile or press 2.  If you want to 
                    call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL) {%>
                    Here's some help.  I just need to know where to connect you.  You
                    can say work or press 1, or voicemail or press 2.  If you want to 
                    call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME) {%>
                    Here's some help.  I just need to know where to connect you.  You 
                    can say work or press 1, home or press 2, or voicemail or press 3.
                    If you want to call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_HOME_MOBILE) {%>
                    Here's some help.  I just need to know where to connect you.  You can
                    say work or press 1, home or press 2, mobile or press 3, or voicemail
                    or press 4.  If you want to call someone else, just say "go back".
                    <%} else if (destType == CalleeBean.DEST_WORK_VOICEMAIL_MOBILE) {%>
                    Here's some help.  I just need to know where to connect you.  You can
                    say work or press 1, mobile or press 2, or voicemail
                    or press 3.  If you want to call someone else, just say "go back".
                    <%}%>
                </audio>
            </prompt>
        </help>
    </field>
    <% } else { //Only 1 phone number%>
    <block>
        <assign name="phoneNumber" expr="'<%= calleeBean.getPhoneNumber() %>'"/>
        <assign name="useType" expr="false"/>
        <goto next="#LastChance"/>
    </block>
    <% } %>
</form>

<form id="LastChance">
    <block>
        <if cond="typeSelected == 'voicemail'">
            <prompt>
                <audio src="<%=contactAudioDir%>/VM-<%=calleeBean.getAudioName()%>">
                    Connecting to the voicemail for, <%= calleeBean.getName() %>.
                </audio>
            </prompt>
        <elseif cond="typeSelected == 'home'" />
            <if cond="useType">
                <prompt>
                    <audio src="<%=contactAudioDir%>/H-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>, at Home
                    </audio>
                </prompt>
            <else/>
                <prompt>
                    <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>
                    </audio>
                </prompt>
            </if>
       <elseif cond="typeSelected == 'mobile'" />
            <if cond="useType">
                <prompt>
                    <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>, 
                    </audio>
                    <audio src="<%=audioDir%>/Mobile-Phone.wav">
                        Mobile
                    </audio>
                </prompt>
            <else/>
                <prompt>
                    <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>
                    </audio>
                </prompt>
            </if>         
        <elseif cond="typeSelected == 'work'" />
            <if cond="useType">
                <prompt>
                    <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>, 
                    </audio>
                    <audio src="<%=audioDir%>/At-Work.wav">
                        At Work
                    </audio>
                </prompt>
            <else/>
                <prompt>
                    <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                        Connecting to <%= calleeBean.getName() %>
                    </audio>
                </prompt>
            </if>        
        <else/>
            <prompt>
                <% if (calleeBean.isEntity()) {%>
                <audio src="<%=entityAudioDir%>/<%=calleeBean.getAudioName()%>">
                    Connecting to <%= calleeBean.getName() %>
                </audio>
                <%} else {%>
                <audio src="<%=contactAudioDir%>/C-<%=calleeBean.getAudioName()%>">
                    Connecting to <%= calleeBean.getName() %>
                </audio>
                <%}%>
            </prompt>
        </if>
    </block>
    
    <field name="cancel">
        <grammar src="<%= grammarDir %>/Cancel.xml" type="application/grammar-xml" />
        <property name="nuance.core.client.NoSpeechTimeoutSecs" value="2.0s"/>
        
        <prompt timeout="2.0s">
            <% if (!calleeBean.isEntity()) {%>
            <audio src="<%=audioDir%>/Stop.wav">
                Say "stop" to cancel.
            </audio>
            <%}%>
        </prompt>
        <filled>
            <if cond="cancel == 'connect'">
                <goto next="#MakeCall"/>
            <else/>
                <audio src="<%=voxfxAudioDir%>/Stop-Tone.wav"/>
                <goto next="<%=cancelURL%>"/>
            </if>
        </filled>
        
        <noinput>
            <goto next="#MakeCall"/>
        </noinput>
        <nomatch>
            <goto next="#MakeCall"/>
        </nomatch>
    </field>
</form>

<form id="MakeCall">
    <block>
        <if cond="typeSelected == 'voicemail'">
            <goto next="#MakeVoicemailCall"/>
        <else/>
            <goto expr="'<%= response.encodeURL(appDir+"/MainController?reqState=vxmlShowMakeCall&amp;aniNumber=" + aniNumber + "&amp;phoneNumber=")%>' + phoneNumber"/>
        </if>

        
    </block>
</form>

<form id="MakeVoicemailCall">
    <block>
        <submit next="<%= voicemailUrl%>" namelist="contactId"/>
    </block>
</form>

</vxml>
