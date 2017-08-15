package org.cientopolis.samplers;

/**
 * Created by Xavier on 15/08/2017.
 */

public class SelectOneOption {

    private int id;
    private Integer nextStepId;
    private String textToShow;

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
