package org.cientopolis.samplers.modelo;

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
        return null;
    }
}
