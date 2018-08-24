package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.Firebase.PhoneNumberSMS.PhoneNumberFirebase;
import com.lono.Utils.Alerts;
import com.lono.Views.View_Login;
import com.lono.Views.View_Plans_List;
import com.lono.Views.View_Principal;
import com.lono.Views.View_Validation_SMS;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Login(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    public void check_cellphone (final String cellphone){

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("telefone", cellphone);
            AndroidNetworking.post(Server.URL()+"services/telefone-cadastrado")
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
                                    Intent intent = new Intent(activity, View_Validation_SMS.class);
                                    intent.putExtra("cellphone", cellphone);
                                    intent.putExtra("token", response.getString("authcode"));
                                    activity.startActivity(intent);
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    Intent new_ac = new Intent(activity, View_Plans_List.class);
                                    new_ac.putExtra("cellphone", cellphone);
                                    activity.startActivity(new_ac);
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
                                SharedPreferences.Editor editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
                                switch (status){
                                    case "success":
                                        Alerts.progress_clode();
                                        editor.putString("token", response.getString("authcode"));
                                        editor.commit();
                                        Intent intent = new Intent(activity, View_Principal.class);
                                        activity.startActivity(intent);
                                        activity.finishAffinity();
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

                        SharedPreferences.Editor editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();

                        editor.putInt("id", response.getJSONObject("usuario_info").getInt("id"));
                        editor.putString("name", response.getJSONObject("usuario_info").getString("nome"));
                        editor.putString("avatar_url", response.getJSONObject("usuario_info").getString("avatar_url") != null ? response.getJSONObject("usuario_info").getString("avatar_url") : "");
                        editor.putString("email", response.getJSONObject("usuario_info").getString("email"));
                        editor.putLong("created_at", response.getJSONObject("usuario_info").getLong("dat_cad"));
                        editor.putBoolean("admin", response.getJSONObject("usuario_info").getBoolean("admin"));
                        editor.putBoolean("exibir_notificacoes", response.getJSONObject("usuario_info").getBoolean("exibir_notificacoes"));

                        editor.putString("type_account", response.getJSONObject("conta_info").getString("tipo"));
                        editor.putString("name_plan", response.getJSONObject("conta_info").getString("nome_plano"));
                        editor.putString("name_account", response.getJSONObject("conta_info").getString("nome"));
                        editor.putString("name_contact", response.getJSONObject("conta_info").getString("nome_contato"));
                        editor.putString("document", response.getJSONObject("conta_info").getString("docnum"));
                        editor.putString("razao_social", response.getJSONObject("conta_info").getString("razao"));
                        editor.putString("cellphone_account", response.getJSONObject("conta_info").getString("telefone"));

                        editor.putString("cep", response.getJSONObject("conta_info").getString("cep"));
                        editor.putString("lougradouro", response.getJSONObject("conta_info").getString("logradouro"));
                        editor.putString("numero", response.getJSONObject("conta_info").getString("numero"));
                        editor.putString("complemento", response.getJSONObject("conta_info").getString("complemento"));
                        editor.putString("bairro", response.getJSONObject("conta_info").getString("bairro"));
                        editor.putString("cidade", response.getJSONObject("conta_info").getString("cidade"));
                        editor.putString("estado", response.getJSONObject("conta_info").getString("uf"));

                        editor.putString("sit_cad", response.getJSONObject("conta_info").getString("sit_cad"));
                        editor.putInt("advogado", response.getJSONObject("conta_info").getInt("advogado"));

                        editor.commit();

                    } catch (JSONException e) {}
                }
                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    System.out.println(anError.getMessage());
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
        } catch (JSONException e) {}
    }

}

