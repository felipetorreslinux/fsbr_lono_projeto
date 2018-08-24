package com.lono.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Adapter.Adapter_Journals_Fragment;
import com.lono.Adapter.Adapter_Terms_Fragment;
import com.lono.Models.Journals_Model;
import com.lono.Models.Terms_Model;
import com.lono.R;
import com.lono.Utils.Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Terms_Journals {

    Activity activity;
    SharedPreferences.Editor editor;

    public Service_Terms_Journals(Activity activity){
        this.activity = activity;
        this.editor = activity.getSharedPreferences("all_journals", Context.MODE_PRIVATE).edit();
    }

    public void listTerms (final RecyclerView recyclerView, final ProgressBar progress_terms, final LinearLayout layout_box_termos){
        progress_terms.setVisibility(View.VISIBLE);
        layout_box_termos.setVisibility(View.GONE);
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
                                        layout_box_termos.setVisibility(View.VISIBLE);
                                    }else{
                                        progress_terms.setVisibility(View.VISIBLE);
                                        layout_box_termos.setVisibility(View.GONE);
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

    public void listAllJournals (){
        AndroidNetworking.post(Server.URL()+"services/listar-todos-jornais")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray jsonArray = response.getJSONArray("jornais");
                                editor.putString("journals", jsonArray.toString());
                                editor.commit();
                                break;
                            default:
                                editor.putString("journals", "");
                                editor.commit();
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {

                }
            });
    }

    public void listJournals (final RecyclerView recyclerView, final LinearLayout layout_box_jornais){
        final List<Journals_Model> list_journals = new ArrayList<Journals_Model>();
        list_journals.clear();
        layout_box_jornais.setVisibility(View.GONE);
        AndroidNetworking.post(Server.URL()+"services/listar-jornais-cliente")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray jsonArray = response.getJSONArray("jornais");
                                if(jsonArray.length() > 0){
                                    for (int i= 0; i < jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Journals_Model journalsModel = new Journals_Model(
                                                jsonObject.getInt("id_jornal"),
                                                jsonObject.getString("sigla_jornal"),
                                                jsonObject.getString("nome_jornal"),
                                                jsonObject.getString("nome_orgao"),
                                                jsonObject.getString("sigla_orgao"));
                                        list_journals.add(journalsModel);
                                    }
                                    Adapter_Journals_Fragment adapterJournalsFragment = new Adapter_Journals_Fragment(activity, list_journals);
                                    recyclerView.setAdapter(adapterJournalsFragment);
                                }
                                layout_box_jornais.setVisibility(View.VISIBLE);
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

    public void listJournalsSpinner (final Spinner spinner, final List<String> list){
        AndroidNetworking.post(Server.URL()+"services/listar-jornais-cliente")
                .addHeaders("token", Server.token(activity))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status = response.getString("status");
                            switch (status){
                                case "success":
                                    JSONArray jsonArray = response.getJSONArray("jornais");
                                    if(jsonArray.length() > 0){
                                        for (int i= 0; i < jsonArray.length(); i++){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            list.add(jsonObject.getString("sigla_jornal"));
                                        }
                                    }else{
                                        list.add("Não há jornais cadastrados");
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, list);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                    spinner.setAdapter(arrayAdapter);
                                    list.remove(0);
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
        Snackbar.make(activity.getWindow().getDecorView(),
                "Adicionando termo...", Snackbar.LENGTH_SHORT).show();
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
                                        Snackbar.make(activity.getWindow().getDecorView(),
                                                "Termos adicionado com sucesso", Snackbar.LENGTH_SHORT).show();
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

    public void addJournal(int id, final String name){
        Snackbar.make(activity.getWindow().getDecorView(),
                "Adicionando " + name, Snackbar.LENGTH_SHORT).show();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_jornal", id);
            AndroidNetworking.post(Server.URL()+"services/adicionar-jornal-cliente")
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
                                            name + " adicionado com sucesso", Snackbar.LENGTH_SHORT).show();
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
