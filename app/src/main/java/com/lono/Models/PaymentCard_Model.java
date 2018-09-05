package com.lono.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class PaymentCard_Model {

    Context mContext;

    String TYPE_PLAM;
    String QTD_TERMS;
    String TOKEN;

    ProgressDialog progressDialog;

    public PaymentCard_Model(Context mContext){
        this.mContext = mContext;

    }

    @JavascriptInterface
    public void openProgress(String message) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @JavascriptInterface
    public void closeProgress() {
        progressDialog.dismiss();
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
    public String getTOKEN() {
        return TOKEN;
    }

    @JavascriptInterface
    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}
