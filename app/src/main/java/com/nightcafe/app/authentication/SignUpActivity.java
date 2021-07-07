package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nightcafe.app.MainActivity;
import com.nightcafe.app.R;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        mAuth = FirebaseAuth.getInstance();

        Button btn_signup = findViewById(R.id.signup);
        ImageView back = findViewById(R.id.arrow);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
               finish();

            }
        });


    }


    private void createUser(){

        String user_name =  validateFullName();
        String user_email = validateEmail();
        String user_phone = validatePhoneNumber();


        if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_phone))
        {
          Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        }
        else{


            Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);


            intent.putExtra("_fullName", user_name);
            intent.putExtra("_email", user_email);
            intent.putExtra("_phone", user_phone);

                    startActivity(intent);
                    finish();
        }
    }




    private String validateFullName() {
        TextInputLayout name = findViewById(R.id.fullname);
        String user_name = name.getEditText().getText().toString();


        if (user_name.isEmpty()) {
            name.setError("Field can not be empty");
            return null;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return user_name;
        }
    }

    private String validateEmail() {
        TextInputLayout email = findViewById(R.id.email);
        String user_email = email.getEditText().getText().toString().trim();


        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (user_email.isEmpty()) {
            email.setError("Field can not be empty");
            return null;
        } else if (!user_email.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return null;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return user_email;
        }
    }


    private String validatePhoneNumber() {
        TextInputLayout phone = findViewById(R.id.phone);
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