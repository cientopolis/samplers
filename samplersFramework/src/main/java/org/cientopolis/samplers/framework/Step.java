package org.cientopolis.samplers.framework;

import org.cientopolis.samplers.framework.base.StepFragment;

import java.io.Serializable;

/**
 * Created by Xavier on 06/06/2016.
 * Represents a Step in the Workflow
 */
public interface Step extends Serializable{

    <T extends StepFragment> Class<T> getStepFragmentClass();
    <T extends StepFragment> void setStepFragmentClass(Class<T> type);

    int getId();
    Integer getNextStepId();

    void setStepResult(StepResult stepResult);
    StepResult getStepResult();

    void setHelpResourseId(Integer helpResourseId);
    Integer getHelpResourseId();
}
