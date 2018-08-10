package com.lono.Views;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lono.R;
import com.lono.Service.Service_Login;
import com.lono.Utils.Alerts;
import com.lono.Utils.Conexao;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

import org.json.JSONException;
import org.json.JSONObject;


public class View_Login extends AppCompatActivity implements View.OnClickListener{

    EditText editText_email_login;
    EditText editText_pass_login;
    Button button_login;
    TextView textview_recovery_pass;

    Service_Login service_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        service_login = new Service_Login(this);
        editText_email_login = (EditText) findViewById(R.id.edit_text_email_login);
        editText_pass_login = (EditText) findViewById(R.id.edit_text_pass_login);
        button_login= (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);

        textview_recovery_pass = (TextView) findViewById(R.id.textview_recovery_pass);
        textview_recovery_pass.setOnClickListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                buttonLogin();
                break;

            case R.id.textview_recovery_pass:
                openRecoveryPass();
                break;
        }
    }

    private void buttonLogin() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String email = editText_email_login.getText().toString().trim();
        String password = editText_pass_login.getText().toString().trim();

        if(email.isEmpty()){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.erro_email_empty);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            editText_email_login.requestFocus();
        }else if(Valitations.email(email) == false){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.erro_email_invalid);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            editText_email_login.requestFocus();
        }else if(password.isEmpty()){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.erro_pass_empty);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            editText_pass_login.requestFocus();
        }else if(Valitations.password(password) == false){
            builder.setTitle("Ops!!!");
            builder.setMessage(R.string.erro_pass_length);
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            editText_pass_login.requestFocus();
        }else{
            Keyboard.close(this, getWindow().getDecorView());
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", editText_email_login.getText().toString().trim());
                jsonObject.put("senha", editText_pass_login.getText().toString().trim());
                if(Conexao.check(this) == true){
                    Alerts.progress_open(this, null, getResources().getString(R.string.progress_dialog_login), false);
                    service_login.access(jsonObject);
                }else{
                    Alerts.snacs(getWindow().getDecorView(), R.string.not_connected);
                };
            } catch (JSONException e) {} catch (NullPointerException e){}
        }
    };

    private void openRecoveryPass() {
        Intent intent = new Intent(this, View_Recovery_Pass.class);
        intent.putExtra("email", editText_email_login.getText().toString());
        startActivityForResult(intent, 1);
    };

    @Override
    public void onBackPressed(){
        finish();
    }
}
