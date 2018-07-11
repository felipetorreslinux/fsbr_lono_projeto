package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.Firebase.Database;
import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

import org.w3c.dom.Text;

import java.security.Key;

public class View_Login extends Activity implements View.OnClickListener {

    ImageView imageview_back_login;
    EditText editText_email_login;
    EditText editText_pass_login;
    Button button_login;
    TextView textview_recovery_pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);

        imageview_back_login = (ImageView) findViewById(R.id.imageview_back_login);
        imageview_back_login.setOnClickListener(this);

        editText_email_login = (EditText) findViewById(R.id.edit_text_email_login);
        editText_pass_login = (EditText) findViewById(R.id.edit_text_pass_login);
        button_login= (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);

        textview_recovery_pass = (TextView) findViewById(R.id.textview_recovery_pass);
        textview_recovery_pass.setOnClickListener(this);

        Database.addUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(editText_email_login.getText().toString().isEmpty()){
            Keyboard.open(this, editText_email_login);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_login:
                onBackPressed();
                break;

            case R.id.button_login:
                if(editText_email_login.getText().toString().isEmpty()){
                    Alerts.curto(this, R.string.erro_email_empty);
                }else if(Valitations.valid_email(editText_email_login) == false){
                    Alerts.curto(this, R.string.erro_email_invalid);
                }else if(editText_pass_login.getText().toString().isEmpty()){
                    Alerts.curto(this, R.string.erro_pass_empty);
                }else if(editText_pass_login.getText().toString().length() < 6){
                    Alerts.curto(this, R.string.erro_pass_length);
                }else{
                    Keyboard.close(this, getWindow().getDecorView());
                    if(editText_email_login.getText().toString() == "admin@admin.com" || editText_pass_login.getText().toString() == "123456"){
                        Alerts.curto(this, R.string.login_ok_access);
                        Intent intent = new Intent(View_Login.this, View_Principal.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Alerts.curto(this, R.string.login_error_access);
                        editText_email_login.setText(null);
                        editText_pass_login.setText(null);
                    }
                }
                break;

            case R.id.textview_recovery_pass:
                Intent intent = new Intent(this, View_Recovery_Pass.class);
                intent.putExtra("email", editText_email_login.getText().toString());
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
