# The Official MHacks Android App
<a href="https://play.google.com/store/apps/details?id=org.mhacks.android&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img width="300" alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" /></a>

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

# Credits
We would like to thank the maintainers of the following for past and present version of the app:
- [Square OkHTTP](http://square.github.io/okhttp/ "Square OkHTTP")
- [Square Retrofit](http://square.github.io/retrofit/ "Square Retrofit")
- [MaterialDrawer](http://mikepenz.github.io/MaterialDrawer/ "Mike Penz's Material Drawer")
- [Parse Android](https://parse.com/docs/android_guide "Parse Android Developer Guide")
- [AndroidWeekView](https://github.com/alamkanak/Android-Week-View "Alam Kanak's AndroidWeekView")
- [Square Picasso](http://square.github.io/picasso/ "Square Picasso")
- [Google Guava](https://code.google.com/p/guava-libraries/ "Google Guava Libraries")
- [Firebase Android](https://www.firebase.com/docs/android/quickstart.html "Firebase Android Quick Start")


# License
The MIT License (MIT)

Copyright (c) 2016 MHacks

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
