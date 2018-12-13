#!/bin/sh

set -e

# validated if created build is debug build 
cd $APK_PATH
EXPECTED_FLAG='application-debuggable'

for file in *
do 
  name=$(echo $file| tail -c 4)
  if [ $name = 'apk' ]
  then     
     echo "Found $file" 
     APK_NAME=$file
     break
  else 
     echo "Unable to find apk in given directory" 
  fi
done

DEBUGGABLE=`$ANDROID_HOME/build-tools/27.0.3/aapt dump badging $APK_NAME |grep $EXPECTED_FLAG`
if [ $? == 0 ]; then
   echo "The build is debuggable"
   exit 0
else
   echo "The build is not debuggable"
   exit 1
fi