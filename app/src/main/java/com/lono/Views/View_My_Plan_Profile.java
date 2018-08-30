package com.lono.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class View_My_Plan_Profile extends AppCompatActivity{

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_plan_profile);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        builder = new AlertDialog.Builder(this);
        createToolbar(toolbar);
        infoPlan();
        new Service_Profile(this).detailsPlanProfile(value_terms, name_plan, terms_plan, termos_usados, price_plan, date_expira_plan, type_pay_plan, item_pay_my_plan, loading);
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
        builder.setTitle(R.string.app_name);
        builder.setMessage("Para alterar seu plano você deve entrar no gerenciador do Lono.");
        builder.setPositiveButton("Ir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Alerts.progress_open(View_My_Plan_Profile.this, null, "Carregando...", false);
                new Service_Profile(View_My_Plan_Profile.this).editPlanProfile();
            }
        });
        builder.setNegativeButton("Voltar", null);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
