package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafe.app.MainActivity;
import com.nightcafe.app.R;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    String fullName,email,newPhone,phoneNew;
    PinView pinFromUser;
    String codeBySystem;
    static String ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Button btn_submit = findViewById(R.id.submit);
        ImageView back = findViewById(R.id.arrow);
        TextView resend = findViewById(R.id.resend);

        pinFromUser = findViewById(R.id.pinview);

        ref = getIntent().getStringExtra("_Ref");

        if(ref == "signup"){
            fullName = getIntent().getStringExtra("_fullName");
            email = getIntent().getStringExtra("_email");

            String phone = getIntent().getStringExtra("_phone");

            Integer set=Integer.valueOf(phone);
            newPhone = String.valueOf("+94"+set);

            sendVerificationCodeToUser(newPhone);

        }
        else {
            phoneNew = getIntent().getStringExtra("_phone");
            sendVerificationCodeToUser(phoneNew);
        }






        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = pinFromUser.getText().toString();
                if(!code.isEmpty()){
                    verifyCode(code);

                }
                else{
                    Toast.makeText(OtpActivity.this, "Code Empty.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ref == "signup"){

                    sendVerificationCodeToUser(newPhone);

                }
                else {

                    sendVerificationCodeToUser(phoneNew);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ref == "signup")
                {
                    startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                }
                else {
                    startActivity(new Intent(OtpActivity.this, SignInActivity.class));
                }

                finish();
            }
        });

    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.d("tag", "onVerificationFailed", e);

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    }

                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Verified.", Toast.LENGTH_SHORT).show();
                            if(ref == "signup")
                            {
                                storeNewUsersData();
                            }
                            else {
                                startActivity(new Intent(OtpActivity.this, MainActivity.class));
                                finish();
                            }


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpActivity.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");

        //Create helperclass reference and store data using firebase
        User addNewUser = new User(fullName, email, newPhone);
        reference.child(newPhone).setValue(addNewUser);

        startActivity(new Intent(OtpActivity.this, MainActivity.class));
        finish();
        //We will also create a Session here in next videos to keep the user logged In

    }
}