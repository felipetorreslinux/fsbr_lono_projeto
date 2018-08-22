package com.lono.Service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_Terms_Fragment;
import com.lono.Models.Terms_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Terms_Journals {

    Activity activity;

    public Service_Terms_Journals(Activity activity){
        this.activity = activity;
    }

    public void listTerms (final RecyclerView recyclerView, final ProgressBar progress_terms){
        progress_terms.setVisibility(View.VISIBLE);
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
                                    if(jsonArray.length() > 0){
                                        List<Terms_Model> list_terms = new ArrayList<Terms_Model>();
                                        list_terms.clear();
                                        for(int i = 0; i < jsonArray.length(); i++){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Terms_Model termsModel = new Terms_Model(
                                                    jsonObject.getInt("id_nome_pesquisa"),
                                                    jsonObject.getString("nome_pesquisa"),
                                                    jsonObject.getBoolean("todos_jornais"),
                                                    jsonObject.getBoolean("literal"));
                                            list_terms.add(termsModel);
                                        }
                                        Adapter_Terms_Fragment adapterListTerms = new Adapter_Terms_Fragment(activity, list_terms);
                                        recyclerView.setAdapter(adapterListTerms);
                                        progress_terms.setVisibility(View.GONE);
                                    }else{
                                        progress_terms.setVisibility(View.VISIBLE);
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
