package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.PhotoFragment;


/**
 * Created by Xavier on 06/06/2016.
 */
public class PhotoStep extends BaseStep {

    private String instructionsToShow;
    // TODO: 15/03/2017 Add functionality to support this
    private String imageToOverlay;
    private Integer nextStepId;


    public PhotoStep(String anInstructionsToShow, String anImageToOverlay) {
        /*
        TO DO
            instantiate appropiate class
            if(Android > 5) then
              PhotoFragment2
            else
              PhotoFragment
        */
        stepFragmentClass = PhotoFragment.class;
        instructionsToShow = anInstructionsToShow;
        imageToOverlay = anImageToOverlay; //comment here!
    }

    public String getInstructionsToShow() {
        return instructionsToShow;
    }

    public String getImageToOverlay() {
        return imageToOverlay;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }

}
