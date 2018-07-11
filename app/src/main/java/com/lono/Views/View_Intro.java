package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.lono.Firebase.Database;
import com.lono.R;

public class View_Intro extends Activity implements View.OnClickListener{

    Button button_login;
    Button button_register;

    @Override
    protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.view_intro);

        button_login = (Button) findViewById(R.id.button_intro_login);
        button_login.setOnClickListener(this);
        button_register = (Button) findViewById(R.id.button_intro_register);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_intro_login:
                Intent intent = new Intent(this, View_Login.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.button_intro_register:

                break;
        }
    }
}
