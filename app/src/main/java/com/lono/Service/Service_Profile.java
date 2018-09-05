package com.lono.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Price;
import com.lono.Views.Fragments.Person_Fragment;
import com.lono.Views.View_Login;
import com.lono.Views.View_My_Plan_Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Service_Profile {

    Activity activity;
    View view;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseStorage storage;
    StorageReference storageReference;

    public Service_Profile(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
        this.sharedPreferences = activity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
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

    public void detailsPlanProfile(final TextView val_terms, final TextView namePlan, final TextView qtdTerms, final TextView qtdTermosUtil, final TextView pricePlan, final TextView datePlanExpire, final TextView typePayPlan, final LinearLayout typePay, final ViewStub loading){
       AndroidNetworking.post(Server.URL()+"services/informacoes-plano")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                JSONArray jsonArray = response.getJSONArray("outros_planos");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    double value_terms = jsonObject.getDouble("valor_termo");
                                    if(value_terms != 0){
                                        val_terms.setText(String.valueOf(value_terms));
                                    }
                                }
                                namePlan.setText(response.getString("nome_plano"));
                                qtdTerms.setText(String.valueOf(response.getInt("qtd_termos")));
                                pricePlan.setText(Price.real(response.getDouble("valor_plano")));
                                qtdTermosUtil.setText(String.valueOf(response.getString("num_termos_cadastrados")));
                                typePayPlan.setText(response.getString("tipo_cobranca"));
                                typePay.setVisibility(View.GONE);
                                if(response.getString("nome_plano").equals("Plus")){
                                    typePay.setVisibility(View.VISIBLE);
                                }
                                if(response.getString("nome_plano").equals("Free")){
                                    datePlanExpire.setText("Ilimitado");
                                }else{
                                    datePlanExpire.setText(response.getString("expiracao_data"));
                                }
                                loading.setVisibility(View.GONE);
                                break;

                            default:
                                loading.setVisibility(View.VISIBLE);
                                break;
                        }
                    }catch (JSONException e){}
                }

                @Override
                public void onError(ANError anError) {
                    loading.setVisibility(View.VISIBLE);
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
    }

    public void editPlanProfile (){
        AndroidNetworking.post(Server.URL()+"services/open-edit-plan")
            .addHeaders("token", Server.token(activity))
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "success":
                                Alerts.progress_clode();
                                Uri uri = Uri.parse(response.getString("url"));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                activity.startActivity(intent);
                                break;
                        }
                    }catch (JSONException e){};
                }

                @Override
                public void onError(ANError anError) {
                    Alerts.progress_clode();
                    Server.ErrorServer(activity, anError.getErrorCode());
                }
            });
    }

    public void uploadImage(Uri filePath) {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Carregando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+sharedPreferences.getInt("id", 0)+".jpg");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Snackbar.make(activity.getWindow().getDecorView(),
                                    "Foto atualizada com sucesso", Snackbar.LENGTH_SHORT).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    editor.putString("avatar_url", uri.toString());
                                    editor.commit();
                                    progressDialog.dismiss();
                                    activity.getFragmentManager().beginTransaction().replace(R.id.container, new Person_Fragment()).commit();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(activity.getWindow().getDecorView(),
                                    ""+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            editor.putString("image", "");
                            editor.commit();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setProgress((int) progress);
                        }
                    });
        }
    }

}
