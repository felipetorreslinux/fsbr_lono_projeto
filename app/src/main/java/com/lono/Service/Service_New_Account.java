package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Views.View_Payment;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_New_Account {

    Activity activity;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;

    public Service_New_Account(Activity activity){
        this.activity = activity;
        editor = activity.getSharedPreferences( "profile", Context.MODE_PRIVATE ).edit();
        builder = new AlertDialog.Builder(activity);
    }

    public void create_more_200(String name, String email, String cellphone, String comments){
        final AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        try {
            JSONObject jsonObject = new JSONObject( );
            jsonObject.put( "id_servico", "2" );
            jsonObject.put( "nome", name );
            jsonObject.put( "telefone", cellphone );
            jsonObject.put( "email", email );
            jsonObject.put( "comentario", comments );
            AndroidNetworking.post( Server.URL()+"services/adicionar-conta" )
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
                                    builder.setTitle( R.string.app_name );
                                    builder.setMessage( "Solicitção enviada com sucesso.\nEntraremos em contato o mais rápido possível." );
                                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = activity.getIntent();
                                            activity.setResult( Activity.RESULT_OK, intent );
                                            activity.finish();
                                        }
                                    });
                                    builder.create().show();
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    builder.setTitle( "Ops!!!" );
                                    builder.setMessage( response.getString( "message" ) );
                                    builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = activity.getIntent();
                                            activity.setResult( Activity.RESULT_OK, intent );
                                            activity.finish();
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
                } );
        }catch (JSONException e){}
    }

    public void create_free (final String name, final String email, String password, String cellphone, String genre, String document){
        final AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setCancelable( false );
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put( "id_servico", "3" );
            jsonObject.put( "senha", password );
            jsonObject.put( "telefone", cellphone );
            jsonObject.put( "email", email );
            jsonObject.put( "nome", name );
            jsonObject.put( "sexo", genre );
            jsonObject.put( "documento", document );

            AndroidNetworking.post( Server.URL()+"services/adicionar-conta" )
            .addJSONObjectBody( jsonObject )
            .build()
            .getAsJSONObject( new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        String status = response.getString( "status" );
                        switch (status){
                            case "success":
                                Alerts.progress_clode();
                                builder.setTitle( R.string.app_name );
                                builder.setMessage("Usuário cadastrado com sucesso");
                                builder.setCancelable(false);
                                builder.setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = activity.getIntent();
                                        activity.setResult( Activity.RESULT_OK, intent );
                                        activity.finish();
                                    }
                                });
                                builder.create().show();
                                break;
                            default:
                                Alerts.progress_clode();
                                builder.setTitle( R.string.app_name );
                                builder.setMessage( response.getString( "message" ) );
                                builder.setCancelable(false);
                                builder.setPositiveButton( "Tente novamente", null);
                                builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = activity.getIntent();
                                        activity.setResult( Activity.RESULT_CANCELED, intent );
                                        activity.finish();
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

    public void create_plus_pf (final String document, final String name, final String email, final String password, final String cellphone, String genre, final String qtd_terms, final double val_term){
        final AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        try {
            JSONObject jsonObject = new JSONObject(  );
            jsonObject.put( "id_servico", "1" );
            jsonObject.put( "senha", password );
            jsonObject.put( "telefone", cellphone );
            jsonObject.put( "email", email );
            jsonObject.put( "nome", name );
            jsonObject.put( "sexo", genre );
            jsonObject.put( "documento", document );
            jsonObject.put( "qtd_termos", qtd_terms );

            AndroidNetworking.post( Server.URL()+"services/adicionar-conta")
                .addJSONObjectBody( jsonObject )
                .build()
                .getAsJSONObject( new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString( "status" );
                            switch (status){
                                case "success":
                                    Alerts.progress_clode();
                                    editor.putString( "token", response.getString( "authcode" ));
                                    editor.commit();
                                    if(editor.commit()){
                                        Intent intent = new Intent( activity, View_Payment.class );
                                        intent.putExtra( "type_person",0);
                                        intent.putExtra( "document", document );
                                        intent.putExtra( "name", name );
                                        intent.putExtra( "email", email);
                                        intent.putExtra( "cellphone", cellphone );
                                        intent.putExtra( "password", password );
                                        intent.putExtra( "qtd_plan", qtd_terms);
                                        intent.putExtra( "valor_termo", val_term);
                                        activity.startActivity( intent );
                                    }
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    builder.setTitle( "Ops!!!" );
                                    builder.setMessage( response.getString("message" ));
                                    builder.setPositiveButton( "Ok", null );
                                    builder.setCancelable( false );
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
                } );
        }catch (JSONException e){}

    }

    public void create_plus_pj (final String document, final String razao_social, final String name, final String email, final String password, final String cellphone, final String qtd_terms, final double val_term){

        try {

            JSONObject jsonObject = new JSONObject(  );
            jsonObject.put( "id_servico", "1" );
            jsonObject.put( "razao_social", razao_social);
            jsonObject.put( "senha", password );
            jsonObject.put( "telefone", cellphone );
            jsonObject.put( "email", email );
            jsonObject.put( "nome", name );
            jsonObject.put( "documento", document );
            jsonObject.put( "qtd_termos", qtd_terms );

            AndroidNetworking.post( Server.URL()+"services/adicionar-conta")
                    .addJSONObjectBody( jsonObject )
                    .build()
                    .getAsJSONObject( new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString( "status" );
                                switch (status){
                                    case "success":
                                        Alerts.progress_clode();
                                        Intent intent = new Intent( activity, View_Payment.class );
                                        intent.putExtra( "type_person",1);
                                        intent.putExtra( "document", document );
                                        intent.putExtra("razao_social", razao_social);
                                        intent.putExtra( "name", name );
                                        intent.putExtra( "email", email);
                                        intent.putExtra( "cellphone", cellphone );
                                        intent.putExtra( "password", password );
                                        intent.putExtra( "qtd_plan", qtd_terms);
                                        intent.putExtra( "valor_termo", val_term);
                                        activity.startActivity( intent );
                                        break;
                                    default:
                                        Alerts.progress_clode();
                                        builder.setTitle( "Ops!!!" );
                                        builder.setMessage( response.getString("message" ));
                                        builder.setPositiveButton( "Ok", null );
                                        builder.setCancelable( false );
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
                    } );
        }catch (JSONException e){}

    }

}
