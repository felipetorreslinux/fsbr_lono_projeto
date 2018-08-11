package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.lono.APIServer.Server;
import com.lono.PagSeguro.LonoPagamentoUtils;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskNumberCreditCard;
import com.lono.Utils.MaskValidateCreditCard;

public class View_New_CreditCard extends AppCompatActivity {

    Toolbar toolbar;

    TextInputLayout layout_number_creditcard;
    TextInputLayout layout_date_validate_creditcard;
    TextInputLayout layout_cvv_creditcard;
    TextInputLayout layout_document_client;
    TextInputLayout layout_name_client;

    EditText number_creditcard;
    EditText date_validate_creditcard;
    EditText cvv_creditcard;
    EditText document_client_creditcard;
    EditText document_name_creditcard;

    AlertDialog.Builder builder;
    LonoPagamentoUtils lonoPagamentoUtils;
    Service_Payment servicePayment;

    String PRICE_PLAN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_new_creditcard );

        servicePayment = new Service_Payment( this );
        lonoPagamentoUtils = new LonoPagamentoUtils( this, Server.payment());

        createToolbar(toolbar);

        PRICE_PLAN = getIntent().getExtras().getString("price_plan").replace("R$","").replace(" ","");

        layout_number_creditcard = (TextInputLayout) findViewById(R.id.layout_number_creditcard);
        layout_date_validate_creditcard = (TextInputLayout) findViewById(R.id.layout_date_validate_creditcard);
        layout_cvv_creditcard = (TextInputLayout) findViewById(R.id.layout_cvv_creditcard);
        layout_document_client = (TextInputLayout) findViewById(R.id.layout_document_client);
        layout_name_client = (TextInputLayout) findViewById(R.id.layout_name_client);

        number_creditcard = (EditText) findViewById(R.id.number_creditcard);
        date_validate_creditcard = (EditText) findViewById(R.id.date_validate_creditcard);
        cvv_creditcard = (EditText) findViewById(R.id.cvv_creditcard);

        document_client_creditcard = (EditText) findViewById(R.id.document_client_creditcard);
        document_name_creditcard = (EditText) findViewById(R.id.document_name_creditcard);

        document_client_creditcard.addTextChangedListener(MaskCPF.insert(document_client_creditcard));
        document_client_creditcard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == false){
                    String cpf = document_client_creditcard.getText().toString().trim();
                    if((!cpf.isEmpty()) && (cpf.length() == 14)){
                        Alerts.progress_open(View_New_CreditCard.this, null, "Consultando informações", false);
                        servicePayment.check_cpf(cpf, document_name_creditcard);
                    }else{

                    }
                }
            }
        });

        number_creditcard.addTextChangedListener(MaskNumberCreditCard.mask(number_creditcard));
        date_validate_creditcard.addTextChangedListener(MaskValidateCreditCard.mask(date_validate_creditcard));

    }

    private void createToolbar(Toolbar toolbar){
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_new_creditcard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cartão de Crédito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_creditcard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.register_card:
                saveCard();
                break;
        }
        return true;
    }

    public void saveCard(){
        String[] validate = date_validate_creditcard.getText().toString().trim().split("/");
        String number = number_creditcard.getText().toString().trim();
        String data = date_validate_creditcard.getText().toString().trim();
        int month = 0;
        int year= 0;
        if(!data.isEmpty()){
            month = Integer.parseInt(validate[0]);
            year = Integer.parseInt(validate[1]);
        }
        String cvv = cvv_creditcard.getText().toString().trim();
        String document = document_client_creditcard.getText().toString().trim();
        String name = document_name_creditcard.getText().toString().trim();

        if(number.isEmpty()) {
            layout_number_creditcard.setErrorEnabled(true);
            layout_date_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_number_creditcard.setError("Informe o número do cartão");
            number_creditcard.requestFocus();
        }else if(number.length() < 16) {
            layout_number_creditcard.setErrorEnabled(true);
            layout_date_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_number_creditcard.setError("Número de cartão inválido");
            number_creditcard.requestFocus();
        }else if(data.isEmpty()) {
            layout_number_creditcard.setErrorEnabled(false);
            layout_date_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_date_validate_creditcard.setError("Informe a data de validade do cartão");
            date_validate_creditcard.requestFocus();
        }else if(data.length() < 6){
            layout_number_creditcard.setErrorEnabled(false);
            layout_date_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_date_validate_creditcard.setError("Validade inválida.\nEx.: MM/AAAA");
            date_validate_creditcard.requestFocus();
        }else if(month > 12 || month == 0){
            layout_number_creditcard.setErrorEnabled(false);
            layout_date_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_date_validate_creditcard.setError("Mês inválido");
            date_validate_creditcard.requestFocus();
        }else if(year < 2018 || year > 2050 || year == 0){
            layout_number_creditcard.setErrorEnabled(false);
            layout_date_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_date_validate_creditcard.setError("Ano inválido");
            date_validate_creditcard.requestFocus();
        }else if(cvv.isEmpty()){
            layout_number_creditcard.setErrorEnabled(false);
            layout_date_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(true);
            layout_document_client.setErrorEnabled(false);
            layout_name_client.setErrorEnabled(false);
            layout_cvv_creditcard.setError("Informe o código de segurança");
            cvv_creditcard.requestFocus();
        }else if(document.isEmpty()){
                layout_number_creditcard.setErrorEnabled(false);
                layout_date_validate_creditcard.setErrorEnabled(false);
                layout_cvv_creditcard.setErrorEnabled(false);
                layout_document_client.setErrorEnabled(true);
                layout_name_client.setErrorEnabled(false);
                layout_document_client.setError("Informe o CPF do titular do cartão");
                document_client_creditcard.requestFocus();
        }else if(name.isEmpty()){
                layout_number_creditcard.setErrorEnabled(false);
                layout_date_validate_creditcard.setErrorEnabled(false);
                layout_cvv_creditcard.setErrorEnabled(false);
                layout_document_client.setErrorEnabled(false);
                layout_name_client.setErrorEnabled(true);
                layout_name_client.setError("Informe o nome completo do titular do cartão");
                document_name_creditcard.requestFocus();
        }else{
            String token = Server.token(this);
            if(!token.equals("")){
                Alerts.progress_open(this, null, "Analisando informações", false);
                servicePayment.addCard( token, number, String.valueOf(month), String.valueOf(year), document, name, PRICE_PLAN);
                lonoPagamentoUtils.GenerateCardToken( number, cvv, month, year, new LonoPagamentoUtils.GenerateCardTokenListener() {
                    @Override
                    public void onSuccess(String cardToken) {
                        Server.tokenCard = cardToken;
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }

}

