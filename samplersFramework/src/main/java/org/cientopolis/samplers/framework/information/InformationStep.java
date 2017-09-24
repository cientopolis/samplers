package org.cientopolis.samplers.framework.information;

import org.cientopolis.samplers.framework.base.BaseStep;


/**
 * Created by Xavier on 06/06/2016.
 */
public class InformationStep extends BaseStep {

    private Integer nextStepId;
    private String textToShow;

    public InformationStep(int id, String aTextToShow, Integer nextStepId) {
        super(id);
        stepFragmentClass = InformationFragment.class;
        textToShow = aTextToShow;
        this.nextStepId = nextStepId;
    }

    public String getTextToShow(){

        return textToShow;
    }


    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }


}
