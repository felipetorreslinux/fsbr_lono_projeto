package com.lono.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.lono.R;
import com.lono.Service.Service_Login;
import com.lono.Service.Service_Profile;
import com.lono.Service.Service_Terms_Journals;
import com.lono.Utils.Alerts;
import com.lono.Views.Fragments.Alerts_Fragment;
import com.lono.Views.Fragments.Person_Fragment;
import com.lono.Views.Fragments.Publications_Fragment;
import com.lono.Views.Fragments.Terms_Journals_Fragment;
import com.lono.Views.Terms_Jornals.View_Add_Journal;
import com.lono.Views.Terms_Jornals.View_Add_Terms;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class View_Principal extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    MenuItem settings_profile;
    MenuItem add_terms_journals;

    LinearLayout item_home;
    LinearLayout item_search;
    LinearLayout item_notifi;
    LinearLayout item_person;
    static int TAB_INDEX;

    Service_Terms_Journals serviceTermsJournals;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        serviceTermsJournals = new Service_Terms_Journals(this);
        serviceTermsJournals.listAllJournals();

        builder = new AlertDialog.Builder(this);
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

    private void createToolbar(Toolbar toolbar) {
        toolbar = (Toolbar) findViewById(R.id.actionbar_principal);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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
        getFragmentManager().beginTransaction().replace(R.id.container, new Publications_Fragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }

        settings_profile = menu.findItem(R.id.settings_profile).setVisible(false);
        add_terms_journals = menu.findItem(R.id.add_terms_journals).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.add_terms_journals:
                View view = getLayoutInflater().inflate(R.layout.dialog_add_terms_journals, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView text_add_terms = view.findViewById(R.id.text_add_terms);
                TextView text_add_journals = view.findViewById(R.id.text_add_journals);

                text_add_terms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Intent add_termos = new Intent(View_Principal.this, View_Add_Terms.class);
                        startActivityForResult(add_termos, 1000);
                    }
                });

                text_add_journals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Intent add_journal = new Intent(View_Principal.this, View_Add_Journal.class);
                        startActivityForResult(add_journal, 1000);
                    }
                });

                break;

            case R.id.settings_profile:
                Intent intent = new Intent(this, View_Settings_Profile.class);
                startActivityForResult(intent, 2000);
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
                    getFragmentManager().beginTransaction().replace(R.id.container, new Publications_Fragment()).commit();
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
                    getFragmentManager().beginTransaction().replace(R.id.container, new Terms_Journals_Fragment()).commit();
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
                    getFragmentManager().beginTransaction().replace(R.id.container, new Alerts_Fragment()).commit();
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
                    getFragmentManager().beginTransaction().replace(R.id.container, new Person_Fragment()).commit();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(TAB_INDEX != 0){
            TAB_INDEX = 0;
            item_home.setAlpha(1.0f);
            item_search.setAlpha(0.3f);
            item_notifi.setAlpha(0.3f);
            item_person.setAlpha(0.3f);
            getSupportActionBar().setTitle("Publicações");
            menuPrincipal();
            getFragmentManager().beginTransaction().replace(R.id.container, new Publications_Fragment()).commit();
        }else{
            finish();
        }
    }

    private void menuPrincipal(){
         switch (TAB_INDEX){
             case 0:
                 settings_profile.setVisible(false);
                 add_terms_journals.setVisible(false);
                 break;
             case 1:
                 settings_profile.setVisible(false);
                 add_terms_journals.setVisible(true);
                 break;
             case 2:
                 settings_profile.setVisible(false);
                 add_terms_journals.setVisible(false);
                 break;
             case 3:
                 settings_profile.setVisible(true);
                 add_terms_journals.setVisible(false);
                 break;
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1000:
                TAB_INDEX = 1;
                item_home.setAlpha(0.3f);
                item_search.setAlpha(1.0f);
                item_notifi.setAlpha(0.3f);
                item_person.setAlpha(0.3f);
                getSupportActionBar().setTitle("Termos e Jornais");
                menuPrincipal();
                getFragmentManager().beginTransaction().replace(R.id.container, new Terms_Journals_Fragment()).commit();
                break;

            case 2000:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Informações atualizadas com sucesso",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }















        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            Picasso.with(this)
                .load(new File(image.getPath()))
                .resize(150,150)
                .transform(new CropCircleTransformation())
                .into(Person_Fragment.image_profile);

            Alerts.progress_open(this, null, "Carregando imagem\nAguarde...", false);
            new Service_Profile(this).uploadImage(new File(image.getPath()));
        }
    }
}
