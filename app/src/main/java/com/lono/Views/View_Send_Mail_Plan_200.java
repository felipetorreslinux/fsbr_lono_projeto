package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_Send_Mail_Plan_200 extends AppCompatActivity {

    Toolbar toolbar;

    TextInputLayout layout_name_more_200;
    TextInputLayout layout_email_more_200;
    TextInputLayout layout_cellphone_more_200;
    TextInputLayout layout_msg_more_200;

    EditText name_more_200;
    EditText email_more_200;
    EditText cellphone_more_200;
    EditText msg_more_200;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plan_more_200);

        serviceNewAccount = new Service_New_Account( this );
        createToolbar(toolbar);

        layout_name_more_200 = (TextInputLayout) findViewById(R.id.layout_name_more_200);
        layout_email_more_200 = (TextInputLayout) findViewById(R.id.layout_email_more_200);
        layout_cellphone_more_200 = (TextInputLayout) findViewById(R.id.layout_cellphone_more_200);
        layout_msg_more_200 = (TextInputLayout) findViewById(R.id.layout_msg_more_200);

        name_more_200 = (EditText) findViewById(R.id.name_more_200);
        email_more_200 = (EditText) findViewById(R.id.email_more_200);
        cellphone_more_200 = (EditText) findViewById(R.id.cellphone_more_200);
        msg_more_200 = (EditText) findViewById(R.id.msg_more_200);

        cellphone_more_200.addTextChangedListener(MaskCellPhone.insert(cellphone_more_200));

    }

    private void createToolbar(Toolbar toolbar){
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_list_plans);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_new_acoount_more_200);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plan_more_200, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.save_plan_more_200:
                sendEmailPlan200();
                break;
        }
        return true;
    }

    private void sendEmailPlan200() {

        String name = name_more_200.getText().toString().trim();
        String email = email_more_200.getText().toString().trim();
        String cellphone = cellphone_more_200.getText().toString().trim();
        String msg = msg_more_200.getText().toString().trim();

        if(name.isEmpty()){

            layout_name_more_200.setErrorEnabled(true);
            layout_email_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setErrorEnabled(false);
            layout_msg_more_200.setErrorEnabled(false);
            layout_name_more_200.setError("Informe seu nome");
            name_more_200.requestFocus();

        }else if(email.isEmpty()){

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(true);
            layout_cellphone_more_200.setErrorEnabled(false);
            layout_msg_more_200.setErrorEnabled(false);
            layout_email_more_200.setError("Informe seu email");
            email_more_200.requestFocus();

        }else if(Valitations.email(email) == false){

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(true);
            layout_cellphone_more_200.setErrorEnabled(false);
            layout_msg_more_200.setErrorEnabled(false);
            layout_email_more_200.setError("Email inválido");
            email_more_200.requestFocus();

        }else if(cellphone.isEmpty()){

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setErrorEnabled(true);
            layout_msg_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setError("Informe seu telefone");
            cellphone_more_200.requestFocus();

        }else if(cellphone.length() < 13){

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setErrorEnabled(true);
            layout_msg_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setError("Telefone inválido");
            cellphone_more_200.requestFocus();

        }else if(msg.isEmpty()){

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setErrorEnabled(false);
            layout_msg_more_200.setErrorEnabled(true);
            layout_msg_more_200.setError("Comente algo sobre o que você precisa");
            msg_more_200.requestFocus();

        }else{

            layout_name_more_200.setErrorEnabled(false);
            layout_email_more_200.setErrorEnabled(false);
            layout_cellphone_more_200.setErrorEnabled(false);
            layout_msg_more_200.setErrorEnabled(false);

            Alerts.progress_open(this, null, "Enviando informações", false);
            serviceNewAccount.create_more_200(name, email, cellphone, msg);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
