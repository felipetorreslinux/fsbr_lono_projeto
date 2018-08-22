package com.lono.Views.Terms_Jornals;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lono.R;

import java.util.ArrayList;
import java.util.List;

public class View_Search_Journals extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Animation animation;

    Spinner spinner_states;
    String[] states = {"Escolha", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RO", "RS", "RR", "SC", "SE", "SP", "TO"};

    TextInputLayout layout_name_journal;
    TextInputLayout layout_organ_journal;

    EditText name_journals;
    EditText organ_journals;

    Button button_search_journal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_journals);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);

        createToolbar(toolbar);

        layout_name_journal = findViewById(R.id.layout_name_journal);
        layout_organ_journal = findViewById(R.id.layout_organ_journal);

        name_journals = findViewById(R.id.name_journals);
        organ_journals = findViewById(R.id.organ_journals);

        spinner_states = findViewById(R.id.spinner_states);
        List<String> list = new ArrayList<>();
        for(int i = 0; i < states.length; i++){
            list.add(states[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_states.setAdapter(dataAdapter);

        animation = new TranslateAnimation(0,0,1000,0);
        animation.setDuration(700);
        animation.setFillEnabled(true);
        button_search_journal = findViewById(R.id.button_search_journal);
        button_search_journal.setAnimation(animation);
        button_search_journal.setOnClickListener(this);
        animation.start();

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

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_search_journals);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buscar Jornais");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_search_journal:
                String name = name_journals.getText().toString().trim();
                String organ = organ_journals.getText().toString().trim();
                String state = spinner_states.getSelectedItem().toString();
                Intent intent = getIntent();
                intent.putExtra("name", name != null ? name : "");
                intent.putExtra("organ", organ != null ? organ : "");
                intent.putExtra("state", state != null ? state : "");
                setResult(Activity.RESULT_OK, intent);
                finish();
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
