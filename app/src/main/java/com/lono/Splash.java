package com.lono;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.common.api.Api;
import com.lono.APIServer.Server;
import com.lono.Firebase.Database;
import com.lono.Permissions.Permissions;
import com.lono.Utils.Profile;
import com.lono.Utils.Valitations;
import com.lono.Views.View_Check_Cellphone;
import com.lono.Views.View_Intro;
import com.lono.Views.View_Intro_Slide;
import com.lono.Views.View_Login;
import com.lono.Views.View_New_Account_PF_Plus;
import com.lono.Views.View_Payment;
import com.lono.Views.View_Principal;
import com.lono.Views.View_Type_Payment;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Splash extends Activity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
    };

    @Override
    public void onResume(){
        super.onResume();
        Permissions.request(this);
        loadView();
    };

    private void loadView(){
        sharedPreferences = getSharedPreferences("intro_view", MODE_PRIVATE);
        if(sharedPreferences != null){
            if(sharedPreferences.getInt("view", 0) == 1){
                openIntro();
            }else{
                openIntroSlide();
            }
        }
    }

    private void openIntroSlide(){
        Intent intent = new Intent(Splash.this, View_Intro_Slide.class);
        startActivity(intent);
        finish();
    }

    private void openCentral(){
        Intent intent = new Intent(Splash.this, View_Principal.class);
        startActivity(intent);
        finish();
    };

    private void openIntro(){
        Intent intent = new Intent(Splash.this, View_Intro.class);
        startActivity(intent);
        finish();
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}
