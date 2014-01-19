AppSpecificOrientation
======================

*Android app controlling the orientation of each app*



<dl>
<dt>ABOUT</dt>
</dl>
Rotation manager is a tool that lets you choose which application you want to rotate or which one you don't want to rotate. It is a really simple app designed for the least possible power and RAM consumption.

<dl>
<dt>HOW TO USE</dt>
</dl>
The main button on top of the screen is the global orientation for all the apps. It is a 4-state button and gives you all the orientation possibilities. Then you can select from the list for every app your desired orientation.
Your selection will override the default global orientation. If you want only two apps to rotate and all the others to stay at their default, just disable global orientation and then select both Portrait and Landscape for the specific app in the list.
After you have selected your desired apps, just click on play and your preferences will be in effect.
Also, there is an option in the settings tab where you can set the app to start on boot. If you select it, then every time your phone boots it will remember your configurations and the app will run automatically.
"Chuck Norris mode" enables an Invisible permanent notification which ensures that the service will not be killed by the OS. Enable it if you have low RAM problems and the orientation service stops.

Check out a review about our app at techcookies.net!!
http://techcookies.net/rotation-manager-control-which-apps-rotate/



<dl>
<dt>PERMISSIONS</dt>
</dl>
These are the permissions are needed by the app to work:
android.permission.GET_TASKS : In order to retrieve the currently running app.
android.permission.CHANGE_CONFIGURATION : In order to change the orientation.
android.permission.WRITE_SETTINGS : In order to change the global orientation settings.
android.permission.RECEIVE_BOOT_COMPLETED : In order to start on boot.
android.permission.SYSTEM_ALERT_WINDOW : In order to change the foreground orientation
android.permission.VIBRATE : In order to detonate your phone...

These permissions are needed for the ads in the "Donate" section:
android.permission.INTERNET
android.permission.WRITE_EXTERNAL_STORAGE
android.permission.ACCESS_NETWORK_STATE
android.permission.ACCESS_WIFI_STATE
android.permission.READ_PHONE_STATE

<dl>
<dt>BUGS</dt>
</dl>
Please write your phone model and Android version when reporting a bug.
- Upon update you need to stop and restart the service
- ...waiting for you...

<dl>
<dt>CHANGELOG</dt>
</dl>

v2.0_070114
+ Added Forced Landscape and Forced Portrait
+ Added alarm for KitKat bug (onTaskRemoved())
+ Small RAM optimizations
+ Updated Gradle
+ Updated translations
+ Optimized Performance
+ Changed instructions and about
+ Added link to our apps in Play Store
+ Fixed donation codes


<dl>
<dt>SITE FOR TRANSLATION UPLOADS</dt>
</dl>
*Help with the translation of the app.*

http://crowdin.net/project/rotation-manager/invite

<dl>
<dt>DOWNLOAD FROM GOOGLE PLAY</dt>
</dl>
https://play.google.com/store/apps/details?id=com.spydiko.rotationmanager
