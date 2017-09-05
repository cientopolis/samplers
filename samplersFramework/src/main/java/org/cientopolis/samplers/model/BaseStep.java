package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.StepFragment;

import java.io.Serializable;

/**
 * Created by Xavier on 15/04/2017.
 */

public abstract class BaseStep implements Step, Serializable {
    protected Class stepFragmentClass;
    protected int id;
    protected StepResult stepResult;

    public BaseStep(int id) {
        this.id = id;
    }

    @Override
    public <T extends StepFragment> void setStepFragmentClass(Class<T> type) {
        stepFragmentClass = type;
    }

    @Override
    public <T extends StepFragment> Class<T> getStepFragmentClass() {
        return stepFragmentClass;
    }

    @Override
    public int getId() {
        return id;
    }


    @Override
    public void setStepResult(StepResult stepResult) {
        this.stepResult = stepResult;
    }

    @Override
    public StepResult getStepResult() {
        return stepResult;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() +"{" +
                "id=" + id +
                ", nextStepId=" + String.valueOf(getNextStepId()) +
                ", stepFragmentClass=" + stepFragmentClass.getSimpleName() +
                ", stepResult=" + stepResult +
                '}';
    }
}
