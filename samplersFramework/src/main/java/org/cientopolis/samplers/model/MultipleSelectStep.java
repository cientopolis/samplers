package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.MultipleSelectFragment;


import java.util.ArrayList;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends BaseStep {

    private ArrayList<SelectOption> optionsToSelect;
    private String title;
    private Integer nextStepId;


    public MultipleSelectStep(int id, String title, Integer nextStepId) {

        this(id, new ArrayList<SelectOption>(), title, nextStepId);
    }

    public MultipleSelectStep(int id, ArrayList<SelectOption> anOptionsToSelect, String title, Integer nextStepId) {
        super(id);
        stepFragmentClass = MultipleSelectFragment.class;
        this.optionsToSelect = anOptionsToSelect;
        this.title = title;
        this.nextStepId = nextStepId;
    }

    public ArrayList<SelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }


    public String getTitle() {
        return title;
    }


    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }
}
