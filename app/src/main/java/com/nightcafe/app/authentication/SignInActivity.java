package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide(); //hide action bar

        Button btn_signin = findViewById(R.id.signin);
        Button btn_forgotpw = findViewById(R.id.forgotpw);
        Button btn_signup = findViewById(R.id.signup);
        Button btn_google = findViewById(R.id.google);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginUser();

                        Intent intent = new Intent(SignInActivity.this, OtpActivity.class);
                      startActivity(intent);
//                finish();
            }
        });


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            loginUser();

        //        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
          //      startActivity(intent);
//                finish();
            }
        });


        btn_forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
//                finish();
            }
        });


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
               finish();
            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();

    }


    private void loginUser(){

        TextInputLayout email = findViewById(R.id.username);
        TextInputLayout password = findViewById(R.id.password);

        String user_email = email.getEditText().getText().toString();
        String user_password = password.getEditText().getText().toString();


        if(TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_password))
        {
            Toast.makeText(getApplicationContext(),"Enter values", Toast.LENGTH_SHORT).show();
            onRestart();
        }
        else{


        }
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Exit");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit from the app?")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}