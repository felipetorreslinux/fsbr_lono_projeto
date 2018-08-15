package com.lono.Service;

import android.app.Activity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.lono.APIServer.Server;
import com.lono.Utils.Alerts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Service_Profile {

    Activity activity;

    public Service_Profile(Activity activity){
        this.activity = activity;
    }

    public void uploadImage (File file){
        AndroidNetworking.upload(Server.URL()+"")
            .addMultipartFile("image",file)
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
