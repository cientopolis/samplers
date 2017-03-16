package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 06/06/2016.
 */
public class PhotoStep extends Step {

    private String instructionsToShow;
    // TODO: 15/03/2017 Add functionality to support this
    private String imageToOverlay;

    public PhotoStep(String anInstructionsToShow, String anImageToOverlay) {
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
