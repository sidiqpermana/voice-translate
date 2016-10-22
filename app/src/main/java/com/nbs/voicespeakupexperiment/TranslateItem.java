package com.nbs.voicespeakupexperiment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sidiq on 15/06/2016.
 */
public class TranslateItem {
    @SerializedName("translatedText")
    private String translatedText;

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
