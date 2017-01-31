package org.cientopolis.samplers.modelo;

import java.util.ArrayList;

/**
 * Created by Xavier on 27/06/2016.
 */
public class SelectOneStep extends Step {
    private ArrayList<MultipleSelectOption> optionsToSelect;

    public SelectOneStep() {
        optionsToSelect = new ArrayList<MultipleSelectOption>();
    }

    public SelectOneStep(ArrayList<MultipleSelectOption> anOptionsToSelect) {

        optionsToSelect = anOptionsToSelect;
    }

    public ArrayList<MultipleSelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }
}
