package com.lono.Firebase.PhoneNumberSMS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

    public static String sms_dode = null;

    @Override
    public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {
        sms_dode = phoneAuthCredential.getSmsCode();
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            sms_dode = null;
        } else if (e instanceof FirebaseTooManyRequestsException) {
            sms_dode = null;
        }
    }
}
