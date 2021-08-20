package com.nightcafe.app.databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ValueManager {

    //variables
    SharedPreferences values;
    SharedPreferences.Editor editor;
    Context context;

    public static final String KEY_total = "total";


    public ValueManager(Context _context){
        context = _context;
        values = context.getSharedPreferences("Values",Context.MODE_PRIVATE);
        editor = values.edit();
    }

    public void saveValues(String total)
    {

        editor.putString(KEY_total,total);

        editor.commit();
    }

    public HashMap<String, String> getValues(){
        HashMap<String,String> valueData = new HashMap<String,String>();

        valueData.put(KEY_total,values.getString(KEY_total,null));

        return valueData;
    }


}
