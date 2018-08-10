package com.lono.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Utils.Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Service_Contact {
    Activity activity;
    AlertDialog.Builder builder;
    public Service_Contact(Activity activity){
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }

    public void distanceRota(String origem, String destino){
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+origem+"&destinations="+destino+"&key=AIzaSyBlWhMjgZxACUYhCbwXYF30wysdFJx7_dk")
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String status = response.getString("status");
                        switch (status){
                            case "OK":
                                JSONArray jsonArray = response.getJSONArray("rows");
                                JSONObject el = jsonArray.getJSONObject(0).getJSONObject("elements");
                                System.out.println(el);
                                break;
                        }
                    }catch (JSONException e){}
                    System.out.println(response);
                }

                @Override
                public void onError(ANError anError) {

                }
            });
    }

    public void sendEmail (String name, String email, String message){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nome", name);
            jsonObject.put("email", email);
            jsonObject.put("mensagem", message);
            AndroidNetworking.post(Server.URL()+"services/entrar-contato")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        Alerts.progress_clode();
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Email enviado com sucesso.\nEm breve entraremos em contato.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        });
                        builder.create().show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Alerts.progress_clode();
                        System.out.println(anError.getMessage());
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Não foi possível enviar seu email neste momento.\nTente mais tarde");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                activity.finish();
                            }
                        });
                        builder.create().show();
                    }
                });
        }catch (JSONException e){}

    }

    public void faq_whatsapp() {
        try{
            String number = "5581996090066";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Estou com problemas em pagamento");
            sendIntent.putExtra("jid", number + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e) {}
    }
}
