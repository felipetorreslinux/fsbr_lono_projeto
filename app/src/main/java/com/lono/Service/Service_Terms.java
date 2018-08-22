package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Models.Terms_Model;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Terms {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Terms(Activity activity){
        this.activity = activity;
        this.builder  = new AlertDialog.Builder(activity);
    }

    public void addTerms (String terms, boolean literal){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nome_pesquisa", terms);
            jsonObject.put("literal", literal);
            AndroidNetworking.post(Server.URL()+"services/adicionar-termo-cliente")
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
                                Alerts.progress_clode();
                                Intent intent = activity.getIntent();
                                activity.setResult(Activity.RESULT_OK, intent);
                                activity.finish();
                                break;
                            default:
                                Alerts.progress_clode();
                                builder.setTitle(R.string.app_name);
                                builder.setMessage(response.getString("message"));
                                builder.setPositiveButton("Ok", null);
                                builder.create().show();
                                break;
                        }
                    }catch (JSONException e){}
                }
                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
        }catch (JSONException e){}
    }

    public void removeTerms (int id){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_nome_pesquisa", id);
            AndroidNetworking.post(Server.URL()+"services/remover-termo-cliente")
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
                                Snackbar.make(activity.getWindow().getDecorView(),
                                        response.getString("message"), Snackbar.LENGTH_SHORT).show();
                                break;

                            default:
                                Snackbar.make(activity.getWindow().getDecorView(),
                                        response.getString("message"), Snackbar.LENGTH_SHORT).show();
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
