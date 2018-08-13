package com.lono.Views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
    TextInputLayout layout_number_local_creditcard;


    EditText number_creditcard;
    EditText validate_creditcard;
    EditText cvv_creditcard;
    EditText cpf_creditcard;
    EditText name_creditcard;
    EditText date_nasc_creditcard;
    EditText cellphone_creditcard;
    EditText cep_creditcard;
    EditText number_local_creditcard;

    Spinner parcelas_creditcard;

    Button button_pay_creditcard;

    String TYPE_PLAM;
    String HASH;
    int QTC_PARCELAS;
    String QTD_TERMS;
    double PRICE;
    String NAME;
    String DOCUMENT;
    String TOKEN_CARD;

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
        layout_number_local_creditcard = (TextInputLayout) findViewById(R.id.layout_number_local_creditcard);

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
                    String cep = cep_creditcard.getText().toString().trim();

                    servicePayment.checkCEP(cep);
                    System.out.println(cep);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        number_local_creditcard = (EditText) findViewById(R.id.number_local_creditcard);
        parcelas_creditcard = (Spinner) findViewById(R.id.parcelas_creditcard);

        button_pay_creditcard = (Button) findViewById(R.id.button_pay_creditcard);
        button_pay_creditcard.setOnClickListener(this);

        try{
            TYPE_PLAM = getIntent().getExtras().getString("type_plan");
            QTD_TERMS = getIntent().getExtras().getString("qtd_terms");
            PRICE = getIntent().getExtras().getDouble("price");
            DOCUMENT = getIntent().getExtras().getString("document");
            NAME = getIntent().getExtras().getString("name");
            TOKEN_CARD = null;
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

        String number_card = MaskNumberCreditCard.unmask(number_creditcard.getText().toString().trim());
        String validate_card = validate_creditcard.getText().toString().trim();
        String[] validate = validate_card.split("/");
        int monthCard = Integer.parseInt(validate[0]);
        int yearCard = Integer.parseInt(validate[1]);
        String cvv_card = cvv_creditcard.getText().toString().trim();

        final String token = Server.token(this);
        final String qtd_terms = QTD_TERMS;
        final String type_plan = TYPE_PLAM;
        final String sessionPayment = Server.sessionPayment;
        final String hash = Server.hashSession;
        final String cpf = cpf_creditcard.getText().toString().trim();
        final String name = name_creditcard.getText().toString().trim();
        final String data_nasc = date_nasc_creditcard.getText().toString().trim();
        final String cellphone = cellphone_creditcard.getText().toString().trim();
        QTC_PARCELAS = parcelas_creditcard.getSelectedItemPosition();
        Alerts.progress_open(this, null, "Realizando pagamento", false);
        lonoPagamentoUtils.GenerateCardToken(sessionPayment, number_card, cvv_card, monthCard, yearCard, new LonoPagamentoUtils.GenerateCardTokenListener() {
            @Override
            public void onSuccess(String cardToken) {
                TOKEN_CARD = cardToken;

                System.out.println(cardToken);

                servicePayment.paymentCard(token, qtd_terms, type_plan.toLowerCase(), hash, cardToken,
                        cep_creditcard.getText().toString(), number_local_creditcard.getText().toString(),
                        cpf, name, data_nasc, cellphone, String.valueOf(QTC_PARCELAS));            }

            @Override
            public void onError(String errorMessage) {
                TOKEN_CARD = null;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
