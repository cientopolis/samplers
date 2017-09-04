package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.InsertDateFragment;

/**
 * Created by Xavier on 01/09/2017.
 */

public class InsertDateStep extends BaseStep {

    private String textToShow;
    private Integer nextStepId;

    public InsertDateStep(int id, String textToShow, Integer nextStepId) {
        super(id);
        this.stepFragmentClass = InsertDateFragment.class;
        this.textToShow = textToShow;
        this.nextStepId = nextStepId;
    }


    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }
}
