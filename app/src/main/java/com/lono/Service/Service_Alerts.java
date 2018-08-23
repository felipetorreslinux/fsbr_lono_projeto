package com.lono.Service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_List_Alerts;
import com.lono.Models.Alerts_Model;
import com.lono.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Alerts {

    Activity activity;


    public Service_Alerts(Activity activity){
        this.activity = activity;
    }

    public void list_alerts (final RecyclerView recyclerView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
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
                                    List<Alerts_Model> list_alerts = new ArrayList<>();
                                    list_alerts.clear();
                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Alerts_Model alertsModel = new Alerts_Model(
                                                jsonObject.getInt("id_notificacao"),
                                                jsonObject.getString("mensagem"),
                                                jsonObject.getString("assunto"),
                                                jsonObject.getString("elapsed_time"),
                                                jsonObject.getBoolean("lida"));
                                        list_alerts.add(alertsModel);
                                    }
                                    Adapter_List_Alerts adapterListAlerts = new Adapter_List_Alerts(activity, list_alerts);
                                    recyclerView.setAdapter(adapterListAlerts);
                                    progressBar.setVisibility(View.GONE);
                                }else{
                                    progressBar.setVisibility(View.VISIBLE);
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

    public void readMessage(Alerts_Model alerts_model){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_notificacao", alerts_model.getId());
            AndroidNetworking.post(Server.URL()+"services/marcar-notificacao-lida")
                    .addHeaders("token", Server.token(activity))
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                String status = response.getString("status");
                                switch (status){
                                    case "success":
                                        break;
                                }
                            }catch (JSONException e){}
                        }

                        @Override
                        public void onError(ANError anError) {
                            Server.ErrorServer(activity, anError.getErrorCode());
                        }
                    });
        }catch (JSONException e){}
    }
}
