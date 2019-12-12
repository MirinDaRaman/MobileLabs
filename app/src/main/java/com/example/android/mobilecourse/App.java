package com.example.android.mobilecourse;

import android.app.Application;
import com.google.firebase.auth.FirebaseAuth;
public class App extends Application {

    private FirebaseAuth auth;

    @Override
    public void onCreate() {
        super.onCreate();
        auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth(){
        return auth;
    }
}
