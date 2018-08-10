package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_New_Account;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.Valitations;

public class View_New_Account extends Activity implements View.OnClickListener{

    ImageView imageview_back_new_account;

    LinearLayout box_physical_person;
    LinearLayout item_razao_social_new_account;
    LinearLayout item_genre_new_account;

    TextView name_document_new_account;
    EditText document_new_account;
    EditText name_new_account;
    EditText email_new_account;
    EditText cellphone_new_account;
    EditText password_new_account;
    EditText conf_password_new_account;
    EditText razao_social_new_account;

    Button button_physical_person;
    Button button_legal_person;
    Button button_send_new_account;

    CheckBox check_box_genre_men;
    CheckBox check_box_genre_girl;

    //  PAYMENT
    LinearLayout box_payment_new_account;
    TabLayout tab_layout_payment_new_account;
    LinearLayout box_payment_boleto;
    LinearLayout box_payment_credit;
    LinearLayout box_payment_debit;


    TextView name_plan_new_account;
    TextView text_view_change_plan;

    static int ID_SERVICE;
    static String NAME_PLAN;
    static String QTD_TERMS_PLAN;
    static String TYPE_PERSON;
    static String GENRE_PERSON;

    Service_New_Account serviceNewAccount;
    Animation animation_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_new_account);

        animation_open = new TranslateAnimation(1000,0,0,0);
        animation_open.setDuration(250);
        animation_open.setFillEnabled(true);

        serviceNewAccount = new Service_New_Account( this );

        ID_SERVICE = getIntent().getExtras().getInt( "id_servico" );
        NAME_PLAN = getIntent().getExtras().getString( "nome_servico" );
        QTD_TERMS_PLAN = getIntent().getExtras().getString( "qtd_termos" );
        TYPE_PERSON = null;
        GENRE_PERSON = null;

        button_physical_person = (Button) findViewById( R.id.button_physical_person );
        button_physical_person.setBackgroundResource( R.drawable.ripple_gray );
        button_physical_person.setOnClickListener( this );

        button_legal_person = (Button) findViewById( R.id.button_legal_person );
        button_legal_person.setBackgroundResource( R.drawable.ripple_gray );
        button_legal_person.setOnClickListener( this );

        imageview_back_new_account = (ImageView) findViewById(R.id.imageview_back_new_account);
        imageview_back_new_account.setOnClickListener(this);

        check_box_genre_men = (CheckBox) findViewById(R.id.check_box_genre_men);
        check_box_genre_men.setOnClickListener( this );
        check_box_genre_girl = (CheckBox) findViewById(R.id.check_box_genre_girl);
        check_box_genre_girl.setOnClickListener( this );

        name_plan_new_account = (TextView) findViewById(R.id.name_plan_new_account);

        box_physical_person = (LinearLayout) findViewById(R.id.box_physical_person);
        box_physical_person.setVisibility(View.GONE);

        text_view_change_plan = (TextView) findViewById( R.id.text_view_change_plan);
        text_view_change_plan.setOnClickListener(this);

        item_razao_social_new_account = (LinearLayout) findViewById( R.id.item_razao_social_new_account );
        item_razao_social_new_account.setVisibility( View.GONE );

        item_genre_new_account = (LinearLayout) findViewById( R.id.item_genre_new_account );
        item_genre_new_account.setVisibility( View.GONE );

        //ITENS OF PAYMENTS
        tab_layout_payment_new_account = (TabLayout) findViewById( R.id.tab_layout_payment_new_account );
        box_payment_new_account = (LinearLayout) findViewById( R.id.box_payment_new_account );
        box_payment_new_account.setVisibility( View.GONE );
        box_payment_boleto = (LinearLayout) findViewById( R.id.box_payment_boleto );
        box_payment_credit = (LinearLayout) findViewById( R.id.box_payment_credit );
        box_payment_debit = (LinearLayout) findViewById( R.id.box_payment_debit );
        box_payment_boleto.setVisibility( View.VISIBLE );
        box_payment_credit.setVisibility( View.GONE );
        box_payment_debit.setVisibility( View.GONE );

        // ITENS PHYSYCAL PERSON
        name_document_new_account = (TextView) findViewById( R.id.name_document_new_account );
        document_new_account = (EditText) findViewById( R.id.document_new_account );
        name_new_account = (EditText) findViewById( R.id.name_new_account );
        email_new_account = (EditText) findViewById( R.id.email_new_account );
        cellphone_new_account = (EditText) findViewById( R.id.cellphone_new_account );
        password_new_account = (EditText) findViewById( R.id.password_new_account );
        conf_password_new_account = (EditText) findViewById( R.id.conf_password_new_account );
        razao_social_new_account = (EditText) findViewById( R.id.razao_social_new_account );

        button_send_new_account = (Button) findViewById( R.id.button_send_new_account );
        button_send_new_account.setVisibility( View.GONE );
        button_send_new_account.setOnClickListener( this );

        changeTypePayment();
        openViewPlans();

        document_new_account.addTextChangedListener( MaskCPF.insert( document_new_account ) );
        cellphone_new_account.addTextChangedListener( MaskCellPhone.insert( cellphone_new_account ) );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_new_account:
                onBackPressed();
                break;

            case R.id.text_view_change_plan:
                onBackPressed();
                break;

            case R.id.button_physical_person:
                buttonPhysicalPerson();
                break;

            case R.id.button_legal_person:
                buttonLegalPerson();
                break;

            case R.id.check_box_genre_men:
                check_box_genre_girl.setChecked( false );
                TYPE_PERSON = "M";
                break;

            case R.id.check_box_genre_girl:
                check_box_genre_men.setChecked( false );
                TYPE_PERSON = "F";
                break;

            case R.id.button_send_new_account:
                if(ID_SERVICE == 1){
                    insertPlanPlanPlus();
                }else{
                    insertPlanFree();
                }
                break;
        }
    }

    private void openViewPlans(){
        if(ID_SERVICE == 1){
            name_plan_new_account.setText(NAME_PLAN+" - "+QTD_TERMS_PLAN+" termos");
        }else{
            name_plan_new_account.setText(NAME_PLAN);
            button_send_new_account.setVisibility( View.VISIBLE );
            button_physical_person.setVisibility( View.GONE );
            button_legal_person.setVisibility( View.GONE );
            box_physical_person.setVisibility( View.VISIBLE );
            box_physical_person.setAnimation( animation_open );
            animation_open.start();
            document_new_account.requestFocus();
            TYPE_PERSON = "F";
        }
    }

    private void buttonPhysicalPerson(){
        document_new_account.setText( null );
        document_new_account.requestFocus();
        document_new_account.setFilters(new InputFilter[] {new InputFilter.LengthFilter(14)});
        name_document_new_account.setText( R.string.label_cpf_new_account );
        item_razao_social_new_account.setVisibility( View.GONE );
        button_physical_person.setBackgroundResource( R.drawable.button_check_blue );
        button_physical_person.setTextColor( getResources().getColor( R.color.colorWhite ) );
        button_legal_person.setBackgroundResource( R.drawable.button_check_gray );
        button_legal_person.setTextColor( getResources().getColor( R.color.colorDarkGray ) );
        box_physical_person.setVisibility( View.VISIBLE );
        item_genre_new_account.setVisibility( View.VISIBLE );
        TYPE_PERSON = "F";

        if(ID_SERVICE == 1){
            box_payment_new_account.setVisibility( View.VISIBLE );
            box_payment_new_account.setAnimation( animation_open );
            animation_open.start();
        }else{
            box_payment_new_account.setVisibility( View.GONE );
        }

        button_send_new_account.setVisibility( View.VISIBLE );
    };

    private void buttonLegalPerson(){
        document_new_account.setText( null );
        document_new_account.requestFocus();
        document_new_account.setFilters(new InputFilter[] {new InputFilter.LengthFilter(18)});
        name_document_new_account.setText( R.string.label_cnpj_new_account );
        button_physical_person.setBackgroundResource( R.drawable.button_check_gray );
        button_physical_person.setTextColor( getResources().getColor( R.color.colorDarkGray ) );
        button_legal_person.setBackgroundResource( R.drawable.button_check_blue );
        button_legal_person.setTextColor( getResources().getColor( R.color.colorWhite ) );
        box_physical_person.setVisibility( View.VISIBLE );
        item_genre_new_account.setVisibility( View.GONE );

        TYPE_PERSON = "J";

        if(ID_SERVICE == 1){
            box_payment_new_account.setVisibility( View.VISIBLE );
            box_payment_new_account.setAnimation( animation_open );
            animation_open.start();
        }else{
            box_payment_new_account.setVisibility( View.GONE );
        }

        button_send_new_account.setVisibility( View.VISIBLE );
    }

    private void changeTypePayment(){
        tab_layout_payment_new_account.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        box_payment_boleto.setVisibility( View.VISIBLE );
                        box_payment_credit.setVisibility( View.GONE );
                        box_payment_debit.setVisibility( View.GONE );
                        break;

                    case 1:
                        box_payment_boleto.setVisibility( View.GONE );
                        box_payment_credit.setVisibility( View.VISIBLE );
                        box_payment_debit.setVisibility( View.GONE );
                        break;

                    case 2:
                        box_payment_boleto.setVisibility( View.GONE );
                        box_payment_credit.setVisibility( View.GONE );
                        box_payment_debit.setVisibility( View.VISIBLE );
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        } );
    }

    private void insertPlanPlanPlus() {
        String id_service = String.valueOf( ID_SERVICE );
        String document = document_new_account.getText().toString().trim();
        String razao = razao_social_new_account.getText().toString().trim();
        String name = name_new_account.getText().toString().trim();
        String email = email_new_account.getText().toString().trim();
        String cellphone = cellphone_new_account.getText().toString().trim();
        String password = password_new_account.getText().toString().trim();
        String conf_password = conf_password_new_account.getText().toString().trim();
        switch (TYPE_PERSON){
            case "F":

                break;
            case "J":

                break;
        }

    }

    private void insertPlanFree(){
        String id_service = String.valueOf( ID_SERVICE );
        String cpf = document_new_account.getText().toString().trim();
        String name = name_new_account.getText().toString().trim();
        String email = email_new_account.getText().toString().trim();
        String cellphone = cellphone_new_account.getText().toString().trim();
        String password = password_new_account.getText().toString().trim();
        String conf_password = conf_password_new_account.getText().toString().trim();
        String genre = TYPE_PERSON;
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        if(cpf.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu CPF" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            document_new_account.requestFocus();
        }else if(name.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu nome" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            name_new_account.requestFocus();
        }else if(email.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu email" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            email_new_account.requestFocus();
        }else if(Valitations.email( email ) == false){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Email informado é inválido" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            email_new_account.requestFocus();
        }else if(cellphone.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Informe seu telefone" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            cellphone_new_account.requestFocus();
        }else if(password.isEmpty()){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Crie sua senha" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            password_new_account.requestFocus();
        }else if(!password.equals( conf_password )){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Senha não conferem" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
            password_new_account.requestFocus();
        }else if(genre.equals( null )){
            builder.setTitle( "Ops!!!" );
            builder.setMessage( "Escolha seu gênero" );
            builder.setPositiveButton( "Ok", null );
            builder.create().show();
        }else{
            Alerts.progress_open( this, null, "Cadastrando usuário", false );
            serviceNewAccount.create_free( name, email, password, cellphone, genre, cpf);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = getIntent();
        setResult( Activity.RESULT_CANCELED, intent );
        finish();
    }

}
