package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 06/06/2016.
 */
public class PhotoStep extends Step {

    private String instructionsToShow;
    private String pathToImageToOverlay;

    public PhotoStep(String anInstructionsToShow, String aPathToImageToOverlay) {
        instructionsToShow = anInstructionsToShow;
        pathToImageToOverlay = aPathToImageToOverlay; //comment here!
    }

    public String getInstructionsToShow() {
        return instructionsToShow;
    }

    public String getPathToImageToOverlay() {
        return pathToImageToOverlay;
    }
}
