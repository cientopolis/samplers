package org.cientopolis.samplers.framework;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cientopolis.samplers.framework.selectOne.SelectOneStep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xavier on 06/06/2016.
 * A Workflow contains the collection of {@link Step} to collect a {@link Sample}
 */
@SuppressLint("UseSparseArrays") // SparseArrays will throw an exception in Unit Tests, 'cuase they are part of the Android API, so we will use HashMaps
public class Workflow implements Serializable {

    private Map<Integer,Step> steps;
    private List<Step> stepStack = new ArrayList<>();
    private Step firstStep;
    private Step actualStep = null;

    /**
     * Default constructor.
     * If you use this contructor, you will need to populate the workflow with the {@link Workflow#addStep(Step)} method,
     * and the first step you add will be the first step of the workflow.
     */
    public Workflow() {

        steps = new HashMap<>();
    }

    /**
     * Alternative constructor.
     * Use this constructor to create a workflow with a given Map<StepId,Step> and a first step.
     *
     * @param steps The steps of the workflow
     * @param firstStep The step that will be the first step of the workflow. It must be included in the steps param too.
     */
    public Workflow(List<Step> steps, Step firstStep) {

        this.steps = new HashMap<>();

        for (Step step: steps) {

            this.steps.put(step.getId(),step);
        }

        this.firstStep = firstStep;
    }

    /**
     * Validate the workflow by checking if the next step of each step is in the workflow
     *
     * @return True if the workflow is valid or False otherwise
     */
    public boolean validate() {
        boolean ok = true;

        // Validate if the next step of each step is in the workflow
        for (Step step :steps.values()){

            if (SelectOneStep.class.isInstance(step)) {
                // TODO: For SelectOneStep test over all posible step results (all options to select)
                //((SelectOneStep) step).getOptionsToSelect()
            }
            else if ((step.getNextStepId() != null) && (steps.get(step.getNextStepId()) == null)) {
                Log.e("Workflow","NextStepId="+String.valueOf(step.getNextStepId())+" of step id="+String.valueOf(step.getId())+" not found in the workflow");
                ok = false;
            }
        }

        return ok;
    }

    /**
     * Adds a {@link Step} to the workflow
     * If the workflow is empty, the Step will be considered as the first step of the workflow
     *
     * @param step The Step to add to the workflow
     */
    public void addStep(Step step) {

        if (steps.isEmpty())
            firstStep = step;

        steps.put(step.getId(),step);
    }

    /**
     * Returns the next step in the workflow. It could be null, indicating the end of the workflow.
     * After calling this method, the returned Step becomes the actual step
     *
     * @return The next step in the workflow.
     */
    public @Nullable Step nextStep() {
        Step step = null;

        if (this.actualStep == null)
            step = this.firstStep;
        else {
            Integer nextStepId = this.actualStep.getNextStepId();
            if (nextStepId != null) {
                step = steps.get(nextStepId);
            }
        }

        if (step != null) {

            if (this.actualStep != null)
                stepStack.add(this.actualStep);

            this.actualStep = step;
        }

        return step;
    }

    /**
     * Returns the previous step in the workflow. It could be null, indicating the beginning of the workflow.
     * After calling this method, the returned Step becomes the actual step
     *
     * @return The previous step in the workflow.
     */
    public @Nullable Step previuosStep() {
        Step step = null;

        if (! this.stepStack.isEmpty()) {
            // Pop from the stack
            step = this.stepStack.remove(this.stepStack.size()-1);
        }

        //if (step != null) {
            this.actualStep = step;
        //}

        return step;
    }

    /**
     * Returns the current position in the workflow (the count of steps)
     *
     * @return the current position
     */
    public int getStepPosition() {

        return this.stepStack.size();
    }


    /**
     * Check if the workflow is at the beginning
     * @return True if the workflow is positioned at the beginning, false otherwise
     */
    public boolean isBeginning() {
        return (this.actualStep == null) || (this.actualStep == this.firstStep);
    }

    /**
     * Check if the workflow is at the end
     * @return True if the workflow is positioned at the end, false otherwise
     */
    public boolean isEnd() {
        return (this.actualStep != null) && (this.actualStep.getNextStepId() == null);
    }
}
