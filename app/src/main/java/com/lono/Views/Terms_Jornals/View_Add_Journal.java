package com.lono.Views.Terms_Jornals;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lono.Adapter.Adapter_All_Journals;
import com.lono.Models.All_Jorunals_Model;
import com.lono.Models.Journals_Model;
import com.lono.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        listJournals();

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

            case R.id.speak_journal:
                speakJournal();
                break;

            case R.id.search_journal:
                searchJournals();
                break;
        }
        return true;
    }

    private void listJournals(){
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

    private void speakJournal() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora...");
        try {
            startActivityForResult(intent, 1600);
        } catch (ActivityNotFoundException a) {}
    }

    private void searchJournals() {
        Intent search_journals = new Intent(this, View_Search_Journals.class);
        startActivityForResult(search_journals, 1500);
    }

    @Override
    public void onClick(View v) {

    }

    private void searchSpeakJournal(String text){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("nome_jornal").contains(text)){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1500:
                if(resultCode == Activity.RESULT_OK){
                    String name = data.getExtras().getString("name");
                    String organ = data.getExtras().getString("organ");
                    String state = data.getExtras().getString("state");
                    if((!name.isEmpty()) && (organ.isEmpty()) && (state.equals("Escolha"))){
                        searchNameJournal(name);
                    }else if((name.isEmpty()) && (!organ.isEmpty()) && (state.equals("Escolha"))){
                        searchOrganJournal(organ);
                    }else if((!name.isEmpty()) && (!organ.isEmpty()) && (state.equals("Escolha"))){
                        searchNameSigleJournal(name, organ);
                    }else if((name.isEmpty()) && (!organ.isEmpty()) && (!state.equals("Escolha"))){
                        searchSigleStateJournal(organ, state);
                    }else if((!name.isEmpty()) && (organ.isEmpty()) && (!state.equals("Escolha"))){
                        searchNameStateJournal(name, state);
                    }else if((name.isEmpty()) && (organ.isEmpty()) && (!state.equals("Escolha"))){
                        searchStateJournal(state);
                    }else{
                        listJournals();
                    }
                }else{
                    listJournals();
                }
                break;

            case 1600:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchSpeakJournal(result.get(0));
                }
                break;
        }
    }

    private void searchNameJournal(String name){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("nome_jornal").contains(name)){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    private void searchOrganJournal(String organ){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("sigla_jornal").contains(organ)){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    private void searchStateJournal(String state){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.getString("estados").contains(state)){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    private void searchNameSigleJournal(String name, String sigle){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if((jsonObject.getString("nome_jornal").contains(name)) && (jsonObject.getString("sigla_jornal").contains(sigle))){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    private void searchSigleStateJournal(String sigle, String state){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if((jsonObject.getString("sigla_jornal").contains(sigle)) && (jsonObject.getString("estados").contains(state))){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

    private void searchNameStateJournal(String name, String state){
        try{
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString("journals", ""));
            if(jsonArray.length() > 0){
                List<All_Jorunals_Model> list_journals = new ArrayList<>();
                list_journals.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if((jsonObject.getString("nome_jornal").contains(name)) && (jsonObject.getString("estados").contains(state))){
                        All_Jorunals_Model all_jorunals_model = new All_Jorunals_Model(
                                jsonObject.getInt("id_jornal"),
                                jsonObject.getString("sigla_jornal"),
                                jsonObject.getString("nome_jornal"),
                                jsonObject.getString("abrangencia"),
                                jsonObject.getString("estados"),
                                false);
                        list_journals.add(all_jorunals_model);
                    }
                }
                Adapter_All_Journals adapterAllJournals = new Adapter_All_Journals(this, list_journals);
                recycler_journals.setAdapter(adapterAllJournals);
            }else{

            }
        }catch (JSONException e){}
    }

}
