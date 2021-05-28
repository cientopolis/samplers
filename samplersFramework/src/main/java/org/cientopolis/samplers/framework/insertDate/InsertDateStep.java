package org.cientopolis.samplers.framework.insertDate;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 01/09/2017.
 */

public class InsertDateStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_insert_date";

    private String textToShow;
    private Integer nextStepId;

    public InsertDateStep(int id, String textToShow, Integer nextStepId) {
        super(id);
        this.stepFragmentClass = InsertDateFragment.class;
        this.textToShow = textToShow;
        this.nextStepId = nextStepId;
    }


    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        return nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }
}
