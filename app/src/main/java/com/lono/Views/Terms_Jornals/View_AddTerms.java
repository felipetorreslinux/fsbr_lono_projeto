package com.lono.Views.Terms_Jornals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import com.lono.R;
import com.lono.Service.Service_Terms;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;

import java.util.ArrayList;
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

    Service_Terms serviceTermsJournals;

    boolean LITERAL = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_termos);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        serviceTermsJournals = new Service_Terms(this);
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
                }else {
                    LITERAL = false;
                }
            }
        });
        record_mic_terms = findViewById(R.id.record_mic_terms);
        record_mic_terms.setOnClickListener(this);
        image_info_ad_termos = findViewById(R.id.image_info_ad_termos);
        image_info_ad_termos.setOnClickListener(this);

        LITERAL = false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_term, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.save_term:
                saveTerms();
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
        }

    }

    private void saveTerms() {
        String text_term = text_add_terms.getText().toString().trim();
        if(text_term.isEmpty()){
            Snackbar.make(getWindow().getDecorView(),
                    "Informe o termo", Snackbar.LENGTH_SHORT).show();
            text_add_terms.requestFocus();
        }else{
            Keyboard.close(this, getWindow().getDecorView());
            Alerts.progress_open(this, null, "Salvando termo", false);
            serviceTermsJournals.addTerms(text_term, LITERAL);
            text_add_terms.setText(null);
        }
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
