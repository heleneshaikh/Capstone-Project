package com.hfad.james.config;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by heleneshaikh on 01/09/16.
 */
public class DatabaseConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
