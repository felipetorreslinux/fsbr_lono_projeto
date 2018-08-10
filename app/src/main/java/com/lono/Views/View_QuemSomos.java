package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lono.R;

public class View_QuemSomos extends AppCompatActivity implements View.OnClickListener {

    ImageView imageview_back_quemsomos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_quemsomos);

        imageview_back_quemsomos = (ImageView) findViewById(R.id.imageview_back_quemsomos);
        imageview_back_quemsomos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_quemsomos:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
