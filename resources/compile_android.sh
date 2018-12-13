#!/bin/sh

set -e

# Clear old outputs if any 
rm -rf OpenEdXMobile/build/

# Compile app 
echo 'Compiling Android App'
./gradlew assembleProdDebuggable