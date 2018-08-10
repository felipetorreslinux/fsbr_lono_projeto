package com.lono.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.CalcTerms;
import com.lono.Utils.Price;
import com.lono.Utils.Valitations;

import org.w3c.dom.Text;

import java.util.Timer;

public class View_Payment extends Activity implements View.OnClickListener {

    ImageView imageview_back_payment_plan_plus;

    LinearLayout item_plan_anual;
    LinearLayout item_plan_mensal;

    TextView name_user_view_payment;
    TextView qtd_terms_selected_payment;

    TextView price_plam_anual;
    TextView price_economy_plam_anual;

    LinearLayout box_info_plan_anual;
    TextView info_plan_anual;
    LinearLayout button_selected_plam_anual;

    LinearLayout box_info_plan_mensal;
    TextView info_plan_mensal;
    LinearLayout button_selected_plam_mensal;

    TextView text_view_price_plan_anual;
    TextView text_view_price_plan_mensal;

    ImageView edit_terms_selected_payment;

    String DOCUMENT;
    String NAME;
    String EMAIL;
    String CELLPHONE;
    String QTD_TERMS;
    double VALUE_TERM;
    int MIN_TERMS;
    int MAX_TERMS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.view_payment );

        imageview_back_payment_plan_plus = (ImageView) findViewById( R.id.imageview_back_payment_plan_plus );
        imageview_back_payment_plan_plus.setOnClickListener( this );

        item_plan_anual = (LinearLayout) findViewById( R.id.item_plan_anual );
        item_plan_anual.setOnClickListener( this );
        item_plan_mensal = (LinearLayout) findViewById( R.id.item_plan_mensal );
        item_plan_mensal.setOnClickListener( this );

        name_user_view_payment = (TextView) findViewById( R.id.name_user_view_payment );
        qtd_terms_selected_payment = (TextView) findViewById( R.id.qtd_terms_selected_payment );

        edit_terms_selected_payment = (ImageView) findViewById( R.id.edit_terms_selected_payment );
        edit_terms_selected_payment.setOnClickListener( this );


        price_plam_anual = (TextView) findViewById(R.id.price_plam_anual);
        price_economy_plam_anual = (TextView) findViewById(R.id.price_economy_plam_anual);

        text_view_price_plan_anual = (TextView) findViewById( R.id.text_view_price_plan_anual );
        text_view_price_plan_mensal = (TextView) findViewById( R.id.text_view_price_plan_mensal );

        box_info_plan_anual = (LinearLayout) findViewById(R.id.box_info_plan_anual);
        box_info_plan_anual.setVisibility(View.GONE);

        button_selected_plam_anual = (LinearLayout) findViewById(R.id.button_selected_plam_anual);
        button_selected_plam_anual.setOnClickListener(this);

        box_info_plan_mensal = (LinearLayout) findViewById(R.id.box_info_plan_mensal);
        box_info_plan_mensal.setVisibility(View.GONE);

        button_selected_plam_mensal = (LinearLayout) findViewById(R.id.button_selected_plam_mensal);
        button_selected_plam_mensal.setOnClickListener(this);

        info_plan_anual = (TextView) findViewById(R.id.info_plan_anual);
        info_plan_mensal = (TextView) findViewById(R.id.info_plan_mensal);

        paramsUserPaypment();

    }

    public void paramsUserPaypment(){
        QTD_TERMS = getIntent().getExtras().getString( "qtd_plan" );
        VALUE_TERM = getIntent().getExtras().getDouble( "valor_termo" );
        DOCUMENT = getIntent().getExtras().getString( "document" );
        NAME = getIntent().getExtras().getString( "name" );
        EMAIL = getIntent().getExtras().getString( "email" );
        CELLPHONE = getIntent().getExtras().getString( "cellphone" );
        MIN_TERMS = getIntent().getExtras().getInt( "min_termos" );
        MAX_TERMS = getIntent().getExtras().getInt( "max_termos" );

        name_user_view_payment.setText( "Olá, "+ Valitations.name_profile(NAME));
        qtd_terms_selected_payment.setText( QTD_TERMS );
        text_view_price_plan_anual.setText( Price.real( CalcTerms.value_anual( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
        text_view_price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
        price_plam_anual.setText("De: "+ Price.real(CalcTerms.close_value_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
        price_economy_plam_anual.setText("Economia de "+Price.real(CalcTerms.economy_plan_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_payment_plan_plus:
                onBackPressed();
                break;

            case R.id.edit_terms_selected_payment:
                editTermsPayment();
                break;

            case R.id.item_plan_anual:
                box_info_plan_anual.setVisibility(View.VISIBLE);
                info_plan_anual.setText( R.string.info_plan_anual_text );
                break;

            case R.id.item_plan_mensal:
                box_info_plan_mensal.setVisibility( View.VISIBLE );
                info_plan_mensal.setText( R.string.info_plan_mensal_text );
                break;

            case R.id.button_selected_plam_anual:
                Intent intent_anual = new Intent( this, View_Type_Payment.class );
                intent_anual.putExtra("name_plam","Anual");
                intent_anual.putExtra("document", DOCUMENT);
                intent_anual.putExtra("name", NAME);
                intent_anual.putExtra("qtd_terms", qtd_terms_selected_payment.getText().toString().trim());
                intent_anual.putExtra("price_plam", Price.real( CalcTerms.value_anual( VALUE_TERM,  Integer.parseInt( qtd_terms_selected_payment.getText().toString().trim() ))));
                intent_anual.putExtra("validate_plam", "12 meses");
                startActivity( intent_anual );
                break;

            case R.id.button_selected_plam_mensal:
                Intent intent_mensal = new Intent( this, View_Type_Payment.class );
                intent_mensal.putExtra("name_plam","Mensal");
                intent_mensal.putExtra("document", DOCUMENT);
                intent_mensal.putExtra("name", NAME);
                intent_mensal.putExtra("qtd_terms", qtd_terms_selected_payment.getText().toString().trim());
                intent_mensal.putExtra("price_plam", Price.real( CalcTerms.value_mensal( VALUE_TERM,  Integer.parseInt( qtd_terms_selected_payment.getText().toString().trim() ))));
                intent_mensal.putExtra("validate_plam", "30 dias");
                startActivity( intent_mensal );
                break;

        }
    }

    private void editTermsPayment(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog( this );
        View view = getLayoutInflater().inflate( R.layout.bottomsweet_edit_terms_payment, null );
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        final ImageView remove = view.findViewById( R.id.terms_remove );
        final ImageView add = view.findViewById( R.id.terms_add );
        final TextView qtd_terms = view.findViewById( R.id.qtd_terms );
        qtd_terms.setText( qtd_terms_selected_payment.getText().toString().trim() );

        remove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TERMS = Integer.parseInt( qtd_terms_selected_payment.getText().toString().trim() );
                TERMS--;
                if(TERMS <= MIN_TERMS) {
                    TERMS = MIN_TERMS;
                }
                qtd_terms.setText( String.valueOf( TERMS ) );
                qtd_terms_selected_payment.setText(String.valueOf( TERMS ));
                text_view_price_plan_anual.setText( Price.real( CalcTerms.value_anual( VALUE_TERM, TERMS)));
                text_view_price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( VALUE_TERM, TERMS)));
                price_plam_anual.setText("De: "+ Price.real(CalcTerms.close_value_anual(VALUE_TERM,  TERMS)));
                price_economy_plam_anual.setText("Economia de "+Price.real(CalcTerms.economy_plan_anual(VALUE_TERM,  TERMS)));

            }
        });

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TERMS = Integer.parseInt( qtd_terms_selected_payment.getText().toString().trim() );
                TERMS++;
                qtd_terms.setText( String.valueOf( TERMS ) );
                qtd_terms_selected_payment.setText(String.valueOf( TERMS ));
                text_view_price_plan_anual.setText( Price.real( CalcTerms.value_anual( VALUE_TERM, TERMS)));
                text_view_price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( VALUE_TERM, TERMS)));
                price_plam_anual.setText("De: "+ Price.real(CalcTerms.close_value_anual(VALUE_TERM,  TERMS)));
                price_economy_plam_anual.setText("Economia de "+Price.real(CalcTerms.economy_plan_anual(VALUE_TERM,  TERMS)));

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult( Activity.RESULT_OK, intent );
        finish();
    }
}
