package com.lono.Views;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;

public class View_Pub_Materia extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pub_materia);

        createToolbar(toolbar);

        WebView view = new WebView(this);
        view.setVerticalScrollBarEnabled(false);
        view.setSelected(false);
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        ((LinearLayout)findViewById(R.id.view_materia)).addView(view);

        view.loadData(getIntent().getExtras().getString("materia"), "text/html; charset=utf-8", "utf-8");

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_pub_materia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("#" + getIntent().getExtras().getString("id_materia"));
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

    }
}
