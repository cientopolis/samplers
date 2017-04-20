package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.camera2.PhotoFragment2;
import org.cientopolis.samplers.ui.take_sample.PhotoFragment;


/**
 * Created by Xavier on 06/06/2016.
 */
public class PhotoStep extends BaseStep {

    private String instructionsToShow;
    // TODO: 15/03/2017 Add functionality to support this
    private String imageToOverlay;


    public PhotoStep(String anInstructionsToShow, String anImageToOverlay) {
        stepFragmentClass = PhotoFragment2.class;
        instructionsToShow = anInstructionsToShow;
        imageToOverlay = anImageToOverlay; //comment here!
    }

    public String getInstructionsToShow() {
        return instructionsToShow;
    }

    public String getImageToOverlay() {
        return imageToOverlay;
    }


}
