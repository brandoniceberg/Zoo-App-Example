### Zoo-App-Example
This project is a prototype idea on what a Tulsa Zoo producation app would look like. A lot of the main core concepts and ideas have been applied to this project like device compatabliity, security and ease-of use. This project now (as of Jun. 27 2020) uses Firebase Firestore to hold our data so that the application can be easily expanded to other systems (ie. iOS and web). MapBox was choosen as the map provider because of it's overall better performance compared to Google Maps when it comes to launch time and consistent frame rate. The application supports the latest biometric standards provided by Google (face unlock on Pixel 4 works perfectly fine here). Glide provides fast load times when it comes to displaying images from the database. Leak Canary was used to help track and eleminate memory leak issues. All these considerations were made to provide the absoulte best experience to every user.

## Libraries Used:
- implementation 'androidx.biometric:biometric:1.0.1'
- implementation 'com.mapbox.mapboxsdk:mapbox-android-sdk:9.0.0'
- debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.2'
- implementation 'androidx.cardview:cardview:1.0.0'
- implementation "androidx.recyclerview:recyclerview:1.1.0"
- implementation 'androidx.preference:preference:1.1.0'

## Screenshots:
![image](https://user-images.githubusercontent.com/52220639/79058901-39079600-7c39-11ea-980b-777815b16ed7.png)
![image](https://user-images.githubusercontent.com/52220639/79058936-a3203b00-7c39-11ea-993a-b0a806429e32.png)
![image](https://user-images.githubusercontent.com/52220639/79059018-a36d0600-7c3a-11ea-8f59-33ce08ba2ce1.png)
