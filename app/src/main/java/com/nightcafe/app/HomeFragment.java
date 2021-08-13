package com.nightcafe.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.items.FoodItemAdapter;
import com.nightcafe.app.items.ItemModel;
import com.nightcafe.app.orders.CartFragment;

public class HomeFragment extends Fragment {

    FoodItemAdapter foodItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.fooditemrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase query
        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("status").equalTo("Available"), ItemModel.class)
                        .build();

        //set data adapter
        foodItemAdapter = new FoodItemAdapter(options);
        recyclerView.setAdapter(foodItemAdapter);

        //initialize components
        FloatingActionButton cart = (FloatingActionButton)view.findViewById(R.id.cart);

        //get local database phone value
        SharedPreferences shared = getContext().getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        String UserPhone = (shared.getString("phone"," "));

        //firebase query
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone).child("Cart");

        //check carts values exists
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    cart.setVisibility(View.GONE); //if not cart button hide
                }
                else {
                    cart.setVisibility(View.VISIBLE); //if it is cart button show
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    return;
            }
            });

        //Click cart button
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cart fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new CartFragment()).addToBackStack(null).commit();

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        foodItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        foodItemAdapter.startListening();
    }

}