package com.lono.Views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.lono.R;
import com.lono.Service.Service_Terms_Journals;
import com.lono.Utils.Keyboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class View_Search_Publications extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    List<String> list_status = new ArrayList<String>();
    Spinner spinner_status;

    final List<String> list_journals = new ArrayList<String>();
    Spinner spinner_jornais;

    EditText number_process;
    EditText date_start;
    EditText date_end;

    Animation animation;
    Button button_search_pub;

    Calendar calendar = Calendar.getInstance();

    Service_Terms_Journals serviceTermsJournals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_publications);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        serviceTermsJournals = new Service_Terms_Journals(this);

        createToolbar(toolbar);

        spinner_status = findViewById(R.id.spinner_status);
        list_status.clear();
        list_status.add("Todos");
        list_status.add("Lidas");
        list_status.add("Não Lidas");
        list_status.add("Lidas e Não Lidas");
        list_status.add("Ignoradas");
        ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list_status);
        adapter_status.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_status.setAdapter(adapter_status);

        spinner_jornais = findViewById(R.id.spinner_jornais);
        list_journals.clear();
        list_journals.add("Carregando jornais...");
        ArrayAdapter<String> adapter_journals = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list_journals);
        adapter_journals.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_jornais.setAdapter(adapter_journals);

        serviceTermsJournals.listJournalsSpinner(spinner_jornais, list_journals);

        number_process = findViewById(R.id.number_process);

        date_start = findViewById(R.id.date_start);
        date_start.setOnClickListener(this);
        date_end = findViewById(R.id.date_end);
        date_end.setOnClickListener(this);


        animation = new TranslateAnimation(0, 0, 1000, 0);
        animation.setDuration(700);
        animation.setFillEnabled(true);
        button_search_pub = findViewById(R.id.button_search_pub);
        button_search_pub.setOnClickListener(this);
        button_search_pub.setAnimation(animation);
        animation.start();

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_search_publications);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buscar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.date_start:
                new DatePickerDialog(this, start, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                Keyboard.close(this, getWindow().getDecorView());
                break;

            case R.id.date_end:
                new DatePickerDialog(this, end, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                Keyboard.close(this, getWindow().getDecorView());
                break;

            case R.id.button_search_pub:
                Intent intent = getIntent();
                intent.putExtra("status", spinner_status.getSelectedItem().toString());
                intent.putExtra("number_process", number_process.getText().toString().trim());
                intent.putExtra("date_start", date_start.getText().toString().trim());
                intent.putExtra("date_end", date_end.getText().toString().trim());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
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
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }



    DatePickerDialog.OnDateSetListener start = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStart();
        }

    };

    DatePickerDialog.OnDateSetListener end = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEnd();
        }

    };

    private void updateStart() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        date_start.setText(sdf.format(calendar.getTime()));
    }

    private void updateEnd() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        date_end.setText(sdf.format(calendar.getTime()));
    }

}
