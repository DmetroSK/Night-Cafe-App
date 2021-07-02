package com.nightcafe.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SplashFragment extends Fragment {
   NavController navController;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                navController.navigate(R.id.action_splashFragment_to_firstScreenFragment);
            }
        };

        handler.postDelayed(runnable, 3000);


        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}