#!/bin/sh

#######################################################################
# This script will fetch the latest AppDynamics Sun/Oracle Java agent #
# and place it in /opt/appdynamics/java.                              #
# This script requires:                                               #
# curl                                                                #
# unzip       							      #
# jq 								      #
#######################################################################

# Download Root URL
DOWNLOAD_PATH=https://download-files.appdynamics.com/

# Fetch latest Sun Java Agent download path from AppD
FILE_PATH=$(curl https://download.appdynamics.com/download/downloadfilelatest/ | jq -r '.[].s3_path' | grep java-jdk8)

# Construct the full URL
DOWNLOAD_PATH=$DOWNLOAD_PATH$FILE_PATH

# Print URL to stdout
echo "Downloading agent from: " $DOWNLOAD_PATH

# Fetch agent and write into working directory
curl -L $DOWNLOAD_PATH -o ./JavaAgent.zip

# Create directory in which to put the agent bits
mkdir -p /opt/appdynamics/java

# Unzip the agent
unzip ./JavaAgent.zip -d /opt/appdynamics/java

# Remove the zip
rm -f ./JavaAgent.zip
