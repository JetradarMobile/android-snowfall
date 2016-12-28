Android-Snowfall
================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android--Snowfall-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/4952)
[![Release](https://jitpack.io/v/jetradarmobile/android-snowfall.svg)](https://jitpack.io/#jetradarmobile/android-snowfall)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)

Fully customizable implementation of "Snowfall View" on Android.

That's how we use it in our app "Hotellook"

![image](https://raw.githubusercontent.com/JetradarMobile/android-snowfall/master/art/hotellook-demo.gif)


Compatibility
-------------

This library is compatible from API 15 (Android 4.0.3).


Download
--------

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency

```groovy
dependencies {
    compile 'com.github.jetradarmobile:android-snowfall:1.1.3'
}
```


Usage
-----

Default implementation with round snowflakes:

```xml
<com.jetradarmobile.snowfall.SnowfallView
      android:layout_width="match_parent"
      android:layout_height="match_parent"/>
```

Fully customized implementation:

```xml
<com.jetradarmobile.snowfall.SnowfallView
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:snowflakesNum="250"
      app:snowflakeAlphaMin="150"
      app:snowflakeAlphaMax="255"
      app:snowflakeAngleMax="5"
      app:snowflakeSizeMin="8dp"
      app:snowflakeSizeMax="32dp"
      app:snowflakeSpeedMin="4"
      app:snowflakeSpeedMax="12"
      app:snowflakesFadingEnabled="true"
      app:snowflakesAlreadyFalling="false"
      app:snowflakeImage="@drawable/snowflake"/>
```


License
-------

    Copyright 2016 JetRadar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
