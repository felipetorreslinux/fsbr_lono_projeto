package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_Contact;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

public class View_Contact extends Activity implements View.OnClickListener {

    ImageView imageview_back_contact;
    EditText name_send_contact;
    EditText email_send_contact;
    EditText message_send_contact;
    Button button_send_contact;

    TextView button_call_contact;
    LinearLayout button_faq_whats;
    LinearLayout button_como_chegar;

    Service_Contact service_contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        service_contact = new Service_Contact(this);

        imageview_back_contact = (ImageView) findViewById(R.id.imageview_back_contact);
        imageview_back_contact.setOnClickListener(this);

        name_send_contact = (EditText) findViewById(R.id.name_send_contact);
        email_send_contact = (EditText) findViewById(R.id.email_send_contact);
        message_send_contact = (EditText) findViewById(R.id.message_send_contact);

        button_send_contact = (Button) findViewById(R.id.button_send_contact);
        button_send_contact.setOnClickListener(this);

        button_call_contact = (TextView) findViewById(R.id.button_call_contact);
        button_call_contact.setOnClickListener(this);

        button_faq_whats = (LinearLayout) findViewById(R.id.button_faq_whats);
        button_faq_whats.setOnClickListener(this);

        button_como_chegar = (LinearLayout) findViewById(R.id.button_como_chegar);
        button_como_chegar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imageview_back_contact:
                onBackPressed();
                break;

            case R.id.button_send_contact:
                sendEmailContact();
                break;

            case R.id.button_call_contact:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja ligar para nós?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+558130236587", null)));
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
                break;

            case R.id.button_faq_whats:
                Alerts.progress_open(this, null, "Carregando FAQ...", false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Alerts.progress_clode();
                        service_contact.faq_whatsapp();
                    }
                }, 2000);
                break;

            case R.id.button_como_chegar:
                comoChegarMaps();
                break;
        }

    }

    private void sendEmailContact() {
        final String name = name_send_contact.getText().toString().trim();
        final String email = email_send_contact.getText().toString().trim();
        final String message = message_send_contact.getText().toString().trim();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(name.isEmpty()) {

            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.error_name_empty_send_contact);
            builder.setPositiveButton("Ok", null);
            builder.create().show();

        }else if(email.isEmpty()){

            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.error_email_empty_send_contact);
            builder.setPositiveButton("Ok", null);
            builder.create().show();

        }else if(Valitations.email(email) == false){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.error_email_invalid_send_contact);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
        }else if(message.isEmpty()){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.error_message_empty_send_contact);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
        }else{
            Keyboard.close(this, getWindow().getDecorView());
            Alerts.progress_open(this, null, "Enviando email...", false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    service_contact.sendEmail(name, email, message);
                }
            }, 2200);
        }


    }

    private void comoChegarMaps(){
        Uri uri = Uri.parse("google.navigation:q=-8.0458145,-34.8895672");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
