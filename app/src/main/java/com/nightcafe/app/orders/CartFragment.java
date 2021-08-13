package com.nightcafe.app.orders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafe.app.HomeFragment;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;
import java.util.HashMap;


public class CartFragment extends Fragment {
    CartItemAdapter cartItemAdapter;
    String UserPhone;
    LinearLayout hide;
    int fTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.cartItemRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView stotal = (TextView) view.findViewById(R.id.txtSubTotal);
        TextView deliveryFee = (TextView) view.findViewById(R.id.txtDeliverFee);
        TextView FullTotal = (TextView) view.findViewById(R.id.txtTotal);

        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
         UserPhone = userDetails.get(SessionManager.KEY_phone);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone);
        DatabaseReference cartRef = userRef.child("Cart");

        FirebaseRecyclerOptions<CartItemModel> options =
                new FirebaseRecyclerOptions.Builder<CartItemModel>()
                        .setQuery(cartRef, CartItemModel.class)
                        .build();

        cartItemAdapter = new CartItemAdapter(options);
        recyclerView.setAdapter(cartItemAdapter);

         hide = view.findViewById(R.id.hideSection);


        userRef.child("Cart").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum=0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                  String s = data.child("price").getValue(String.class);
                    sum += Integer.parseInt(s);

                }
                stotal.setText("Rs. " + String.valueOf(sum));

                deliveryFee.setText("Rs. 200");

                 fTotal = sum+200;

                FullTotal.setText("Rs. " + String.valueOf(fTotal));

                if(fTotal == 200){
                    hide.setVisibility(View.VISIBLE);
                    deliveryFee.setText("Rs. 0");
                    FullTotal.setText("Rs. 0");
                    btnConfirm.setEnabled(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ImageView back = view.findViewById(R.id.arrow);
        //Click back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, new HomeFragment() ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

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