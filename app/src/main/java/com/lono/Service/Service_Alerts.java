package com.lono.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Service_Alerts {

    Activity activity;
    SharedPreferences.Editor editor;

    public Service_Alerts(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("all_alerts", Context.MODE_PRIVATE).edit();
    }

    public void list_alerts(){
        AndroidNetworking.post(Server.URL()+"services/listar-notificacoes")
            .addHeaders("token", Server.token(activity))
            .addBodyParameter("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray jsonArray = response.getJSONArray("notification_list");
                                if(jsonArray.length() > 0){
                                    editor.putString("alerts", jsonArray.toString());
                                    editor.commit();
                                }else{
                                    editor.putString("alerts", "");
                                    editor.commit();
                                }
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
    }
}
