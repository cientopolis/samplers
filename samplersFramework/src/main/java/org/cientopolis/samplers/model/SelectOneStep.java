package org.cientopolis.samplers.model;

import java.util.ArrayList;

/**
 * Created by Xavier on 27/06/2016.
 */
public class SelectOneStep extends Step {

    private ArrayList<SelectOption> optionsToSelect;

    public SelectOneStep() {
        optionsToSelect = new ArrayList<SelectOption>();
    }

    public SelectOneStep(ArrayList<SelectOption> anOptionsToSelect) {

        optionsToSelect = anOptionsToSelect;
    }

    public ArrayList<SelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }

    public SelectOption getSelectedOption() {
        SelectOption selectedOption = null;

        for (SelectOption option: optionsToSelect) {
            if (option.isSelected()) {
                selectedOption = option;
                break;
            }
        }

        return selectedOption;
    }
}
