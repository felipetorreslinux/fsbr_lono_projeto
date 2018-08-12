package com.lono.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lono.Permissions.Permissions;
import com.lono.R;

public class View_Intro extends AppCompatActivity implements View.OnClickListener{

    Button button_login;
    Button button_register;

    LinearLayout button_quem_somos;
    LinearLayout button_contact;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_intro);

        Permissions.request(this);

        button_login = (Button) findViewById(R.id.button_intro_login);
        button_login.setOnClickListener(this);
        button_register = (Button) findViewById(R.id.button_intro_register);
        button_register.setOnClickListener(this);

        button_quem_somos = (LinearLayout) findViewById(R.id.button_quem_somos);
        button_quem_somos.setOnClickListener(this);
        button_contact = (LinearLayout) findViewById(R.id.button_contact);
        button_contact.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        editor = getSharedPreferences("intro_view", MODE_PRIVATE).edit();
        editor.putInt("view", 1);
        editor.commit();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_intro_login:
                Intent login = new Intent(this, View_Login.class);
                startActivity(login);
                break;

            case R.id.button_intro_register:
                Intent plans = new Intent(this, View_Plans_List.class);
                startActivity(plans);
                break;

            case R.id.button_quem_somos:
                Intent intent = new Intent(this, View_QuemSomos.class);
                startActivity(intent);
                break;

            case R.id.button_contact:
                Intent contact = new Intent(this, View_Contact.class);
                startActivity(contact);
                break;
        }
    }
}
