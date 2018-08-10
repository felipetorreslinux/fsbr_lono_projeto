package com.lono.Views;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_New_Account_PF_Plus extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    TextInputLayout layout_cpf_pf;
    TextInputLayout layout_name_pf;
    TextInputLayout layout_email_pf;
    TextInputLayout layout_cellphone_pf;
    TextInputLayout layout_password_pf;
    TextInputLayout layout_conf_password_pf;
    TextInputLayout layout_genre_pf;

    EditText cpf_pf;
    EditText name_pf;
    EditText email_pf;
    EditText cellphone_pf;
    EditText password_pf;
    EditText conf_password_pf;
    CheckBox item_genre_masculino;
    CheckBox item_genre_feminino;

    String TYPE_GENRE;
    String NAME_PLAN;
    String QTD_TERMS;
    double VAL_TERMS;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_account_pf_plus );

        serviceNewAccount = new Service_New_Account( this );

        actionBar(toolbar);

        try{
            TYPE_GENRE = null;
            NAME_PLAN = getIntent().getExtras().getString( "nome_servico" );
            QTD_TERMS = getIntent().getExtras().getString( "qtd_termos" );
            VAL_TERMS = getIntent().getExtras().getDouble( "valor_termo" );
        }catch (NullPointerException e){}

        layout_cpf_pf = (TextInputLayout) findViewById(R.id.layout_cpf_pf);
        layout_name_pf = (TextInputLayout) findViewById(R.id.layout_name_pf);
        layout_email_pf = (TextInputLayout) findViewById(R.id.layout_email_pf);
        layout_cellphone_pf = (TextInputLayout) findViewById(R.id.layout_cellphone_pf);
        layout_password_pf = (TextInputLayout) findViewById(R.id.layout_password_pf);
        layout_conf_password_pf = (TextInputLayout) findViewById(R.id.layout_conf_password_pf);
        layout_genre_pf = (TextInputLayout) findViewById(R.id.layout_genre_pf);

        cpf_pf = (EditText) findViewById(R.id.cpf_pf);
        name_pf = (EditText) findViewById(R.id.name_pf);
        email_pf = (EditText) findViewById(R.id.email_pf);
        cellphone_pf = (EditText) findViewById(R.id.cellphone_pf);
        password_pf = (EditText) findViewById(R.id.password_pf);
        conf_password_pf = (EditText) findViewById(R.id.conf_password_pf);

        item_genre_masculino = (CheckBox) findViewById(R.id.item_genre_masculino);
        item_genre_feminino = (CheckBox) findViewById(R.id.item_genre_feminino);
        item_genre_masculino.setChecked(false);
        item_genre_feminino.setChecked(false);

        checkBoxGenre();

        cellphone_pf.addTextChangedListener(MaskCellPhone.insert(cellphone_pf));
        cpf_pf.addTextChangedListener(MaskCPF.insert(cpf_pf));
        cpf_pf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String cpf = cpf_pf.getText().toString().trim();
                if(b == false){
                    if((!cpf.isEmpty()) && (cpf.length() > 13)){
                        Alerts.progress_open(View_New_Account_PF_Plus.this, null, "Consultando informações", false);
                        serviceNewAccount.check_cpf(cpf, cpf_pf, layout_cpf_pf, name_pf, email_pf);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Server.hash_pagseguro(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_pf, menu);
        return true;
    }

    private void actionBar(Toolbar toolbar){
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_newaccount_pf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_new_acoount_pf_plus);
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

            case R.id.next_new_pf:
                registerUserPF();
                break;
        }
        return true;
    }

    private void checkBoxGenre(){
        item_genre_masculino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    item_genre_masculino.setChecked(true);
                    item_genre_feminino.setChecked(false);
                    TYPE_GENRE = "M";
                }else{
                    TYPE_GENRE = null;
                }
            }
        });

        item_genre_feminino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    item_genre_masculino.setChecked(false);
                    item_genre_feminino.setChecked(true);
                    TYPE_GENRE = "F";
                }else{
                    TYPE_GENRE = null;
                }
            }
        });
    }


    private void registerUserPF() {
        String cpf = cpf_pf.getText().toString().trim();
        String name = name_pf.getText().toString().trim();
        String email= email_pf.getText().toString().trim();
        String cellphone = cellphone_pf.getText().toString().trim();
        String password = password_pf.getText().toString().trim();
        String conf_password = conf_password_pf.getText().toString().trim();
        String genre = TYPE_GENRE;

        if(cpf.isEmpty()){
            layout_cpf_pf.setErrorEnabled(true);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_cpf_pf.setError("Informe seu CPF");
            cpf_pf.requestFocus();
        }else if(name.isEmpty()){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(true);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_name_pf.setError("Informe seu nome completo");
            name_pf.requestFocus();
        }else if(email.isEmpty()){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(true);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_email_pf.setError("Informe seu email");
            email_pf.requestFocus();
        }else if(Valitations.email(email) == false){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(true);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_email_pf.setError("Email inválido");
            email_pf.requestFocus();
        }else if(cellphone.isEmpty()){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(true);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_cellphone_pf.setError("Informe seu telefone");
            cellphone_pf.requestFocus();
        }else if(cellphone.length() < 13){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(true);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_cellphone_pf.setError("Telefone inválido");
            cellphone_pf.requestFocus();
        }else if(password.isEmpty()){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(true);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_password_pf.setError("Informe uma senha para seu acesso");
            password_pf.requestFocus();
        }else if(password.length() < 6){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(true);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            layout_password_pf.setError("Sua senha deve conter no mínimo 6 digitos");
            password_pf.requestFocus();
        }else if(!password.equals(conf_password)){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(true);
            layout_conf_password_pf.setErrorEnabled(true);
            layout_genre_pf.setErrorEnabled(false);
            layout_password_pf.setError("Senhas não conferem");
            layout_conf_password_pf.setError("Senhas não conferem");
            password_pf.requestFocus();
        }else if(TYPE_GENRE == null){
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(true);
            layout_genre_pf.setError("Informe seu gênero");
        }else{
            layout_cpf_pf.setErrorEnabled(false);
            layout_name_pf.setErrorEnabled(false);
            layout_email_pf.setErrorEnabled(false);
            layout_cellphone_pf.setErrorEnabled(false);
            layout_password_pf.setErrorEnabled(false);
            layout_conf_password_pf.setErrorEnabled(false);
            layout_genre_pf.setErrorEnabled(false);
            Alerts.progress_open(this, null, "Salvando informações", false);
            serviceNewAccount.create_plus_pf(cpf, name, email, password, cellphone, genre, QTD_TERMS, VAL_TERMS);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_genre_masculino:
                item_genre_masculino.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                item_genre_masculino.setTextColor(getResources().getColor(R.color.colorWhite));
                item_genre_feminino.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                item_genre_feminino.setTextColor(getResources().getColor(R.color.colorDarkGray));
                TYPE_GENRE = "M";
                break;
            case R.id.item_genre_feminino:
                item_genre_masculino.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                item_genre_masculino.setTextColor(getResources().getColor(R.color.colorDarkGray));
                item_genre_feminino.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                item_genre_feminino.setTextColor(getResources().getColor(R.color.colorWhite));
                TYPE_GENRE = "F";
                break;
        }
    }
}
