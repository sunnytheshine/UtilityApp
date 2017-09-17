# Utility App
The Objective of this app is to help users to use their daily utilities like selfie, calculator, maps and they can also create shortcut of their frequently used apps.

Users can also connect to their Flickr account using oauth authentication.

App will run offline but to use Flickr internet is needed.
 
# Devices Used for testing (Emulators): 
Pixel (Api 26)
Nexus 5X (Api 25)
Nexus 5X 2 (Api 26)
Nexus 6 (Api 23)

# Permissions required:
Camera 
Write External Drive
Read External Drive
Android special permission to run app (ACTION_MANAGE_OVERLAY_PERMISSION)

# Test Cases:
Running app for first time require to add permissions and then run the app again.
The circle pointer can be placed anywhere on screen, On touching it will open the controller.
Long touch will hide the pointer to notification, User can bring it back by tapping on the notification.
Map icon on controller will open googlemaps app.
Calculator icon will open calculator
Torch icon will open flashlight if device does not have LED it will open a white screen (Activity).
Home icon will take user to dashboard.
More button (one with 3 dots) will open new controller, clicking on setting will open list of installed apps.
User can select apps he/she wants to create shortcut of.
Click on app icon on small circle will open the app.
Flickr button on Big Circle will open the Flickr login page.
After oauth user will see the list of images from flickr 
Once Oauth is completed user will not see the login button.

Note:- App still have some bugs. Improvement, move big circle config from file to database. UI of Flickr photos list.
