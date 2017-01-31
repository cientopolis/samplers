package org.cientopolis.samplers.modelo;

import java.util.ArrayList;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends Step {

    private ArrayList<MultipleSelectOption> optionsToSelect;

    public MultipleSelectStep() {
        optionsToSelect = new ArrayList<MultipleSelectOption>();
    }

    public MultipleSelectStep(ArrayList<MultipleSelectOption> anOptionsToSelect) {

        optionsToSelect = anOptionsToSelect;
    }

    public ArrayList<MultipleSelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }
}
