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
   String confirmation = (String)request.getAttribute("confirmation");
   boolean explicitConfirmation = confirmation.equals("1");
   String finalURL= null;
%>


   <var name="phoneNumber"/>
<%
   if(explicitConfirmation) {
%>
<form id="confirmation">
   <field name="selection" slot="confirm">
      <grammar src="<%= grammarDir %>/Yes_No.xml" type="application/grammar-xml"/>

      <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>

      <prompt timeout="<%=timeout%>s">
         <audio src="<%=audioDir%>/111.wav">
         I heard you say....
         </audio>
         <audio src="<%=contactAudioDir%>/<%= calleeBean.getNameAudio() %>">
         <%= calleeBean.getName() %>
         </audio>
         <audio  src="<%=audioDir%>/112.wav">
         Did I get that right?
         </audio>
         <audio src="<%=audioDir%>/voxfx/waiting3_0.wav"/>
      </prompt>
      <%
         promptSet = audioDir + "/044.wav," +  voxfxAudioDir + "/silence1_0.wav;" +
                      audioDir + "/046.wav," + audioDir + "/191.wav";
      %>
      <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL("MainController?reqState=vxmlShowOperator") %>"/>
      <%
         promptSet = audioDir + "/041.wav," + voxfxAudioDir + "/silence1_0.wav;" +
                      audioDir + "/043.wav," + audioDir + "/191.wav";
      %>
      <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="true"
            finalURI="<%= response.encodeURL("MainController?reqState=vxmlShowOperator") %>"/>
      <%
         promptSet = voxfxAudioDir + "/helpin.wav," +
                     audioDir + "/121.wav," +
                     voxfxAudioDir + "/helpout.wav," +
                     voxfxAudioDir + "/silence1_0.wav";
      %>
      <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
      <%
         finalURL = response.encodeURL("MainController?reqState=vxmlProcessSkipName");
         finalURL += "&amp;skip=" + activeId;
      %>
      <attendant:vxmlEvent
            type="cancel"
            promptSet="<%=audioDir+"/113.wav"%>"
            reprompt="false"
            finalURI="<%=finalURL%>"/>

      <filled>
         <if cond="selection == 'yes'">
            <goto next="#choosePhoneType"/>
         <else/>
            <throw event="cancel"/>
         </if>
      </filled>
   </field>
</form>
<%
   }
%>
<form id="choosePhoneType">
<%
   if(calleeBean.hasMultiplePhoneNumbers()) {
%>
   <field name="selection" slot="phonetype">
      <grammar src="<%= grammarDir %>/PhoneType.xml" type="application/grammar-xml"/>

      <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="3"/>

      <prompt timeout="3s">
         <audio src="<%=audioDir%>/131.wav">
         office or cell phone?
         </audio>
      </prompt>
      <%
         promptSet = audioDir + "/044.wav," + audioDir + "/142.wav;" +
                      audioDir + "/046.wav," + audioDir + "/191.wav";
      %>
      <attendant:vxmlEvent
            type="noinput"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL("MainController?reqState=vxmlShowOperator") %>"/>
      <%
         promptSet = audioDir + "/041.wav,"  + audioDir + "/142.wav;" +
                      audioDir + "/043.wav," + audioDir + "/191.wav";
      %>
      <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL("MainController?reqState=vxmlShowOperator") %>"/>
      <%
         promptSet = voxfxAudioDir + "/helpin.wav," +
                     audioDir + "/141.wav," + audioDir + "/142.wav," +
                     voxfxAudioDir + "/helpout.wav";

      %>
      <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
      <%
         finalURL = response.encodeURL("MainController?reqState=vxmlProcessSkipName");
         finalURL += "&amp;skip=" + activeId;
      %>
      <attendant:vxmlEvent
            type="cancel"
            promptSet="<%=audioDir+"/113.wav"%>"
            reprompt="false"
            finalURI="<%=finalURL%>"/>

      <filled>
         <if cond="selection == 'office'">
            <assign name="phoneNumber" expr="<%= calleeBean.getWorkPhoneNumber() %>"/>
            <goto next="#lastChance"/>

         <elseif cond="selection == 'cell'"/>
            <assign name="phoneNumber" expr="<%= calleeBean.getMobilePhoneNumber() %>"/>
            <goto next="#lastChance"/>

         <else/>
            <throw event="cancel"/>
         </if>
      </filled>
   </field>
<%
   } else {
%>
   <block>
      <assign name="phoneNumber" expr="'<%= calleeBean.getPhoneNumber() %>'"/>
      <goto next="#lastChance"/>
   </block>
<% } %>
</form>
			  
<form id="lastChance">
   <block>
      <audio src="<%=audioDir%>/151.wav">
      OK, I'll connect you with ...
      </audio>
      <audio src="<%=contactAudioDir%>/<%= calleeBean.getNameAudio() %>">
      <%= calleeBean.getName() %>
      </audio>
   </block>

   <field name="selection" slot="cancel">
      <grammar src="<%= grammarDir %>/Cancel.xml" type="application/grammar-xml" />

      <property name="com.nuance.core.client.NoSpeechTimeoutSecs" value="<%=timeout%>"/>

      <prompt timeout="1.5s">
         <audio src="<%=audioDir%>/154.wav">
         Say "cancel" to stop this call.
         </audio>
      </prompt>
      <attendant:vxmlEvent
            type="noinput"
            reprompt="false"
            finalURI="#makeCall"/>
      <%
         promptSet = audioDir + "/161.wav," + contactAudioDir + "/" + calleeBean.getNameAudio() + "," + voxfxAudioDir + "/waiting4_0.wav;" +
                      audioDir + "/162.wav," + voxfxAudioDir + "/waiting4_0.wav;" +
                      audioDir + "/163.wav";
      %>
      <attendant:vxmlEvent
            type="nomatch"
            promptSet="<%=promptSet%>"
            reprompt="false"
            finalURI="<%= response.encodeURL("MainController?reqState=vxmlShowMainMenu") %>"/>
      <%
         promptSet = voxfxAudioDir + "/helpin.wav," +
                     audioDir + "/167.wav," +
                     contactAudioDir + "/" + calleeBean.getNameAudio() + "," +
                     audioDir + "/168.wav," +
                     voxfxAudioDir + "/helpout.wav," +
                     voxfxAudioDir + "/silence1_0.wav";
      %>
      <attendant:vxmlEvent
            type="help"
            promptSet="<%=promptSet%>"
            reprompt="false"/>
      <%
         finalURL = response.encodeURL("MainController?reqState=vxmlProcessSkipName");
         finalURL += "&amp;skip=" + activeId;
      %>
      <attendant:vxmlEvent
            type="cancel"
            promptSet="<%=audioDir+"/153.wav"%>"
            reprompt="false"
            finalURI="<%=finalURL%>"/>

      <filled>
         <if cond="selection == 'connect'">
            <goto next="#makeCall"/>
         <else/>
            <throw event="cancel"/>
         </if>
      </filled>
   </field>
</form>

<form id="makeCall">
   <block>
      <goto expr="'<%= response.encodeURL("MainController?reqState=vxmlShowMakeCall&amp;phoneNumber=")%>' + phoneNumber"/>
   </block>
</form>
</vxml>
