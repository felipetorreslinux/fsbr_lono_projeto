package com.lono.Views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.lono.R;
import com.lono.Service.Service_Login;

public class View_Principal extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences.Editor editor;
    Toolbar toolbar;
    MenuItem exit;
    MenuItem settings_profile;

    LinearLayout item_home;
    LinearLayout item_search;
    LinearLayout item_notifi;
    LinearLayout item_person;
    static int TAB_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);

        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();

        infoUserProfile();

        createToolbar(toolbar);

        createBottomBar();


    }

    private void infoUserProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        Service_Login serviceLogin = new Service_Login(this);
        if(sharedPreferences != null){
            String token = sharedPreferences.getString("token", null);
            serviceLogin.info_profile(token);
        }
    }

    private void createBottomBar() {
        item_home = (LinearLayout) findViewById(R.id.item_home);
        item_search = (LinearLayout) findViewById(R.id.item_search);
        item_notifi = (LinearLayout) findViewById(R.id.item_notifi);
        item_person = (LinearLayout) findViewById(R.id.item_person);
        item_home.setOnClickListener(this);
        item_search.setOnClickListener(this);
        item_notifi.setOnClickListener(this);
        item_person.setOnClickListener(this);
        item_home.setAlpha(1.0f);
        item_search.setAlpha(0.3f);
        item_notifi.setAlpha(0.3f);
        item_person.setAlpha(0.3f);
        TAB_INDEX = 0;
        getSupportActionBar().setTitle("Publicações");
    }

    private void createToolbar(Toolbar toolbar) {
        toolbar = (Toolbar) findViewById(R.id.actionbar_principal);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        settings_profile = menu.findItem(R.id.settings_profile);
        exit = menu.findItem(R.id.exit_app);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_profile:
                break;

            case R.id.exit_app:
                editor.putString("token", "");
                editor.commit();
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_home:
                if(TAB_INDEX != 0){
                    TAB_INDEX = 0;
                    item_home.setAlpha(1.0f);
                    item_search.setAlpha(0.3f);
                    item_notifi.setAlpha(0.3f);
                    item_person.setAlpha(0.3f);
                    getSupportActionBar().setTitle("Publicações");
                    menuPrincipal();
                }
                break;
            case R.id.item_search:
                if(TAB_INDEX != 1){
                    TAB_INDEX = 1;
                    item_home.setAlpha(0.3f);
                    item_search.setAlpha(1.0f);
                    item_notifi.setAlpha(0.3f);
                    item_person.setAlpha(0.3f);
                    getSupportActionBar().setTitle("Termos e Jornais");
                    menuPrincipal();
                }
                break;
            case R.id.item_notifi:
                if(TAB_INDEX != 2){
                    TAB_INDEX = 2;
                    item_home.setAlpha(0.3f);
                    item_search.setAlpha(0.3f);
                    item_notifi.setAlpha(1.0f);
                    item_person.setAlpha(0.3f);
                    getSupportActionBar().setTitle("Alertas");
                    menuPrincipal();
                }
                break;
            case R.id.item_person:
                if(TAB_INDEX != 3){
                    TAB_INDEX = 3;
                    item_home.setAlpha(0.3f);
                    item_search.setAlpha(0.3f);
                    item_notifi.setAlpha(0.3f);
                    item_person.setAlpha(1.0f);
                    getSupportActionBar().setTitle("Meu Perfil");
                    menuPrincipal();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(TAB_INDEX != 0){
            item_home.setAlpha(1.0f);
            item_search.setAlpha(0.3f);
            item_notifi.setAlpha(0.3f);
            item_person.setAlpha(0.3f);
            getSupportActionBar().setTitle("Publicações");
            TAB_INDEX = 0;
        }else{
            finish();
        }
    }


    private void menuPrincipal(){
         switch (TAB_INDEX){
             case 0:
                 settings_profile.setVisible(false);
                 exit.setVisible(false);
                 break;
             case 1:
                 settings_profile.setVisible(false);
                 exit.setVisible(false);
                 break;
             case 2:
                 settings_profile.setVisible(false);
                 exit.setVisible(false);
                 break;
             case 3:
                 settings_profile.setVisible(true);
                 exit.setVisible(true);
                 break;
         }
    }
}
