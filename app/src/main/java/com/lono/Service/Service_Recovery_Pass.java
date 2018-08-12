package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.model.Progress;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;

import org.json.JSONException;
import org.json.JSONObject;

public class Service_Recovery_Pass {

    Activity activity;
    AlertDialog.Builder builder;

    public Service_Recovery_Pass(Activity activity){
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    public void recovery (String email, final TextInputLayout textInputLayout){

        try{

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            AndroidNetworking.post(Server.URL()+"services/recuperar-senha")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                switch (response.getString("status")){
                                    case "success":
                                        Alerts.progress_clode();
                                        builder.setTitle(R.string.app_name);
                                        builder.setMessage(response.getString("message"));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton(R.string.label_positive_alert, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                activity.finish();
                                            }
                                        });
                                        builder.create().show();
                                        break;
                                    default:
                                        Alerts.progress_clode();
                                        textInputLayout.setErrorEnabled(true);
                                        textInputLayout.setError(response.getString("message"));

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
