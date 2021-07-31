#!/usr/bin/env bash
set -e

# The default android version ENV is a mess on GH Actions, so just install it ourselves.
# Adapted workaround from https://github.com/actions/virtual-environments/issues/60

echo "Setting up Actions Android SDK"

echo "Installing sdkmanager"
echo "Download https://dl.google.com/android/repository/commandlinetools-mac-${ANDROID_SDK_TOOLS}.zip"
wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/commandlinetools-mac-${ANDROID_SDK_TOOLS}.zip
# unpacking into cmdline-tools/ because of https://issuetracker.google.com/issues/150942306
echo "Mkdir $ANDROID_HOME/cmdline-tools/"
mkdir -p $ANDROID_HOME/cmdline-tools/
echo "unzip files"
unzip -o android-sdk.zip -d $ANDROID_HOME/cmdline-tools
# rename folder to latest to match Android Studio/sdkmanager behavior
echo "move folder"
mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest

echo "Installing required Android tools"
echo "y" | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses
# without manually installing this the license check in AGP fails https://issuetracker.google.com/issues/160361319
echo "y" | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "patcher;v4"

echo "Done!"
