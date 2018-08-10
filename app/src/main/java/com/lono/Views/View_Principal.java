package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lono.APIServer.Server;
import com.lono.Fragments.Home_Fragment;
import com.lono.Fragments.Person_Fragment;
import com.lono.R;

public class View_Principal extends Activity implements View.OnClickListener {

    static int TAB_INDEX = 0;

    LinearLayout bottom_view_item_home;
    LinearLayout bottom_view_item_search;
    LinearLayout bottom_view_item_alerts;
    LinearLayout bottom_view_item_person;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        bottom_view_item_home = (LinearLayout) findViewById(R.id.bottom_view_item_home);
        bottom_view_item_home.setOnClickListener(this);
        bottom_view_item_search = (LinearLayout) findViewById(R.id.bottom_view_item_search);
        bottom_view_item_search.setOnClickListener(this);
        bottom_view_item_alerts = (LinearLayout) findViewById(R.id.bottom_view_item_alerts);
        bottom_view_item_alerts.setOnClickListener(this);
        bottom_view_item_person = (LinearLayout) findViewById(R.id.bottom_view_item_person);
        bottom_view_item_person.setOnClickListener(this);

        bottom_view_item_home.setAlpha(1.0f);
        bottom_view_item_search.setAlpha(0.5f);
        bottom_view_item_alerts.setAlpha(0.5f);
        bottom_view_item_person.setAlpha(0.5f);
        TAB_INDEX = 0;
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new Home_Fragment()).commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bottom_view_item_home:
                bottom_view_item_home.setAlpha(1f);
                bottom_view_item_search.setAlpha(0.5f);
                bottom_view_item_alerts.setAlpha(0.5f);
                bottom_view_item_person.setAlpha(0.5f);
                if(TAB_INDEX != 0){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new Home_Fragment()).commit();
                }
                TAB_INDEX = 0;
                break;

            case R.id.bottom_view_item_search:
                bottom_view_item_home.setAlpha(0.5f);
                bottom_view_item_search.setAlpha(1.0f);
                bottom_view_item_alerts.setAlpha(0.5f);
                bottom_view_item_person.setAlpha(0.5f);
                TAB_INDEX = 1;
                break;

            case R.id.bottom_view_item_alerts:
                bottom_view_item_home.setAlpha(0.5f);
                bottom_view_item_search.setAlpha(0.5f);
                bottom_view_item_alerts.setAlpha(1.0f);
                bottom_view_item_person.setAlpha(0.5f);
                TAB_INDEX = 2;
                break;

            case R.id.bottom_view_item_person:
                bottom_view_item_home.setAlpha(0.5f);
                bottom_view_item_search.setAlpha(0.5f);
                bottom_view_item_alerts.setAlpha(0.5f);
                bottom_view_item_person.setAlpha(1.0f);
                if(TAB_INDEX != 3){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new Person_Fragment()).commit();
                }
                TAB_INDEX = 3;
                break;
        }
    }
}
