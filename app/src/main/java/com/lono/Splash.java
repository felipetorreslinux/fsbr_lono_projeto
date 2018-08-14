package com.lono;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.lono.Permissions.Permissions;
import com.lono.Views.View_Intro;
import com.lono.Views.View_Intro_Slide;
import com.lono.Views.View_Principal;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Splash extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences share_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

        sharedPreferences = getSharedPreferences("intro_view", MODE_PRIVATE);
        share_profile = getSharedPreferences("profile", MODE_PRIVATE);

    };

    @Override
    public void onResume(){
        super.onResume();
        loadView();
    };

    private void loadView(){
        if(sharedPreferences != null){
            if(sharedPreferences.getInt("view", 0) == 1){
                if(share_profile != null){
                    if(!share_profile.getString("token", "").equals("")){
                        openCentral();
                    }else{
                        openIntro();
                    }
                }
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
