package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.lono.Firebase.Database;
import com.lono.R;

public class View_Intro extends Activity implements View.OnClickListener{

    Button button_login;
    Button button_register;
    VideoView videoview_intro;

    @Override
    protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.view_intro);

        videoview_intro = (VideoView) findViewById(R.id.videoview_intro);
        button_login = (Button) findViewById(R.id.button_intro_login);
        button_login.setOnClickListener(this);
        button_register = (Button) findViewById(R.id.button_intro_register);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_intro);
        videoview_intro.setVideoURI(uri);
        videoview_intro.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
            }
        });
        videoview_intro.start();
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
