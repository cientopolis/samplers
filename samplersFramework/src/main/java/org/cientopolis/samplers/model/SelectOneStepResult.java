package org.cientopolis.samplers.model;


/**
 * Created by Xavier on 07/02/2017.
 */

public class SelectOneStepResult extends StepResult {

    private SelectOption selectedOption;

    public SelectOneStepResult(SelectOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public SelectOption getSelectedOption() {
        return selectedOption;
    }
}
