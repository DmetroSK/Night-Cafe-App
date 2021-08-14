package com.nightcafe.app.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nightcafe.app.R;
import com.nightcafe.app.authentication.SignInActivity;
import com.nightcafe.app.databases.SessionManager;
import com.nightcafe.app.settings.AddressFragment;
import com.nightcafe.app.settings.CardInfoFragment;
import com.nightcafe.app.settings.ProfileFragment;

import java.util.HashMap;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container, false);

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
        String Username = userDetails.get(SessionManager.KEY_NAME);
        String UserPhone = userDetails.get(SessionManager.KEY_phone);

        //Elements define
        RelativeLayout logout = (RelativeLayout)view.findViewById(R.id.subRaw4);
        RelativeLayout profile = (RelativeLayout)view.findViewById(R.id.layout1);
        RelativeLayout address = (RelativeLayout)view.findViewById(R.id.subRaw2);
        RelativeLayout card = (RelativeLayout)view.findViewById(R.id.subRaw1);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView phone = (TextView)view.findViewById(R.id.phone);


        //Remove 3 Characters and add 0 to Phone number
        String correctPhone = "0"+UserPhone.substring(3);

        //Set Values
        name.setText(Username);
        phone.setText(correctPhone);


        //Click profile button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //profile fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new ProfileFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();


            }
        });

        //Click card button
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //profile fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new CardInfoFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();


            }
        });

        //Click address button
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //profile fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new AddressFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();


            }
        });

        //Click logout button
        logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //SignOut Firebase
                    FirebaseAuth.getInstance().signOut();

                    //Start next activity
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);

                    //fragment finish back press not redirect
                    getActivity().getFragmentManager().popBackStack();

                    //Toast message
                    Toast.makeText(getContext(),"Logout" , Toast.LENGTH_SHORT).show();

                    //Clear values from session
                    sessionManager.logout();
                }
            });

        return view;

    }
}