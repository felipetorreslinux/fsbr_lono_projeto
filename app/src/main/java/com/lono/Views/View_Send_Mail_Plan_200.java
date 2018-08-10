package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_Send_Mail_Plan_200 extends Activity implements View.OnClickListener {

    ImageView imageview_back_plan_200;

    EditText edit_text_name_send_email;
    EditText edit_text_email_send_email;
    EditText edit_text_cellphone_send_email;
    EditText edit_text_comment_send_email;
    Button button_send_email_plan_more_200;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plan_more_200);

        serviceNewAccount = new Service_New_Account( this );

        imageview_back_plan_200 = (ImageView) findViewById(R.id.imageview_back_plan_200);
        imageview_back_plan_200.setOnClickListener(this);

        edit_text_name_send_email = (EditText) findViewById(R.id.edit_text_name_send_email);
        edit_text_email_send_email = (EditText) findViewById(R.id.edit_text_email_send_email);
        edit_text_cellphone_send_email = (EditText) findViewById(R.id.edit_text_cellphone_send_email);
        edit_text_comment_send_email = (EditText) findViewById(R.id.edit_text_comment_send_email);
        button_send_email_plan_more_200 = (Button) findViewById(R.id.button_send_email_plan_more_200);
        button_send_email_plan_more_200.setOnClickListener(this);

        edit_text_cellphone_send_email.addTextChangedListener( MaskCellPhone.insert( edit_text_cellphone_send_email ) );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageview_back_plan_200:
                onBackPressed();
                break;

            case R.id.button_send_email_plan_more_200:
                sendEmailPlan200();
                break;
        }
    }

    private void sendEmailPlan200() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String name = edit_text_name_send_email.getText().toString().trim();
        String email = edit_text_email_send_email.getText().toString().trim();
        String cellphone = edit_text_cellphone_send_email.getText().toString().trim();
        String comment = edit_text_comment_send_email.getText().toString().trim();

        if(name.isEmpty()) {
            builder.setTitle("Ops!!!");
            builder.setMessage("Informe seu nome e sobrenome");
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            edit_text_name_send_email.requestFocus();
        }else if(email.isEmpty()){
            builder.setTitle("Ops!!!");
            builder.setMessage("Informe seu email");
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            edit_text_email_send_email.requestFocus();
        }else if(Valitations.email(email) == false){
            builder.setTitle("Ops!!!");
            builder.setMessage("Email informado é inválido");
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            edit_text_email_send_email.requestFocus();
        }else if(cellphone.isEmpty()){
            builder.setTitle("Ops!!!");
            builder.setMessage("Informe seu telefone");
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            edit_text_cellphone_send_email.requestFocus();
        }else if(cellphone.length() < 11){
            builder.setTitle("Ops!!!");
            builder.setMessage("Telefone informado é inválido");
            builder.setPositiveButton("Ok", null);
            builder.create().show();
            edit_text_cellphone_send_email.requestFocus();
        }else{
            Alerts.progress_open(this, null, "Enviando email...", false);
            String id_service = "2";
            serviceNewAccount.create_more_200( id_service, name, cellphone, email, comment );
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
