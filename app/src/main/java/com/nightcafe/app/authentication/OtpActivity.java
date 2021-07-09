package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafe.app.MainActivity;
import com.nightcafe.app.R;
import com.nightcafe.app.SplashActivity;
import com.nightcafe.app.databases.SessionManager;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    String fullName,email,newPhoneS,phoneNewU,oldPhone,phoneNew;
    PinView pinFromUser;
    String codeBySystem;
    static String ref;
    FirebaseAuth firebaseAuth;

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
        Log.d("tag-otp-ref", ref );

        if(ref.equals("signup")){
            fullName = getIntent().getStringExtra("_fullName");
            email = getIntent().getStringExtra("_email");

            String phone1 = getIntent().getStringExtra("_phone");

            Integer set1 = Integer.valueOf(phone1);
            newPhoneS = String.valueOf("+94"+set1);
            Log.d("tag-otp-sign-phone", newPhoneS );
            sendVerificationCodeToUser(newPhoneS);

        }

        else if(ref.equals("update_Phone"))
        {
            String phone2 = getIntent().getStringExtra("_n_phone");
            Log.d("tag-otp-new-con-phone2", phone2 );
            Integer set2=Integer.valueOf(phone2);
            phoneNewU = String.valueOf("+94"+set2);

            oldPhone = getIntent().getStringExtra("_old_phone");
            Log.d("tag-otp-old-phone", oldPhone );
            Log.d("tag-otp-new-phone", phoneNewU );
            sendVerificationCodeToUser(phoneNewU);
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
                    onRestart();
                    Toast.makeText(OtpActivity.this, "Code Empty.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ref.equals("signup")){

                    sendVerificationCodeToUser(newPhoneS);

                }
               else if(ref.equals("update_Phone")){

                    sendVerificationCodeToUser(phoneNewU);

                }
                else {

                    sendVerificationCodeToUser(phoneNew);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ref.equals("signup"))
                {
                    startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                }
                else if(ref.equals("update_Phone"))
                {
                    startActivity(new Intent(OtpActivity.this, UpdatePhoneActivity.class));
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

    @Override
    public void onRestart() {
        super.onRestart();


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

    private void updatePhone(String otp){
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential( oldPhone, otp );
            // Update Mobile Number...
        firebaseAuth.getCurrentUser().updatePhoneNumber(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener <Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task <Void> task) {
                                               if (task.isSuccessful()) {
                                                   // Update Successfully
                                                   Intent intent = new Intent(OtpActivity.this, SignInActivity.class);
                                                     startActivity(intent);
                                                    finish();
                                               } else {
                                                   Toast.makeText(OtpActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }
                );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        if(ref.equals("update_Phone"))
        {
           // updatePhone(code);
           updateUsersData();
        }

        else{
            signInWithPhoneAuthCredential(credential);
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

         firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Verified.", Toast.LENGTH_SHORT).show();
                            Log.d("tag-otp-dt-data-bef-ref", ref );

                            if(ref.equals("signup"))
                            {

                                storeNewUsersData();
                             //   Log.d("tag-otp-dt-data-pass", user.toString() );
                                //session create
                                SessionManager sessionManager = new SessionManager(OtpActivity.this);
                                sessionManager.createLoginSession(fullName,email,newPhoneS);
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


        User addNewUser = new User(fullName, email, newPhoneS);
        Log.d("tag-otp-st-u-data", fullName+email+newPhoneS );
        reference.child(addNewUser.getPhone()).setValue(addNewUser);
        Log.d("tag-otp-st-new-user", "passed" );
        startActivity(new Intent(OtpActivity.this, MainActivity.class));
        finish();
        //We will also create a Session here in next videos to keep the user logged In

    }

    private void updateUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");


        reference.child(oldPhone).get().addOnSuccessListener(dataSnapshot -> {
            reference.child(phoneNewU).setValue(dataSnapshot.getValue());
            reference.child(oldPhone).removeValue();
        });

        SessionManager sessionManager = new SessionManager(this);
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                reference.child(phoneNewU).child("phone").setValue(phoneNewU);

                sessionManager.logout();
                startActivity(new Intent(OtpActivity.this, MainActivity.class));
                finish();

            }
        },2000);




//        reference.child("phone").setValue(phoneNewU);

        //We will also create a Session here in next videos to keep the user logged In

    }


}