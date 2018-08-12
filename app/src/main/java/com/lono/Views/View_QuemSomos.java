package com.lono.Views;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.lono.R;

public class View_QuemSomos extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_quemsomos);

        createToolbar(toolbar);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_quemsomos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_quemsomom);
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
