# AdaptMobile Android Seed project
A good starting point for future Android apps!

## Requirements
Have python an brew installed on your mac.

For installing homebrew (remember to have xcode installed):

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

For installing the latest python 3:

```bash
brew install python3
```

## Pre-commit Installation
At the root of the project run this command:

```bash
brew list ktlint | brew upgrade ktlint || brew install ktlint && curl -s -L "https://raw.githubusercontent.com/adaptdk/am-ktlint-pre-commit/master/pre-commit" > ../.git/hooks/pre-commit && chmod 755 ../.git/hooks/pre-commit && curl -s -L "https://raw.githubusercontent.com/adaptdk/am-ktlint-editorconfig/master/.editorconfig" > .editorconfig
```

This will add a pre-commit executable and a .editorconfig for the teams personalized rules. 
If .editorconfig needs to have rules changed the please change it in [am-ktlint-editorconfig](https://github.com/adaptdk/am-ktlint-editorconfig/blob/master/.editorconfig) to have the rules implemented in existing projects that has the pre-commit.

This will have the code style streamlined in our kotlin projects. 

## Setup
First create a new keystore and place it in the launch folder. You can create a new keystore in Android studio or by running:
```
keytool -genkey -v -keystore app-name.keystore -alias alias_name -keyalg RSA -keysize 40964 -validity 10000
```
Remember to change the app-name to the actual app name. Then fillout the passwords in the app.gradle file.



