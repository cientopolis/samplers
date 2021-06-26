package org.cientopolis.samplers.framework.information;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by Xavier on 01/12/2017.
 */

public class InformationStepResult extends BaseStepResult {
    /**
     * Constructor.
     *
     * @param step The id of the {@link Step} that generated the StepResult.
     */
    public InformationStepResult(Step step) {
        super(step);
    }
}
