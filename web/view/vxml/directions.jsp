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

<var name="playedDirections" expr="false"/>
<var name="playedRepeat" expr="false"/>

<var name="errorCount" expr="0"/>
<var name="niCount" expr="0"/>
<var name="nmCount" expr="0"/>


<form id="Directions">
    <block>
    <prompt>
        <audio src="<%=audioDir%>/Directions.wav">
            OK, The directions to the office go here. 
        </audio>
    </prompt>

    <if cond="!playedRepeat">
        <goto next="#Repeat"/>
    <else/>
        <goto next="<%=appDir%>/MainController?reqState=vxmlShowMain"/>
    </if>
    </block>
</form>

<form id="Repeat">
    
    <assign name="playedRepeat" expr="true"/>
    <field name="confirm" >
        <grammar src="<%=grammarDir%>/Yes_No.xml" type="application/grammar-xml"/> 
        <prompt>
            <audio src="<%=audioDir%>/Dir_Rep_Main.wav">
                Do you need to hear that again?
            </audio>
        </prompt>
        <filled>
            <if cond="confirm=='yes'">
                <goto next="#Directions"/>
            <else/>
                <goto next="<%=response.encodeURL(appDir + "/MainController?reqState=vxmlShowMainMenu")%>"/>
            </if>
        </filled>
        
        <!--***  NOINPUT EVENTS *** -->
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
                <audio src="<%=audioDir%>/Dir_Rep_Main_Noinput1.wav">
                    Would you like to hear that again? Say yes or no.
                </audio>
            </prompt>
        </catch>
        <catch event="NoInput2"> 
            <prompt>
                <audio src="<%=audioDir%>/Dir_Rep_Main_Noinput2.wav">
                    Sorry, I didn't hear you. Do you need to hear our company info again? 
                    Press 1 for yes or 2 for no.
                </audio>
            </prompt>
        </catch>
        <catch event="NoInput3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** NOMATCH EVENTS *** -->
        <catch event="NoMatch1"> 
            <prompt>
                <audio src="<%=audioDir%>/Dir_Rep_Main_Nomatch-1.wav">
                    Sorry, I didn't get that. Do you need the company info again? Just say yes or no. 
                </audio>
            </prompt>
        </catch>
        <catch event="NoMatch2"> 
            <prompt>
                <audio src="<%=audioDir%>/Dir_Rep_Main_Nomatch-2.wav">
                    I'm sorry, I still didn't get that. Would you like to hear 
                    the company info again? Press 1 for yes or 2 for no.
                </audio>
            </prompt>
        </catch>
        <catch event="NoMatch3"> 
            <goto next="<%=appDir%>/MainController?reqState=vxmlShowOperator"/>
        </catch>
        
        <!-- *** HELP EVENTS *** -->
        <help>
            <prompt>
                <audio src="<%=audioDir%>/Dir_Rep_Main_Help.wav">
                    Here's some help. Would you like to hear the driving directions again? 
                    Just say yes or no. Or press 0 if you'd like to talk to someone at the front desk.
                </audio>
            </prompt>
        </help>
    </field>
</form>

</vxml>
