package org.cientopolis.samplers.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 06/06/2016.
 */
public class Workflow {

    private List<Step> steps;
    private int actualStep = -1;

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
            actualStep++;
            step = steps.get(actualStep);
        }

        return step;
    }

    public Step previuosStep() {
        Step step = null;

        if (! isEnd()) {
            actualStep--;
            step = steps.get(actualStep);
        }

        return step;
    }

    public int getStepCount() {
        return steps.size();
    }

    // from 0 to count()-1
    public int getStepPosition() {

        return actualStep;
    }

    // from 0 to count()-1
    public void setStepPosition(int aPosition) {
        if ((aPosition >= 0) &&(aPosition < steps.size()))
            actualStep = aPosition;
    }


    public boolean isBegining() {
        return actualStep <= 0;
    }

    public boolean isEnd() {
        return actualStep == (steps.size()-1);
    }
}
