package com.lono.Firebase.PhoneNumberSMS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.lono.R;
import com.lono.Service.Service_Login;
import com.lono.Views.View_Principal;
import com.lono.Views.View_Validation_SMS;

public class CallBackPhoneNumberValid extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    Activity activity;
    EditText code_sms;
    SharedPreferences.Editor editor;

    public CallBackPhoneNumberValid(Activity activity){
        this.code_sms = activity.findViewById(R.id.code_sms);
        this.editor = activity.getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
    }

    @Override
    public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {
        code_sms.setText(phoneAuthCredential.getSmsCode());
        code_sms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 5){
                    if(code_sms.getText().toString().equals(phoneAuthCredential.getSmsCode())){
                        editor.putString("token", View_Validation_SMS.token);
                        editor.commit();
                        Intent intent = new Intent(activity, View_Principal.class);
                        activity.startActivity(intent);
                        activity.finishAffinity();
                    }else{
                        code_sms.requestFocus();
                        Snackbar.make(activity.getWindow().getDecorView(), "Código inválido", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {

        } else if (e instanceof FirebaseTooManyRequestsException) {

        }
    }
}
