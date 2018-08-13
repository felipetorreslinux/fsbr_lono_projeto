package com.lono.PagSeguro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lono.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.concurrent.CompletionService;

/**
 * Created by Petrus A. (R@G3), ESPE... On 25/07/2018.
 */
public class LonoPagamentoUtils {
    // Tipo de dados retorno com resposta no 'GetBrandInstallments'
    public class InstallmentInfo {
        public int Quantity;
        public double ValorParcela;
        public double ValorTotal;
    }

    // Interfaces para a definção dos métodos de eventos
    // -> Evento de obtenção do código da sessão de pagamento
    public interface GetPaymentSessionListener {
        void onSuccess(String sessionCode, String senderHash);
        void onError(String errorMessage);
    }

    // -> Evento de obtenção do Token do cartão
    public interface GenerateCardTokenListener {
        void onSuccess(String cardToken);
        void onError(String errorMessage);
    }
    // -> Evento de obtenção da Bandeira do Cartão
    public interface GetCardBrandNameListener {
        void onSuccess(String brandName);
        void onError(String errorMessage);
    }
    // -> Evento de obtenção dos parcelamentos possiveis do valor/cartão
    public interface GetBrandInstallmentsListener {
        void onSuccess(InstallmentInfo[] installmentInfos);
        void onError(String errorMessage);
    }

    public interface GetSenderHashListener {
        void onSuccess(String senderHash);
        void onError(String errorMessage);
    }

    // Enum interno utilizado APENAS para controlar qual a requisicao atual
    private enum CURRENT_ACTION_IN_REQUEST {
        SESSION, TOKEN, BRAND, INSTALLMENTS, HASH
    };

    // Propriedades de uso interno da Lib
    private String LOG_TAG;
    private WebView webView;
    private Context mContext;
    private String lonoEngineHostName;
    private CURRENT_ACTION_IN_REQUEST currentActionInRequest = null;
    private String lastSessionCode = null;

    private GetPaymentSessionListener getPaymentSessionListener = null;
    private GetCardBrandNameListener getCardBrandNameListener = null;
    private GenerateCardTokenListener generateCardTokenListener = null;
    private GetBrandInstallmentsListener getBrandInstallmentsListener = null;
    private GetSenderHashListener getSenderHashListener = null;

    /**
     * Contrutor
     * @param mContext -> Context
     * @param hostLonoEngine -> Endereço do serviço de engine do Lono (ex: https://engine.lono.com.br)
     */
    public LonoPagamentoUtils(Context mContext, final String hostLonoEngine) {
        this.LOG_TAG = LonoPagamentoUtils.class.getName();
        this.mContext = mContext;
        this.lonoEngineHostName = hostLonoEngine;
        this.webView = new WebView(mContext);

        // Inicializando parametros da WebView
        this.SetWebViewSettings();
    }

