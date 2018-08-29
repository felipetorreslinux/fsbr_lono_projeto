package com.lono.Views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.github.barteksc.pdfviewer.PDFView;
import com.lono.R;

public class View_Termos_de_Uso extends AppCompatActivity {

    Toolbar toolbar;

    PDFView pdf_termos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_termos_de_uso);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);

        createToolbar(toolbar);

        pdf_termos = findViewById(R.id.pdf_termos);
        pdf_termos.fromAsset("termos.pdf").load();

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_termosdeuso);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Termos de Uso");
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
    public void onBackPressed() {
        finish();
    }
}
