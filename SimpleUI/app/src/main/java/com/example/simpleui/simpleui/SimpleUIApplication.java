package com.example.simpleui.simpleui;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by new on 2016/3/21.
 */
public class SimpleUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
    }
}
