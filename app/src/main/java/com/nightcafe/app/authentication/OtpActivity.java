package com.nightcafe.app.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nightcafe.app.MainActivity;
import com.nightcafe.app.R;

public class OtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getSupportActionBar().hide(); //hide action bar

        Button btn_submit = findViewById(R.id.submit);
        ImageView back = findViewById(R.id.arrow);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this, SignUpActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }
}