package org.cientopolis.samplers.framework.selectOne;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;


import java.util.ArrayList;

/**
 * Created by Xavier on 27/06/2016.
 */
public class SelectOneStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_select_one";

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
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        if (stepResult == null) {
            throw new RuntimeException("You must set the StepResult first");
        }

        // NextStepId depends on the selected option
        return ((SelectOneStepResult) stepResult).getSelectedOption().getNextStepId();
    }


}
