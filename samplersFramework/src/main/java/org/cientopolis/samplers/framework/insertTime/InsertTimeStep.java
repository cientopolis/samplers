package org.cientopolis.samplers.framework.insertTime;

import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 03/09/2017.
 */

public class InsertTimeStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_insert_time";

    private String textToShow;
    private Integer nextStepId;

    public InsertTimeStep(int id, String textToShow, Integer nextStepId) {
        super(id);
        this.stepFragmentClass = InsertTimeFragment.class;
        this.textToShow = textToShow;
        this.nextStepId = nextStepId;
    }


    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }
}
