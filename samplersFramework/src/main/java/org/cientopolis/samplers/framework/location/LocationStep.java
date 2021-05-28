package org.cientopolis.samplers.framework.location;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;


/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_location";

    private Integer nextStepId;
    private String textToShow;


    public LocationStep(int id, String aTextToShow, Integer nextStepId) {
        super(id);
        stepFragmentClass = LocationFragment.class;
        textToShow = aTextToShow;
        this.nextStepId = nextStepId;
    }

    public String getTextToShow(){

        return textToShow;
    }

    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }


}
