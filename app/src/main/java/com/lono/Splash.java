package com.lono;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.lono.Firebase.Database;
import com.lono.Views.View_Intro;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120,TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
    }

    @Override
    public void onResume(){
        super.onResume();
        openIntro();
    }

    private void openIntro(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, View_Intro.class);
                startActivityForResult(intent,1);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}
