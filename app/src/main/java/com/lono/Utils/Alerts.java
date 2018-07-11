package com.lono.Utils;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Alerts {

    public static void curto (Activity activity, int message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void snacs (View view, int message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
