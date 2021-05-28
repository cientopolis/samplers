package org.cientopolis.samplers.framework.multipleSelect;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;


import java.util.ArrayList;

/**
 * Created by Xavier on 12/06/2016.
 */
public class MultipleSelectStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_multiple_select";

    private ArrayList<MultipleSelectOption> optionsToSelect;
    private String title;
    private Integer nextStepId;


    public MultipleSelectStep(int id, String title, Integer nextStepId) {

        this(id, new ArrayList<MultipleSelectOption>(), title, nextStepId);
    }

    public MultipleSelectStep(int id, ArrayList<MultipleSelectOption> anOptionsToSelect, String title, Integer nextStepId) {
        super(id);
        stepFragmentClass = MultipleSelectFragment.class;
        this.optionsToSelect = anOptionsToSelect;
        this.title = title;
        this.nextStepId = nextStepId;
    }

    public ArrayList<MultipleSelectOption> getOptionsToSelect() {
        return optionsToSelect;
    }


    public String getTitle() {
        return title;
    }


    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }
}
