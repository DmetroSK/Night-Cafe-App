package com.nightcafe.app.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.nightcafe.app.R;

public class UpdatePhoneActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    String old_phone,user_new_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Button btn_done = findViewById(R.id.done);
        ImageView back = findViewById(R.id.arrow);

        mAuth = FirebaseAuth.getInstance();
        old_phone = getIntent().getStringExtra("_oldPhone");



        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePhone();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdatePhoneActivity.this, ChangedPhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void updatePhone() {

         user_new_phone = validatePhoneNumber();
        if(TextUtils.isEmpty(user_new_phone))
        {
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        }
        else{

            Intent intent = new Intent(UpdatePhoneActivity.this, OtpActivity.class);

            intent.putExtra("_n_phone", user_new_phone);
            intent.putExtra("_Ref", "update_Phone");
            intent.putExtra("_old_phone", old_phone);

            Log.d("tag-up-Act-old-phone",  old_phone);

            Log.d("tag-up-Act-new-phone",  user_new_phone);

            startActivity(intent);
            finish();
        }

    }

    private String validatePhoneNumber() {
        TextInputLayout phone = findViewById(R.id.newphone);
        String user_phone = phone.getEditText().getText().toString().trim();

        String checkspaces = "\\d{10}";
        if (user_phone.isEmpty()) {
            phone.setError("Enter valid phone number");
            return null;
        }
//        else if (!user_phone.matches(checkspaces)) {
//            phone.setError("Enter 10 digits");
//            return null;
//        }
        else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return user_phone;
        }
    }
}