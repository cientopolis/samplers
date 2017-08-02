package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.LocationFragment;


/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStep extends BaseStep {

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
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }


}
