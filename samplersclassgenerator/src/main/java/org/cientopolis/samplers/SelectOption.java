package org.cientopolis.samplers;

/**
 * Created by Xavier on 07/06/2017.
 */

public class SelectOption {

    private int id;
    private String textToShow;

    public SelectOption(int aId, String aTextToShow) {
        id = aId;
        textToShow = aTextToShow;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public int getId() {
        return id;
    }


}
