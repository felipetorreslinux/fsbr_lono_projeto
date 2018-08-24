package com.lono.Views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lono.Firebase.PhoneNumberSMS.CallBackPhoneNumberValid;
import com.lono.Firebase.PhoneNumberSMS.PhoneNumberFirebase;
import com.lono.R;
import com.lono.Service.Service_Login;
import com.lono.Utils.Alerts;

import java.util.Timer;
import java.util.TimerTask;

public class View_Validation_SMS extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    String cellphone;
    public static String token;
    TextView info_validation_sms;
    TextView count_time_sms;
    TextInputLayout layout_cellphone_sms;
    Service_Login serviceLogin;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_validation_sms);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        serviceLogin = new Service_Login(this);

        token = getIntent().getExtras().getString("token");
        cellphone = getIntent().getExtras().getString("cellphone");
        PhoneNumberFirebase.send(this, cellphone);
        createToolbar(toolbar);

        info_validation_sms = findViewById(R.id.info_validation_sms);
        info_validation_sms.setText("Enviamos um código sms para o número "+cellphone+".\n\n" +
                "Informe o código abaixo para validarmos seu acesso.");

        layout_cellphone_sms = findViewById(R.id.layout_cellphone_sms);
        count_time_sms = findViewById(R.id.count_time_sms);

        count();

    }

    private void count(){
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                count_time_sms.setText("Aguarde: 00:" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                count_time_sms.setText("Reenviar código");
                count_time_sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(getWindow().getDecorView(), "Reenviando código", Snackbar.LENGTH_SHORT).show();
                        count();
                    }
                });
            }
        }.start();
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

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_validation_sms);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validar Telefone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
