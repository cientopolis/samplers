package org.cientopolis.samplers.model;



/**
 * Created by Xavier on 07/02/2017.
 */

public class PhotoStepResult extends StepResult {
    private String imageFileName;

    public PhotoStepResult(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

}
