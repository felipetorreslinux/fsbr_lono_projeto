package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_Profile;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCellPhone;

public class View_Edit_Profile extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    TextView name_profile_edit;
    EditText email_profile_edit;
    EditText cellphone_profile_edit;
    LinearLayout item_password_profile_edit;

    TextInputLayout layout_password_atual;
    TextInputLayout layout_new_password_atual;
    EditText password_atual_edit;
    EditText new_password_atual_edit;
    Button save_password_edit;

    Service_Profile serviceProfile;
    BottomSheetDialog bottomSheetDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_edit_profile);
        serviceProfile = new Service_Profile(this);
        overridePendingTransition(R.anim.slide_left, R.anim.fade_out);
        sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);

        createToolbar(toolbar);
        infoEditProfile();


    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar Dados");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void infoEditProfile(){
        name_profile_edit = findViewById(R.id.name_profile_edit);
        email_profile_edit = findViewById(R.id.email_profile_edit);
        cellphone_profile_edit = findViewById(R.id.cellphone_profile_edit);
        name_profile_edit.setText(sharedPreferences.getString("name", null));
        email_profile_edit.setText(sharedPreferences.getString("email", null));
        cellphone_profile_edit.setText(sharedPreferences.getString("cellphone_account", null));
        cellphone_profile_edit.addTextChangedListener(MaskCellPhone.insert(cellphone_profile_edit));
        item_password_profile_edit = (LinearLayout) findViewById(R.id.item_password_profile_edit);
        item_password_profile_edit.setOnClickListener(this);
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
            case R.id.item_password_profile_edit:
                editPassword();
                break;
        }
    }

    private void editPassword(){
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottomsweet_edit_password, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        layout_password_atual = view.findViewById(R.id.layout_password_atual);
        layout_new_password_atual = view.findViewById(R.id.layout_new_password_atual);
        password_atual_edit = view.findViewById(R.id.password_atual_edit);
        new_password_atual_edit = view.findViewById(R.id.new_password_atual_edit);
        save_password_edit = view.findViewById(R.id.save_password_edit);
        save_password_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = password_atual_edit.getText().toString().trim();
                String new_password = new_password_atual_edit.getText().toString().trim();
                if(password.isEmpty()) {
                    layout_password_atual.setErrorEnabled(true);
                    layout_new_password_atual.setErrorEnabled(false);
                    layout_password_atual.setError("Informe sua senha atual");
                    password_atual_edit.requestFocus();
                }else if(password.length() < 6){
                    layout_password_atual.setErrorEnabled(true);
                    layout_new_password_atual.setErrorEnabled(false);
                    layout_password_atual.setError("Senha inválida");
                    password_atual_edit.requestFocus();
                }else if(new_password.isEmpty()){
                    layout_password_atual.setErrorEnabled(false);
                    layout_new_password_atual.setErrorEnabled(true);
                    layout_new_password_atual.setError("Informe sua nova senha");
                    new_password_atual_edit.requestFocus();
                }else if(new_password.length() < 6){
                    layout_password_atual.setErrorEnabled(false);
                    layout_new_password_atual.setErrorEnabled(true);
                    layout_new_password_atual.setError("Senha inválida");
                    new_password_atual_edit.requestFocus();
                }else{
                    layout_password_atual.setErrorEnabled(false);
                    layout_new_password_atual.setErrorEnabled(false);
                    Alerts.progress_open(View_Edit_Profile.this, null, "Atualizando senha...", true);
                    serviceProfile.resetPassword(password, new_password, bottomSheetDialog);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