    /**
     * Obtém o SenderHash do cliente/transação atual
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetSenderHash(GetSenderHashListener listener) {
        // Garantindo argumentos
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        // Executando o 'GenerateCardToken' passando a ultima Sessao como argumento
        GetSenderHash(lastSessionCode, listener);
    }

    /**
     * Obtém o SenderHash do cliente/transação atual
     * @param sessionCode -> Codigo da sessao (obtida em: GetPaymentSession)
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetSenderHash(String sessionCode, GetSenderHashListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        try {
            // Definindo o listener e setando qual foi a requisicao atraves do ENUM
            this.currentActionInRequest = CURRENT_ACTION_IN_REQUEST.HASH;
            this.getSenderHashListener = listener;

            // Enviando requisição através do WebView
            final String URLRequest = this.lonoEngineHostName + mContext.getString(R.string.lono_pagamento_utils_route);
            final String postData = "action=sender-hash&payment_session=" + sessionCode;
            this.webView.postUrl(URLRequest, postData.getBytes());

            this.lastSessionCode = sessionCode;
        } catch ( Exception ex ) {
            Log.e(LOG_TAG, ex.getMessage());
            listener.onError(ex.getMessage());
        }
    }

    /**
     * Obtém o código da sessão de pagamento (necessário p/ todas as outros requisições)
     * Nota: Este método utilizara o ÚLTIMO código de sessão obtido/usado, se não houver, dispara erro
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetPaymentSession(GetPaymentSessionListener listener){
        // Garantindo argumentos
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        // Definindo o listener e setando qual foi a requisicao atraves do ENUM
        this.currentActionInRequest = CURRENT_ACTION_IN_REQUEST.SESSION;
        this.getPaymentSessionListener = listener;

        // Enviando a requisicao
        final String URLRequest = this.lonoEngineHostName + mContext.getString(R.string.lono_pagamento_utils_route);
        final String postData = "action=payment-session";
        if(webView.getProgress() == 100){
            this.webView.postUrl(URLRequest, postData.getBytes());
        }else{
            listener.onError("sem conexao");
        }

    }

    /**
     * Inicia a requisição do Token do Cartão
     * @param sessionCode -> Codigo da sessao (obtida em: GetPaymentSession)
     * @param numeroCartao -> Numero do cartão
     * @param cvv -> Código CVV
     * @param expirationMonth -> Mês de validade
     * @param expirationYear -> Ano de validade
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GenerateCardToken(String sessionCode, String numeroCartao, String cvv, int expirationMonth, int expirationYear, GenerateCardTokenListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        try {
            // Garantindo a validade das strings
            if (numeroCartao == null || cvv == null)
                throw new Exception("Parâmetros inválidos");

            // Removendo possíveis mascaras
            numeroCartao = numeroCartao.replaceAll("\\D+", "");
            cvv = cvv.replaceAll("\\D+", "");

            // Validando argumentos
            if (numeroCartao.length() != 16)
                throw new Exception("Numéro do cartão inválido");
            if (cvv.length() < 3)
                throw new Exception("Numéro do CVV inválido");
            if (expirationMonth < 1 || expirationMonth > 12)
                throw new Exception("Mês de validade inválido");
            if (expirationYear < Calendar.getInstance().get(Calendar.YEAR))
                throw new Exception("Ano de validade inválido");

            // Definindo o listener e setando qual foi a requisicao atraves do ENUM
            this.currentActionInRequest = CURRENT_ACTION_IN_REQUEST.TOKEN;
            this.generateCardTokenListener = listener;

            // Enviando requisição através do WebView
            final String URLRequest = this.lonoEngineHostName + mContext.getString(R.string.lono_pagamento_utils_route);
            final String postData = "action=card-token&payment_session=" + sessionCode + "&numero_cartao=" + numeroCartao + "&numero_cvv=" + cvv + "&validate_month=" + expirationMonth + "&validate_year=" + expirationYear;
            this.webView.postUrl(URLRequest, postData.getBytes());

            this.lastSessionCode = sessionCode;
        } catch ( Exception ex ) {
            Log.e(LOG_TAG, ex.getMessage());
            listener.onError(ex.getMessage());
        }
    }

    /**
     * Inicia a requisição do Token do Cartão
     * Nota: Este método utilizara o ÚLTIMO código de sessão obtido/usado, se não houver, dispara erro
     * @param numeroCartao -> Numero do cartão
     * @param cvv -> Código CVV
     * @param expirationMonth -> Mês de validade
     * @param expirationYear -> Ano de validade
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
//    public void GenerateCardToken(String numeroCartao, String cvv, int expirationMonth, int expirationYear, GenerateCardTokenListener listener) {
//        // Garantindo o listener
//        if ( listener == null ) {
//            Log.e(LOG_TAG, "O Listener não pode ser NULL");
//            return;
//        }
//
//        // Verificando o último lastSessionCode
//        if ( lastSessionCode == null ) {
//            listener.onError("Nenhuma sessão foi inicializada");
//            return;
//        }
//
//        // Executando o 'GenerateCardToken' passando a ultima Sessao como argumento
//        GenerateCardToken(lastSessionCode, numeroCartao, cvv, expirationMonth, expirationYear, listener);
//    }

    /**
     * Obtém o nome da bandeira do cartão informado
     * @param sessionCode -> Codigo da sessao (obtida em: GetPaymentSession)
     * @param numeroCartao -> Numéro do cartão (no minimo, os 5 primeios digitos)
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetCardBrandName(String sessionCode, String numeroCartao, GetCardBrandNameListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        try {
            // Garantindo a validade das strings
            if (numeroCartao == null )
                throw new Exception("Parâmetros inválidos");

            // Removendo possíveis mascaras
            numeroCartao = numeroCartao.replaceAll("\\D+", "");

            // Validando argumentos
            if (numeroCartao.length() < 5 || numeroCartao.length() > 16 )
                throw new Exception("Numéro do cartão inválido");

            // Definindo o listener e setando qual foi a requisicao atraves do ENUM
            this.currentActionInRequest = CURRENT_ACTION_IN_REQUEST.BRAND;
            this.getCardBrandNameListener = listener;

            // Enviando requisição através do WebView
            final String URLRequest = this.lonoEngineHostName + mContext.getString(R.string.lono_pagamento_utils_route);
            final String postData = "action=card-brandname&payment_session=" + sessionCode + "&numero_cartao=" + numeroCartao;
            this.webView.postUrl(URLRequest, postData.getBytes());
            this.lastSessionCode = sessionCode;
        } catch ( Exception ex ) {
            Log.e(LOG_TAG, ex.getMessage());
            listener.onError(ex.getMessage());
        }
    }

    /**
     * Obtém o nome da bandeira do cartão informado
     * @param numeroCartao -> Numéro do cartão (no minimo, os 5 primeios digitos)
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetCardBrandName(String numeroCartao, GetCardBrandNameListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        // Verificando o último lastSessionCode
        if ( lastSessionCode == null ) {
            listener.onError("Nenhuma sessão foi inicializada");
            return;
        }

        // Executando o 'GenerateCardToken' passando a ultima Sessao como argumento
        GetCardBrandName(lastSessionCode, numeroCartao, listener);
    }

    /**
     * Obtém os dados de parcelamento da bandeira/valor informado
     * @param sessionCode -> Codigo da sessao (obtida em: GetPaymentSession)
     * @param brandName -> Bendeira do cartão
     * @param valorTotal -> Valor total a ser parcelado
     * @param listener -> Listener que sera executado (onSuccess/onError) após o processamento
     */
    public void GetBrandInstallments(String sessionCode, String brandName, double valorTotal, GetBrandInstallmentsListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        try {
            // Garantindo a validade das strings
            if (brandName == null || brandName.length() <= 0)
                throw new Exception("Parâmetros inválidos");

            // Definindo o listener e setando qual foi a requisicao atraves do ENUM
            this.currentActionInRequest = CURRENT_ACTION_IN_REQUEST.INSTALLMENTS;
            this.getBrandInstallmentsListener = listener;

            // Enviando requisição através do WebView
            final String URLRequest = this.lonoEngineHostName + mContext.getString(R.string.lono_pagamento_utils_route);
            final String postData = "action=card-installments&payment_session=" + sessionCode + "&brand_name=" + brandName + "&valor_pgto=" + valorTotal;
            this.webView.postUrl(URLRequest, postData.getBytes());
            this.lastSessionCode = sessionCode;
        } catch ( Exception ex ) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    public void GetBrandInstallments(String brandName, double valorTotal, GetBrandInstallmentsListener listener) {
        // Garantindo o listener
        if ( listener == null ) {
            Log.e(LOG_TAG, "O Listener não pode ser NULL");
            return;
        }

        // Verificando o último lastSessionCode
        if ( lastSessionCode == null ) {
            listener.onError("Nenhuma sessão foi inicializada");
            return;
        }

        // Executando o 'GenerateCardToken' passando a ultima Sessao como argumento
        GetBrandInstallments(lastSessionCode, brandName, valorTotal, listener);
    }

    /**
     * Configura o WebView e define um Hook no jsAlert para obter a resopsta da página
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void SetWebViewSettings() {
        // Criando o hook no jsAlert
        this.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // Processando a resposta
                result.confirm();
                ProcessPageResponse(message);
                return true;
            }
        });

        // Definindo a classe do WebViewCliente (que ira tratar os erros/eventos do WebView)
        this.webView.setWebViewClient(new LonoCartaoGenTokenWebViewCliente());

        // Definindo o restando dos parametros p/ a WebView
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setAllowContentAccess(true);
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }

    /**
     * Processa o JSON (em string) recebido e dispara o Listener correspondente
     * @param responseJsonText -> JSON no formato texto/string
     */
    private void ProcessPageResponse(String responseJsonText) {
        // Nota: O listener com as respectivas argumentos sera disparado com base no
        //       'currentActionInRequest' definido.
        final CURRENT_ACTION_IN_REQUEST actionInRequest = this.currentActionInRequest;
        this.currentActionInRequest = null;

        try {
            // Gerando o objeto JSON
            JSONObject jsonObject = new JSONObject(responseJsonText);
            if ( !jsonObject.getString("status").equalsIgnoreCase("success") )
                throw new Exception("Erro tratando a resposta. (JSON -> " + responseJsonText + ")");

            // Tratando a resposta com base no tipo de request
            switch ( actionInRequest ) {
                case BRAND:
                    this.getCardBrandNameListener.onSuccess(jsonObject.getString("brand"));
                    break;
                case TOKEN:
                    this.generateCardTokenListener.onSuccess(jsonObject.getString("card_token"));
                    break;
                case SESSION:
                    // Salvando o SessionCode e retornando-o
                    this.lastSessionCode = jsonObject.getString("payment_session");
                    this.getPaymentSessionListener.onSuccess(this.lastSessionCode, jsonObject.getString("sender_hash"));
                    break;
                case INSTALLMENTS:
                    this.getBrandInstallmentsListener.onSuccess(this.GetInstallmentDataInfoFromJSON(jsonObject.getJSONArray("installments")));
                    break;
                case HASH:
                    this.getSenderHashListener.onSuccess(jsonObject.getString("sender_hash"));
                    break;
            }
        } catch ( Exception ex ) {
            switch ( actionInRequest ) {
                case BRAND:
                    this.getCardBrandNameListener.onError(ex.getMessage());
                    break;
                case TOKEN:
                    this.generateCardTokenListener.onError(ex.getMessage());
                    break;
                case SESSION:
                    this.getPaymentSessionListener.onError(ex.getMessage());
                    break;
                case INSTALLMENTS:
                    this.getBrandInstallmentsListener.onError(ex.getMessage());
                    break;
                case HASH:
                    this.getSenderHashListener.onError(ex.getMessage());
                    break;
            }
        }
    }

    /**
     * Processa a reposta do 'GetBrandInstallments' retornando um objeto tratado
     * @param jsonArray -> JSONArray objeto
     * @return -> Array do objeto InstallmentInfo
     */
    private InstallmentInfo[] GetInstallmentDataInfoFromJSON(JSONArray jsonArray) throws JSONException {
        final int arrayLenght = jsonArray.length();
        InstallmentInfo[] installmentInfoArr = new InstallmentInfo[arrayLenght];
        for ( int i = 0; i < arrayLenght; i++ ) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            installmentInfoArr[i] = new InstallmentInfo();
            installmentInfoArr[i].Quantity = jsonObject.getInt("quantity");
            installmentInfoArr[i].ValorParcela = jsonObject.getDouble("installmentAmount");
            installmentInfoArr[i].ValorTotal = jsonObject.getDouble("totalAmount");
        }

        return installmentInfoArr;
    }

    /**
     * Classe para a definições/tratamento dos eventos do WebView
     */
    private class LonoCartaoGenTokenWebViewCliente extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // Aceitando TODOS os tipos de SSL (inclusive, o self-signed)
            handler.proceed();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            view.stopLoading(); // Garantindo que a view não esta mais em execucao
            // Disparando o evento de erro
            Log.e(LOG_TAG, "Erro1 -> " + errorResponse.toString());
            ProcessPageResponse("{\"status\":\"error\",\"message\":\"\"Erro[0] processando a requisição. Contate o desenvolvedor\"}");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.stopLoading(); // Garantindo que a view não esta mais em execucao
            // Disparando o evento de erro
            Log.e(LOG_TAG, "Erro1 -> " + error.toString());
            ProcessPageResponse("{\"status\":\"error\",\"message\":\"\"Erro[1] processando a requisição. Contate o desenvolvedor\"}");
        }
    }
}
