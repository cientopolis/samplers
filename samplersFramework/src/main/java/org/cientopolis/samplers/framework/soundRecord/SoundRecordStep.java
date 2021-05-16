package org.cientopolis.samplers.framework.soundRecord;


import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by laura on 05/09/17.
 */

public class SoundRecordStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_sound_record";

    private String instructionsToShow;
    private Integer nextStepId;

    public SoundRecordStep(int id, String anInstructionsToShow, Integer nextStepId){
        super(id);
        this.instructionsToShow = anInstructionsToShow;
        this.nextStepId = nextStepId;
        stepFragmentClass = SoundRecordFragment.class;
    }

    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {

        this.nextStepId = nextStepId;
    }


}
