package com.lono.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lono.Adapter.Adapter_Slide_Plans_Payment;
import com.lono.Models.Slide_Payment_Model;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.CalcTerms;
import com.lono.Utils.Price;
import com.lono.Utils.Valitations;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class View_Payment extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    UltraViewPager ultraviewpager;
    List<Slide_Payment_Model> list_slide_payment = new ArrayList<>();

    LinearLayout item_plan_anual;
    LinearLayout item_plan_mensal;

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

    int TYPE_PERSON;
    String DOCUMENT;
    String RAZAO_SOCIAL;
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

        createToolbar(toolbar);

        paramsUserPaypment();

        ultraviewpager = (UltraViewPager) findViewById(R.id.ultrapage_plans);

        Slide_Payment_Model slideAnual = new Slide_Payment_Model(0, TYPE_PERSON, DOCUMENT, NAME,"Plano Anual", "30 dias grátis", QTD_TERMS, VALUE_TERM,
                "Parcele em até 05 (cinco) vezes.\n" +
                        "Caso pague com seu cartão de crédito, todos os recursos de nossos serviços estarão disponíveis de imediato.",
                "Quero este", MIN_TERMS, MAX_TERMS);

        Slide_Payment_Model slideMensal = new Slide_Payment_Model(1, TYPE_PERSON,  DOCUMENT, NAME,"Plano Mensal", "Básico", QTD_TERMS, VALUE_TERM,
                "Escolhendo o tipo de pagamento mensal você será avisado para renovação do seu plano via SMS ou por email.\n" +
                        "Podendo pagar por cartão de crédito ou boleto bancário.",
                "Quero este", MIN_TERMS, MAX_TERMS);

        list_slide_payment.clear();
        list_slide_payment.add(slideAnual);
        list_slide_payment.add(slideMensal);

        Adapter_Slide_Plans_Payment adapterSlidePlansPayment = new Adapter_Slide_Plans_Payment(this, list_slide_payment);

        ultraviewpager.setAdapter(adapterSlidePlansPayment);
        ultraviewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraviewpager.initIndicator();
        ultraviewpager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getResources().getColor(R.color.colorPrimary))
                .setNormalColor(getResources().getColor(R.color.colorGray ))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        ultraviewpager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraviewpager.getIndicator().setMargin(0,25, 0 ,25);
        ultraviewpager.getIndicator().build();
        ultraviewpager.setInfiniteLoop(false);
        ultraviewpager.setPageTransformer(false, new UltraScaleTransformer());

        item_plan_anual = (LinearLayout) findViewById( R.id.item_plan_anual );
        item_plan_anual.setOnClickListener( this );
        item_plan_mensal = (LinearLayout) findViewById( R.id.item_plan_mensal );
        item_plan_mensal.setOnClickListener( this );

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


    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void paramsUserPaypment(){
        try{
            TYPE_PERSON = getIntent().getExtras().getInt("type_person");
            QTD_TERMS = getIntent().getExtras().getString( "qtd_plan" );
            VALUE_TERM = getIntent().getExtras().getDouble( "valor_termo" );
            DOCUMENT = getIntent().getExtras().getString( "document" );
            RAZAO_SOCIAL = getIntent().getExtras().getString("razao_social");
            NAME = getIntent().getExtras().getString( "name" );
            EMAIL = getIntent().getExtras().getString( "email" );
            CELLPHONE = getIntent().getExtras().getString( "cellphone" );
            MIN_TERMS = getIntent().getExtras().getInt( "min_termos" );
            MAX_TERMS = getIntent().getExtras().getInt( "max_termos" );

            if(TYPE_PERSON == 0){
                getSupportActionBar().setTitle("Olá, "+ Valitations.name_profile(NAME));
                qtd_terms_selected_payment.setText( QTD_TERMS );
                text_view_price_plan_anual.setText( Price.real( CalcTerms.value_anual( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                text_view_price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                price_plam_anual.setText("De: "+ Price.real(CalcTerms.close_value_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                price_economy_plam_anual.setText("Economia de "+Price.real(CalcTerms.economy_plan_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
            }else{
                getSupportActionBar().setTitle("Olá, "+ RAZAO_SOCIAL);
                qtd_terms_selected_payment.setText( QTD_TERMS );
                text_view_price_plan_anual.setText( Price.real( CalcTerms.value_anual( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                text_view_price_plan_mensal.setText( Price.real( CalcTerms.value_mensal( VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                price_plam_anual.setText("De: "+ Price.real(CalcTerms.close_value_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
                price_economy_plam_anual.setText("Economia de "+Price.real(CalcTerms.economy_plan_anual(VALUE_TERM,  Integer.parseInt( QTD_TERMS ))));
            }
        }catch (NullPointerException e){}

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

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

                break;

            case R.id.button_selected_plam_mensal:

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
