package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lono.R;
import com.lono.Service.Service_Recovery_Pass;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.Valitations;

import org.json.JSONException;
import org.json.JSONObject;

public class View_Recovery_Pass extends Activity implements View.OnClickListener {

    Service_Recovery_Pass service_recovery_pass;

    ImageView imageview_back_recovery_pass;
    EditText edit_text_email_recovery_pass;
    Button button_recovery_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recovery_pass);

        service_recovery_pass = new Service_Recovery_Pass(this);

        imageview_back_recovery_pass = (ImageView) findViewById(R.id.imageview_back_recovery_pass);
        imageview_back_recovery_pass.setOnClickListener(this);

        edit_text_email_recovery_pass = (EditText) findViewById(R.id.edit_text_email_recovery_pass);
        edit_text_email_recovery_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Valitations.email(edit_text_email_recovery_pass.getText().toString()) == true){
                    button_recovery_pass.setVisibility(View.VISIBLE);
                }else{
                    button_recovery_pass.setVisibility(View.GONE);
                }
            }
        });

        button_recovery_pass = (Button) findViewById(R.id.button_recovery_pass);
        button_recovery_pass.setVisibility(View.GONE);
        button_recovery_pass.setOnClickListener(this);

        String email = getIntent().getExtras().getString("email");
        edit_text_email_recovery_pass.setText(email);

    }

    @Override
    protected void onResume() {
        super.onResume();
        emailKeyboardOpen();
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_back_recovery_pass:
                onBackPressed();
                break;

            case R.id.button_recovery_pass:
                sendRecoveryPass();
                break;
        }
    }

    private void sendRecoveryPass() {
        String email = edit_text_email_recovery_pass.getText().toString().trim();
        Keyboard.close(this, getWindow().getDecorView());
        Alerts.progress_open(this, null, getResources()
                .getString(R.string.progress_recovery_pass), false);
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            service_recovery_pass.recovery(jsonObject);
        }catch (JSONException e){}catch (NullPointerException e){}
    }

    private void emailKeyboardOpen(){
        String email = edit_text_email_recovery_pass.getText().toString().trim();
        if(email.isEmpty()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Keyboard.open(View_Recovery_Pass.this, edit_text_email_recovery_pass);
                }
            }, 100);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
