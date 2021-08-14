package com.nightcafe.app.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.orders.CheckoutFragment;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;
import java.util.HashMap;


public class AddressFragment extends Fragment {

    TextInputLayout street,city;
    String UserPhone,ref;
    SessionManager sessionManager;
    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get value from fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ref = bundle.getString("ref"," ");
        }
        else {
            ref =" ";
        }

        View view = inflater.inflate(R.layout.fragment_address, container, false);

        //Elements define
        ImageView back = view.findViewById(R.id.arrow);
        street = view.findViewById(R.id.street);
        city = view.findViewById(R.id.city);
        Button save = view.findViewById(R.id.btnSave);

        //session
        sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
        UserPhone = userDetails.get(SessionManager.KEY_phone);

        //Get values from firebase
        reference = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Set Values from database
                street.getEditText().setText(dataSnapshot.child("street").getValue(String.class));
                city.getEditText().setText(dataSnapshot.child("city").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        reference.addListenerForSingleValueEvent(valueEventListener);

        //update button press
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });


        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();

                if(ref.equals("chk")){
                    //setting fragment open
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new CheckoutFragment()).addToBackStack(null).commit();
                }

                else {
                    //setting fragment open
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SettingsFragment()).addToBackStack(null).commit();
                }

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    //save user address into firebase
    private void saveAddress() {

        String uStreet = validateStreet();
        String uCity = validateCity();

        reference.child("street").setValue(uStreet);
        reference.child("city").setValue(uCity);

        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();

    }

    private String validateStreet() {

        String user_street = street.getEditText().getText().toString();

        if (user_street.isEmpty()) {
            street.setError("Field can not be empty");
            return null;
        } else {
            street.setError(null);
            street.setErrorEnabled(false);
            return user_street;
        }
    }

    private String validateCity() {

       String user_city = city.getEditText().getText().toString();

        if (user_city.isEmpty()) {
            city.setError("Field can not be empty");
            return null;
        } else {
            city.setError(null);
            city.setErrorEnabled(false);
            return user_city;
        }
    }
}