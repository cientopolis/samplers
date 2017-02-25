package org.cientopolis.samplers.modelo;

import java.io.Serializable;

/**
 * Created by Xavier on 12/06/2016.
 */

public class SelectOption implements Serializable {

    private int id;
    private String textToShow;
    private boolean selected;

    public SelectOption(int aId, String aTextToShow, boolean aSelected) {
        id = aId;
        textToShow = aTextToShow;
        selected = aSelected;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean aSelected) {
        selected = aSelected;
    }
}
