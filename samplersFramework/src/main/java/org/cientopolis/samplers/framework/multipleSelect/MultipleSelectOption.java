package org.cientopolis.samplers.framework.multipleSelect;

import java.io.Serializable;

/**
 * Created by Xavier on 12/06/2016.
 */

public class MultipleSelectOption implements Serializable {

    private int id;
    private String textToShow;

    public MultipleSelectOption(int aId, String aTextToShow) {
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
