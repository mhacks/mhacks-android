The Official MHacks Android App
===========================
Screenshots
-----
![](https://raw.githubusercontent.com/mhacks/MHacks-Android/master/mhacks_android_github_sc.png)

Building
-----

The project has been built with Android Studio, and consequently uses the Gradle build system.
If you have used Gradle before, great! If you haven't, you might want to take a look at some guides online.

Before you get started, you'll need your Parse App ID and Client Key. Add these to the Java properties file located in `~/.gradle/gradle.properties`. Until you add these, Gradle will complain that you haven't set the values.

To build the project for debug, you should only need to import it into Android studio. You may also build it from the command line, using:

`$ ./gradlew clean assembleDebug`

If you plan on building this project for release, you'll need to add a `signingConfig` block to app/build.gradle with your keys. This will depend on how you plan on signing it.


Credits
-----
We would like to thank the maintainers of the following:
- [Parse Android Library](https://parse.com/docs/android_guide "Parse Android Developer Guide")
- [Alam Kanak's AndroidWeekView Library](https://github.com/alamkanak/Android-Week-View "AndroidWeekView")
- [Square's Picasso Image Library](http://square.github.io/picasso/ "Picasso")
- [Guava Google Core Libraries](https://code.google.com/p/guava-libraries/ "guava-libraries")
- [Firebase Android Library](https://www.firebase.com/docs/android/quickstart.html "Firebase Android Quick Start")


License
-----
The MIT License (MIT)

Copyright (c) <2015> <MHacks>

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
