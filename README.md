# Android App Demo #

This is a basic functioning checklist app built using Android Studio. This app was the product of a crash course in learning Android app development.

## Features ##

- Create checklists
- Set and change the title of said checklists
- Add notes to a checklist
- Change said notes on a checklist
- Check off (and undo the check off) said notes
- Delete notes from a checklist
- Delete a checklist and all of its notes
- Persists checklists when app is stopped

## Get the demo ##

Simply clone this repo to obtain the demo android app.

```
    git clone https://github.com/gshaw-pivotal/android-checklist-demo.git
```

Clean, build and sync (gradle) the project in Android Studio to ensure all dependencies are pulled down.

## Running in the emulator ##

The app can be run using the emulators found within Android Studio.

To set up an emulator:

1. `Tools -> Android -> AVD Manager`

2. Select `Create Virtual Device`

3. On the `Virtual Device Configuration` screen, select the type of device. We selected a Pixel Phone.

4. Click Next

5. Download a system image, generally the latest version. We used `Nougat (API level 25)`. Click the download link associated with the image you wish to use.

6. Allow Android Studio to download and install the image. Once finish click `Finish`.

7. Click `Next` and verify the configuration. Usually defaults are fine. Click `Finish` once done.

This will bring you back to the `Your Virtual Devices` screen and should now list the virtual device you have just created.

8. Close out of this screen and back to the main Android Studio screen.

9. From the `Run` menu, select `Run 'app'`. This will open up a screen from which you select the virtual device to run the app on. Select the device of choice.

Once Android Studio has finished packaging and loading the app it will be running in the emulator.

## Testing ##

The demo also includes a selection of integration tests and simulated end user tests. This demonstrates possible testing approaches that can be used to support TDD.

We used [Roboelectric](http://robolectric.org/) and [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/) to support testing.

We leave it those that follow to flesh out further tests and features as potential learning exercises.

## Dependencies ##

The following are dependencies for this demo app.

### Codebase ###

```
   compile fileTree(dir: 'libs', include: ['*.jar'])
   compile 'com.android.support:appcompat-v7:25.3.1'
   compile 'com.android.support:design:25.3.1'
   
   compile 'com.jakewharton:butterknife:8.5.1'
   annotationProcessor 'com.jakewharton:butterknife-compiler:8.5.1'
   
   compile 'io.reactivex.rxjava2:rxjava:2.1.0'
   compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
   
   compile group: 'com.google.guava', name: 'guava', version: '20.0'
```

### Testing ###

```
    testCompile 'junit:junit:4.12'
    testCompile 'org.robolectric:robolectric:3.3.2'
    testCompile 'org.mockito:mockito-core:2.+'
    testCompile('com.squareup.assertj:assertj-android:1.1.1') {
        exclude module: 'support-annotations'
    }

    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    androidTestCompile 'com.android.support.test:runner:0.5', {
        exclude group: 'com.android.support', module: 'support-annotations'
    }
    androidTestCompile 'com.android.support.test:rules:0.5', {
        exclude group: 'com.android.support', module: 'support-annotations'
    }
```