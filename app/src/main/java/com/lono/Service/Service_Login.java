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
import com.lono.Utils.Alerts;
import com.lono.Views.View_Principal;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Login {

    Activity activity;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public Service_Login(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        this.sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
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
                        String name = response.getJSONObject("usuario_info").getString("nome");
                        String email = response.getJSONObject("usuario_info").getString("email");
                        String created_at = response.getJSONObject("usuario_info").getString("data_cad");
                        boolean admin = response.getJSONObject("usuario_info").getBoolean("nome");
                        boolean view_notifications = response.getJSONObject("usuario_info").getBoolean("nomexibir_notificacoese");

                        String type = response.getString("tipo");
                        String name_account = response.getString("nome");
                        String name_contact = response.getString("nome_contato");
                        String document = response.getString("docnum");
                        String razao_social = response.getString("razao");
                        String cellphone = response.getString("telefone");

                        String cep = response.getString("cep");
                        String logradouro = response.getString("logradouro");
                        String numero = response.getString("numero");
                        String complemento = response.getString("complemento");
                        String bairro = response.getString("bairro");
                        String cidade = response.getString("cidade");
                        String estado = response.getString("uf");

                        String situacao_cad = response.getString("sit_cad");
                        int advoagodos = response.getInt("advogado");

                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("created_at", created_at);
                        editor.putBoolean("admin", admin);
                        editor.putBoolean("view_notifications", view_notifications);

                        editor.putString("type_account", type);
                        editor.putString("name_account", name_account);
                        editor.putString("name_contact", name_contact);
                        editor.putString("document", document);
                        editor.putString("razao_social", razao_social);
                        editor.putString("cellphone_account", cellphone);

                        editor.putString("cep", cep);
                        editor.putString("lougradouro", logradouro);
                        editor.putString("numero", numero);
                        editor.putString("complemento", complemento);
                        editor.putString("bairro", bairro);
                        editor.putString("cidade", cidade);
                        editor.putString("estado", estado);

                        editor.putString("situacao_cad", situacao_cad);
                        editor.putInt("advogado", advoagodos);

                        editor.commit();

                    } catch (JSONException e) {}
                }
                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
        } catch (JSONException e) {}
    }

}

