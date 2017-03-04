package org.cientopolis.samplers.model;

import java.util.List;

/**
 * Created by Xavier on 07/02/2017.
 */

public class MultipleSelectStepResult extends StepResult {

    private List<SelectOption> selectedOptions;

    public MultipleSelectStepResult(List<SelectOption> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public List<SelectOption> getSelectedOptions() {
        return selectedOptions;
    }
}
