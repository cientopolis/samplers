package org.cientopolis.samplers.model;

import java.io.Serializable;

/**
 * Created by Xavier on 07/02/2017.
 */

public class StepResult implements Serializable {
    private int stepId;

    public StepResult(int stepId) {
        this.stepId = stepId;
    }

    public int getStepId() {
        return stepId;
    }
}
