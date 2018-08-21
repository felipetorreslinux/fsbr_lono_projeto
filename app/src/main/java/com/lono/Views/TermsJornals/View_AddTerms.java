package com.lono.Views.TermsJornals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.Adapter.Adapter_List_Terms;
import com.lono.Models.Terms_Model;
import com.lono.R;
import com.lono.Service.Service_Terms_Journals;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class View_AddTerms extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    AlertDialog.Builder builder;
    Toolbar toolbar;

    TextInputLayout layout_add_terms;
    EditText text_add_terms;
    CheckBox check_termos_literal;
    ImageView record_mic_terms;
    ImageView image_info_ad_termos;
    Button button_save_term;

    Adapter_List_Terms adapterListTerms;
    RecyclerView recycler_terms;

    Service_Terms_Journals serviceTermsJournals;

    boolean LITERAL = false;

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
        check_termos_literal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LITERAL = true;
                }else{
                    LITERAL = false;
                }
            }
        });
        record_mic_terms = findViewById(R.id.record_mic_terms);
        record_mic_terms.setOnClickListener(this);
        image_info_ad_termos = findViewById(R.id.image_info_ad_termos);
        image_info_ad_termos.setOnClickListener(this);

        button_save_term = findViewById(R.id.button_save_term);
        button_save_term.setVisibility(View.GONE);
        button_save_term.setOnClickListener(this);

        text_add_terms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    button_save_term.setVisibility(View.VISIBLE);
                    if(s.length() > 4){
                        check_termos_literal.setChecked(false);
                        LITERAL = false;
                    }else{
                        check_termos_literal.setChecked(true);
                        LITERAL = true;
                    }
                }else{
                    button_save_term.setVisibility(View.GONE);
                    check_termos_literal.setChecked(true);
                    LITERAL = true;
                };
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        recycler_terms = findViewById(R.id.recycler_terms);
        recycler_terms.setLayoutManager(new LinearLayoutManager(this));
        recycler_terms.setHasFixedSize(true);
        recycler_terms.setNestedScrollingEnabled(false);
        serviceTermsJournals.listTerms(recycler_terms);

    }

    @Override
    protected void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Keyboard.close(View_AddTerms.this, getWindow().getDecorView());
            }
        }, 100);
        super.onResume();
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_add_termos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Termos");
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

            case R.id.record_mic_terms:
                speakTerm();
                break;

            case R.id.button_save_term:
                saveTerms();
                break;
        }

    }

    private void saveTerms() {
        String term = text_add_terms.getText().toString().trim();
        Alerts.progress_open(this, null, "Adicionando termo", false);
        serviceTermsJournals.addTerms(term, LITERAL, recycler_terms);
        text_add_terms.setText(null);
    }

    private void speakTerm() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale o termo...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_add_terms.setText(result.get(0));
                }else {
                    text_add_terms.setText(null);
                }
                break;
            }

        }
    }
}
