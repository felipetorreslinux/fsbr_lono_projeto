package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.lono.APIServer.Server;
import com.lono.Adapter.List_Plans_Adapter;
import com.lono.Models.List_Plans_Model;
import com.lono.R;
import com.lono.Utils.Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service_List_Plans {

    static Activity activity;
    static AlertDialog.Builder builder;
    public Service_List_Plans(Activity activity){
        this.activity = activity;
        builder = new AlertDialog.Builder( activity );
    }

    public void list (final RecyclerView recyclerView) {
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
                            if(plans.length() > 0){
                                List<List_Plans_Model> list_plans = new ArrayList<>();
                                for (int i = 0; i < plans.length(); i++){
                                    JSONObject jsonObject = plans.getJSONObject(i);
                                    List_Plans_Model list_plans_model = new List_Plans_Model(jsonObject);
                                    list_plans.add(list_plans_model);
                                }
                                List_Plans_Adapter list_plans_adapter = new List_Plans_Adapter(activity, list_plans);
                                recyclerView.setAdapter(list_plans_adapter);
                                Alerts.progress_clode();
                            }
                            break;
                        default:
                            System.out.println(response);
                            Alerts.progress_clode();
                            builder.setTitle( "Ops!!!" );
                            builder.setMessage( response.toString());
                            builder.setCancelable( false );
                            builder.setPositiveButton( "Ok", null );
                            builder.create().show();
                    }
                }catch (JSONException e){}catch (NullPointerException e){}
            }

            @Override
            public void onError(ANError anError) {
                System.out.println(anError.getMessage());
                Alerts.progress_clode();
                if(anError.getErrorCode() == 0){
                    builder.setTitle( "Ops!!!" );
                    builder.setMessage( R.string.not_connected );
                    builder.setCancelable( false );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    } );
                    builder.create().show();
                }else{
                    builder.setTitle( "Ops!!!" );
                    builder.setMessage( anError.getMessage() );
                    builder.setCancelable( false );
                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    } );
                    builder.create().show();
                }
            }
        });
    }

}
