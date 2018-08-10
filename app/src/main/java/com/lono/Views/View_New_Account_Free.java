package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_New_Account_Free extends Activity implements View.OnClickListener{

    ImageView imageview_back_new_account_free;
    EditText document_new_account_free;
    EditText name_new_account_free;
    EditText email_new_account_free;
    EditText cellphone_new_account_free;
    EditText password_new_account_free;
    EditText conf_password_new_account_free;
    CheckBox check_box_genre_men_free;
    CheckBox check_box_genre_girl_free;
    String NAME_PLAN_SERVICE;
    String TYPE_GENRE;
    Button button_send_new_account_free;
    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_account_free );

        serviceNewAccount = new Service_New_Account( this );
        NAME_PLAN_SERVICE = getIntent().getExtras().getString( "nome_servico" );

        imageview_back_new_account_free = (ImageView) findViewById( R.id.imageview_back_new_account_free );
        imageview_back_new_account_free.setOnClickListener( this );
        document_new_account_free = (EditText) findViewById( R.id.document_new_account_free );
        name_new_account_free = (EditText) findViewById( R.id.name_new_account_free );
        email_new_account_free = (EditText) findViewById( R.id.email_new_account_free );
        cellphone_new_account_free = (EditText) findViewById( R.id.cellphone_new_account_free );
        password_new_account_free = (EditText) findViewById( R.id.password_new_account_free );
        conf_password_new_account_free = (EditText) findViewById( R.id.conf_password_new_account_free );
        check_box_genre_men_free = (CheckBox) findViewById( R.id.check_box_genre_men_free );
        check_box_genre_men_free.setOnClickListener( this );
        check_box_genre_girl_free = (CheckBox) findViewById( R.id.check_box_genre_girl_free );
        check_box_genre_girl_free.setOnClickListener( this );
        button_send_new_account_free = (Button) findViewById( R.id.button_send_new_account_free );
        button_send_new_account_free.setOnClickListener( this );
        TYPE_GENRE = null;

        document_new_account_free.addTextChangedListener( MaskCPF.insert( document_new_account_free ) );
        cellphone_new_account_free.addTextChangedListener( MaskCellPhone.insert( cellphone_new_account_free ) );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageview_back_new_account_free:
                onBackPressed();
                break;

            case R.id.check_box_genre_men_free:
                check_box_genre_girl_free.setChecked( false );
                check_box_genre_men_free.setChecked( true );
                TYPE_GENRE="M";
                break;

            case R.id.check_box_genre_girl_free:
                check_box_genre_girl_free.setChecked( true );
                check_box_genre_men_free.setChecked( false );
                TYPE_GENRE="F";
                break;

            case R.id.button_send_new_account_free:
                insertNewAccountFree();
                break;
        }
    }

    private void insertNewAccountFree() {
        String document = document_new_account_free.getText().toString().trim();
        String name = name_new_account_free.getText().toString().trim();
        String email = email_new_account_free.getText().toString().trim();
        String cellphone = cellphone_new_account_free.getText().toString().trim();
        String password = password_new_account_free.getText().toString().trim();
        String conf_password = conf_password_new_account_free.getText().toString().trim();
        String genre = TYPE_GENRE;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        if(document.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu CPF" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            document_new_account_free.requestFocus();
        }else if(name.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu nome" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            name_new_account_free.requestFocus();
        }else if(email.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu email" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            email_new_account_free.requestFocus();
        }else if(Valitations.email( email ) == false){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Email informado é inválido" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            email_new_account_free.requestFocus();
        }else if(cellphone.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu telefone" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            cellphone_new_account_free.requestFocus();
        }else if(password.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Crie sua senha" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            password_new_account_free.requestFocus();
        }else if(!password.equals( conf_password )){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Senha não conferem" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            password_new_account_free.requestFocus();
        }else if(genre.equals( null )){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Escolha seu gênero" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
        }else{
            Alerts.progress_open( this, null, "Cadastrando usuário", false );
            serviceNewAccount.create_free( name, email, password, cellphone, genre, document );
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }
}
