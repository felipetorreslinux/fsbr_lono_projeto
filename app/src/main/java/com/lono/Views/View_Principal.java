package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lono.R;

public class View_Principal extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
    }

    @Override
    public void onClick(View view) {

    }
}
