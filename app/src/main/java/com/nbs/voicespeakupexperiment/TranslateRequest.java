package com.nbs.voicespeakupexperiment;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Sidiq on 15/06/2016.
 */
public class TranslateRequest {
    private OnTranslateRequestListener onTranslateRequestListener;
    private  AsyncHttpClient client;
    private Gson gson;
    public TranslateRequest(){
        client = new AsyncHttpClient();
        gson = new Gson();
    }

    public OnTranslateRequestListener getOnTranslateRequestListener() {
        return onTranslateRequestListener;
    }

    public void setOnTranslateRequestListener(OnTranslateRequestListener onTranslateRequestListener) {
        this.onTranslateRequestListener = onTranslateRequestListener;
    }

    public void callApi(String words){
        String encodedWords = TextUtils.htmlEncode(words.replace("\"", "").trim());
        String url = "YOUR END POINT";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                if (!TextUtils.isEmpty(response)){
                    TranslateResponse translateResponse = gson.fromJson(response, TranslateResponse.class);
                    if (translateResponse != null){
                        String translatedText = translateResponse.getTranslateData().getTranslateItems().get(0).getTranslatedText();
                        getOnTranslateRequestListener().onTranslateSuccess(translatedText);
                    }else{
                        getOnTranslateRequestListener().onTranslateFailed("Invalid Response");
                    }
                }else{
                    getOnTranslateRequestListener().onTranslateFailed("Invalid Response");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                getOnTranslateRequestListener().onTranslateFailed("Unable to connect to Google Translate Api");
            }
        });
    }

    public void disconnect(){
        if (client != null){
            client.cancelAllRequests(true);
        }
    }

    public interface OnTranslateRequestListener{
        void onTranslateSuccess(String translatedText);
        void onTranslateFailed(String errorMessage);
    }
}
