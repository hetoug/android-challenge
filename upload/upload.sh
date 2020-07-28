
#!/bin/sh
#./upload/upload.sh -apk {APK_PATH} -env {DEV|PROD|TEST} -m Test message

BASE_URL="https://test-box-am.appspot.com"
BUILD_TYPE=""
function jsonval {
    temp=`echo $json | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $key`
    echo ${temp##*|}
}

function getUploadUrl {
  json=`curl -i -H "Accept: application/json" -H "Content-Type: application/json" -H "AUTH_TOKEN: 2YPAqaWwcYmd62AbbcH3sf5w" -H "X_TOKEN: ehgr9evxjv3f" $1/api/v1/auth/upload/get-build-url`
  key='url'
  upload_url=`jsonval`
}

function uploadBuild {
  echo "Getting package name from provided apk file..."
  if hash aapt 2>/dev/null; then
        PACKAGE_NAME=$(aapt dump badging "$2" | awk -v FS="'" '/package: name=/{print $2}')
        VERSION=$(aapt dump badging "$2" | awk -v FS="'" '/versionName=/{print $6}')
        BUILD_NUMBER=$(aapt dump badging "$2" | awk -v FS="'" '/versionCode=/{print $4}')
    else
        echo "Fallback to local aapt"
        PACKAGE_NAME=$(./upload/aapt dump badging "$2" | awk -v FS="'" '/package: name=/{print $2}')
        VERSION=$(./upload/aapt dump badging "$2" | awk -v FS="'" '/versionName=/{print $6}')
        BUILD_NUMBER=$(./upload/aapt dump badging "$2" | awk -v FS="'" '/versionCode=/{print $4}')
    fi
    #Output info

    echo "PACKAGE_NAME: ${PACKAGE_NAME}"
    echo "VERSION: ${VERSION}"
    echo "BUILD_NUMBER: ${BUILD_NUMBER}"

  echo "Uploading file..."
  UPLOAD=$(curl -i -H "Content-Type: multipart/form-data" -H "AUTH_TOKEN: 2YPAqaWwcYmd62AbbcH3sf5w" -H "X_TOKEN: ehgr9evxjv3f" -X POST -F "file=@$2;type=application/vnd.android.package-archive" --form "version=$VERSION" --form "build_number=$BUILD_NUMBER" --form "type=$3" --form "note=$COMMENT" --form "package_name=$PACKAGE_NAME" $1)
  echo "Upload has finished"
  echo "$UPLOAD"
}



set -e #Allow script to exit
while [[ $# > 1 ]] #Read input
do
    key="$1"
    case $key in
        -apk)
        APK_PATH="$2"
        shift # past argument
        ;;
        -m|--msg)
        COMMENT="$2"
        shift # past argument
        ;;
        -env|--environment)
        ENVIRONMENT="$2"
        shift # past argument
        ;;
        *)
            echo Unknown parameter "${key}"
            exit 1
        ;;
    esac
    shift # past argument or value
done

if [[ -z "$APK_PATH" ]]; then #Check if environment is set
    echo "Apk path argument is missing (-apk)"
    exit 1
fi

if [[ -z "$ENVIRONMENT" ]]; then #Check if environment is set
    echo "No environment (-env) chosen"
    exit 1
fi

getUploadUrl $BASE_URL
uploadBuild $upload_url $APK_PATH $ENVIRONMENT