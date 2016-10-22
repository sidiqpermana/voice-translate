package com.nbs.voicespeakupexperiment;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sidiq on 15/06/2016.
 */
public class TranslateData {
    @SerializedName("translations")
    private ArrayList<TranslateItem> translateItems = new ArrayList<>();

    public ArrayList<TranslateItem> getTranslateItems() {
        return translateItems;
    }

    public void setTranslateItems(ArrayList<TranslateItem> translateItems) {
        this.translateItems = translateItems;
    }
}
