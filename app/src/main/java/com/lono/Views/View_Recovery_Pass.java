package com.lono.Views;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.lono.R;
import com.lono.Service.Service_Recovery_Pass;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

public class View_Recovery_Pass extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    TextInputLayout layout_email_recovery;

    EditText email_recovery;

    Button button_recovery_pass;

    Service_Recovery_Pass serviceRecoveryPass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recovery_pass);

        createToolbar(toolbar);

        serviceRecoveryPass = new Service_Recovery_Pass(this);

        layout_email_recovery = (TextInputLayout) findViewById(R.id.layout_email_recovery);

        email_recovery = (EditText) findViewById(R.id.email_recovery);

        button_recovery_pass = (Button) findViewById(R.id.button_recovery_pass);
        button_recovery_pass.setOnClickListener(this);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_recovery_pass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recuperar Senha");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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
                sendRecoveryPass();
                break;
        }
    }

    private void sendRecoveryPass() {
        String email = email_recovery.getText().toString().trim();
        if(email.isEmpty()){

            layout_email_recovery.setErrorEnabled(true);
            layout_email_recovery.setError("Informe seu email");
            email_recovery.requestFocus();

        }else if(Valitations.email(email) == false){

            layout_email_recovery.setErrorEnabled(true);
            layout_email_recovery.setError("Email inválido");
            email_recovery.requestFocus();

        }else{

            layout_email_recovery.setErrorEnabled(false);
            Keyboard.close(this, getWindow().getDecorView());
            Alerts.progress_open(this, null, "Consultando informações", false);
            serviceRecoveryPass.recovery(email, layout_email_recovery);

        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
