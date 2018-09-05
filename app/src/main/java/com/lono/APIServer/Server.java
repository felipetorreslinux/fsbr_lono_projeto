package com.lono.APIServer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.lono.R;

public class Server {

    public static String hashSession = null;
    public static String sessionPayment = null;

    public static String payment (){
//        return "http://179.188.38.70:8888";
        return "https://engine.lono.com.br";
//        return "http://192.168.15.220";
    }

    public static String URL (){
//        return "http://179.188.38.70:8888/";
        return "https://engine.lono.com.br/";
//        return "http://192.168.15.220:80/";
    };

    public static String token (Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences( "profile", Context.MODE_PRIVATE );
        return sharedPreferences.getString( "token", "" );
    }

    public static void ErrorServer (final Activity activity, int code){
        AlertDialog.Builder builder  = new AlertDialog.Builder(activity);
        switch (code){
            case 0:
                builder.setTitle("Ops!!!");
                builder.setMessage("Aparelho sem conexão com a internet ou nosso servidor está em manutenção.");
                builder.setPositiveButton("Ok", null);
                builder.create().show();
                break;
            case 300:
                builder.setTitle("Ops!!!");
                builder.setMessage("Token inválido.\nFaça seu login novamente");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
                        editor.putString("token", "");
                        editor.commit();
                        activity.finishAffinity();
                    }
                });
                builder.create().show();
                break;
            case 401:
                builder.setTitle("Ops!!!");
                builder.setMessage("Servidor em manutenção - 401");
                builder.setPositiveButton("Ok", null);
                builder.create().show();
                break;
            case 403:
                builder.setTitle("Ops!!!");
                builder.setMessage("Servidor em manutenção - 403");
                builder.setPositiveButton("Ok", null);
                builder.create().show();
                break;
            case 404:
                builder.setTitle("Ops!!!");
                builder.setMessage("Servidor em manutenção - 404");
                builder.setPositiveButton("Ok", null);
                builder.create().show();
                break;
            case 500:
                builder.setTitle("Ops!!!");
                builder.setMessage("Servidor em manutenção - 500");
                builder.setPositiveButton("Ok", null);
                builder.create().show();
                break;
        }

    }
}
