package com.nightcafe.app.items;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafe.app.HomeFragment;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.SessionManager;
import com.nightcafe.app.orders.CartItemModel;
import java.util.HashMap;


public class SingleFoodItemFragment extends Fragment {

    String name,image,regular,large,type,price,UserPhone;
    TextView quantity,totalPrice;
    RadioButton radioButton;
    int qty=1,basePrice,total;
    RadioGroup radioGroup;
    Button cartButton;
    DatabaseReference databaseReference;


    public SingleFoodItemFragment() {

    }

    public SingleFoodItemFragment(String name,String image,String regular,String large) {
            this.name = name;
            this.image = image;
            this.regular = regular;
            this.large = large;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Session Create
        SessionManager sessionManager = new SessionManager(container.getContext());
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        //Get session values
         UserPhone = userDetails.get(SessionManager.KEY_phone);

        View view = inflater.inflate(R.layout.fragment_single_food_item, container, false);
        ImageView back = view.findViewById(R.id.arrow);
        ImageView plusQty = view.findViewById(R.id.plus);
        ImageView minusQty = view.findViewById(R.id.minus);
        quantity = view.findViewById(R.id.qty);
        totalPrice = view.findViewById(R.id.txtTotal);
         radioGroup = view.findViewById(R.id.radio);
         cartButton = view.findViewById(R.id.btnAddToCart);

        ImageView imageUrl = view.findViewById(R.id.image);
        TextView  itemName = view.findViewById(R.id.itemName);
        TextView  regularPrice = view.findViewById(R.id.regularPrice);
        TextView  regularTag = view.findViewById(R.id.txtRegular);
        TextView  largeTag = view.findViewById(R.id.txtLarge);

        itemName.setText(name);
        Glide.with(getContext()).load(image).into(imageUrl);
        regularPrice.setText("Rs. " + regular);
        regularTag.setText("Rs. " + regular);
        largeTag.setText("Rs. " + large);
        totalPrice.setText("Rs. " + regular);

        radioGroup = view.findViewById(R.id.radio);

        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = view.findViewById(selectedId);

        setRegular();

        view.findViewById(R.id.rbtRegular).setOnClickListener(this::onClick);
        view.findViewById(R.id.rbtLarge).setOnClickListener(this::onClick);

        //Click back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).addToBackStack(null).commit();

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();

                qty=1;
                totalPrice.setText("Rs. " + String.valueOf(regular));

            }
        });


        plusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                qty++;
                displayQuantity();
                 total = basePrice * qty;
                totalPrice.setText("LKR " + String.valueOf(total));

            }
        });

        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // because we dont want the quantity go less than 0
                if (qty == 1) {
                    Toast.makeText(getContext(), "Can't decrease quantity", Toast.LENGTH_SHORT).show();
                }
                else {
                    qty--;
                    displayQuantity();
                     total = basePrice * qty;
                   totalPrice.setText("LKR " + String.valueOf(total));

                }

            }
        });


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(type == "Regular")
               {
                   price = regular;
               }

               else if(type == "Large")
               {
                   price = large;
               }

               else
               {
                   return;
               }

                addToCart();
                Toast.makeText(getContext(),"Added Item" , Toast.LENGTH_SHORT).show();

            }
        });


        return view;

    }


    private void displayQuantity() {
        quantity.setText(String.valueOf(qty));
    }

    private void setRegular() {
        type = "Regular";
        basePrice = Integer.parseInt(regular);
         total = basePrice * qty;
        totalPrice.setText("Rs. " + String.valueOf(total));
    }

    private void setLarge() {
        type = "Large";
        basePrice = Integer.parseInt(large);
         total = basePrice * qty;
        totalPrice.setText("Rs. " + String.valueOf(total));
    }


    public void onClick(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){

            case R.id.rbtRegular:
                setRegular();break;

            case R.id.rbtLarge:
                setLarge();break;

        }
    }

    private void addToCart(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UserPhone);
        CartItemModel itemInfo = new CartItemModel(image,name,type,String.valueOf(qty),String.valueOf(total),"Cart");
        databaseReference.child("Cart").push().setValue(itemInfo);

    }


}