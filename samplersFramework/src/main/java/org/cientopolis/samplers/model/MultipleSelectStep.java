package org.cientopolis.samplers.model;

import java.util.ArrayList;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends Step {

    private ArrayList<SelectOption> optionsToSelect;
    private String title;

    public MultipleSelectStep() {
        optionsToSelect = new ArrayList<SelectOption>();
        title = "";
    }

    public MultipleSelectStep(ArrayList<SelectOption> anOptionsToSelect, String title) {

        this.optionsToSelect = anOptionsToSelect;
        this.title = title;
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

    public String getTitle() {
        return title;
    }
}
