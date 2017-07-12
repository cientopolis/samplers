package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.StepFragment;

/**
 * Created by Xavier on 15/04/2017.
 */

public abstract class BaseStep implements Step {
    protected Class stepFragmentClass;
    protected Integer id;

    @Override
    public <T extends StepFragment> void setStepFragmentClass(Class<T> type) {
        stepFragmentClass = type;
    }

    @Override
    public <T extends StepFragment> Class<T> getStepFragmentClass() {
        return stepFragmentClass;
    }

    @Override
    public Integer getId() {
        return id;
    }


}
