package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.List_Plans_Adapter;
import com.lono.Models.List_Plans_Model;
import com.lono.R;
import com.lono.Utils.Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_List_Plans {

    Activity activity;
    SharedPreferences.Editor editor;
    public Service_List_Plans(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("plans", Context.MODE_PRIVATE).edit();
    }

    public void list() {
        AndroidNetworking.post(Server.URL()+"services/obter-lista-planos")
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try{
                    String status = response.getString("status");
                    switch (status){
                        case "success":
                            JSONArray plans = response.getJSONArray("planos");
                            editor.putString("list", plans.toString());
                            editor.commit();
                            break;
                    }
                }catch (JSONException e){}catch (NullPointerException e){}
            }

            @Override
            public void onError(ANError anError) {
                Alerts.progress_clode();
                Server.ErrorServer(activity, anError.getErrorCode());
            }
        });
    }

}
