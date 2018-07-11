package com.lono.Firebase;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Database {

    static DatabaseReference databaseReference;

    public static void addUser(){
        String time = String.valueOf(new Date().getTime());
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("users").child("167");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Felipe Torres");
        map.put("email", "felipe.torres.oficial@hotmail.com");
        map.put("password", "123456");
        map.put("create_at", time);
        databaseReference.setValue(map);
    }

}
