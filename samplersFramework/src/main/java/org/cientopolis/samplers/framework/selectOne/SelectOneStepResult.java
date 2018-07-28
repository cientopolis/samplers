package org.cientopolis.samplers.framework.selectOne;


import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by Xavier on 07/02/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link SelectOneStep}
 */

public class SelectOneStepResult extends BaseStepResult {

    /**
     * The option selected.
     */
    private SelectOneOption selectedOption;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param selectedOption The selected option.
     */
    public SelectOneStepResult(int stepId, SelectOneOption selectedOption) {
        super(stepId);
        this.selectedOption = selectedOption;
    }

    /**
     * Returns the selected option.
     *
     * @return The selected option.
     */
    public SelectOneOption getSelectedOption() {
        return selectedOption;
    }
}
