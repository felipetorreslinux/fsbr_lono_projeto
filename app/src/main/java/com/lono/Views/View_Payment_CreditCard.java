package com.lono.Views;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lono.APIServer.Server;
import com.lono.Models.PaymentCard_Model;
import com.lono.R;
import com.lono.Service.Service_Payment;
import com.lono.Utils.Alerts;
import com.lono.Utils.MaskCPF;
import com.lono.Utils.MaskCellPhone;
import com.lono.Utils.MaskData;
import com.lono.Utils.MaskNumberCreditCard;
import com.lono.Utils.MaskValidateCreditCard;
import com.lono.Utils.Price;
import com.lono.Utils.ValidData;
import com.lono.Utils.ValidaCPF;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class View_Payment_CreditCard extends AppCompatActivity{

    Toolbar toolbar;

    String TYPE_PLAM;
    String QTD_TERMS;
    double PRICE;
    String NAME;
    String DOCUMENT;

    WebView webView;

    Snackbar loadingPagWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_creditcard);

        createToolbar(toolbar);

        loadingPagWeb = Snackbar.make(getWindow().getDecorView(),
                "Conectando ao PagSeguro S/A\nAguarde...", Snackbar.LENGTH_INDEFINITE);

        try{
            PaymentCard_Model paymentCardModel = new PaymentCard_Model(this);
            paymentCardModel.setQTD_TERMS("10");
            paymentCardModel.setTYPE_PLAM("Anual");
            paymentCardModel.setTOKEN(Server.token(this));

            webView = findViewById(R.id.webview_payment_card);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(paymentCardModel, "Android");

            webView.loadUrl("http://192.168.15.220/services/lono-pagamento-html-request");

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    loadingPagWeb.show();
                };

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if(url.contains("petrus")){
                        Snackbar.make(getWindow().getDecorView(), "Pagemento realizado com sucesso",
                                Snackbar.LENGTH_SHORT).show();
                    }
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    loadingPagWeb.dismiss();
                    loadingPagWeb = Snackbar.make(getWindow().getDecorView(),
                            "Conectado com sucesso.", Snackbar.LENGTH_SHORT);
                    loadingPagWeb.show();
                }
            });

        }catch (NullPointerException e){}

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = findViewById(R.id.actionbar_pay_creditcard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pagar com Cartão de Crédito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
