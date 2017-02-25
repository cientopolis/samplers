package org.cientopolis.samplers.modelo;

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
}
