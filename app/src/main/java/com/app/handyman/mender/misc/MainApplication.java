package com.app.handyman.mender.misc;

import android.app.Application;

import com.app.handyman.mender.common.utils.FontsOverride;

/**
 * Created by apple on 04/12/17.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Quicksand Light.otf");

    }
}
