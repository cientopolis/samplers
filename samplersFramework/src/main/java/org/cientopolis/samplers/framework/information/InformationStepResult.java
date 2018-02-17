package org.cientopolis.samplers.framework.information;

import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by Xavier on 01/12/2017.
 */

public class InformationStepResult extends StepResult {
    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     */
    public InformationStepResult(int stepId) {
        super(stepId);
    }
}
