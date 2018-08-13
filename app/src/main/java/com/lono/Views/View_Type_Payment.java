package com.lono.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskNumberCreditCard;
import com.lono.Utils.MaskValidateCreditCard;
import com.lono.Utils.Price;

import java.util.ArrayList;
import java.util.List;

public class View_Type_Payment extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    RelativeLayout item_boleto_pay;
    RelativeLayout item_cartao_pay;

    ImageView check_boleto;
    ImageView check_cartao;

    LinearLayout box_pay_creditcard;
    LinearLayout box_pay_boleto;
    LinearLayout box_codebar_boleto;
    TextView text_codebar_boleto;
    Switch swipe_cpf_creditcard;

    Spinner parcelas_creditcard;
    Button button_pay_creditcard;
    Button button_pay_boleto;

    TextInputLayout layout_number_creditcard;
    TextInputLayout layout_validate_creditcard;
    TextInputLayout layout_cvv_creditcard;
    TextInputLayout layout_cpf_creditcard;
    TextInputLayout layout_name_creditcard;

    EditText number_creditcard;
    EditText validate_creditcard;
    EditText cvv_creditcard;
    EditText cpf_creditcard;
    EditText name_creditcard;

    TextView textview_name_plam;
    TextView textview_qtd_terms;
    TextView price_plan_selected;
    TextView textview_validade_plam;

    int TYPE_PERSON;
    String TYPE_PLAN;
    String DOCUMENT;
    String NAME;
    String QTD_PLAN;
    String PRICE_PLAM;
    double PRICE;
    String VALID_PLAN;
    int TYPE_PAY;

    Service_Payment servicePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_type_payment );

        servicePayment = new Service_Payment(this);

        createToolbar(toolbar);

        item_boleto_pay = (RelativeLayout) findViewById(R.id.item_boleto_pay);
        item_boleto_pay.setOnClickListener(this);
        item_cartao_pay = (RelativeLayout) findViewById(R.id.item_cartao_pay);
        item_cartao_pay.setOnClickListener(this);

        check_boleto = (ImageView) findViewById(R.id.check_boleto);
        check_cartao = (ImageView) findViewById(R.id.check_cartao);


        box_pay_boleto = (LinearLayout) findViewById(R.id.box_pay_boleto);
        box_pay_boleto.setVisibility(View.GONE);
        box_codebar_boleto = (LinearLayout) findViewById(R.id.box_codebar_boleto);
        box_codebar_boleto.setVisibility(View.GONE);
        text_codebar_boleto = (TextView) findViewById(R.id.text_codebar_boleto);
        text_codebar_boleto.setText(null);
        button_pay_boleto = (Button) findViewById(R.id.button_pay_boleto);
        button_pay_boleto.setOnClickListener(this);

        box_pay_creditcard = (LinearLayout) findViewById(R.id.box_pay_creditcard);
        box_pay_creditcard.setVisibility(View.GONE);

        layout_number_creditcard = (TextInputLayout) findViewById(R.id.layout_number_creditcard);
        layout_validate_creditcard = (TextInputLayout) findViewById(R.id.layout_validate_creditcard);
        layout_cvv_creditcard = (TextInputLayout) findViewById(R.id.layout_cvv_creditcard);
        layout_cpf_creditcard = (TextInputLayout) findViewById(R.id.layout_cpf_creditcard);
        layout_name_creditcard = (TextInputLayout) findViewById(R.id.layout_name_creditcard);

        number_creditcard = (EditText) findViewById(R.id.number_creditcard);
        validate_creditcard = (EditText) findViewById(R.id.validate_creditcard);
        cvv_creditcard = (EditText) findViewById(R.id.cvv_creditcard);
        cpf_creditcard = (EditText) findViewById(R.id.cpf_creditcard);
        name_creditcard = (EditText) findViewById(R.id.name_creditcard);

        number_creditcard.addTextChangedListener(MaskNumberCreditCard.mask(number_creditcard));
        validate_creditcard.addTextChangedListener(MaskValidateCreditCard.mask(validate_creditcard));
        cpf_creditcard.addTextChangedListener(MaskCPF.insert(cpf_creditcard));

        swipe_cpf_creditcard = (Switch) findViewById(R.id.swipe_cpf_creditcard);
        swipe_cpf_creditcard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    cpf_creditcard.setText(DOCUMENT);
                    name_creditcard.setText(NAME);
                }else{
                    cpf_creditcard.setText(null);
                    name_creditcard.setText(null);
                }
            }
        });

        parcelas_creditcard = (Spinner) findViewById(R.id.parcelas_creditcard);


        button_pay_creditcard = (Button) findViewById(R.id.button_pay_creditcard);
        button_pay_creditcard.setOnClickListener(this);

        infoActivity();
        parcelas(PRICE);

    }

    private void infoActivity(){

            TYPE_PAY = 0;
            TYPE_PERSON = getIntent().getExtras().getInt("type_person");
            TYPE_PLAN = getIntent().getExtras().getString("name_plam");
            DOCUMENT = getIntent().getExtras().getString("document");
            NAME = getIntent().getExtras().getString("name");
            QTD_PLAN = getIntent().getExtras().getString("qtd_terms");
            PRICE = getIntent().getExtras().getDouble("price");
            PRICE_PLAM = getIntent().getExtras().getString("price_plam");
            VALID_PLAN = getIntent().getExtras().getString("validate_plam");

            textview_name_plam = (TextView)findViewById(R.id.textview_name_plam);
            textview_name_plam.setText(TYPE_PLAN);
            textview_qtd_terms = (TextView)findViewById(R.id.textview_qtd_terms);
            textview_qtd_terms.setText(QTD_PLAN);
            price_plan_selected = (TextView)findViewById(R.id.price_plan_selected);
            price_plan_selected.setText(PRICE_PLAM);
            textview_validade_plam = (TextView)findViewById(R.id.textview_validade_plam);
            textview_validade_plam.setText(VALID_PLAN);

        if(TYPE_PERSON == 0){
            item_boleto_pay.setVisibility(View.VISIBLE);
            item_cartao_pay.setVisibility(View.VISIBLE);
        }else{
            item_boleto_pay.setVisibility(View.VISIBLE);
            item_cartao_pay.setVisibility(View.GONE);
            box_pay_creditcard.setVisibility(View.GONE);
        }

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

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_pay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pagar");
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
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_boleto_pay:
                TYPE_PAY=1;
                check_boleto.setVisibility(View.VISIBLE);
                check_cartao.setVisibility(View.GONE);
                box_pay_creditcard.setVisibility(View.GONE);
                box_pay_boleto.setVisibility(View.VISIBLE);
                box_codebar_boleto.setVisibility(View.GONE);
                button_pay_boleto.setText("Gerar boleto");
                break;

            case R.id.item_cartao_pay:
                TYPE_PAY=2;
//                check_boleto.setVisibility(View.GONE);
//                check_cartao.setVisibility(View.VISIBLE);
//                box_pay_creditcard.setVisibility(View.VISIBLE);
//                box_pay_boleto.setVisibility(View.GONE);
//                box_codebar_boleto.setVisibility(View.GONE);
//                if(TYPE_PLAN.equals("Anual")){
//                    parcelas_creditcard.setVisibility(View.VISIBLE);
//                }else{
//                    parcelas_creditcard.setVisibility(View.GONE);
//                }
                Intent intent = new Intent(this, View_Payment_CreditCard.class);
                intent.putExtra("document", DOCUMENT);
                intent.putExtra("name", NAME);
                intent.putExtra("qtd_terms", QTD_PLAN);
                intent.putExtra("price", PRICE);
                startActivityForResult(intent, 1001);
                break;

            case R.id.button_pay_creditcard:
                payCreditCard();
                break;
                
            case R.id.button_pay_boleto:
                payBoleto();
                break;
        }
    }

    private void payBoleto() {
        button_pay_boleto.setText("Gerando boleto...");
        String token = Server.token(this);
        String hash = Server.hashSession;
        servicePayment.paymentBoleto(token, QTD_PLAN, TYPE_PLAN.toLowerCase(), hash, box_codebar_boleto, text_codebar_boleto, button_pay_boleto );
    }

    private void payCreditCard() {
        String number = number_creditcard.getText().toString().trim();
        String[] validate = validate_creditcard.getText().toString().trim().split("/");
        String cvv = cvv_creditcard.getText().toString().trim();
        String document = cpf_creditcard.getText().toString().trim();
        String name = name_creditcard.getText().toString().trim();

        int parcell = parcelas_creditcard.getSelectedItemPosition();
        String parcelas = String.valueOf(parcelas_creditcard.getSelectedItem());

        System.out.println(parcelas);
        System.out.println(parcell);

        if(number.isEmpty()){
            layout_number_creditcard.setErrorEnabled(true);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_number_creditcard.setError("Informe o número do cartão");
            number_creditcard.requestFocus();
        }else if(number.length() < 18){
            layout_number_creditcard.setErrorEnabled(true);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_number_creditcard.setError("Cartão inválido");
            number_creditcard.requestFocus();
        }else if(validate[0].isEmpty()) {
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setError("Informe o mês da validade");
            validate_creditcard.requestFocus();
        }else if(Integer.parseInt(validate[0]) > 12){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setError("Mês inválido");
            validate_creditcard.requestFocus();
        }else if(validate[1].isEmpty()) {
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setError("Informe o ano da validade");
            validate_creditcard.requestFocus();
        }else if(Integer.parseInt(validate[1]) < 2018){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(true);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setError("Ano inválido");
            validate_creditcard.requestFocus();
        }else if(cvv.isEmpty()){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(true);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setError("Informe o CVV");
            cvv_creditcard.requestFocus();
        }else if(cvv.length() < 3){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(true);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setError("CVV inválido");
            cvv_creditcard.requestFocus();
        }else if(document.isEmpty()){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(true);
            layout_name_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setError("Informe o CPF do titutar");
            cpf_creditcard.requestFocus();
        }else if(document.length() < 14){
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(true);
            layout_name_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setError("CPF inválido");
            cpf_creditcard.requestFocus();
        }else if(name.isEmpty()) {
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(true);
            layout_name_creditcard.setError("informe o nome do titular");
            name_creditcard.requestFocus();
        }else{
            layout_number_creditcard.setErrorEnabled(false);
            layout_validate_creditcard.setErrorEnabled(false);
            layout_cvv_creditcard.setErrorEnabled(false);
            layout_cpf_creditcard.setErrorEnabled(false);
            layout_name_creditcard.setErrorEnabled(false);

            if(parcell == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ops!!!").setMessage("Informe a parcela para pagamento")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parcelas_creditcard.performClick();
                        }
                    }).create().show();
            }else{
                button_pay_creditcard.setText("Realizando pagamento\nAguarde...");
            }




        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

