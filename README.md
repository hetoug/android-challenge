# AdaptMobile Android Seed project

A good starting point for future Android apps!

## Requirements

Have bitrise, hub, python and homebrew installed on your mac.

For installing homebrew (remember to have xcode installed):

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

For installing the latest python 3:

```bash
brew install python3
```

```bash
brew install bitrise
```

```bash
brew install hub
```

## Clone and Setup

If you dont wan't to manually copy the git url and then type two commands, then just run this in your terminal.
It will clone the seed project and change directory into the project and run the `setup? python script.

```bash
git clone git@github.com:adaptdk/adaptmobile-android-seed.git && cd $(basename $_ .git) && python3 setup.py
```

## Setup (If you already have the project cloned)

Run the python script `setup.py` and follow the steps for setting up the project.

```bash
python3 setup.py
```

## Pre-commit Installation

At the root of the project run this command:

```bash
brew list ktlint | brew upgrade ktlint || brew install ktlint && curl -s -L "https://raw.githubusercontent.com/adaptdk/am-ktlint-pre-commit/master/pre-commit" > ../.git/hooks/pre-commit && chmod 755 ../.git/hooks/pre-commit && curl -s -L "https://raw.githubusercontent.com/adaptdk/am-ktlint-editorconfig/master/.editorconfig" > .editorconfig
```

This will add a pre-commit executable and a .editorconfig for the teams personalized rules. 
If .editorconfig needs to have rules changed the please change it in [am-ktlint-editorconfig](https://github.com/adaptdk/am-ktlint-editorconfig/blob/master/.editorconfig) to have the rules implemented in existing projects that has the pre-commit.

This will have the code style streamlined in our kotlin projects.
