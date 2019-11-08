import fileinput
import os
import shlex
import subprocess

OLD_SEED_MODULE = "Android-Seed"
APP_BUILD_GRADLE = "/app/build.gradle"
APP_NAME_CHANGE = "APP_NAME_CHANGE"
KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD"
KEYSTORE_ALIAS = "KEYSTORE_ALIAS"
PACKAGE_NAME = "dk.adaptmobile.android_seed"
APP_JAVA_PATH = "/app/src/main/java"


def find_and_replace(new, old, file_name):
    with fileinput.FileInput(file_name, inplace=True) as file:
        for line in file:
            print(line.replace(old, new), end='')


def generate_keystore(alias, password):
    cmd = f'keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 16384 -validity 10000 -alias {alias} -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" -storepass {password} -keypass {password}'
    args = shlex.split(cmd)
    subprocess.Popen(args)


def rename_package_dirs():
    package_name = input("Enter new package name like -> dk.adaptmobile.android_seed: ")

    os.chdir(root_module + APP_JAVA_PATH)

    old_package_name_split = PACKAGE_NAME.split(".")
    new_package_name_split = package_name.split(".")

    for i in range(len(old_package_name_split)):
        os.rename(old_package_name_split[i], new_package_name_split[i])
        os.chdir(new_package_name_split[i])


def rename_app_name():
    app_name = input("What's the app name: ")

    find_and_replace(new=app_name, old=APP_NAME_CHANGE, file_name=root_module + APP_BUILD_GRADLE)


if __name__ == '__main__':
    print("The dir is: %s" % os.listdir(os.getcwd()))

    root_module = input("Name root module: ")

    os.rename("Android-Seed", root_module)

    print("Renamed root module")

    # rename_app_name()

    os.chdir("launch")
    alias = input("Alias for the keystore: ")
    password = input("Password for the keystore: ")
    generate_keystore(alias, password)
    os.chdir("..")
    
    find_and_replace(new=alias, old=KEYSTORE_ALIAS, file_name=root_module + APP_BUILD_GRADLE)
    find_and_replace(new=password, old=KEYSTORE_PASSWORD, file_name=root_module + APP_BUILD_GRADLE)


    # rename_package_dirs()
