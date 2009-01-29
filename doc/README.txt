#Web Site
http://voxattendant.org
http://rocketsource.org

#About voxattendant 
voxattendant is a software package that uses VoiceXML, Java, and SQLite (or any other database) and JSP to create a system that will answer incoming calls, interact with caller to determine their desired contact and then route the caller to the appropriate contact phone number or sip address.

#Voxattendant offers the following features:
-Multiple Users 
-Multiple contacts per user 
-Multiple departments with individual contact information 
-Customizable greetings 
-Operator opt out 
-Complete web based configuration 

#How to Use voxattendant 
Step 1: Verify everything is working correctly by making a test call
1a. Open the SIP phone by clicking on Start > All Programs > Voxeo > SIP Phone.
1b. Dial the number sip:aa@127.0.0.1
1c. When prompted for a person or department to connect to say "Jon Stewart".


Step 2: Setting Up Additional Users
You're now ready to start inputting new users into the Auto Attendant!
2a. Open a web browser and go to the following URL: http://127.0.0.1:9991/vox-attendant/LoginController
2b. The login passcode is: 2001
2c. On the top menu bar, click the "Contacts" link.
2d. Under the top menu bar, click the "New Contact" link.
2e. Fill out all of the relevant information. Use the phone number "user@127.0.0.1:5070" if you want your local sip phone to be called.
2f. Click on the "Save" button.

Step 3: Setting Up Additional Departments
Departments can be used to help find a particular person or to help the caller differentiate between multiple people with similar sounding names.
3a. Open a web browser and go to the following URL: http://127.0.0.1:9991/vox-attendant/LoginController
3b. The login passcode is: 2001
3c. On the top menu bar, click the "Departments" link.
3d. Under the top menu bar, click the "New Department" link.
3e. Fill out all of the relevant information. Use the phone number "user@127.0.0.1:5070" if you want your local sip phone to be called.
3f. Click on the "Save" button.

Step 4: Setting Up Operators
Operators are the default contacts who are called when a caller is either having trouble using the system or requests a operator directly.
4a. Open a web browser and go to the following URL: http://127.0.0.1:9991/vox-attendant/LoginController
4b. The login passcode is: 2001
4c. On the top menu bar, click the "Settings" link.
4d. Next to where it says "Operator Settings" click on the "edit" link.
4e. Fill out the operator name and number. Use the phone number "user@127.0.0.1:5070" if you want your local sip phone to be called.
4f. Click on the "Save" button.
4g. Next select the default operator using the drop down menu.


#Below are some hints on configuration the application correctly.
Contact audio filenames - When you add or modify a contact, the audio filename should be in the form of First-Last.wav.  For instance Test User, would be Test-User.wav.  These files should be in the /audio/contact/ directory.

For Entity audio, just put the filename in exactly as they are recorded located in the /audio/entity/ directory.
So for "Engineering", it would be "OK-Engineering.wav".

