package com.evadethefail.app.objectBox;

import android.app.Application;

import io.objectbox.BoxStore;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }

    public static BoxStore getBoxStore() {
        return ObjectBox.get();
    }
}