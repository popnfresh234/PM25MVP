package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Alexander on 5/21/2015.
 */
public class PM25Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "LjHPYqZjwGsySkallPNtszswbIgjgaWyk8U2qTAm", "PA8BJlK2HVFRDggiikzdow8uaGHOdBhArXBlHkiL");
    }
}
