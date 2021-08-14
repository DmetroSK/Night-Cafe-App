package com.nightcafe.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.authentication.OtpActivity;
import com.nightcafe.app.authentication.UpdateSuccessActivity;
import com.nightcafe.app.databases.CardManager;
import com.nightcafe.app.databases.SessionManager;
import com.nightcafe.app.orders.CartFragment;
import com.nightcafe.app.orders.TrackingFragment;
import com.nightcafe.app.settings.AddressFragment;
import com.nightcafe.app.settings.CardInfoFragment;
import com.nightcafe.app.settings.SettingsFragment;

import java.util.HashMap;

public class CheckoutFragment extends Fragment {

    TextView street,city;
    DatabaseReference reference;
    String UserPhone,dStreet,dCity,cNumber,ref;
    RelativeLayout hideAddressSection,hideAddAddressSection,hidePaymentSection,hideAddPaymentSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get value from fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ref = bundle.getString("Total"," ");
        }
        else {
            ref =" ";
        }

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        //Elements define
        ImageView back = view.findViewById(R.id.arrow);

        //components declaration
        hideAddressSection = view.findViewById(R.id.section2);
        hideAddAddressSection = view.findViewById(R.id.section3);
        hidePaymentSection = view.findViewById(R.id.section4);
        hideAddPaymentSection = view.findViewById(R.id.section5);
        street = view.findViewById(R.id.txtStreet);
        city = view.findViewById(R.id.txtCity);
        TextView subTotal = view.findViewById(R.id.txtSubTotal);
        TextView deliverFee = view.findViewById(R.id.txtDeliverFee);
        TextView total = view.findViewById(R.id.txtTotal);
        Button order = view.findViewById(R.id.btnOrder);

        TextView cardNumber = view.findViewById(R.id.txtCardNumber);

       //session create
       CardManager cardManager = new CardManager(getContext());
       HashMap<String,String> cardDetails = cardManager.getCardInfo();
       SessionManager sessionManager = new SessionManager(container.getContext());
       HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //session get values
        cNumber = cardDetails.get(CardManager.KEY_number);
        UserPhone = userDetails.get(SessionManager.KEY_phone);

        //Get values from firebase
        reference = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               //Set Values from database
               dStreet =  dataSnapshot.child("street").getValue(String.class);
               dCity = dataSnapshot.child("city").getValue(String.class);


                if(dStreet == null && dCity == null)
                {
                    hideAddressSection.setVisibility(View.GONE);
                    hideAddAddressSection.setVisibility(View.VISIBLE);
                }
                else {
                    hideAddressSection.setVisibility(View.VISIBLE);
                    hideAddAddressSection.setVisibility(View.GONE);
                    street.setText(dStreet);
                    city.setText(dCity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        };

        reference.addListenerForSingleValueEvent(valueEventListener);


        if(cNumber == null)
        {
            hidePaymentSection.setVisibility(View.GONE);
            hideAddPaymentSection.setVisibility(View.VISIBLE);
        }

        else {
            hidePaymentSection.setVisibility(View.VISIBLE);
            hideAddPaymentSection.setVisibility(View.GONE);

            //Set card number last 4 digits
            cardNumber.setText("XXXX XXXX XXXX " +cNumber.substring(12));
        }

        //set prices values
        int t = Integer.parseInt(ref);
        int sub = t -200;
        subTotal.setText("Rs. "+ String.valueOf(sub));
        deliverFee.setText("Rs. 200");
        total.setText("Rs. "+ ref);


        //Add address button press
        hideAddAddressSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass data to card Fragment
                AddressFragment fragment = new AddressFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ref", "chk");
                fragment.setArguments(bundle);


                //setting fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();


            }
        });


        //Add payment button press
        hideAddPaymentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass data to card Fragment
                CardInfoFragment fragment = new CardInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ref", "chk");
                fragment.setArguments(bundle);


                //setting fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();


            }
        });

        //order button press
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child("Cart").get().addOnSuccessListener(dataSnapshot -> {
                    reference.child("Orders").setValue(dataSnapshot.getValue());
                    reference.child("Cart").removeValue();
                });

                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        reference.child("Orders").child("status").setValue("pending");

                        Toast.makeText(getContext(),"Order Placed" , Toast.LENGTH_SHORT).show();

                        //setting fragment open
                        AppCompatActivity activity = (AppCompatActivity)view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new TrackingFragment()).addToBackStack(null).commit();

                    }
                },3000);



            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setting fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new CartFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}