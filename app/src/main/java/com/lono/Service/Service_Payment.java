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

    public void check_cpf (String cpf, final EditText editText){
        AndroidNetworking.get(Server.cpf()+"api/check_cpf/"+ MaskCPF.unmask(cpf))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int code = response.getInt("code");
                            switch (code){
                                case 0:
                                    Alerts.progress_clode();
                                    String name = response.getJSONObject("content").getJSONObject("cpf").getString("name");
                                    editText.setText(name);
                                    editText.setEnabled(false);
                                    break;

                                case 35:
                                    Alerts.progress_clode();
                                    String new_name = response.getJSONObject("content").getJSONObject("user_info").getString("name");
                                    editText.setText(new_name);
                                    editText.setEnabled(false);
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    builder.setTitle("Ops!!!");
                                    builder.setMessage("CPF inválido");
                                    builder.setPositiveButton("Ok", null);
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
    };

    public void addCard(String token, final String number, final String month, final String year, final String document, final String name, final String price){
        final AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        try {
            JSONObject jsonObject = new JSONObject(  );
            jsonObject.put( "token", token );
            jsonObject.put( "numero_cartao", number );
            jsonObject.put( "validade_mes", month );
            jsonObject.put( "validade_ano", year );
            jsonObject.put("valor", price);
            jsonObject.put("nome_titular", name);

            System.out.println(jsonObject);

            AndroidNetworking.post( Server.URL()+"services/adicionar-cartao-usuario" )
                    .addJSONObjectBody( jsonObject )
                    .build()
                    .getAsJSONObject( new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                String status = response.getString( "status" );
                                switch (status){
                                    case "success":
                                        Alerts.progress_clode();
                                        JSONArray array = response.getJSONArray("parcelas");
                                        System.out.println(array);
                                        Intent intent = activity.getIntent();
                                        intent.putExtra( "number", number );
                                        intent.putExtra( "month", month );
                                        intent.putExtra( "year", year );
                                        intent.putExtra( "document", document);
                                        intent.putExtra( "name", name );
                                        intent.putExtra("array_parcelas", array.toString());
                                        activity.setResult( Activity.RESULT_OK, intent );
                                        activity.finish();
                                        break;
                                    default:
                                        Alerts.progress_clode();
                                        builder.setTitle( "Ops!!!" );
                                        builder.setMessage( response.getString("mesaage") );
                                        builder.setCancelable( false );
                                        builder.setPositiveButton( "Ok", null );
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

    public void paymentBoleto(String token, String qtd_terms, String type_plam, final String senderHash, final CardView cardView, final TextView textView, final Button button_pay){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("qtd_termos", Integer.parseInt(qtd_terms));
            jsonObject.put("tipo_plano", type_plam);
            jsonObject.put("forma_pagamento", "boleto");
            jsonObject.put("sender_hash", senderHash);
            AndroidNetworking.post(Server.URL()+"services/contratar-plano")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            String status = response.getString("status");
                            switch (status){
                                case "success":
                                    Alerts.progress_clode();
                                    cardView.setVisibility(View.VISIBLE);
                                    button_pay.setVisibility(View.VISIBLE);
                                    textView.setText(response.getString("boleto_barcode"));
                                    break;

                                default:
                                    Alerts.progress_clode();
                                    cardView.setVisibility(View.GONE);
                                    button_pay.setVisibility(View.GONE);
                                    textView.setText(null);
                                    builder.setTitle("Ops!!!");
                                    builder.setMessage(response.toString());
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
