package com.example.study4child.Tools;

import android.app.Application;

public class MyApplication extends Application {
    public static SFXPool sfxPool;

    public static SFXPool getPoolInstance() {
        return sfxPool;
    }

    public static void setPoolInstance(SFXPool pool) {
        sfxPool = pool;
    }
}
