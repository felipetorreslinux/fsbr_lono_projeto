package com.lono.Utils;

import java.util.Calendar;

public class ValidData {
    public static boolean check (String data){
        String[] dt = data.split("/");
        int day = Integer.parseInt(dt[0]);
        int month = Integer.parseInt(dt[1]);
        int year = Integer.parseInt(dt[2]);
        if(day > 31){
            return false;
        }else if((month > 12) | (month == 0)){
            return false;
        }else if(year > 2000){
            return false;
        }else{
            return true;
        }
    }

    public static boolean checkDataCard(String data){
        String[] dt = data.split("/");
        int month = Integer.parseInt(dt[0]);
        int year = Integer.parseInt(dt[1]);
        if(month > 12 || month == 0){
            return false;
        }else if(year < Calendar.getInstance().get(Calendar.YEAR)){
            return false;
        }else{
            return true;
        }
    }
}
