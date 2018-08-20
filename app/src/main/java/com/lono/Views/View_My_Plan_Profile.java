package com.lono.Views;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_Profile;
import com.lono.Utils.Alerts;

public class View_My_Plan_Profile extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    ViewStub loading;
    TextView name_plan;
    TextView edit_plan;
    TextView terms_plan;
    TextView termos_usados;
    TextView price_plan;
    LinearLayout item_pay_my_plan;
    TextView type_pay_plan;

    Service_Profile serviceProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_plan_profile);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        serviceProfile = new Service_Profile(this);
        createToolbar(toolbar);
        infoPlan();
        serviceProfile.detailsPlanProfile(name_plan, terms_plan, termos_usados, price_plan, type_pay_plan, item_pay_my_plan, loading);
    }

    private void infoPlan(){
        name_plan = findViewById(R.id.name_plan);
        edit_plan = findViewById(R.id.edit_plan);
        terms_plan = findViewById(R.id.terms_plan);
        termos_usados = findViewById(R.id.termos_usados);
        price_plan = findViewById(R.id.price_plan);
        item_pay_my_plan = findViewById(R.id.item_pay_my_plan);
        type_pay_plan = findViewById(R.id.type_pay_plan);
        edit_plan.setOnClickListener(this);
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
            case R.id.edit_plan:
                Alerts.progress_open(this, null, "Carregando planos...", true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Alerts.progress_clode();
                        Snackbar.make(getWindow().getDecorView(), "Em desenvolvimento", Snackbar.LENGTH_LONG).show();
                    }
                }, 2000);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
