package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Views.View_Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Service_Profile {

    Activity activity;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;

    public Service_Profile(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
    }

    public void resetEmailCellphoe(final String email, final String cellphone, final TextView erro){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("telefone", cellphone);
            AndroidNetworking.post(Server.URL()+"services/editar-usuario")
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
                                    editor.putString("email", email);
                                    editor.putString("cellphone_account", cellphone);
                                    editor.commit();
                                    if(editor.commit()){
                                        erro.setVisibility(View.VISIBLE);
                                        erro.setText("Informações atualizadas com sucesso");
                                        erro.setBackgroundColor(activity.getResources().getColor(R.color.colorGreenLight));
                                    }
                                    break;
                                default:
                                    Alerts.progress_clode();
                                    erro.setVisibility(View.VISIBLE);
                                    erro.setText(response.getString("message"));
                                    erro.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
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

    public void resetPassword(String old_password, String new_password, final BottomSheetDialog bottomSheetDialog){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", Server.token(activity));
            jsonObject.put("senha_anterior", old_password);
            jsonObject.put("senha_nova", new_password);
            AndroidNetworking.post(Server.URL()+"services/atualizar-senha-usuario")
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
                                    bottomSheetDialog.dismiss();
                                    builder.setTitle(R.string.app_name);
                                    builder.setMessage("Senha alterada com sucesso");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Fazer Login", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editor.putString("token", "");
                                            editor.commit();
                                            if(editor.commit()){
                                                activity.finishAffinity();
                                                Intent intent = new Intent(activity, View_Login.class);
                                                activity.startActivity(intent);
                                            }
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
                        bottomSheetDialog.dismiss();
                        Server.ErrorServer(activity, anError.getErrorCode());
                    }
                });
        }catch (JSONException e){}


    }

    public void uploadImage (File file){
        AndroidNetworking.upload(Server.URL()+"services/enviar-avatar-usuario")
            .addMultipartFile("avatar",file)
            .addMultipartParameter("token",Server.token(activity))
            .build()
            .setUploadProgressListener(new UploadProgressListener() {
                @Override
                public void onProgress(long bytesUploaded, long totalBytes) {
                    System.out.println(totalBytes / bytesUploaded);
                }
            })
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                Alerts.progress_clode();

                                break;
                            default:
                                Alerts.progress_clode();

                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    Server.ErrorServer(activity, anError.getErrorCode());
                    System.out.println(anError.getMessage());
                }
            });
    }

}
