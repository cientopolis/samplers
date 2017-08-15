package org.cientopolis.samplers.model;

import java.util.List;

/**
 * Created by Xavier on 07/02/2017.
 */

public class MultipleSelectStepResult extends StepResult {

    private List<MultipleSelectOption> selectedOptions;

    public MultipleSelectStepResult(int stepId, List<MultipleSelectOption> selectedOptions) {
        super(stepId);
        this.selectedOptions = selectedOptions;
    }

    public List<MultipleSelectOption> getSelectedOptions() {
        return selectedOptions;
    }
}
