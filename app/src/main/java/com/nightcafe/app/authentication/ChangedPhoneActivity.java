package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;

public class ChangedPhoneActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressBar probar;
    TextInputLayout phone,email;
    String newPhone,newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_phone);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Button btn_next = findViewById(R.id.next);
        ImageView back = findViewById(R.id.arrow);

        probar = findViewById(R.id.progressbar);
        probar.setVisibility(View.GONE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
    //            Intent intent = new Intent(ChangedPhoneActivity.this, UpdatePhoneActivity.class);
   //             startActivity(intent);
//                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangedPhoneActivity.this, SignInActivity.class));
                finish();
            }
        });

    }

    private void checkUser() {

        String user_phone = validatePhoneNumber();
         newEmail = validateEmail();

        Integer set=Integer.valueOf(user_phone);
        newPhone = String.valueOf("+94"+set);
        Log.d("tag-2", newPhone);
        Log.d("tag-2", newEmail);
        probar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(newPhone) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        else {
            Query checkuser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(newPhone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        String retrieve_email = snapshot.child(newPhone).child("email").getValue(String.class);
                        String retrieve_phone = snapshot.child(newPhone).child("phone").getValue(String.class);


                         if( newPhone.equals(retrieve_phone) && newEmail.equals(retrieve_email))
                        {
                            probar.setVisibility(View.GONE);
                            Log.d("tag--name", retrieve_email +retrieve_phone );
                            Intent intent = new Intent(ChangedPhoneActivity.this, UpdatePhoneActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            probar.setVisibility(View.GONE);
                             Toast.makeText(getApplicationContext(), "Details not matched", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        probar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No such user exit", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    probar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    private String validateEmail() {
         email = findViewById(R.id.email);
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