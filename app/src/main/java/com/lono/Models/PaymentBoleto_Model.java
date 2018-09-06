package com.lono.Models;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.webkit.JavascriptInterface;

import com.lono.Utils.Keyboard;

public class PaymentBoleto_Model {

    Activity activity;

    String TYPE_PLAM;
    String QTD_TERMS;
    String NAME;
    String DOCUMENT;
    String CODEBAR;
    String TOKEN;

    ProgressDialog progressDialog;

    public PaymentBoleto_Model(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public String getTYPE_PLAM() {
        return TYPE_PLAM;
    }

    @JavascriptInterface
    public void setTYPE_PLAM(String TYPE_PLAM) {
        this.TYPE_PLAM = TYPE_PLAM;
    }

    @JavascriptInterface
    public String getQTD_TERMS() {
        return QTD_TERMS;
    }

    @JavascriptInterface
    public void setQTD_TERMS(String QTD_TERMS) {
        this.QTD_TERMS = QTD_TERMS;
    }

    @JavascriptInterface
    public String getNAME() {
        return NAME;
    }

    @JavascriptInterface
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @JavascriptInterface
    public String getDOCUMENT() {
        return DOCUMENT;
    }

    @JavascriptInterface
    public void setDOCUMENT(String DOCUMENT) {
        this.DOCUMENT = DOCUMENT;
    }

    @JavascriptInterface
    public String getCODEBAR() {
        return CODEBAR;
    }

    @JavascriptInterface
    public void setCODEBAR(String CODEBAR) {
        this.CODEBAR = CODEBAR;
    }

    @JavascriptInterface
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @JavascriptInterface
    public void setProgressDialog(String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @JavascriptInterface
    public void closeProgress() {
        progressDialog.dismiss();
    }

    @JavascriptInterface
    public String getTOKEN() {
        return TOKEN;
    }

    @JavascriptInterface
    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

}
