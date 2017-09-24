package org.cientopolis.samplers.framework.selectOne;

import java.io.Serializable;

/**
 * Created by Xavier on 13/08/2017.
 */

public class SelectOneOption implements Serializable {

    private int id;
    private String textToShow;
    private Integer nextStepId;


    public SelectOneOption(int id, String textToShow, Integer nextStepId) {
        this.id = id;
        this.textToShow = textToShow;
        this.nextStepId = nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public int getId() {
        return id;
    }

    public Integer getNextStepId() {
        return nextStepId;
    }
}
