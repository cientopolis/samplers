package org.cientopolis.samplers.modelo;

import java.io.Serializable;

/**
 * Created by Xavier on 12/06/2016.
 */

public class MultipleSelectOption implements Serializable {

    private String textToShow;
    private boolean selected;

    public MultipleSelectOption(String aTextToShow, boolean aSelected) {
        textToShow = aTextToShow;
        selected = aSelected;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean aSelected) {
        selected = aSelected;
    }
}
