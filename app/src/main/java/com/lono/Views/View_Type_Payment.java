package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.widget.Toast;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskNumberCreditCard;
import com.lono.Utils.Price;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

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

    EditText cpf_creditcard;
    EditText name_creditcard;

    Spinner parcelas_creditcard;

    Button button_pay_creditcard;
    Button button_pay_boleto;



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

        cpf_creditcard = (EditText) findViewById(R.id.cpf_creditcard);
        name_creditcard = (EditText) findViewById(R.id.name_creditcard);

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
                check_boleto.setVisibility(View.GONE);
                check_cartao.setVisibility(View.VISIBLE);
                box_pay_creditcard.setVisibility(View.VISIBLE);
                box_pay_boleto.setVisibility(View.GONE);
                box_codebar_boleto.setVisibility(View.GONE);
                if(TYPE_PLAN.equals("Anual")){
                    parcelas_creditcard.setVisibility(View.VISIBLE);
                }else{
                    parcelas_creditcard.setVisibility(View.GONE);
                }
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

