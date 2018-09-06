package com.lono.Utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Valitations {

    public static boolean email (String email) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }

    public static boolean password (String password){
        if(password.length() < 6){
            return false;
        }else{
            return true;
        }
    }

    public static String name_profile (String name){
        String[] array = name.split(" ");
        String name_pro = null;
        if(array[1].equals("da") || array[1].equals("de") || array[1].equals("do")){
            name_pro = array[0] + " " + array[1] + " " + array[2];
        }else{
            name_pro = array[0] + " " + array[1];
        }
        return name;
    }

    public static String convertSpinnerStatusPub(String status){
        String response = null;
        switch (status){
            case "Todos":
                response = "";
                break;

            case "Lidas":
                response = "L";
                break;

            case "Não Lidas":
                response = "N";
                break;

            case "Lidas e Não Lidas":
                response = "LN";
                break;

            case "Ignoradas":
                response = "I";
                break;
        }
        return response;
    }


}
