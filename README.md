# CiD
Automating the Detection of API-related Compatibility Issues in Android Apps.

The Android Application Programming Interface provides the necessary building blocks for
app developers to harness the functionalities of the Android devices, including for interacting
with services and accessing hardware. This API thus evolves rapidly to meet new requirements for
security, performance and advanced features, creating a race for developers to update apps. Unfortunately,
given the extent of the API and the lack of automated alerts on important changes, Android apps are suffered from API-related compatibility issues.
These issues can manifest themselves as runtime crashes creating a poor
user-experience. We propose in this paper an automated approach named CiD for systematically modelling the
lifecycle of the Android APIs and analysing app bytecode to flag usages that can lead to potential compatibility issues. We demonstrate
the usefulness of CiD by helping developers repair their apps, and we validate that our tool outperforms the state-of-the-art
on benchmark apps that take into account several challenges for automatic detection.


# Quick Start

CiD takes as input two parameters which are summarized as follows:
* apkPath: A path points to an Android apk.
* androidJars: A path points to a directory containing a collection of Android jars, with each one represents each Android platform version.
Interested users can refer to the [android-platofrms] (https://github.com/lilicoding/android-platforms) project for possible samples.

Users can launch CiD through the following script:
```
java -Xmx8G -jar CiD.jar $apkPath $androidJars
```