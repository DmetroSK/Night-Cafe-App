package com.nightcafe.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.nightcafe.app.authentication.SignInActivity;
import com.nightcafe.app.databases.SessionManager;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container, false);

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
        String Username = userDetails.get(SessionManager.KEY_NAME);
        String UserPhone = userDetails.get(SessionManager.KEY_phone);

        //Elements define
        RelativeLayout logout = (RelativeLayout)view.findViewById(R.id.subRaw4);
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView phone = (TextView)view.findViewById(R.id.phone);

        //Remove 3 Characters and add 0 to Phone number
        String correctPhone = "0"+UserPhone.substring(3);

        //Set Values
        name.setText(Username);
        phone.setText(correctPhone);

        //Click logout
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

                    //Clear values from session
                    sessionManager.logout();
                }
            });
        return view;

    }
}