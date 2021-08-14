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
import com.nightcafe.app.CheckoutFragment;
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
        name = cardName.getEditText().getText().toString();
        number = cardNumber.getEditText().getText().toString();
        year = cardYear.getEditText().getText().toString();
        month = cardMonth.getEditText().getText().toString();
        cvv = cardCvv.getEditText().getText().toString();

        cardManager.saveCardInfo(name,number,year,month,cvv);

        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

}