#! /bin/sh

APK_PATH=$1
ANDROID_JARS=`cat android_platforms.config`

java -Xmx8G -jar CiD.jar $APK_PATH $ANDROID_JARS