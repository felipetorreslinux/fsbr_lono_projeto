package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class View_Plans_List extends Activity implements View.OnClickListener {

    ImageView imageview_back_list_plans;
    RecyclerView recyclerview_plans;
    Service_List_Plans serviceListPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plans_list);

        serviceListPlans = new Service_List_Plans( this );

        imageview_back_list_plans = (ImageView) findViewById(R.id.imageview_back_list_plans);
        imageview_back_list_plans.setOnClickListener(this);

        recyclerview_plans = (RecyclerView) findViewById(R.id.recyclerview_plans);
        recyclerview_plans.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_plans.setNestedScrollingEnabled(false);
        recyclerview_plans.setHasFixedSize(true);

        Alerts.progress_open(this, null, "Listando planos...", false);
        loadListPlans();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadListPlans() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Conexao.check(View_Plans_List.this) == true){
                    serviceListPlans.list(recyclerview_plans);
                }else{
                    Alerts.progress_clode();
                    Alerts.curto(View_Plans_List.this, R.string.not_connected);
                }
            }
        }, 1000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_list_plans:
                onBackPressed();
                break;
        }
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
        }else{

        }
    }
}
