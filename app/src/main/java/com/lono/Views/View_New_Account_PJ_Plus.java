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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCNPJ;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.ValidaCNPF;
import com.lono.Utils.ValidaCPF;
import com.lono.Utils.Valitations;

public class View_New_Account_PJ_Plus extends AppCompatActivity{

    Toolbar toolbar;

    TextInputLayout layout_cnpj_pj;
    TextInputLayout layout_razao_social;
    TextInputLayout layout_name_pj;
    TextInputLayout layout_email_pj;
    TextInputLayout layout_callphone_pj;
    TextInputLayout layout_password_pj;
    TextInputLayout layout_conf_password_pj;

    EditText cnpj_pj;
    EditText razao_social_pj;
    EditText name_pj;
    EditText email_pj;
    EditText cellphone_pj;
    EditText password_pj;
    EditText conf_password_pj;

    String NAME_PLAN;
    String QTD_TERMS;
    double VAL_TERMS;

    Service_New_Account serviceNewAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_account_pj_plus );

        try{
            createToolbar(toolbar);
            NAME_PLAN = getIntent().getExtras().getString( "nome_servico" );
            QTD_TERMS = getIntent().getExtras().getString( "qtd_termos" );
            VAL_TERMS = getIntent().getExtras().getDouble( "valor_termo" );
        }catch (NullPointerException e){}

        serviceNewAccount = new Service_New_Account( this );

        layout_cnpj_pj = (TextInputLayout) findViewById(R.id.layout_cnpj_pj);
        layout_razao_social = (TextInputLayout) findViewById(R.id.layout_razao_social);
        layout_name_pj = (TextInputLayout) findViewById(R.id.layout_name_pj);
        layout_email_pj = (TextInputLayout) findViewById(R.id.layout_email_pj);
        layout_callphone_pj = (TextInputLayout) findViewById(R.id.layout_callphone_pj);
        layout_password_pj = (TextInputLayout) findViewById(R.id.layout_password_pj);
        layout_conf_password_pj = (TextInputLayout) findViewById(R.id.layout_conf_password_pj);

        cnpj_pj = (EditText) findViewById(R.id.cnpj_pj);
        razao_social_pj = (EditText) findViewById(R.id.razao_social_pj);
        name_pj = (EditText) findViewById(R.id.name_pj);
        email_pj = (EditText) findViewById(R.id.email_pj);
        cellphone_pj = (EditText) findViewById(R.id.cellphone_pj);
        password_pj = (EditText) findViewById(R.id.password_pj);
        conf_password_pj = (EditText) findViewById(R.id.conf_password_pj);

        cnpj_pj.addTextChangedListener(MaskCNPJ.insert(cnpj_pj));
        cellphone_pj.addTextChangedListener(MaskCellPhone.insert(cellphone_pj));

        cnpj_pj.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cnpf = cnpj_pj.getText().toString().trim();
                if((!cnpf.isEmpty()) && (cnpf.length() > 17)){
                    if(ValidaCNPF.check(MaskCNPJ.unmask(cnpf)) == false){
                        layout_cnpj_pj.setErrorEnabled(true);
                        layout_cnpj_pj.setError("CNPJ inválido");
                    }else{
                        layout_cnpj_pj.setErrorEnabled(false);
                        layout_cnpj_pj.setError(null);
                    }
                }
            }
        });

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_newaccount_pj);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_new_account_pj_plus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Server.hash_pagseguro(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_pj, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.next_new_pj:
                registerUserPF();
                break;
        }
        return true;
    }

    private void registerUserPF() {

        String cnpf = cnpj_pj.getText().toString().trim();
        String razao_social = razao_social_pj.getText().toString().trim();
        String name = name_pj.getText().toString().trim();
        String email = email_pj.getText().toString().trim();
        String cellphone = cellphone_pj.getText().toString().trim();
        String password = password_pj.getText().toString().trim();
        String conf_password = conf_password_pj.getText().toString().trim();

        if(cnpf.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(true);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_cnpj_pj.setError("Informe seu CNPJ");
            cnpj_pj.requestFocus();

        }else if(cnpf.length() < 18){

            layout_cnpj_pj.setErrorEnabled(true);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_cnpj_pj.setError("CNPJ inválido");
            cnpj_pj.requestFocus();

        }else if(razao_social.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(true);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_razao_social.setError("Informe sua Razão Social");
            razao_social_pj.requestFocus();

        }else if(name.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(true);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_name_pj.setError("Informe seu nome e sobrenome");
            name_pj.requestFocus();

        }else if(email.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(true);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_email_pj.setError("Informe seu email");
            email_pj.requestFocus();

        }else if(Valitations.email(email) == false){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(true);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_email_pj.setError("Email inválido");
            email_pj.requestFocus();

        }else if(cellphone.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(true);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_callphone_pj.setError("Informe seu telefone");
            cellphone_pj.requestFocus();

        }else if(cellphone.length() < 13){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(true);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_callphone_pj.setError("Telefone inválido");
            cellphone_pj.requestFocus();

        }else if(password.isEmpty()){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(true);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_password_pj.setError("Crie sua senha de acesso");
            password_pj.requestFocus();

        }else if(password.length() < 6){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(true);
            layout_conf_password_pj.setErrorEnabled(false);

            layout_password_pj.setError("Senha deve conter no mínimo 6 digitos");
            password_pj.requestFocus();

        }else if(!password.equals(conf_password)){

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(true);
            layout_conf_password_pj.setErrorEnabled(true);

            layout_password_pj.setError("Senhas não conferem");
            layout_conf_password_pj.setError("Senhas não conferem");
            password_pj.requestFocus();

        }else{

            layout_cnpj_pj.setErrorEnabled(false);
            layout_razao_social.setErrorEnabled(false);
            layout_name_pj.setErrorEnabled(false);
            layout_email_pj.setErrorEnabled(false);
            layout_callphone_pj.setErrorEnabled(false);
            layout_password_pj.setErrorEnabled(false);
            layout_conf_password_pj.setErrorEnabled(false);

            Alerts.progress_open(this, null, "Salvando informações", false);
            serviceNewAccount.create_plus_pj(cnpf, razao_social, name, email, password, cellphone, QTD_TERMS, VAL_TERMS);

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }
}

