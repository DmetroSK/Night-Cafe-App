package com.nightcafe.app.orders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.CheckoutFragment;
import com.nightcafe.app.HomeFragment;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;
import com.nightcafe.app.settings.AddressFragment;

import java.util.HashMap;


public class CartFragment extends Fragment {
    CartItemAdapter cartItemAdapter;
    String UserPhone;
    LinearLayout hideSection;
    int totalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.cartItemRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initialize components
        TextView subTotal = (TextView) view.findViewById(R.id.txtSubTotal);
        TextView deliveryFee = (TextView) view.findViewById(R.id.txtDeliverFee);
        TextView Total = (TextView) view.findViewById(R.id.txtTotal);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        ImageView back = view.findViewById(R.id.arrow);

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
         UserPhone = userDetails.get(SessionManager.KEY_phone);

        //database query
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone);
        DatabaseReference cartRef = userRef.child("Cart");

        FirebaseRecyclerOptions<CartItemModel> options =
                new FirebaseRecyclerOptions.Builder<CartItemModel>()
                        .setQuery(cartRef, CartItemModel.class)
                        .build();

        //set data adapter
        cartItemAdapter = new CartItemAdapter(options);
        recyclerView.setAdapter(cartItemAdapter);

        //components declaration
        hideSection = view.findViewById(R.id.hideSection);

        //recycle view changes listen
        userRef.child("Cart").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int sum=0;

                //total sum of cart items through firebase
                for(DataSnapshot data: dataSnapshot.getChildren()){
                  String sumText = data.child("price").getValue(String.class);
                    sum += Integer.parseInt(sumText);

                }

                //set sub total value
                subTotal.setText("Rs. " + String.valueOf(sum));

                //set deliver charges fixed value
                deliveryFee.setText("Rs. 200");

                //calculate total sum
                totalSum = sum+200;

                //set total value
                Total.setText("Rs. " + String.valueOf(totalSum));

                //if cart empty show message and set other values to zero and button disable
                if(totalSum == 200){
                    hideSection.setVisibility(View.VISIBLE);
                    deliveryFee.setText("Rs. 0");
                    Total.setText("Rs. 0");
                    btnConfirm.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast message
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        });


        //Click back button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass data to card Fragment
                CheckoutFragment fragment = new CheckoutFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Total", String.valueOf(totalSum));
                fragment.setArguments(bundle);

                //checkout fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();

            }
        });

        //Click back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //home fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();

            }
        });

        return view;
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