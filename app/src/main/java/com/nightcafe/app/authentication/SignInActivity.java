package com.nightcafe.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressBar probar;
    TextInputLayout phone;
    String newPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide(); //hide action bar

        Button btn_signin = findViewById(R.id.signin);
        TextView btn_changeNum = findViewById(R.id.changeNum);
        TextView btn_signup = findViewById(R.id.signup);
        probar = findViewById(R.id.progressbar);
        probar.setVisibility(View.GONE);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected(SignInActivity.this)){
                    showCustomDialog();
                }
                else {
                    loginUser();
                }
            }
        });


        btn_changeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, ChangedPhoneActivity.class));
                finish();

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

    private void loginUser() {



        String user_phone = validatePhoneNumber();

        Integer set=Integer.valueOf(user_phone);
        newPhone = String.valueOf("+94"+set);
        Log.d("tag", newPhone);
        probar.setVisibility(View.VISIBLE);


        if (TextUtils.isEmpty(newPhone)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        else {

            Query checkuser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(newPhone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        phone.setError(null);
                        phone.setErrorEnabled(false);

                        String email = snapshot.child(newPhone).child("email").getValue(String.class);
                        String name = snapshot.child(newPhone).child("fullName").getValue(String.class);
//                        Toast.makeText(SignInActivity.this, data, Toast.LENGTH_SHORT).show();
                       Log.d("tag--name", name);
                        probar.setVisibility(View.GONE);


                        //session create
                        SessionManager sessionManager = new SessionManager(SignInActivity.this);
                        sessionManager.createLoginSession(name,email,newPhone);


                        Intent intent = new Intent(SignInActivity.this, OtpActivity.class);
                        intent.putExtra("_phone", newPhone);
                        intent.putExtra("_Ref", "signin");
                        startActivity(intent);
                        finish();

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

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setMessage("Please Connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestart();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isConnected(SignInActivity signInActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signInActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
        {
            return true;
        }
        else
            {
                return false;
            }

    }

    private String validatePhoneNumber() {
        phone = findViewById(R.id.phone);
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


    @Override
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