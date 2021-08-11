package com.nightcafe.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    FoodItemAdapter foodItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home,container, false);

        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.fooditemrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items"), ItemModel.class)
                        .build();

         foodItemAdapter = new FoodItemAdapter(options);
        recyclerView.setAdapter(foodItemAdapter);

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