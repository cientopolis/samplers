package org.cientopolis.samplers.framework;

import java.io.Serializable;

/**
 * Created by Xavier on 07/02/2017.
 * The result of each {@link Step}.
 * Abstract class, there is a diferent StepResult subclass for the diferents {@link Step}.
 * See the diferent subclasses for details.
 */
public abstract class StepResult implements Serializable {
    /**
     * The id of the {@link Step} that generated the StepResult.
     */
    private int stepId;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     */
    public StepResult(int stepId) {
        this.stepId = stepId;
    }

    /**
     * Returns the id of the {@link Step} that generated the {@link StepResult}.
     *
     * @return The id of the {@link Step} that generated the {@link StepResult}.
     */
    public int getStepId() {
        return stepId;
    }


    /**
     * Returns whether the {@link StepResult} has a multimedia file (like a photo or a sound) or not.
     *
     * @return true if the {@link StepResult} has a multimedia file, false otherwise.
     */
    public boolean hasMultimediaFile() {
        return false;
    }

    /**
     * Returns the filename of the multimedia file (like a photo or a sound) of the {@link StepResult}.
     *
     * @return the filename of the multimedia file.
     */
    public String getMultimediaFileName() {
        return null;
    }
}
