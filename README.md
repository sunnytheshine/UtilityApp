# Utility App
The Objective of this app is to help users to use their daily utilities like selfie, calculator, maps and they can also create shortcut of their frequently used apps.

Users can also connect to their Flickr account using oauth authentication.

App will run offline but to use Flickr internet is needed.
 
# Devices Used for testing (Emulators): 
 1. Pixel (Api 26) <br />
 2. Nexus 5X (Api 25) <br />
 3. Nexus 5X 2 (Api 26) <br />
 4. Nexus 6 (Api 23) <br />

# Permissions required:
 1. Camera <br />
 2. Write External Drive <br />
 3. Read External Drive <br />
 4. Android special permission to run app (ACTION_MANAGE_OVERLAY_PERMISSION) <br /> 

# Test Cases:
1. Running app for first time require to add permissions and then run the app again. <br />
2. The circle pointer can be placed anywhere on screen, On touching it will open the controller. <br /> 
3. Long touch will hide the pointer to notification, User can bring it back by tapping on the notification. <br />
4. Map icon on controller will open googlemaps app. <br />
5. Calculator icon will open calculator. <br />
6. Torch icon will open flashlight if device does not have LED it will open a white screen (Activity). <br />
7. Home icon will take user to dashboard. <br />
8. More button (one with 3 dots) will open new controller, clicking on setting will open list of installed apps. <br />
9. User can select apps he/she wants to create shortcut of. <br />
10. Click on app icon on small circle will open the app. <br />
11. Flickr button on Big Circle will open the Flickr login page. <br />
12. After oauth user will see the list of images from flickr. <br />
13. Once Oauth is completed user will not see the login button. <br />

Note:- App still have some bugs. Improvement, move big circle config from file to database. UI of Flickr photos list.
