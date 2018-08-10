package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.lono.APIServer.Server;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskNumberCreditCard;

public class View_Type_Payment extends Activity implements View.OnClickListener {

    ImageView imageview_back_type_payment;
    CardView item_boleto_bank;
    CardView item_add_credit_card;

    TextView textview_name_plam;
    TextView textview_qtd_terms;
    TextView price_plan_selected;
    TextView textview_validade_plam;

    String TYPE_PLAN;
    String QTD_PLAN;
    String PRICE_PLAM;
    String VALID_PLAN;
    int TYPE_PAY;

    LinearLayout box_response_boleto;
    TextView response_dados_boleto;
    TextView response_link_boleto;

    CardView card_info_creditcard;
    TextView number_creditcard_payment;
    TextView validate_creditcard_payment;
    TextView name_creditcard_payment;
    CardView card_parcells;
    Spinner parcelas_credit_card;

    CardView card_info_boleto;
    TextView codebar_boleto;
    ImageView button_copy_codebar_boleto;
    ImageView button_share_codebar_boleto;

    Button button_pay;

    Service_Payment servicePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_type_payment );

        servicePayment = new Service_Payment(this);

        TYPE_PLAN = getIntent().getExtras().getString("name_plam");
        QTD_PLAN = getIntent().getExtras().getString("qtd_terms");
        PRICE_PLAM = getIntent().getExtras().getString("price_plam");
        VALID_PLAN = getIntent().getExtras().getString("validate_plam");
        TYPE_PAY = 0;

        imageview_back_type_payment = (ImageView) findViewById( R.id.imageview_back_type_payment );
        imageview_back_type_payment.setOnClickListener( this );

        item_boleto_bank = (CardView) findViewById( R.id.item_boleto_bank );
        item_boleto_bank.setOnClickListener( this );

        item_add_credit_card = (CardView) findViewById( R.id.item_add_credit_card );
        item_add_credit_card.setOnClickListener( this );

        card_info_creditcard = (CardView) findViewById(R.id.card_info_creditcard);
        card_info_creditcard.setVisibility(View.GONE);

        number_creditcard_payment = (TextView) findViewById(R.id.number_creditcard_payment);
        validate_creditcard_payment = (TextView) findViewById(R.id.validate_creditcard_payment);
        name_creditcard_payment = (TextView) findViewById(R.id.name_creditcard_payment);
        card_parcells = (CardView) findViewById(R.id.card_parcells);
        card_parcells.setVisibility(View.GONE);
        parcelas_credit_card = (Spinner) findViewById(R.id.parcelas_credit_card);

        card_info_boleto = (CardView) findViewById(R.id.card_info_boleto);
        card_info_boleto.setVisibility(View.GONE);
        codebar_boleto = (TextView) findViewById(R.id.codebar_boleto);
        button_copy_codebar_boleto = (ImageView) findViewById(R.id.button_copy_codebar_boleto);
        button_share_codebar_boleto = (ImageView) findViewById(R.id.button_share_codebar_boleto);
        button_copy_codebar_boleto.setOnClickListener(this);
        button_share_codebar_boleto.setOnClickListener(this);

        textview_name_plam = (TextView)findViewById(R.id.textview_name_plam);
        textview_name_plam.setText(TYPE_PLAN);
        textview_qtd_terms = (TextView)findViewById(R.id.textview_qtd_terms);
        textview_qtd_terms.setText(QTD_PLAN);
        price_plan_selected = (TextView)findViewById(R.id.price_plan_selected);
        price_plan_selected.setText(PRICE_PLAM);
        textview_validade_plam = (TextView)findViewById(R.id.textview_validade_plam);
        textview_validade_plam.setText(VALID_PLAN);

        box_response_boleto = (LinearLayout) findViewById(R.id.box_response_boleto);
        box_response_boleto.setVisibility(View.GONE);
        response_dados_boleto = (TextView) findViewById(R.id.response_dados_boleto);
        response_link_boleto = (TextView) findViewById(R.id.response_link_boleto);

        button_pay = (Button) findViewById(R.id.button_pay);
        button_pay.setVisibility(View.GONE);
        button_pay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_type_payment:
                onBackPressed();
                break;

            case R.id.item_boleto_bank:
                generateBoleto();
                TYPE_PAY= 1;
                button_pay.setText("Finalizar");
                break;

            case R.id.button_copy_codebar_boleto:
                String codebar = codebar_boleto.getText().toString().trim();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, codebar);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Código copiado com sucesso", Toast.LENGTH_LONG).show();
                break;

            case R.id.button_share_codebar_boleto:
                String codebar_share = codebar_boleto.getText().toString().trim();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Plano Plus\nValor:"+PRICE_PLAM+"\nCódigo de barras para pagamento\n\n"+codebar_share);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar..."));
                break;

            case R.id.item_add_credit_card:
                TYPE_PAY = 2;
                button_pay.setText("Pagar");
                Intent intent = new Intent( this, View_New_CreditCard.class );
                startActivityForResult( intent, 1000);
                break;

            case R.id.button_pay:
                switch (TYPE_PAY){
                    case 1:
                        Intent open_login = new Intent(this, View_Login.class);
                        startActivity(open_login);
                        finishAffinity();
                        break;

                    case 2:

                        break;
                }
                break;
        }
    }

    private void generateBoleto() {
        final String token = Server.token(this);
        final String terms = textview_qtd_terms.getText().toString().trim();
        final String plan = TYPE_PLAN.toLowerCase();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Deseja gerar seu boleto bancário para pagamento do Plano Plus com "+QTD_PLAN+" termos no valor de "+PRICE_PLAM+" com validade de 30 dias?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Alerts.progress_open(View_Type_Payment.this, null, "Gerando boleto...", true);
                servicePayment.paymentBoleto(token, terms, plan, Server.hashSession, card_info_boleto, codebar_boleto, button_pay);
                card_info_creditcard.setVisibility(View.GONE);
                card_parcells.setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("Não", null);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1000:
                if(resultCode == Activity.RESULT_OK){
                    card_info_boleto.setVisibility(View.GONE);
                    card_info_creditcard.setVisibility(View.VISIBLE);

                    String number = data.getExtras().getString( "number" );
                    String month = data.getExtras().getString( "month" );
                    String year = data.getExtras().getString( "year" );
                    String document = data.getExtras().getString( "document" );
                    String name = data.getExtras().getString( "name" );

                    number_creditcard_payment.setText(MaskNumberCreditCard.maskCript(number));
                    validate_creditcard_payment.setText("Validade: "+month+"/"+year);
                    name_creditcard_payment.setText(name);
                    button_pay.setVisibility(View.VISIBLE);

                    if(TYPE_PLAN.equals("Anual")){
                        card_parcells.setVisibility(View.VISIBLE);
                        String[] parcells = {
                                "1 - ",
                                "2 - ",
                                "3 - ",
                                "4 - ",
                                "5 - ",
                        };
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, parcells);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        parcelas_credit_card.setAdapter(arrayAdapter);
                    }

                }
                break;
        }
    }
}

