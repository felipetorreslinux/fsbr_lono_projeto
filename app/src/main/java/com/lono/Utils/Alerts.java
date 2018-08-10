package com.lono.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.model.Progress;
import com.lono.R;

public class Alerts {

    static ProgressDialog progressDialog;

    public static void curto (Activity activity, int message){
        switch (message){
            case 0:
                Toast.makeText(activity, R.string.not_connected, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }

    }

    public static void snacs (View view, int message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void progress_open (Activity activity, String title, String message, boolean cancelable){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    public static void progress_clode (){
        progressDialog.dismiss();
    }

    public static void conexao_error(int code, View view){
        Snackbar.make(view, R.string.not_connected, Snackbar.LENGTH_SHORT).show();
    }

}
