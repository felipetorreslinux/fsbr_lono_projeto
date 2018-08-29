package com.lono.Views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lono.Firebase.PhoneNumberSMS.PhoneNumberFirebase;
import com.lono.R;
import com.lono.Service.Service_Login;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;


public class View_Login extends AppCompatActivity implements View.OnClickListener{

    AlertDialog.Builder builder;
    Toolbar toolbar;

    TextInputLayout layout_email_login;
    TextInputLayout layout_password_login;

    EditText email_login;
    EditText password_login;

    TextView button_recovery_pass;

    Button button_access_login;

    Service_Login serviceLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        builder = new AlertDialog.Builder(this);

        createToolbar(toolbar);

        serviceLogin = new Service_Login(this);

        layout_email_login = (TextInputLayout) findViewById(R.id.layout_email_login);
        layout_password_login = (TextInputLayout) findViewById(R.id.layout_password_login);

        email_login = (EditText) findViewById(R.id.email_login);
        password_login = (EditText) findViewById(R.id.password_login);

        button_recovery_pass = (TextView) findViewById(R.id.button_recovery_pass);
        button_recovery_pass.setOnClickListener(this);

        button_access_login = (Button) findViewById(R.id.button_access_login);
        button_access_login.setOnClickListener(this);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = findViewById(R.id.actionbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onResume(){
        loadCellphoneNumber();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_recovery_pass:
                openRecoveryPass();
                break;
            case R.id.button_access_login:
                validLogin();
                break;
        }
    }

    private void validLogin() {

        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if(email.isEmpty()){

            layout_email_login.setErrorEnabled(true);
            layout_password_login.setErrorEnabled(false);
            layout_email_login.setError("Informe seu email");
            email_login.requestFocus();

        }else if(Valitations.email(email) == false){

            layout_email_login.setErrorEnabled(true);
            layout_password_login.setErrorEnabled(false);
            layout_email_login.setError("Email inválido");
            email_login.requestFocus();

        }else if(password.isEmpty()){

            layout_email_login.setErrorEnabled(false);
            layout_password_login.setErrorEnabled(true);
            layout_password_login.setError("Informe sua senha");
            password_login.requestFocus();

        }else if(password.length() < 6){

            layout_email_login.setErrorEnabled(false);
            layout_password_login.setErrorEnabled(true);
            layout_password_login.setError("Senha inválida");
            password_login.requestFocus();

        }else{
            layout_email_login.setErrorEnabled(false);
            layout_password_login.setErrorEnabled(false);
            Keyboard.close(this, getWindow().getDecorView());
            Alerts.progress_open(this, null, "Autorizando...", false);
            serviceLogin.check(email, password);
        }

    }


    private void openRecoveryPass() {
        Intent intent = new Intent(this, View_Recovery_Pass.class);
        startActivityForResult(intent, 1);
    };

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1000:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void loadCellphoneNumber(){
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        final String number = tm.getLine1Number();
        if(!number.isEmpty() || number == null){
            builder.setMessage("Fazer login com");
            View view = getLayoutInflater().inflate(R.layout.dialog_cellphone_login, null);
            builder.setCancelable(false);
            builder.setView(view);
            builder.setPositiveButton("Cadastre-se", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(View_Login.this, View_Plans_List.class));
                }
            });
            builder.setNegativeButton("Por email", null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            TextView number_cellphone = view.findViewById(R.id.number_cellphone);
            number_cellphone.setText(number.substring(3));
            number_cellphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Alerts.progress_open(View_Login.this, null, "Autorizando...", false);
                    serviceLogin.check_cellphone(number.substring(3));
                }
            });

        }else{
            email_login.setText(null);
            email_login.requestFocus();
        }
    }
}
