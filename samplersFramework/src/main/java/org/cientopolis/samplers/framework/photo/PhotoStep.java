package org.cientopolis.samplers.framework.photo;

import org.cientopolis.samplers.framework.base.BaseStep;


/**
 * Created by Xavier on 06/06/2016.
 */
public class PhotoStep extends BaseStep {

    private String instructionsToShow;
    private Integer nextStepId;

    public PhotoStep(int id, String anInstructionsToShow, Integer nextStepId) {
        super(id);
        stepFragmentClass = PhotoFragment.class;
        instructionsToShow = anInstructionsToShow;
        this.nextStepId = nextStepId;
    }

    public String getInstructionsToShow() {
        return instructionsToShow;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }

}
