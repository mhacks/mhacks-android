The NEW MHacks Android App
===========================

MHacks-Android is the next version of last semester's hugely successful Android application. As of this writing, MHacksAndroid-Public will be deprecated, since the new app will very soon be up to feature parity with the old one... and actually, way better. Great things are in store, so stay tuned.


Building
-----

The project has been built with Android Studio Beta, and consequently uses the Gradle build system.
If you have used Gradle before, great! If you haven't, you might want to take a look at some guides online.

Before you get started, you'll need your Parse App ID and Client Key, as well as the URL to your Firebase. Add these to the java properties file located in `~/.gradle/gradle.properties`. Until you add these, Gradle will complain that you haven't set the values.

To build the project for debug, you should only need to import it into Android studio. You may also build it from the command line, using:

`$ ./gradlew clean assembleDebug`

If you plan on building this project for release, you'll need to add a `signingConfig` block to app/build.gradle with your keys. This will depend on how you plan on signing it.


Credits
-----

As of this writing, this project shares much of its code base with the [Creators Co-Op Android App](https://github.com/CreatorsCoop/Creators-Android "Creators-Android"), since they require many of the same core features.

We would also like to thank the maintainers of the following:

- [Square's Picasso Image Library](http://square.github.io/picasso/ "Picasso")
- [Guava Google Core Libraries](https://code.google.com/p/guava-libraries/ "guava-libraries")
- [Firebase Android Library](https://www.firebase.com/docs/android/quickstart.html "Firebase Android Quick Start")
- [Parse Android Library](https://parse.com/docs/android_guide "Parse Android Developer Guide")


License
-----

Stay tuned.