package org.cientopolis.samplers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 06/06/2016.
 */
public class Workflow implements Serializable {

    private List<Step> steps;
    private int stepIndex = -1;

    public Workflow() {
        steps = new ArrayList<Step>();
    }

    public Workflow(List<Step> aSteps) {
        steps = aSteps;
    }

    public void addStep(Step aStep) {
        steps.add(aStep);
    }

    public Step nextStep() {
        Step step = null;

        if (! isEnd()) {
            stepIndex++;
            step = steps.get(stepIndex);
        }

        return step;
    }

    public Step previuosStep() {
        Step step = null;

        if (! isBegining()) {
            stepIndex--;
            step = steps.get(stepIndex);
        }

        return step;
    }

    public int getStepCount() {
        return steps.size();
    }

    // from 0 to count()-1
    public int getStepPosition() {

        return stepIndex;
    }

    // from 0 to count()-1
    public void setStepPosition(int aPosition) {
        if ((aPosition >= 0) &&(aPosition < steps.size()))
            stepIndex = aPosition;
    }


    public boolean isBegining() {
        return stepIndex <= 0;
    }

    public boolean isEnd() {
        return stepIndex == (steps.size()-1);
    }
}
