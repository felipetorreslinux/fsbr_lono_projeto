package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.lono.Adapter.List_Plans_Adapter;
import com.lono.Models.List_Plans_Model;
import com.lono.R;
import com.lono.Service.Service_List_Plans;
import com.lono.Utils.Alerts;
import com.lono.Utils.Conexao;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class View_List_Plans extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    RecyclerView recyclerview_plans;
    Service_List_Plans serviceListPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plans_list);

        sharedPreferences = getSharedPreferences("plans", MODE_PRIVATE);
        createToolbar(toolbar);
        serviceListPlans = new Service_List_Plans( this );

        recyclerview_plans = findViewById(R.id.recyclerview_plans);
        recyclerview_plans.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_plans.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listPlans();
            }
        }, 10);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listPlans(){
        try{
            JSONArray plans = new JSONArray(sharedPreferences.getString("list", ""));
            if(plans.length() > 0){
                List<List_Plans_Model> list_plans = new ArrayList<>();
                for (int i = 0; i < plans.length(); i++){
                    JSONObject jsonObject = plans.getJSONObject(i);
                    List_Plans_Model list_plans_model = new List_Plans_Model(jsonObject);
                    list_plans.add(list_plans_model);
                }
                List_Plans_Adapter list_plans_adapter = new List_Plans_Adapter(this, list_plans);
                recyclerview_plans.setAdapter(list_plans_adapter);
            }
        }catch (JSONException e){}
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = findViewById(R.id.actionbar_list_plans);
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
