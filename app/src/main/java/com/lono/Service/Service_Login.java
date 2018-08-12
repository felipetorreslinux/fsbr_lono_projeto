package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Views.View_Check_Cellphone;
import com.lono.Views.View_Principal;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    Activity activity;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public Service_Login(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        this.sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
    }



    public void check(String email, String password){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("senha", password);
            AndroidNetworking.post(Server.URL()+"services/efetuar-login")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                switch (status){
                                    case "success":
                                        Alerts.progress_clode();
                                        editor.putString("token", response.getString("authcode"));
                                        editor.commit();
                                        info_profile(response.getString("authcode"));
                                        break;
                                    default:
                                        Alerts.progress_clode();
                                        editor.putString("token", "");
                                        editor.commit();
                                        builder.setTitle("Ops!!!");
                                        builder.setMessage("Email e senha inválidos");
                                        builder.setPositiveButton("Ok",null);
                                        builder.create().show();

                                }
                            } catch (JSONException e) {}
                        }
                        @Override
                        public void onError(ANError anError) {
                            Alerts.progress_clode();
                           Server.ErrorServer(activity, anError.getErrorCode());
                        }
                    });
        }catch (JSONException e){}

    }

    public void info_profile(String token){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            AndroidNetworking.post(Server.URL()+"services/check-token")
            .addJSONObjectBody(jsonObject)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        editor.putString("email", response.getJSONObject("usuario_info").getString("email"));
                        editor.commit();
                        if(editor.commit()){
                            Alerts.progress_clode();
                            activity.startActivity(new Intent(activity, View_Check_Cellphone.class));
                            activity.finish();
                        }
                    } catch (JSONException e) {}
                }
                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
        } catch (JSONException e) {}
    }

}

