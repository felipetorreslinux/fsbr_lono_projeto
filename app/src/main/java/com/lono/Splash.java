package com.lono;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.lono.Views.View_Intro;
import com.lono.Views.View_Intro_Slide;
import com.lono.Views.View_NewAccount;
import com.lono.Views.View_Principal;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class Splash extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences share_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext(), getUnsafeOkHttpClient());

        sharedPreferences = getSharedPreferences("intro_view", MODE_PRIVATE);
        share_profile = getSharedPreferences("profile", MODE_PRIVATE);

    };

    @Override
    public void onResume(){
        super.onResume();
//        loadView();
        startActivity(new Intent(this, View_NewAccount.class));
        finish();
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

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
