package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.lono.Models.List_Plans_Model;
import com.lono.R;
import com.lono.Service.Service_List_Plans;
import com.lono.Utils.Alerts;
import com.lono.Utils.Conexao;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;

public class View_Plans_List extends AppCompatActivity{

    Toolbar toolbar;

    RecyclerView recyclerview_plans;
    Service_List_Plans serviceListPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plans_list);

        createToolbar(toolbar);
        serviceListPlans = new Service_List_Plans( this );

        recyclerview_plans = (RecyclerView) findViewById(R.id.recyclerview_plans);
        recyclerview_plans.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_plans.setNestedScrollingEnabled(false);
        recyclerview_plans.setHasFixedSize(true);
        Alerts.progress_open(this, null, "Listando planos...", false);

    }

    @Override
    protected void onResume() {
        serviceListPlans.list(recyclerview_plans);
        super.onResume();
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_list_plans);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_list_plans);
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
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            finish();
        }
    }
}
