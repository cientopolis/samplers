package org.cientopolis.samplers.framework.multipleSelect;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

import java.util.List;

/**
 * Created by Xavier on 07/02/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link MultipleSelectStep}
 */

public class MultipleSelectStepResult extends BaseStepResult {

    /**
     * A list of the selected options.
     */
    private List<MultipleSelectOption> selectedOptions;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param selectedOptions The list of the selected options.
     */
    public MultipleSelectStepResult(int stepId, List<MultipleSelectOption> selectedOptions) {
        super(stepId);
        this.selectedOptions = selectedOptions;
    }

    /**
     * Returns the list of the selected options.
     *
     * @return The list of the selected options.
     */
    public List<MultipleSelectOption> getSelectedOptions() {
        return selectedOptions;
    }
}
