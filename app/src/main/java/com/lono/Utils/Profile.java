package com.lono.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Profile {

    static SharedPreferences sharedPreferences;

    public static boolean exists (Activity activity){
        sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("token", null) != null){
            return true;
        }
        return false;
    }

}
