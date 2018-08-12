package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.ValidaCPF;
import com.lono.Utils.Valitations;

public class View_New_Account_Free extends AppCompatActivity{

    Toolbar toolbar;

    TextInputLayout layout_cpf_free;
    TextInputLayout layout_name_free;
    TextInputLayout layout_email_free;
    TextInputLayout layout_cellphone_free;
    TextInputLayout layout_password_free;
    TextInputLayout layout_conf_password_free;
    TextInputLayout layout_genre_free;

    EditText cpf_free;
    EditText name_free;
    EditText email_free;
    EditText cellphone_free;
    EditText password_free;
    EditText conf_password_free;

    CheckBox genre_masculino_free;
    CheckBox genre_feminino_free;

    String NAME_PLAN_SERVICE;
    String TYPE_GENRE;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_account_free );

        serviceNewAccount = new Service_New_Account( this );
        createToolbar(toolbar);

        try{
            NAME_PLAN_SERVICE = getIntent().getExtras().getString( "nome_servico" );
            TYPE_GENRE = "";
        }catch (NullPointerException e){}

        layout_cpf_free = (TextInputLayout) findViewById(R.id.layout_cpf_free);
        layout_name_free = (TextInputLayout) findViewById(R.id.layout_name_free);
        layout_email_free = (TextInputLayout) findViewById(R.id.layout_email_free);
        layout_cellphone_free = (TextInputLayout) findViewById(R.id.layout_cellphone_free);
        layout_password_free = (TextInputLayout) findViewById(R.id.layout_password_free);
        layout_conf_password_free = (TextInputLayout) findViewById(R.id.layout_conf_password_free);
        layout_genre_free = (TextInputLayout) findViewById(R.id.layout_genre_free);

        cpf_free = (EditText) findViewById(R.id.cpf_free);
        name_free = (EditText) findViewById(R.id.name_free);
        email_free = (EditText) findViewById(R.id.email_free);
        cellphone_free = (EditText) findViewById(R.id.cellphone_free);
        password_free = (EditText) findViewById(R.id.password_free);
        conf_password_free = (EditText) findViewById(R.id.conf_password_free);

        genre_masculino_free = (CheckBox) findViewById(R.id.genre_masculino_free);
        genre_feminino_free = (CheckBox) findViewById(R.id.genre_feminino_free);

        cpf_free.addTextChangedListener(MaskCPF.insert(cpf_free));
        cellphone_free.addTextChangedListener(MaskCellPhone.insert(cellphone_free));

        changeGenre();

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_newaccount_free);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_new_acoount_free);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void changeGenre(){
        genre_masculino_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    genre_masculino_free.setChecked(true);
                    genre_feminino_free.setChecked(false);
                    TYPE_GENRE="M";
                }else{
                    TYPE_GENRE="";
                }
            }
        });

        genre_feminino_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    genre_masculino_free.setChecked(false);
                    genre_feminino_free.setChecked(true);
                    TYPE_GENRE="F";
                }else{
                    TYPE_GENRE="";
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_free, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.save_free:
                registerFree();
                break;
        }
        return true;
    }

    private void registerFree() {

        String cpf = cpf_free.getText().toString().trim();
        String name = name_free.getText().toString().trim();
        String email = email_free.getText().toString().trim();
        String cellphone = cellphone_free.getText().toString().trim();
        String password = password_free.getText().toString().trim();
        String conf_password = conf_password_free.getText().toString().trim();

        if(cpf.isEmpty()){

            layout_cpf_free.setErrorEnabled(true);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_cpf_free.setError("Informe seu CPF");
            cpf_free.requestFocus();

        }else if(ValidaCPF.check(MaskCPF.unmask(cpf)) == false){

            layout_cpf_free.setErrorEnabled(true);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_cpf_free.setError("CPF inválido");
            cpf_free.requestFocus();

        }else if (name.isEmpty()){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(true);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_name_free.setError("Informe seu nome e sobrenome");
            name_free.requestFocus();

        }else if(email.isEmpty()){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(true);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_email_free.setError("Informe seu email");
            email_free.requestFocus();

        }else if(Valitations.email(email) == false){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(true);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_email_free.setError("Email inválido");
            email_free.requestFocus();

        }else if(cellphone.isEmpty()){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(true);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_cellphone_free.setError("Informe seu telefone");
            cellphone_free.requestFocus();

        }else if(cellphone.length() < 13){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(true);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_cellphone_free.setError("Telefone inválido");
            cellphone_free.requestFocus();

        }else if(password.isEmpty()){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(true);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_password_free.setError("Crie sua senha");
            password_free.requestFocus();

        }else if(password.length() < 6){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(true);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);
            layout_password_free.setError("Sua senha deve contar no mínimo 6 digitos");
            password_free.requestFocus();

        }else if(!password.equals(conf_password)){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(true);
            layout_conf_password_free.setErrorEnabled(true);
            layout_genre_free.setErrorEnabled(false);
            layout_password_free.setError("Senhas não conferem");
            layout_conf_password_free.setError("Senhas não conferem");

        }else if(TYPE_GENRE.equals("")){

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(true);
            layout_genre_free.setError("Escolha seu gênero");

        }else{

            layout_cpf_free.setErrorEnabled(false);
            layout_name_free.setErrorEnabled(false);
            layout_email_free.setErrorEnabled(false);
            layout_cellphone_free.setErrorEnabled(false);
            layout_password_free.setErrorEnabled(false);
            layout_conf_password_free.setErrorEnabled(false);
            layout_genre_free.setErrorEnabled(false);

            Alerts.progress_open(this, null, "Salvando Informações", false);
            serviceNewAccount.create_free(name, email, password, cellphone, TYPE_GENRE, cpf);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }
}
