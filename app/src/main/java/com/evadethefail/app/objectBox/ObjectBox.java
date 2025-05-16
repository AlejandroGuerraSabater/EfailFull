package com.evadethefail.app.objectBox;

import android.content.Context;
import android.util.Log;

import com.evadethefail.app.entidades.MyObjectBox;
import io.objectbox.BoxStore;
import io.objectbox.android.Admin;

public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder().androidContext(context.getApplicationContext()).build();
        //Comentar una vez terminado
        if (true) {
            boolean started = new Admin(boxStore).start(context);
            Log.i("ObjectBoxAdmin", "Started: " + started);
        }
    }

    public static BoxStore get() {
        return boxStore;
    }
}
