package org.cientopolis.samplers.framework;

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

    public boolean validate() {
        boolean ok = true;

        // Validate if the next step of each step is in the workflow
        for (Step step :steps.values()){

            if (SelectOneStep.class.isInstance(step)) {
                // For SelectOneStep test over all posible step results (all options to select)
                //((SelectOneStep) step).getOptionsToSelect()
            }
            else if ((step.getNextStepId() != null) && (steps.get(step.getNextStepId()) == null)) {
                Log.e("Workflow","NextStepId="+String.valueOf(step.getNextStepId())+" of step id="+String.valueOf(step.getId())+" not found in the workflow");
                ok = false;
            }
        }

        return ok;
    }

    public void addStep(Step aStep) {

        if (steps.isEmpty())
            firstStep = aStep;

        steps.put(aStep.getId(),aStep);
        //Log.e("Workflow","StepId:"+String.valueOf(aStep.getId()));
    }

    public Step nextStep() {
        Step step = null;

        if (this.actualStep == null)
            step = this.firstStep;
        else {
            Integer nextStepId = this.actualStep.getNextStepId();
            if (nextStepId != null) {
                //Log.e("Workflow","nextStepId:"+String.valueOf(nextStepId));
                step = steps.get(nextStepId);
                if (step == null)
                    Log.e("Workflow","NULLLLLL:");
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

        //if (step != null) {
            this.actualStep = step;
        //}

        return step;
    }

    /*public int getStepCount() {
        return steps.size();
    }
*/
    // from 0 to count()-1
    public int getStepPosition() {

        return this.stepStack.size();
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
