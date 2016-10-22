package com.nbs.voicespeakupexperiment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sidiq on 15/06/2016.
 */
public class TranslateResponse {
    @SerializedName("data")
    private TranslateData translateData;

    public TranslateData getTranslateData() {
        return translateData;
    }

    public void setTranslateData(TranslateData translateData) {
        this.translateData = translateData;
    }
}
