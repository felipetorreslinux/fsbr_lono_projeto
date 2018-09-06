package com.lono.Views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lono.APIServer.Server;
import com.lono.Models.PaymentBoleto_Model;
import com.lono.R;

public class View_Payment_Boleto extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences.Editor editor;

    WebView webview_payment_boleto;

    String TYPE_PLAM;
    String QTD_TERMS;
    String NAME;
    String DOCUMENT;
    Snackbar loadingPagWeb;
    PaymentBoleto_Model paymentBoletoModel;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_payment_boleto);

        createToolbar(toolbar);

        builder = new AlertDialog.Builder(this);

        editor = getSharedPreferences("profile", MODE_PRIVATE).edit();

        loadingPagWeb = Snackbar.make(getWindow().getDecorView(),
                "Conectando ao PagSeguro S/A\nAguarde...", Snackbar.LENGTH_INDEFINITE);

        webview_payment_boleto = findViewById(R.id.webview_payment_boleto);
        webview_payment_boleto.getSettings().setAllowFileAccess(true);
        webview_payment_boleto.getSettings().setSupportZoom(false);
        webview_payment_boleto.getSettings().setJavaScriptEnabled(true);

        paymentBoletoModel = new PaymentBoleto_Model(this);
        paymentBoletoModel.setTYPE_PLAM(getIntent().getExtras().getString("type_plan"));
        paymentBoletoModel.setQTD_TERMS(getIntent().getExtras().getString("qtd_terms"));
        paymentBoletoModel.setDOCUMENT(getIntent().getExtras().getString("document"));
        paymentBoletoModel.setNAME(getIntent().getExtras().getString("name"));
        paymentBoletoModel.setTOKEN(Server.token(this));

        webview_payment_boleto.addJavascriptInterface(paymentBoletoModel, "Android");

        webview_payment_boleto.loadUrl("https://engine.lono.com.br/services/lono-pagamento-boleto");

        webview_payment_boleto.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("success")){
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Boleto enviado para seu email com sucesso");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putString("token", "");
                            editor.commit();
                            finishAffinity();
                            Intent intent = new Intent(View_Payment_Boleto.this, View_Login.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }else if(url.contains("error")){
                    builder.setTitle("Ops!!!");
                    builder.setMessage("Erro ao processar o pagamento.\nTente novamente");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", null);
                    builder.create().show();
                }
                return true;
            }
        });

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = findViewById(R.id.actionbar_pay_creditcard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pagar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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

    }
}
