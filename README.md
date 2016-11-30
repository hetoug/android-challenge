# AdaptMobile Android Seed project
A good starting point for future Android apps!

## Setup
First create a new keystore and place it in the launch folder. You can create a new keystore in Android studio or by running:
```
keytool -genkey -v -keystore app-name.keystore -alias alias_name -keyalg RSA -keysize 40964 -validity 10000
```
Remember to change the app-name to the actual app name. Then fillout the passwords in the app.gradle file.
