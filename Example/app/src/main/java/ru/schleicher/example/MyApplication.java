package ru.schleicher.example;

import android.app.Application;

import com.facebook.soloader.SoLoader;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        // FB Yoga init
        SoLoader.init(this, false);
    }

}
