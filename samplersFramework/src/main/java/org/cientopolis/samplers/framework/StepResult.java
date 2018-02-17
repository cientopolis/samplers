package org.cientopolis.samplers.framework;

import java.io.Serializable;

/**
 * Created by Xavier on 07/02/2017.
 * The result of each {@link Step}.
 * Abstract class, there is a diferent StepResult subclass for the diferents {@link Step}.
 * See the diferent subclasses for details.
 */
public  class StepResult implements Serializable {
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
}
