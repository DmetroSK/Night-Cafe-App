package com.nightcafe.app.orders;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.CardManager;
import com.nightcafe.app.databases.SessionManager;
import com.nightcafe.app.databases.ValueManager;
import com.nightcafe.app.settings.AddressFragment;
import com.nightcafe.app.settings.CardInfoFragment;
import java.util.HashMap;

public class CheckoutFragment extends Fragment {

    TextView street,city;
    DatabaseReference reference;
    String UserPhone,dStreet,dCity,cNumber,ref,ordersCount,NewOrdersCount,pending;
    RelativeLayout hideAddressSection,hideAddAddressSection,hidePaymentSection,hideAddPaymentSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
       ValueManager valueManager = new ValueManager(container.getContext());
       HashMap<String,String> valueData = valueManager.getValues();

        //session get values
        cNumber = cardDetails.get(CardManager.KEY_number);
        UserPhone = userDetails.get(SessionManager.KEY_phone);
        ref = valueData.get(ValueManager.KEY_total);

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

        //Get orders count from database
        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Set Values from database
                ordersCount =  dataSnapshot.child("orders").getValue(String.class);
                pending =  dataSnapshot.child("pending").getValue(String.class);
                int oldOrderCount = Integer.parseInt(ordersCount);
                NewOrdersCount = String.valueOf(oldOrderCount+1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseDatabase.getInstance().getReference("OrderCount").addListenerForSingleValueEvent(valueEventListener2);

        //check card number null state
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
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();


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
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();


            }
        });

        //order button press
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child("Cart").get().addOnSuccessListener(dataSnapshot -> {
                    FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).child("Order").setValue(dataSnapshot.getValue());
                    reference.child("Cart").removeValue();
                });

                Handler h1= new Handler();
                Handler h2 = new Handler();

                h1.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        //get orders count
                        FirebaseDatabase.getInstance().getReference().child("OrderCount").child("orders").setValue(NewOrdersCount);

                        //update pending order count
                        int pendingOrders = Integer.parseInt(pending);
                        FirebaseDatabase.getInstance().getReference().child("OrderCount").child("pending").setValue(String.valueOf(pendingOrders+1));


                    }
                },1000);


                h2.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        //update order essential details
                        FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).child("status").setValue("pending");
                        FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).child("phone").setValue(UserPhone);
                        FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).child("id").setValue(NewOrdersCount);
                        FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).child("total").setValue(ref);

                        //success message
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