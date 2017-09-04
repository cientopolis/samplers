package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.InsertTimeFragment;

/**
 * Created by Xavier on 03/09/2017.
 */

public class InsertTimeStep extends BaseStep {

    private String textToShow;
    private Integer nextStepId;

    public InsertTimeStep(int id, String textToShow, Integer nextStepId) {
        super(id);
        this.stepFragmentClass = InsertTimeFragment.class;
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
