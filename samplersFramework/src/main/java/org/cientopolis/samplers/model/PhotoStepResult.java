package org.cientopolis.samplers.model;



/**
 * Created by Xavier on 07/02/2017.
 */

public class PhotoStepResult extends StepResult {
    private String pathToImage;

    public PhotoStepResult(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public String getPathToImage() {
        return pathToImage;
    }
}
