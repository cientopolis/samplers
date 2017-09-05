package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.SelectOneFragment;


import java.util.ArrayList;

/**
 * Created by Xavier on 27/06/2016.
 */
public class SelectOneStep extends BaseStep {

    private ArrayList<SelectOneOption> optionsToSelect;
    private String title;


    public SelectOneStep(int id) {

        this(id,new ArrayList<SelectOneOption>(),"");
    }

    public SelectOneStep(int id, ArrayList<SelectOneOption> anOptionsToSelect, String title) {
        super(id);
        stepFragmentClass = SelectOneFragment.class;
        optionsToSelect = anOptionsToSelect;
        this.title = title;
    }

    public ArrayList<SelectOneOption> getOptionsToSelect() {
        return optionsToSelect;
    }


    public String getTitle() {
        return title;
    }

    @Override
    public Integer getNextStepId() {
        if (stepResult == null) {
            throw new RuntimeException("You must set the StepResult first");
        }

        // NextStepId depends on the selected option
        return ((SelectOneStepResult) stepResult).getSelectedOption().getNextStepId();
    }


}
