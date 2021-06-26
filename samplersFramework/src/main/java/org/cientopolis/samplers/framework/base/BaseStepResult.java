package org.cientopolis.samplers.framework.base;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by Xavier on 21/07/2018.
 */

public abstract class BaseStepResult implements StepResult {
    /**
     * The id of the {@link Step} that generated the StepResult.
     */
    private Step step;

    /**
     * Constructor.
     *
     * @param step the {@link Step} that generated the StepResult.
     */
    public BaseStepResult(Step step) {
        this.step = step;
    }

    /**
     * Returns the id of the {@link Step} that generated the {@link StepResult}.
     *
     * @return The id of the {@link Step} that generated the {@link StepResult}.
     */
    @Override
    public Step getStep() {
        return step;
    }


    /**
     * Returns whether the {@link StepResult} has a multimedia file (like a photo or a sound) or not.
     *
     * @return true if the {@link StepResult} has a multimedia file, false otherwise.
     */
    @Override
    public boolean hasMultimediaFile() {
        return false;
    }

    /**
     * Returns the filename of the multimedia file (like a photo or a sound) of the {@link StepResult}.
     *
     * @return the filename of the multimedia file.
     */
    @Override
    public String getMultimediaFileName() {
        return null;
    }
}
