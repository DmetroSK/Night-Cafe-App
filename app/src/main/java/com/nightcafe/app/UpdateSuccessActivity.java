package com.nightcafe.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nightcafe.app.authentication.ChangedPhoneActivity;
import com.nightcafe.app.authentication.SignInActivity;
import com.nightcafe.app.authentication.UpdatePhoneActivity;

public class UpdateSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_success);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Button btn_signin = findViewById(R.id.signin);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateSuccessActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}