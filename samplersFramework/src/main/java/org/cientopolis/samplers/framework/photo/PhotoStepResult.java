package org.cientopolis.samplers.framework.photo;


import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by Xavier on 07/02/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link PhotoStep}.
 */

public class PhotoStepResult extends BaseStepResult {

    /**
     * The filename of the photo file.
     */
    private String imageFileName;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param imageFileName The filename of the photo file
     */
    public PhotoStepResult(int stepId, String imageFileName) {
        super(stepId);
        this.imageFileName = imageFileName;
    }

    /**
     * Returns the filename of the photo file.
     *
     * @return The filename of the photo file.
     */
    public String getImageFileName() {
        return imageFileName;
    }

    @Override
    public boolean hasMultimediaFile() {
        return true;
    }

    @Override
    public String getMultimediaFileName() {
        return imageFileName;
    }

}
