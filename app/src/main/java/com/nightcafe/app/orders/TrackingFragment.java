package com.nightcafe.app.orders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;

import java.util.HashMap;


public class TrackingFragment extends Fragment {

    String UserPhone,status;
    Button call;
    LinearLayout track1,track2,track3,track4,hideSection,bottomSection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        //components declaration
         hideSection = view.findViewById(R.id.hideSection);
        bottomSection = view.findViewById(R.id.bottomSection);
         track1 = view.findViewById(R.id.track1);
         track2 = view.findViewById(R.id.track2);
         track3 = view.findViewById(R.id.track3);
         track4 = view.findViewById(R.id.track4);
         call = view.findViewById(R.id.btnCall);


        //session
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
        UserPhone = userDetails.get(SessionManager.KEY_phone);

        //Get values from firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(UserPhone);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Set Values from database
              status =  dataSnapshot.child("status").getValue(String.class);

              setVisible();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        reference.addListenerForSingleValueEvent(valueEventListener);

        //call button press
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0762513377"));
                startActivity(intent);
            }
        });



        return view;
    }

    private void setVisible(){
        if(status.equals("pending"))
        {
            bottomSection.setVisibility(View.VISIBLE);
            track1.setVisibility(View.VISIBLE);
            track2.setVisibility(View.VISIBLE);

        }
        else {
            hideSection.setVisibility(View.VISIBLE);
        }
    }
}