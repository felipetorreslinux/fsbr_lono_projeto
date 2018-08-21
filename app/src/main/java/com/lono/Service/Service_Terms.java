package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_List_Terms;
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
    TextView textView;
    RecyclerView recyclerView;

    public Service_Terms(Activity activity){
        this.activity = activity;
        this.builder  = new AlertDialog.Builder(activity);
        this.recyclerView = activity.findViewById(R.id.recycler_terms);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.textView = activity.findViewById(R.id.text_info_terms);
    }

    public void listTerms (){
        textView.setText("Carregando termos...");
        textView.setVisibility(View.VISIBLE);
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
                                        Terms_Model termsModel = new Terms_Model(
                                                jsonObject.getInt("id_nome_pesquisa"),
                                                jsonObject.getString("nome_pesquisa"),
                                                jsonObject.getBoolean("todos_jornais"),
                                                jsonObject.getBoolean("literal"));
                                        list_terms.add(termsModel);
                                    }
                                    Adapter_List_Terms adapterListTerms = new Adapter_List_Terms(activity, list_terms);
                                    recyclerView.setAdapter(adapterListTerms);
                                    textView.setText(null);
                                    textView.setVisibility(View.GONE);
                                }else{
                                    textView.setText("Você não tem termos cadastrados");
                                    textView.setVisibility(View.VISIBLE);
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
                                listTerms();
                                Snackbar.make(activity.getWindow().getDecorView(),
                                        response.getString("message"), Snackbar.LENGTH_SHORT).show();
                                Keyboard.close(activity, activity.getWindow().getDecorView());
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
                                listTerms();
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
