package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.MaskCNPJ;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_New_Account_PJ_Plus extends Activity implements View.OnClickListener {

    ImageView imageview_back_new_account_pj_plus;
    TextView title_new_account_pj_plus;

    EditText document_new_account_pj_plus;
    EditText name_social_new_account_pj_plus;
    EditText name_new_account__pj_plus;
    EditText email_new_account_pj_plus;
    EditText cellphone_new_account_pj_plus;
    EditText password_new_account_pj_plus;
    EditText conf_password_new_account_pj_plus;

    Button button_send_new_account_pj_plus;

    String NAME_PLAN;
    String QTD_TERMS;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_account_pj_plus );

        serviceNewAccount = new Service_New_Account( this );

        imageview_back_new_account_pj_plus = (ImageView) findViewById( R.id.imageview_back_new_account_pj_plus );
        imageview_back_new_account_pj_plus.setOnClickListener( this );
        title_new_account_pj_plus = (TextView) findViewById( R.id.title_new_account_pj_plus );
        title_new_account_pj_plus.setText( R.string.title_new_account_pj_plus );

        NAME_PLAN = getIntent().getExtras().getString( "nome_servico" );
        QTD_TERMS = getIntent().getExtras().getString( "qtd_termos" );

        document_new_account_pj_plus = (EditText) findViewById( R.id.document_new_account_pj_plus );
        name_social_new_account_pj_plus = (EditText) findViewById( R.id.name_social_new_account_pj_plus );
        name_new_account__pj_plus = (EditText) findViewById( R.id.name_new_account__pj_plus );
        email_new_account_pj_plus = (EditText) findViewById( R.id.email_new_account_pj_plus );
        cellphone_new_account_pj_plus = (EditText) findViewById( R.id.cellphone_new_account_pj_plus );
        password_new_account_pj_plus = (EditText) findViewById( R.id.password_new_account_pj_plus );
        conf_password_new_account_pj_plus = (EditText) findViewById( R.id.conf_password_new_account_pj_plus );
        button_send_new_account_pj_plus = (Button) findViewById( R.id.button_send_new_account_pj_plus );
        button_send_new_account_pj_plus.setOnClickListener( this );

        document_new_account_pj_plus.addTextChangedListener( MaskCNPJ.insert( document_new_account_pj_plus ) );
        cellphone_new_account_pj_plus.addTextChangedListener( MaskCellPhone.insert( cellphone_new_account_pj_plus ));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_new_account_pj_plus:
                onBackPressed();
                break;

            case R.id.button_send_new_account_pj_plus:
                registerNewAccountPJPlus();
                break;
        }
    }

    private void registerNewAccountPJPlus() {

        String document = document_new_account_pj_plus.getText().toString().trim();
        String name_social = name_social_new_account_pj_plus.getText().toString().trim();
        String name = name_new_account__pj_plus.getText().toString().trim();
        String email = email_new_account_pj_plus.getText().toString().trim();
        String cellphone = cellphone_new_account_pj_plus.getText().toString().trim();
        String password = password_new_account_pj_plus.getText().toString().trim();
        String conf_password = conf_password_new_account_pj_plus.getText().toString().trim();

        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        if(document.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu CNPJ" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            document_new_account_pj_plus.requestFocus();
        }else if(name_social.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe sua Razão Social");
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            name_social_new_account_pj_plus.requestFocus();
        }else if(name.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu Nome e Sobrenome" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            name_new_account__pj_plus.requestFocus();
        }else if(email.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu email" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            email_new_account_pj_plus.requestFocus();
        }else if(Valitations.email( email ) == false){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Email informado não é válido" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            email_new_account_pj_plus.requestFocus();
        }else if(cellphone.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu telefone" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            cellphone_new_account_pj_plus.requestFocus();
        }else if(password.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Crie sua senha" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            password_new_account_pj_plus.requestFocus();
        }else if(password.length() < 6){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Senha muito curta.\nMínimo de 6 digitos ou caracteres" );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            password_new_account_pj_plus.requestFocus();
        }else if(!password.equals( conf_password )){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Senhas não conferem.\nPor favor corrija sua senha." );
            builder.setPositiveButton( "Ok",null );
            builder.create().show();
            password_new_account_pj_plus.requestFocus();
            password_new_account_pj_plus.setText( null );
            conf_password_new_account_pj_plus.setText( null );
        }else{
            serviceNewAccount.create_plus_pj( document, name_social, name, email, password, cellphone, QTD_TERMS );
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }
}

