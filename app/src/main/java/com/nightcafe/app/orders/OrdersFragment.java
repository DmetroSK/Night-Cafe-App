package com.nightcafe.app.orders;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;

import java.util.HashMap;

public class OrdersFragment extends Fragment {

    CartItemAdapter cartItemAdapter;
    DatabaseReference reference;
    String totalPrice,UserPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View orderView = inflater.inflate(R.layout.fragment_orders, container, false);

        //components declaration
        TextView subTotal = orderView.findViewById(R.id.txtSubTotal);
        TextView deliverFee = orderView.findViewById(R.id.txtDeliverFee);
        TextView total = orderView.findViewById(R.id.txtTotal);
        LinearLayout hide = orderView.findViewById(R.id.hideSection1);
        RelativeLayout thirdSection = orderView.findViewById(R.id.thirdSection);
        RelativeLayout fourthSection = orderView.findViewById(R.id.fourthSection);
        RelativeLayout fifthSection = orderView.findViewById(R.id.fifthSection);

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
         UserPhone = userDetails.get(SessionManager.KEY_phone);

        //database query
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone);

        FirebaseRecyclerOptions<CartItemModel> options1 =
                new FirebaseRecyclerOptions.Builder<CartItemModel>()
                        .setQuery(reference.child("Order"), CartItemModel.class)
                        .build();

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)orderView.findViewById(R.id.currentOrderRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set data adapter
        cartItemAdapter = new CartItemAdapter(options1);
        recyclerView.setAdapter(cartItemAdapter);

        //Get orders count from database
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 //Set Values from database
                totalPrice =  dataSnapshot.child("total").getValue(String.class);

                if(totalPrice == null)
                {
                    thirdSection.setVisibility(View.GONE);
                    fourthSection.setVisibility(View.GONE);
                    fifthSection.setVisibility(View.GONE);
                    hide.setVisibility(View.VISIBLE);

                }
                else {
                    //set prices values
                    int t = Integer.parseInt(totalPrice);
                    int sub = t -200;
                    subTotal.setText("Rs. "+ String.valueOf(sub));
                    deliverFee.setText("Rs. 200");
                    total.setText("Rs. "+ totalPrice);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone).addValueEventListener(valueEventListener);


        return orderView;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartItemAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        cartItemAdapter.startListening();
    }

}