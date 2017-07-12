package org.cientopolis.samplers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xavier on 06/06/2016.
 */
public class Workflow implements Serializable {

    private Map<Integer,Step> steps;
    private List<Step> stepStack = new ArrayList<>();
    //private int stepIndex = -1;
    private Step firstStep;
    private Step actualStep = null;

    public Workflow() {
        steps = new HashMap<Integer, Step>();
    }

    public Workflow(Map<Integer, Step> steps, Step firstStep) {

        this.steps = steps;
        this.firstStep = firstStep;
    }

    public void addStep(Step aStep) {

        if (steps.isEmpty())
            firstStep = aStep;

        steps.put(aStep.getId(),aStep);
    }

    public Step nextStep() {
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

    public Step previuosStep() {
        Step step = null;

        if (! this.stepStack.isEmpty()) {
            // Pop from the stack
            step = this.stepStack.remove(this.stepStack.size()-1);
        }

        return step;
    }

    /*public int getStepCount() {
        return steps.size();
    }
*/
    // from 0 to count()-1
    public int getStepPosition() {

        return this.stepStack.size()+1;
    }

    // from 0 to count()-1
 /*   public void setStepPosition(int aPosition) {
        if ((aPosition >= 0) &&(aPosition < steps.size()))
            stepIndex = aPosition;
    }
*/

    public boolean isBegining() {
        return (this.actualStep == null) || (this.actualStep == this.firstStep);
    }

    public boolean isEnd() {
        return (this.actualStep != null) && (this.actualStep.getNextStepId() == null);
    }
}
