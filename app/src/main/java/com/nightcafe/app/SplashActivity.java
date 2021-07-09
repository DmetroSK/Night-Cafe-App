package com.nightcafe.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nightcafe.app.authentication.SignInActivity;
import com.nightcafe.app.databases.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run() {

                SessionManager sessionManager = new SessionManager(SplashActivity.this);


                if(sessionManager.checkLogin() == true)
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }



            }
        },4000);
    }
}