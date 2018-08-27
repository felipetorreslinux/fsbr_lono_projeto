package com.lono.Views;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lono.APIServer.Server;
import com.lono.PagSeguro.LonoPagamentoUtils;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.MaskData;
import com.lono.Utils.MaskNumberCreditCard;
import com.lono.Utils.MaskValidateCreditCard;
import com.lono.Utils.Price;
import com.lono.Utils.ValidData;
import com.lono.Utils.ValidaCPF;

import java.util.ArrayList;
import java.util.List;

public class View_Payment_CreditCard extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;

    TextInputLayout layout_number_creditcard;
    TextInputLayout layout_validate_creditcard;
    TextInputLayout layout_cvv_creditcard;
    TextInputLayout layout_cpf_creditcard;
    TextInputLayout layout_name_creditcard;
    TextInputLayout layout_date_nasc_creditcard;
    TextInputLayout layout_cellphone_creditcard;
    TextInputLayout layout_cep_creditcard;
    TextInputLayout layout_logradouro_creditcard;
    TextInputLayout layout_number_local_creditcard;
    TextInputLayout layout_bairro_creditcard;
    TextInputLayout layout_cidade_creditcard;
    TextInputLayout layout_estado_creditcard;

    EditText number_creditcard;
    EditText validate_creditcard;
    EditText cvv_creditcard;
    EditText cpf_creditcard;
    EditText name_creditcard;
    EditText date_nasc_creditcard;
    EditText cellphone_creditcard;
    EditText cep_creditcard;
    EditText logradouro_creditcard;
    EditText number_local_creditcard;
    EditText bairro_creditcard;
    EditText cidade_creditcard;
    EditText estado_creditcard;

    Spinner parcelas_creditcard;

    Button button_pay_creditcard;

    String TYPE_PLAM;
    int QTC_PARCELAS;
    String QTD_TERMS;
    double PRICE;
    String NAME;
    String DOCUMENT;

    Service_Payment servicePayment;
    LonoPagamentoUtils lonoPagamentoUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_creditcard);

        servicePayment = new Service_Payment(this);
        lonoPagamentoUtils = new LonoPagamentoUtils(this, Server.payment());


        createToolbar(toolbar);


        layout_number_creditcard = (TextInputLayout) findViewById(R.id.layout_number_creditcard);
        layout_validate_creditcard = (TextInputLayout) findViewById(R.id.layout_validate_creditcard);
        layout_cvv_creditcard = (TextInputLayout) findViewById(R.id.layout_cvv_creditcard);
        layout_cpf_creditcard = (TextInputLayout) findViewById(R.id.layout_cpf_creditcard);
        layout_name_creditcard = (TextInputLayout) findViewById(R.id.layout_name_creditcard);
        layout_date_nasc_creditcard = (TextInputLayout) findViewById(R.id.layout_date_nasc_creditcard);
        layout_cellphone_creditcard = (TextInputLayout) findViewById(R.id.layout_cellphone_creditcard);
        layout_cep_creditcard = (TextInputLayout) findViewById(R.id.layout_cep_creditcard);
        layout_logradouro_creditcard = (TextInputLayout) findViewById(R.id.layout_logradouro_creditcard);
        layout_number_local_creditcard = (TextInputLayout) findViewById(R.id.layout_number_local_creditcard);
        layout_bairro_creditcard = (TextInputLayout) findViewById(R.id.layout_bairro_creditcard);
        layout_cidade_creditcard = (TextInputLayout) findViewById(R.id.layout_cidade_creditcard);
        layout_estado_creditcard = (TextInputLayout) findViewById(R.id.layout_estado_creditcard);

        number_creditcard = (EditText) findViewById(R.id.number_creditcard);
        number_creditcard.addTextChangedListener(MaskNumberCreditCard.mask(number_creditcard));
        validate_creditcard = (EditText) findViewById(R.id.validate_creditcard);
        validate_creditcard.addTextChangedListener(MaskValidateCreditCard.mask(validate_creditcard));
        cvv_creditcard = (EditText) findViewById(R.id.cvv_creditcard);
        cpf_creditcard = (EditText) findViewById(R.id.cpf_creditcard);
        cpf_creditcard.addTextChangedListener(MaskCPF.insert(cpf_creditcard));
        cpf_creditcard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String cpf = cpf_creditcard.getText().toString().trim();
                if(!cpf.isEmpty()){
                    if(ValidaCPF.check(MaskCPF.unmask(cpf)) == false){
                        layout_cpf_creditcard.setErrorEnabled(true);
                        layout_cpf_creditcard.setError("CPF inválido");
                    }
                }
            }
        });
        name_creditcard = (EditText) findViewById(R.id.name_creditcard);
        date_nasc_creditcard = (EditText) findViewById(R.id.date_nasc_creditcard);
        date_nasc_creditcard.addTextChangedListener(MaskData.insert(date_nasc_creditcard));
        date_nasc_creditcard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String data = date_nasc_creditcard.getText().toString().trim();
                if(!data.isEmpty()){
                    if(ValidData.check(data) == false){
                        layout_date_nasc_creditcard.setErrorEnabled(true);
                        layout_date_nasc_creditcard.setError("Data inválida");
                    }
                }
            }
        });
        cellphone_creditcard = (EditText) findViewById(R.id.cellphone_creditcard);
        cellphone_creditcard.addTextChangedListener(MaskCellPhone.insert(cellphone_creditcard));
        cep_creditcard = (EditText) findViewById(R.id.cep_creditcard);
        cep_creditcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 7){
                    Alerts.progress_open(View_Payment_CreditCard.this, null, "Consultando CEP" , false);
                    servicePayment.checkCEP(
                            cep_creditcard,
                            logradouro_creditcard,
                            number_local_creditcard,
                            bairro_creditcard,
                            cidade_creditcard,
                            estado_creditcard);
                }else{
                    logradouro_creditcard.setText(null);
                    number_local_creditcard.setText(null);
                    bairro_creditcard.setText(null);
                    cidade_creditcard.setText(null);
                    estado_creditcard.setText(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        logradouro_creditcard = (EditText) findViewById(R.id.logradouro_creditcard);
        number_local_creditcard = (EditText) findViewById(R.id.number_local_creditcard);
        bairro_creditcard = (EditText) findViewById(R.id.bairro_creditcard);
        cidade_creditcard = (EditText) findViewById(R.id.cidade_creditcard);
        estado_creditcard = (EditText) findViewById(R.id.estado_creditcard);

        parcelas_creditcard = (Spinner) findViewById(R.id.parcelas_creditcard);

        button_pay_creditcard = (Button) findViewById(R.id.button_pay_creditcard);
        button_pay_creditcard.setOnClickListener(this);

        try{
            TYPE_PLAM = getIntent().getExtras().getString("type_plan");
            QTD_TERMS = getIntent().getExtras().getString("qtd_terms");
            PRICE = getIntent().getExtras().getDouble("price");
            DOCUMENT = getIntent().getExtras().getString("document");
            NAME = getIntent().getExtras().getString("name");
            parcelas(PRICE);
        }catch (NullPointerException e){}

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_pay_creditcard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pagar com Cartão de Crédito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void parcelas(double price){
        if(TYPE_PLAM.equals("Anual")){
            List<String> list_parcells = new ArrayList<>();
            list_parcells.clear();
            list_parcells.add("Escolha sua parcela");
            for(int i = 1; i < 6; i++){
                list_parcells.add(i + "x de " + (Price.real(price / i)) );
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list_parcells);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            parcelas_creditcard.setAdapter(dataAdapter);
        }else{
            List<String> list_parcells = new ArrayList<>();
            list_parcells.clear();
            list_parcells.add("Escolha sua parcela");
            list_parcells.add( 1 + "x de " + (Price.real(price / 1) ) );
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list_parcells);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            parcelas_creditcard.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onResume(){
        Server.hash_pagseguro(this);
        super.onResume();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_pay_creditcard:
                pay();
                break;
        }
    }

    private void pay() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String number_card = MaskNumberCreditCard.unmask(number_creditcard.getText().toString().trim());
        String validate_card = validate_creditcard.getText().toString().trim();
        String[] validate = validate_card.split("/");
        int monthCard = 0;
        int yearCard = 0;
        if(!validate_card.isEmpty()){
            monthCard = Integer.parseInt(validate[0]);
            yearCard = Integer.parseInt(validate[1]);
        }
        String cvv_card = cvv_creditcard.getText().toString().trim();
        QTC_PARCELAS = parcelas_creditcard.getSelectedItemPosition();

        final String token = Server.token(this);
        final String qtd_terms = QTD_TERMS;
        final String type_plan = TYPE_PLAM;
        final String sessionPayment = Server.sessionPayment;
        final String hash = Server.hashSession;

        final String cpf = cpf_creditcard.getText().toString().trim();
        final String name = name_creditcard.getText().toString().trim();
        final String data_nasc = date_nasc_creditcard.getText().toString().trim();
        final String cellphone = cellphone_creditcard.getText().toString().trim();


        final String cep = cep_creditcard.getText().toString().trim();
        final String logradouro = logradouro_creditcard.getText().toString().trim();
        final String numero = number_local_creditcard.getText().toString().trim();
        final String bairro = bairro_creditcard.getText().toString().trim();
        final String cidade = cidade_creditcard.getText().toString().trim();
        final String estado = estado_creditcard.getText().toString().trim();

        if(number_card.isEmpty()){

            layout_number_creditcard.setErrorEnabled(true);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_number_creditcard.setError("Informe o número do cartão");
            number_creditcard.requestFocus();

        }else if(number_card.length() < 16){

            layout_number_creditcard.setErrorEnabled(true);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_number_creditcard.setError("Cartão informado é inválido");
            number_creditcard.requestFocus();

        }else if(validate_card.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_validate_creditcard.setError("Informe a validade do cartão");
            validate_creditcard.requestFocus();

        }else if(ValidData.checkDataCard(validate_card) == false){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_validate_creditcard.setError("Validade inválida");
            validate_creditcard.requestFocus();

        }else if(cvv_card.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(true);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cvv_creditcard.setError("Informe o cvv do cartão");
            cvv_creditcard.requestFocus();

        }else if(cvv_card.length() < 3){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(true);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cvv_creditcard.setError("CVV inválido");
            cvv_creditcard.requestFocus();

        }else if(QTC_PARCELAS == 0){

            builder.setTitle("Ops!!!");
            builder.setMessage("Informe a quantidade de parcelas desejada");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    parcelas_creditcard.performClick();
                }
            });
            builder.create().show();

        }else if(cpf.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(true);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cpf_creditcard.setError("Informe o CPF do titular");
            cpf_creditcard.requestFocus();

        }else if(cpf.length() < 14){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(true);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cpf_creditcard.setError("CPF inválido");
            cpf_creditcard.requestFocus();

        }else if(name.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(true);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_name_creditcard.setError("Informe o nome do titular");
            name_creditcard.requestFocus();

        }else if(data_nasc.isEmpty()) {

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(true);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_date_nasc_creditcard.setError("Informe a data de nascimento do titular");
            date_nasc_creditcard.requestFocus();

        }else if(data_nasc.length() < 7){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(true);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_date_nasc_creditcard.setError("Data inválida");
            date_nasc_creditcard.requestFocus();

        }else if(ValidData.check(data_nasc) == false){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(true);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_date_nasc_creditcard.setError("Data inválida");
            date_nasc_creditcard.requestFocus();

        }else if(cellphone.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(true);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cellphone_creditcard.setError("Informe o telefone do titular");
            cellphone_creditcard.requestFocus();

        }else if(cellphone.length() < 13){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(true);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cellphone_creditcard.setError("Telefone inválido");
            cellphone_creditcard.requestFocus();

        }else if(cep.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(true);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cep_creditcard.setError("Informe o cep do titular");
            cep_creditcard.requestFocus();

        }else if(cep.length() < 7) {

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(true);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cep_creditcard.setError("CEP inválido");
            cep_creditcard.requestFocus();

        }else if(logradouro.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(true);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_logradouro_creditcard.setError("Informe o logradouro");
            logradouro_creditcard.requestFocus();

        }else if(numero.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(true);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_number_local_creditcard.setError("Informe o número");
            number_local_creditcard.requestFocus();

        }else if(bairro.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(true);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_bairro_creditcard.setError("Informe o bairro");
            bairro_creditcard.requestFocus();

        }else if(cidade.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(true);
            layout_estado_creditcard.setErrorEnabled(false);

            layout_cidade_creditcard.setError("Informe a cidade");
            cidade_creditcard.requestFocus();

        }else if(estado.isEmpty()){

            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_date_nasc_creditcard.setErrorEnabled(false);
            layout_cellphone_creditcard.setErrorEnabled(false);
            layout_cep_creditcard.setErrorEnabled(false);
            layout_logradouro_creditcard.setErrorEnabled(false);
            layout_number_local_creditcard.setErrorEnabled(false);
            layout_bairro_creditcard.setErrorEnabled(false);
            layout_cidade_creditcard.setErrorEnabled(false);
            layout_estado_creditcard.setErrorEnabled(true);

            layout_estado_creditcard.setError("Informe o estado");
            estado_creditcard.requestFocus();

        }else{

            Alerts.progress_open(this, null, "Realizando pagamento", false);
            lonoPagamentoUtils.GenerateCardToken(sessionPayment, number_card, cvv_card, monthCard, yearCard, new LonoPagamentoUtils.GenerateCardTokenListener() {
                @Override
                public void onSuccess(String cardToken) {
                    servicePayment.paymentCard(
                            token,
                            qtd_terms,
                            type_plan.toLowerCase(),
                            hash,
                            cardToken,
                            cep, numero,
                            cpf,
                            name,
                            data_nasc,
                            cellphone,
                            String.valueOf(QTC_PARCELAS));            }

                @Override
                public void onError(String errorMessage) {
                    builder.setTitle("Ops!!!");
                    builder.setMessage("Erro ao processar o cartão de crédito.\nTente novamente");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", null);
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
