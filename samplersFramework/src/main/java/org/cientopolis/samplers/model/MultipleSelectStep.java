package org.cientopolis.samplers.model;

import java.util.ArrayList;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends Step {

    private ArrayList<SelectOption> optionsToSelect;

    public MultipleSelectStep() {
        optionsToSelect = new ArrayList<SelectOption>();
    }

    public MultipleSelectStep(ArrayList<SelectOption> anOptionsToSelect) {

        optionsToSelect = anOptionsToSelect;
    }

    public ArrayList<SelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }

    public ArrayList<SelectOption> getSelectedOptions() {

        ArrayList<SelectOption> options = new ArrayList<>();

        for (SelectOption option: optionsToSelect) {
            if (option.isSelected()) {
                options.add(option);
            }
        }
        return options;
    }
}
