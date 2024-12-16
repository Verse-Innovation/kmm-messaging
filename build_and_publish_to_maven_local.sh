#!/bin/bash

SECONDS=0

####################################################################################################
###########################################  Clean  ################################################
####################################################################################################

./gradlew clean

####################################################################################################
##########################################  Build  #################################################
####################################################################################################

./gradlew :libraries:android-messaging:build
#./gradlew :libraries:android-messaging:android-messaging-test:build

./gradlew :libraries:cloud-messaging:build
#./gradlew :libraries:cloud-messaging:cloud-messaging-test:build

./gradlew :libraries:core-messaging:build
#./gradlew :libraries:core-messaging:core-messaging-test:build

#./gradlew :libraries:inapp-messaging:build
#./gradlew :libraries:inapp-messaging:inapp-messaging-test:build


####################################################################################################
####################################  Publish to maven local  ######################################
####################################################################################################

./gradlew :libraries:android-messaging:publishToMavenLocal
#./gradlew :libraries:android-messaging:android-messaging-test:publishToMavenLocal

./gradlew :libraries:cloud-messaging:publishToMavenLocal
#./gradlew :libraries:cloud-messaging:cloud-messaging-test:publishToMavenLocal

./gradlew :libraries:core-messaging:publishToMavenLocal
#./gradlew :libraries:core-messaging:core-messaging-test:publishToMavenLocal

#./gradlew :libraries:inapp-messaging:publishToMavenLocal
#./gradlew :libraries:inapp-messaging:inapp-messaging-test:publishToMavenLocal

echo "total time taken $SECONDS"