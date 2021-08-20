package com.nightcafe.app.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.nightcafe.app.orders.CheckoutFragment;
import com.nightcafe.app.R;
import com.nightcafe.app.databases.CardManager;
import java.util.HashMap;

public class CardInfoFragment extends Fragment {

    CardManager cardManager;
    String name,number,year,month,cvv,ref;
    TextInputLayout cardName,cardNumber,cardYear,cardMonth,cardCvv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // Get value from fragment
            Bundle bundle = this.getArguments();
             if (bundle != null) {
                 ref = bundle.getString("ref"," ");
            }
             else {
                 ref =" ";
             }

        View view = inflater.inflate(R.layout.fragment_card_info, container, false);

        //Elements define
        ImageView back = view.findViewById(R.id.arrow);
        cardName = view.findViewById(R.id.cardHolderName);
        cardNumber = view.findViewById(R.id.number);
        cardYear = view.findViewById(R.id.year);
        cardMonth = view.findViewById(R.id.month);
        cardCvv = view.findViewById(R.id.cvv);
        Button save = view.findViewById(R.id.btnSave);

        //session create
        cardManager = new CardManager(getContext());
        HashMap<String,String> cardDetails = cardManager.getCardInfo();

        //Set Values
        cardName.getEditText().setText(cardDetails.get(CardManager.KEY_name));
        cardNumber.getEditText().setText(cardDetails.get(CardManager.KEY_number));
        cardYear.getEditText().setText(cardDetails.get(CardManager.KEY_year));
        cardMonth.getEditText().setText(cardDetails.get(CardManager.KEY_month));
        cardCvv.getEditText().setText(cardDetails.get(CardManager.KEY_cvv));

        //save button press
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();

                if(ref.equals("chk")){
                    //setting fragment open
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new CheckoutFragment()).addToBackStack(null).commit();
                }

                else {
                    //setting fragment open
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SettingsFragment()).addToBackStack(null).commit();
                }

                //fragment finish back press not redirect
                getActivity().getFragmentManager().popBackStack();
            }
        });

              return view;

    }

    private void saveData(){
        name = validateName();
        number = validateCardNumber();
        year = validateYear();
        month = validateMonth();
        cvv = validateCVV();



        if(name != null && number != null && year != null && month != null && cvv != null)
        {
            cardManager.saveCardInfo(name,number,year,month,cvv);
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Not Saved", Toast.LENGTH_SHORT).show();
        }


    }

    private String validateName() {

        String card_name = cardName.getEditText().getText().toString();

        if (card_name.isEmpty()) {
            cardName.setError("Field can not be empty");
            return null;
        } else {
            cardName.setError(null);
            cardName.setErrorEnabled(false);
            return card_name;
        }
    }

    private String validateCardNumber() {

        String card_number = cardNumber.getEditText().getText().toString();

        if (card_number.isEmpty()) {
            cardNumber.setError("Field can not be empty");
            return null;
        }
        else if(card_number.length()<12){
            cardNumber.setError("Invalid Card");
            return null;
        }
        else {
            cardNumber.setError(null);
            cardNumber.setErrorEnabled(false);
            return card_number;
        }
    }

    private String validateYear() {

        String card_year = cardYear.getEditText().getText().toString().trim();
        int num = Integer.parseInt(card_year);

        if (card_year.isEmpty()) {
            cardYear.setError("Not empty");
            return null;
        }
        else if(num < 21 || num > 26){
            cardYear.setError("Invalid Year");
            return null;
        }
        else {
            cardYear.setError(null);
            cardYear.setErrorEnabled(false);
            return card_year;
        }
    }

    private String validateMonth() {

        String card_month = cardMonth.getEditText().getText().toString().trim();
        int num = Integer.parseInt(card_month);

        if (card_month.isEmpty()) {
            cardMonth.setError("Not empty");
            return null;
        }
        else if(num < 1 || num >12){
            cardMonth.setError("Invalid Month");
            return null;
        }
        else {
            cardMonth.setError(null);
            cardMonth.setErrorEnabled(false);
            return card_month;
        }
    }

    private String validateCVV() {

        String card_cvv = cardCvv.getEditText().getText().toString().trim();
        int num = Integer.parseInt(card_cvv);

        if (card_cvv.isEmpty()) {
            cardCvv.setError("Not empty");
            return null;
        }
        else if(card_cvv.length()<3){
            cardCvv.setError("Invalid CVV");
            return null;
        }

        else {
            cardCvv.setError(null);
            cardCvv.setErrorEnabled(false);
            return card_cvv;
        }
    }

}