package com.lono.Views.TermsJornals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.Adapter.Adapter_List_Terms;
import com.lono.Models.Terms_Model;
import com.lono.R;
import com.lono.Service.Service_Terms_Journals;

import java.util.ArrayList;
import java.util.List;

public class View_AddTerms extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder;
    Toolbar toolbar;

    TextInputLayout layout_add_terms;
    EditText text_add_terms;

    CheckBox check_termos_literal;
    ImageView image_info_ad_termos;

    RecyclerView recycler_terms;

    Service_Terms_Journals serviceTermsJournals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_termos);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        serviceTermsJournals = new Service_Terms_Journals(this);
        builder = new AlertDialog.Builder(this);
        createToolbar(toolbar);

        layout_add_terms = findViewById(R.id.layout_add_terms);
        text_add_terms = findViewById(R.id.text_add_terms);

        check_termos_literal = findViewById(R.id.check_termos_literal);
        image_info_ad_termos = findViewById(R.id.image_info_ad_termos);
        image_info_ad_termos.setOnClickListener(this);

        recycler_terms = findViewById(R.id.recycler_terms);
        recycler_terms.setLayoutManager(new LinearLayoutManager(this));
        recycler_terms.setHasFixedSize(true);
        recycler_terms.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceTermsJournals.listTerms(recycler_terms);
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_add_termos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Adicionar Termos");
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
            case R.id.image_info_ad_termos:
                builder.setTitle("O que são termos literais?");
                builder.setMessage("Termos literais retornarão resultados somente se o termo pesquisado estiver entre espaçamentos. Se seu termo tiver menos de quatro caracteres ele será, obrigatoriamente literal.");
                builder.setPositiveButton("OK", null);
                builder.create().show();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
