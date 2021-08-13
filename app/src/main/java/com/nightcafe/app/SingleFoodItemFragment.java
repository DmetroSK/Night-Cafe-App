package com.nightcafe.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.nightcafe.app.authentication.SignInActivity;


public class SingleFoodItemFragment extends Fragment {

    String name,image,regular,large;
    TextView quantity,totalPrice;
    RadioButton radioButton;
    int qty=1,basePrice;
    RadioGroup radioGroup;
    Button cartButton;


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
        regularPrice.setText("LKR " + regular);
        regularTag.setText("LKR " + regular);
        largeTag.setText("LKR " + large);
        totalPrice.setText("LKR " + regular);

        radioGroup = view.findViewById(R.id.radio);

        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = view.findViewById(selectedId);

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
                totalPrice.setText("LKR " + String.valueOf(regular));

            }
        });




        plusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                qty++;
                displayQuantity();
                int total = basePrice * qty;
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
                    int total = basePrice * qty;
                   totalPrice.setText("LKR " + String.valueOf(total));


                }

            }
        });



        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getContext(),radioButton.getText() , Toast.LENGTH_SHORT).show();

            }
        });



        return view;

    }



    private void displayQuantity() {
        quantity.setText(String.valueOf(qty));
    }

    private void setRegular() {
        basePrice = Integer.parseInt(regular);
        int total = basePrice * qty;
        totalPrice.setText("LKR " + String.valueOf(total));
    }

    private void setLarge() {
        basePrice = Integer.parseInt(large);
        int total = basePrice * qty;
        totalPrice.setText("LKR " + String.valueOf(total));
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


}