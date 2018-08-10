package com.lono.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.lono.R;
import com.lono.Service.Service_Check_CellPhone;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;

public class View_Check_Cellphone extends Activity implements View.OnClickListener {

    EditText edit_text_cellphone_check;
    Button button_view_check_cellphone;
    Service_Check_CellPhone service_check_cellPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_check_cellphone);

        service_check_cellPhone = new Service_Check_CellPhone(this);
        edit_text_cellphone_check = (EditText) findViewById(R.id.edit_text_cellphone_check);
        edit_text_cellphone_check.setText(null);
        button_view_check_cellphone = (Button) findViewById(R.id.button_view_check_cellphone);
        button_view_check_cellphone.setOnClickListener(this);
        button_view_check_cellphone.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        digitingCellphone();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_view_check_cellphone:
                sendCellPhoneCheck();
                break;
        }
    }

    private void sendCellPhoneCheck() {
        String cellphone_number = edit_text_cellphone_check.getText().toString().trim();
        service_check_cellPhone.check(cellphone_number);
    }

    public void digitingCellphone (){
        edit_text_cellphone_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    button_view_check_cellphone.setVisibility(View.VISIBLE);
                    Keyboard.close(View_Check_Cellphone.this, getWindow().getDecorView());
                } else {
                    button_view_check_cellphone.setVisibility(View.GONE);
                }
            }
        });
    }
}

