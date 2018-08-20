package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.lono.R;

public class View_Settings_Profile extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    Switch switch_notifications_profile;

    LinearLayout item_termos_de_uso;
    LinearLayout item_politic_privacy;
    LinearLayout item_exitapp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_settings_profile);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        builder = new AlertDialog.Builder(this);

        createToolbar(toolbar);

        switch_notifications_profile = (Switch) findViewById(R.id.switch_notifications_profile);

        if(sharedPreferences.getBoolean("view_notifications", true)){
            switch_notifications_profile.setChecked(true);
        }else{
            switch_notifications_profile.setChecked(false);
        }

        item_termos_de_uso = (LinearLayout) findViewById(R.id.item_termos_de_uso);
        item_politic_privacy = (LinearLayout) findViewById(R.id.item_politic_privacy);
        item_exitapp = (LinearLayout) findViewById(R.id.item_exitapp);
        item_termos_de_uso.setOnClickListener(this);
        item_politic_privacy.setOnClickListener(this);
        item_exitapp.setOnClickListener(this);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_settings_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Configurações");
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
            case R.id.item_termos_de_uso:
                Intent terms_of_use = new Intent(this, View_Termos_de_Uso.class);
                startActivity(terms_of_use);
                break;

            case R.id.item_politic_privacy:
                Intent politic_privacy = new Intent(this, View_Politic_Privacy.class);
                startActivity(politic_privacy);
                break;

            case R.id.item_exitapp:
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja realmente sair do Lono?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("token", "");
                        editor.commit();
                        finishAffinity();
                    }
                });
                builder.setNegativeButton("Não", null);
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
