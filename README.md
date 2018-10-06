# The Official MHacks Android App
<a href="https://play.google.com/store/apps/details?id=org.mhacks.app"><img width="300" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" /></a>

# About
The goal of this project is to provide a modular and open source Android client for hackathons of all sizes. Feel free to fork this project and deploy this client for your event.

If you use our code, please link back to this GitHub page and shoot us a line. We'd love to see what you're working on!

# Building
The project has been built with Android Studio, and consequently uses the Gradle build system.
If you have used Gradle before, great! If you haven't, you might want to take a look at some guides online.

To build the project for debug, you should only need to import it into Android studio. You may also build it from the command line, using:

`$ ./gradlew clean assembleDebug`

If you plan on building this project for release, you'll need to add a `signingConfig` block to app/build.gradle with your keys. This will depend on how you plan on signing it.

**NOTE: You may need to clean your project and sync Gradle files before your first run.** `Build > Clean project`

### Prerequisites

To build this project, there are several requirements. 

1. `google-services.json`

   This file is needed to use Google Play Services. More specifically, for Google Maps integration, you must register on Google's [website](https://developers.google.com/maps/documentation/android-sdk/intro) to receive this key

2. `gradle.properties`

   This file contains API keys that are used throughout the app. This is not added to the project for security reasons. Note that there are not quotation marks around MHACKS_GOOGLE_MAPS_API_KEY and there are double quotation marks around GCM_SERVER_ID.

3. ```groovy
   MHACKS_GOOGLE_MAPS_API_KEY=examplekey
   GCM_SERVER_ID="example_key"
   
   // This is needed to transform App Compat libraries in third
   // party libraries to be compat with the newer AndroidX 
   // packages.
   android.useAndroidX = true
   android.enableJetifier = true
   
   // Needed to use data binding for feature modules.
   android.enableExperimentalFeatureDatabinding = true
   ```

4. Android Studio 3.2

   This project uses tools available on a recent version of Android Studio. Older versions of the IDE may not support some aspects of the development.

# Goals

* Demonstrate a good understanding of software architecture and tooling that are important in maintaining a modern codebase.
* Uses MVVM, in the form of Architecture Components ViewModel,  as a solution for seperation of concerns and implementing [clean architecture](https://proandroiddev.com/a-guided-tour-inside-a-clean-architecture-code-base-48bb5cc9fc97).
* Conforming to Material Design standards in creating a predictable and intuitive UI.
* Sensible file and project organization.
* Demonstrate the use of common Android libraries used commonly for modern codebases.
* Use dependency injection with Dagger 2 to produce decoupled objects and services, reduces the amount of boilerplate.
* Use Data Binding to bind UI in a declarative method.

# Screenshots

<table>
    <tr>
        <td><img style="width: 350px: height:auto; margin: 0 50px" src="/screenshots/horizontal.png"></img></td>
        <td><img src="/screenshots/vertical_liked.png"></img></td></img></td>
    </tr>
        <tr>
        <td align="center"><b>Welcome Screen / Timer<b></td>
        <td align="center"><b>Ticket</b></img></td>
    </tr>
</table>

# Credits

We would like to thank the maintainers of the following for past and present version of the app:
- [Square OkHTTP](http://square.github.io/okhttp/ "Square OkHTTP")
- [Square Retrofit](http://square.github.io/retrofit/ "Square Retrofit")
- [Square Moshi](https://github.com/square/moshi)
- [Dagger 2](https://google.github.io/dagger/)
- [RxJava 2](https://github.com/ReactiveX/RxJava)
- [Architecture Components](https://developer.android.com/topic/libraries/architecture/)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP)
- [barcodescanner](https://github.com/dm77/barcodescanner)
- [ArcLayout](https://github.com/florent37/ArcLayout)


# License
The MIT License (MIT)

Copyright (c) 2018 MHacks

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
