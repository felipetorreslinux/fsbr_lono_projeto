package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_List_Terms;
import com.lono.Models.Terms_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Terms_Journals {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Terms_Journals(Activity activity){
        this.activity = activity;
        this.builder  = new AlertDialog.Builder(activity);
    }

    public void listTerms (final RecyclerView recyclerView){
        AndroidNetworking.post(Server.URL()+"services/listar-termos-cliente")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray jsonArray = response.getJSONArray("termos");
                                List<Terms_Model> list_terms = new ArrayList<Terms_Model>();
                                list_terms.clear();
                                if(jsonArray.length() > 0){
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Terms_Model termsModel = new Terms_Model(jsonObject);
                                        list_terms.add(termsModel);
                                    }
                                    Adapter_List_Terms adapterListTerms = new Adapter_List_Terms(activity, list_terms);
                                    recyclerView.setAdapter(adapterListTerms);
                                }else{

                                }
                                break;
                            default:

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
