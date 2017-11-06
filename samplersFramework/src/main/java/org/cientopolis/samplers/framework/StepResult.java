package org.cientopolis.samplers.framework;

import java.io.Serializable;

/**
 * Created by Xavier on 07/02/2017.
 * The result of each {@link Step}
 * Abstract class, there is a diferent StepResult for the diferents {@link Step}
 */
public abstract class StepResult implements Serializable {
    /**
     * The id of the {@link Step} that generated the StepResult
     */
    private int stepId;

    public StepResult(int stepId) {
        this.stepId = stepId;
    }

    public int getStepId() {
        return stepId;
    }
}
