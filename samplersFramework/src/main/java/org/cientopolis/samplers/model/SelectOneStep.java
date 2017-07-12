package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.SelectOneFragment;


import java.util.ArrayList;

/**
 * Created by Xavier on 27/06/2016.
 */
public class SelectOneStep extends BaseStep {

    private ArrayList<SelectOption> optionsToSelect;
    private String title;


    public SelectOneStep() {

        this(new ArrayList<SelectOption>(),"");
    }

    public SelectOneStep(ArrayList<SelectOption> anOptionsToSelect, String title) {
        stepFragmentClass = SelectOneFragment.class;
        optionsToSelect = anOptionsToSelect;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    @Override
    public Integer getNextStepId() {
        return null;
    }


}
