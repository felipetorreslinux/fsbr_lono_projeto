package com.lono.Views;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.TypePlanProfile;

public class View_My_Plan_Profile extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    TextView name_plan;
    TextView edit_plan;
    TextView terms_plan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_plan_profile);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);

        createToolbar(toolbar);

    }

    @Override
    protected void onResume() {
        infoPlan();
        super.onResume();
    }

    private void infoPlan(){
        name_plan = findViewById(R.id.name_plan);
        edit_plan = findViewById(R.id.edit_plan);
        terms_plan = findViewById(R.id.terms_plan);
        edit_plan.setOnClickListener(this);
        name_plan.setText(TypePlanProfile.name(sharedPreferences.getString("type_account", null)));
        if(sharedPreferences.getString("type_account", null).equals("F")){
            terms_plan.setText("01");
        }
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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
