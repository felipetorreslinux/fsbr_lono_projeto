package com.lono.Views;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.TtsSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lono.R;
import com.lono.Service.Service_Profile;
import com.lono.Utils.Alerts;
import com.lono.Utils.CalcTerms;
import com.lono.Utils.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class View_My_Plan_Profile extends AppCompatActivity implements View.OnClickListener{

    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    ViewStub loading;
    TextView value_terms;
    TextView name_plan;
    TextView terms_plan;
    TextView termos_usados;
    TextView price_plan;
    TextView date_expira_plan;
    LinearLayout item_pay_my_plan;
    TextView type_pay_plan;

    Service_Profile serviceProfile;
    double VALUE_TERMS_EDIT_PLAN_PROFILE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_plan_profile);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        serviceProfile = new Service_Profile(this);
        builder = new AlertDialog.Builder(this);
        createToolbar(toolbar);
        infoPlan();
        serviceProfile.detailsPlanProfile(value_terms, name_plan, terms_plan, termos_usados, price_plan, date_expira_plan, type_pay_plan, item_pay_my_plan, loading);
        VALUE_TERMS_EDIT_PLAN_PROFILE = 4.99;
    }

    private void infoPlan(){
        value_terms = findViewById(R.id.value_terms);
        name_plan = findViewById(R.id.name_plan);
        terms_plan = findViewById(R.id.terms_plan);
        termos_usados = findViewById(R.id.termos_usados);
        price_plan = findViewById(R.id.price_plan);
        date_expira_plan = findViewById(R.id.date_expira_plan);
        item_pay_my_plan = findViewById(R.id.item_pay_my_plan);
        type_pay_plan = findViewById(R.id.type_pay_plan);
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_my_plan_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meu Plano");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_plan_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit_pmay_plan:
                editPlanProfile();
                break;
        }
        return true;
    }

    private void editPlanProfile() {

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_plan_profile, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.create().show();

        final double VALUE_TERNS = Double.parseDouble(value_terms.getText().toString().trim());

        System.out.println(value_terms.getText().toString().trim());

        final LinearLayout layout_buttons_edit_plan_profile = view.findViewById(R.id.layout_buttons_edit_plan_profile);
        final LinearLayout box_plan_plus_edit_plan_profile = view.findViewById(R.id.box_plan_plus_edit_plan_profile);
        layout_buttons_edit_plan_profile.setVisibility(View.GONE);
        box_plan_plus_edit_plan_profile.setVisibility(View.GONE);
        final Spinner spinner_plan = view.findViewById(R.id.spinner_plan);

        final ImageView terms_remove = view.findViewById(R.id.terms_remove);
        final TextView qtd_terms = view.findViewById(R.id.qtd_terms);
        final ImageView terms_add = view.findViewById(R.id.terms_add);

        final TextInputLayout layout_pay_plan = view.findViewById(R.id.layout_pay_plan);
        final TextView price_mensal_edit_plan_profile = view.findViewById(R.id.price_mensal_edit_plan_profile);
        price_mensal_edit_plan_profile.setText(Price.real(CalcTerms.value_mensal(4.99, 10)));
        final TextView price_anual_edit_plan_profile = view.findViewById(R.id.price_anual_edit_plan_profile);
        price_anual_edit_plan_profile.setText(Price.real(CalcTerms.value_anual(4.99, 10)));

        final Button button_edit_plan = view.findViewById(R.id.button_edit_plan);


        String[] plans = new String[]{"Escolha","Free","Plus","+200"};
        List<String> list_plans = new ArrayList<>(Arrays.asList(plans));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.item_spinner_plan_profile,list_plans);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner_plan_profile);
        spinner_plan.setAdapter(spinnerArrayAdapter);
        for(int i = 0; i < list_plans.size(); i++){
            String name = list_plans.get(i).toString();
            if(name.equals(name_plan.getText().toString().trim())){
                spinnerArrayAdapter.remove(name);
                spinnerArrayAdapter.notifyDataSetChanged();
            }
        }
        spinner_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item){
                    case "Free":
                        layout_buttons_edit_plan_profile.setVisibility(View.VISIBLE);
                        box_plan_plus_edit_plan_profile.setVisibility(View.GONE);
                        button_edit_plan.setText("Alterar");
                        break;

                    case "Plus":
                        layout_buttons_edit_plan_profile.setVisibility(View.VISIBLE);
                        box_plan_plus_edit_plan_profile.setVisibility(View.VISIBLE);
                        button_edit_plan.setText("Pagar");
                        break;
                    case "+200":
                        layout_buttons_edit_plan_profile.setVisibility(View.VISIBLE);
                        box_plan_plus_edit_plan_profile.setVisibility(View.GONE);
                        button_edit_plan.setText("Solicitar");
                        break;
                    default:
                        layout_buttons_edit_plan_profile.setVisibility(View.GONE);
                        box_plan_plus_edit_plan_profile.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        terms_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TERMS = Integer.parseInt( qtd_terms.getText().toString().trim() );
                TERMS--;
                if(TERMS <= 10) {
                    TERMS = 10;
                }
                qtd_terms.setText( String.valueOf( TERMS ) );
                price_mensal_edit_plan_profile.setText(Price.real(CalcTerms.value_mensal(VALUE_TERNS, TERMS)));
                price_anual_edit_plan_profile.setText(Price.real(CalcTerms.value_anual(VALUE_TERNS, TERMS)));
            }
        });

        terms_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TERMS = Integer.parseInt( qtd_terms.getText().toString().trim() );
                TERMS++;
                qtd_terms.setText( String.valueOf( TERMS ) );
                price_mensal_edit_plan_profile.setText(Price.real(CalcTerms.value_mensal(VALUE_TERNS, TERMS)));
                price_anual_edit_plan_profile.setText(Price.real(CalcTerms.value_anual(VALUE_TERNS, TERMS)));
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
