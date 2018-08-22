package com.lono.Views.Terms_Jornals;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lono.Adapter.Adapter_All_Journals;
import com.lono.Models.All_Jorunals_Model;
import com.lono.Models.Journals_Model;
import com.lono.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class View_Add_Journal extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    SharedPreferences sharedPreferences;

    RecyclerView recycler_journals;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_journal);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("all_journals", MODE_PRIVATE);

        createToolbar(toolbar);

        recycler_journals = findViewById(R.id.recycler_journals);
        recycler_journals.setLayoutManager(new LinearLayoutManager(this));
        recycler_journals.setHasFixedSize(true);

        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                            jsonObject.getInt("id_jornal"),
                            jsonObject.getString("sigla_jornal"),
                            jsonObject.getString("nome_jornal"),
                            jsonObject.getString("abrangencia"),
                            jsonObject.getString("estados"),
                            false);
                    list_journals.add(all_jorunals_model);
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_add_journals);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jornais");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_journal, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.search_journal:
                searchJournals();
                break;

            case R.id.save_journal:
                saveJouunal();
                break;
        }
        return true;
    }

    private void searchJournals() {
    }

    private void saveJouunal() {
    }

    @Override
    public void onClick(View v) {

    }
}
