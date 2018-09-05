package com.lono.Views;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_Payment;

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

        infoActivity();

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

            item_boleto_pay.setVisibility(View.VISIBLE);
            item_cartao_pay.setVisibility(View.VISIBLE);

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
                box_pay_boleto.setVisibility(View.VISIBLE);
                box_codebar_boleto.setVisibility(View.GONE);
                button_pay_boleto.setText("Gerar boleto");
                break;

            case R.id.item_cartao_pay:
                TYPE_PAY=2;
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

        Intent intent = new Intent(this, View_Payment_CreditCard.class);
        intent.putExtra("type_plan", TYPE_PLAN);
        intent.putExtra("document", DOCUMENT);
        intent.putExtra("name", NAME);
        intent.putExtra("qtd_terms", QTD_PLAN);
        intent.putExtra("price", PRICE);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

