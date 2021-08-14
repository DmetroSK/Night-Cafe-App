package com.nightcafe.app.databases;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class CardManager {

    //variables
    SharedPreferences cardInfo;
    SharedPreferences.Editor editor;
    Context context;

    public static final String KEY_name = "name";
    public static final String KEY_number = "number";
    public static final String KEY_year = "year";
    public static final String KEY_month = "month";
    public static final String KEY_cvv = "cvv";

    public CardManager(Context _context){
        context = _context;
        cardInfo = context.getSharedPreferences("userCardInfo",Context.MODE_PRIVATE);
        editor = cardInfo.edit();
    }

    public void saveCardInfo(String name,String number,String year,String month,String cvv)
    {

        editor.putString(KEY_name,name);
        editor.putString(KEY_number,number);
        editor.putString(KEY_year,year);
        editor.putString(KEY_month,month);
        editor.putString(KEY_cvv,cvv);

        editor.commit();
    }

    public HashMap<String, String> getCardInfo(){
        HashMap<String,String> cardData = new HashMap<String,String>();

        cardData.put(KEY_name,cardInfo.getString(KEY_name,null));
        cardData.put(KEY_number,cardInfo.getString(KEY_number,null));
        cardData.put(KEY_year,cardInfo.getString(KEY_year,null));
        cardData.put(KEY_month,cardInfo.getString(KEY_month,null));
        cardData.put(KEY_cvv,cardInfo.getString(KEY_cvv,null));

        return cardData;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

}
