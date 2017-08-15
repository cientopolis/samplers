package org.cientopolis.samplers;

/**
 * Created by Xavier on 07/06/2017.
 */

public class MultipleSelectOption {

    private int id;
    private String textToShow;

    public MultipleSelectOption(int id, String textToShow) {
        this.id = id;
        this.textToShow = textToShow;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public int getId() {
        return id;
    }


}
