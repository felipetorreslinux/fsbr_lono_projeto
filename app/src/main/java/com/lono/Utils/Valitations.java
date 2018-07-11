package com.lono.Utils;

import android.widget.EditText;

public class Valitations {

    public static boolean valid_email (EditText editText){
        if(editText.getText().toString().indexOf("@") == -1 || editText.getText().toString().indexOf(".") == -1){
            return false;
        }else{
            return true;
        }
    }
}
