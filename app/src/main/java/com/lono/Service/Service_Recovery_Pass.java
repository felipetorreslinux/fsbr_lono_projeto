package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

    public Service_Recovery_Pass(Activity activity){
        this.activity = activity;
    }

    public void recovery (JSONObject jsonObject){

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

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
                                    builder.create().dismiss();
                                    activity.finish();
                                }
                            });
                            builder.create().show();
                            break;
                        default:
                            Alerts.progress_clode();
                            builder.setTitle(R.string.app_name);
                            builder.setMessage(response.getString("message"));
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.label_positive_alert, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    builder.create().dismiss();
                                    activity.finish();
                                }
                            });
                            builder.create().show();
                    }
                }catch (JSONException e){}catch (NullPointerException e){}
            }

            @Override
            public void onError(ANError anError) {
                Alerts.progress_clode();
            }
        });
    }

}
