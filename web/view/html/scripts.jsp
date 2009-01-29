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
<!-- Begin scripts.jsp -->
<script LANGUAGE="JavaScript">
<!-- //
//
// Define Error Messages
//
ERROR_LOGIN = "Please enter a valid 4 digit passcode. ";
ERROR_PASSCODE_NON_BLANK = "The Passcode fields cannot be left blank. ";
ERROR_CONFIRM_PASSCODE = "The Confirm Passcode does not match the New Passcode. ";
ERROR_NAMES_NON_BLANK = "The First and Last name fields cannot be left blank. ";
ERROR_NAME_ONLY_LETTERS = "The Name field can only contain letters. ";
ERROR_NAMES_ONLY_LETTERS = "The First and Last name fields can only contain letters. ";
ERROR_PHONENUMBER_ONLY_DIGITS = "Please enter a valid 10 digit phone number, or a SIP address in this format: 'sip:youraddress'. ";
ERROR_PHONENUMBERS_ONLY_DIGITS = "Please enter a valid 10 digit work number, or a SIP address in this format: 'sip:youraddress'. ";
ERROR_CELLPHONE_ONLY_DIGITS = "Please enter a valid 10 digit cell phone number, or a SIP address in this format: 'sip:youraddress'. ";
ERROR_WORKPHONE_ONLY_DIGITS = "Please enter a valid 10 digit work phone number, or a SIP address in this format: 'sip:youraddress'. ";
ERROR_PHONE_NON_0_1 = "The number cannot begin with 0 or 1. ";
ERROR_REQUIRED_FIELDS = "The Name and Phone Number fields are required. ";
ERROR_OPERATOR_NON_BLANK = "The Operator name cannot be left blank. ";
ERROR_GREETINGS = "At least one of the Greeting Prompts must be selected. ";

INFO_DELETE_CONTACT = "Are you sure you want to delete this contact? ";
INFO_DELETE_ENTITY = "Are you sure you want to delete this department/sub-department? "
//
// Check if the passcode is in valid form
//
function isValidPasscode(code) {
   if(code.length != 4)
      return false;
   dummy = Number(code);
   if (isNaN(dummy))
      return false;
   return true;
}
//
// The only valid format that we take is 10 digit string,
// anything else is invalid.
//
function isValidPhoneNumber(number) {
   
    if (number.toLowerCase().indexOf("sip:") != 0) {
        if(number.length != 10)
          return false;

       if((number.charAt(0) == "0") ||
          (number.charAt(0) == "1")) {
          return false;
       }

       dummy = Number(number);
       if (isNaN(dummy))
          return false;
    }
   
   return true;
}
//
// Trims the string
//
function trim(str) {
   if(str == "")
      return "";

   var indexA = -1;
   for(var i = 0; i < str.length; i++) {
      if(str.charAt(i) != " ") {
         indexA = i;
         break;
      }
   }
   if(indexA == -1)
      return "";
   var indexB = str.length;
   for(i = str.length - 1; i >= 0; i--) {
      if(str.charAt(i) != " ") {
         indexB = i;
         break;
      }
   }
   return str.substring(indexA, indexB + 1);
}
//
//
//
function validName(name) {
   var lower = name.toUpperCase();
   for(i = 0; i < lower.length; i ++) {
      if((lower.charCodeAt(i) != 32) &&
         ((lower.charCodeAt(i) < 65) || (lower.charCodeAt(i) > 90))) {
         return false;
      }
   }
   return true;
}
//
// For Menu navigational bar
//
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//  -->
</script>
<link rel="stylesheet" href="<%=view%>/html/attendant.css" type="text/css">
<!-- End scripts.jsp -->
