package org.cientopolis.samplers.framework;

import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Xavier on 06/06/2016.
 * Represents a Step in the Workflow
 */
public interface Step extends Serializable{

    /**
     * Returns the resource name of the step. It is used to show it to the user.
     * NOTE: This resource name must exists in the strings.xml
     * @return The step resource name
     */
    String getStepResourceName();

    /**
     * Returns the Fragment class wich will be used to execute the Step
     * @param <T> T must extends StepFragment
     * @return The Fragment class
     */
    <T extends StepFragment> Class<T> getStepFragmentClass();

    /**
     * Sets the Fragment class wich will be used to execute the Step
     * @param type The Fragment class
     * @param <T> T must extends StepFragment
     */
    <T extends StepFragment> void setStepFragmentClass(Class<T> type);

    /**
     * The unique identifier of the Step. This id must be unique in the workflow.
     * This id is stored in the StepResult to identify wich Step generated it.
     * @return the id of the Step
     */
    int getId();

    /**
     * Returns the id of the next step. If it is null, the Workflow ends.
     * @param stepResult the result of the execution of the Step
     * @return the id of the next step
     */
    @Nullable Integer getNextStepId(StepResult stepResult);

    /**
     * Sets the resourse id of the html help file to show as help of the Step.
     * The file must be stored in the res/raw folder.
     * @param helpResourseId the resourse id of the html help file
     */
    void setHelpResourseId(Integer helpResourseId);

    /**
     * Returns the resourse id of the html help file. If it is null, it's assumed that the Step has no help to show
     * @return the resourse id of the html help file
     */
    @Nullable Integer getHelpResourseId();
}
