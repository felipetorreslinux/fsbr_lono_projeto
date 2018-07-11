package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

public class View_Recovery_Pass extends Activity implements View.OnClickListener {

    ImageView imageview_back_recovery_pass;
    EditText edit_text_email_recovery_pass;
    Button button_recovery_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recovery_pass);

        imageview_back_recovery_pass = (ImageView) findViewById(R.id.imageview_back_recovery_pass);
        imageview_back_recovery_pass.setOnClickListener(this);

        edit_text_email_recovery_pass = (EditText) findViewById(R.id.edit_text_email_recovery_pass);

        button_recovery_pass = (Button) findViewById(R.id.button_recovery_pass);
        button_recovery_pass.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_recovery_pass:
                onBackPressed();
                break;

            case R.id.button_recovery_pass:
                if(edit_text_email_recovery_pass.getText().toString().isEmpty()){
                    Alerts.curto(this, R.string.erro_email_recovery_pass_empty);
                }else if(Valitations.valid_email(edit_text_email_recovery_pass) == false){
                    Alerts.curto(this, R.string.erro_email_recovery_pass_invalid);
                }else{
                    Keyboard.close(this, getWindow().getDecorView());
                }
                break;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
