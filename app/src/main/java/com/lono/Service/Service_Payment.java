package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Views.View_Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.logging.StreamHandler;

public class Service_Payment {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Payment(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    public void checkCEP (EditText cep, final EditText logradouro, final EditText numero, final EditText bairro, final EditText cidade, final EditText estado){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cep", cep.getText().toString().trim());
            AndroidNetworking.post(Server.URL()+"services/consultar-cep")
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
                                        JSONObject dados = response.getJSONObject("cep_info");
                                        logradouro.setText(dados.getString("logradouro"));
                                        bairro.setText(dados.getString("bairro"));
                                        cidade.setText(dados.getString("cidade"));
                                        estado.setText(dados.getString("estado"));
                                        numero.requestFocus();
                                        break;
                                    default:
                                        Alerts.progress_clode();
                                        logradouro.requestFocus();
                                        logradouro.setText(null);
                                        numero.setText(null);
                                        bairro.setText(null);
                                        cidade.setText(null);
                                        estado.setText(null);
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

    public void paymentCard(String token, String qtd_terms, String type_plam, String hash, String token_card, String cep, String number_local, String cpf, String name, String data_nasc, String cellphone, String parcells ){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("qtd_termos", qtd_terms);
            jsonObject.put("tipo_plano", type_plam);
            jsonObject.put("forma_pagamento", "cartao");
            jsonObject.put("sender_hash", hash);
            jsonObject.put("card_token", token_card);
            jsonObject.put("cep", cep);
            jsonObject.put("numero", number_local);
            jsonObject.put("cpf_titular", cpf);
            jsonObject.put("nome_titular", name);
            jsonObject.put("data_nas_titular", data_nasc);
            jsonObject.put("telefone_titular", cellphone);
            jsonObject.put("num_parcelas", parcells);

            AndroidNetworking.post(Server.URL()+"services/contratar-plano")
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
                                    builder.setTitle(R.string.app_name);
                                    builder.setMessage(response.getString("message"));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.finishAffinity();
                                            Intent intent = new Intent(activity, View_Login.class);
                                            activity.startActivity(intent);
                                        }
                                    });
                                    builder.create().show();
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    builder.setTitle(R.string.app_name);
                                    builder.setMessage(response.getString("message"));
                                    builder.setCancelable(false);
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
                        builder.setTitle(R.string.app_name);
                        builder.setMessage(anError.getMessage());
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        });
                        builder.create().show();
                    }
                });

        }catch (JSONException e){}

    }

    public void paymentBoleto(String token, String qtd_terms, String type_plam, final String senderHash, final LinearLayout linearLayout, final TextView textView, final Button button_pay){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("qtd_termos", Integer.parseInt(qtd_terms));
            jsonObject.put("tipo_plano", type_plam);
            jsonObject.put("forma_pagamento", "boleto");
            jsonObject.put("sender_hash", senderHash);

            System.out.println(jsonObject);

            AndroidNetworking.post(Server.URL()+"services/contratar-plano")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            switch (status){
                                case "success":
                                    linearLayout.setVisibility(View.VISIBLE);
                                    button_pay.setVisibility(View.VISIBLE);
                                    button_pay.setText("Boleto gerado com sucesso\nClique aqui para acessar");
                                    button_pay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            activity.finishAffinity();
                                            Intent intent = new Intent(activity, View_Login.class);
                                            activity.startActivity(intent);
                                        }
                                    });
                                    textView.setText(response.getString("boleto_barcode"));
                                    break;

                                default:
                                    linearLayout.setVisibility(View.GONE);
                                    button_pay.setVisibility(View.GONE);
                                    textView.setText(null);
                                    builder.setTitle("Ops!!!");
                                    builder.setMessage(response.getString("message"));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            builder.create().dismiss();
                                        }
                                    });
                                    builder.create().show();
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

}
