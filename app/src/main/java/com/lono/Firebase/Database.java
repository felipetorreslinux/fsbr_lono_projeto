package com.lono.Firebase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

public class Database {

    Activity activity;
    static DatabaseReference databaseReference;
    static TelephonyManager telephonyManager;

    public Database(Activity activity){
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    public void addUser(){
        telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child("167");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Felipe Torres");
        map.put("email", "felipe.torres.oficial@hotmail.com");
        map.put("password", "123456");
        map.put("cellphone", telephonyManager.getLine1Number() != null ? telephonyManager.getLine1Number().toString() : 0);
        map.put("create_at", String.valueOf(new Date().getTime()));
        databaseReference.updateChildren(map);
    }

}
