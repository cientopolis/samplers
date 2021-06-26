package org.cientopolis.samplers.framework;

import org.cientopolis.samplers.framework.information.InformationStep;
import org.cientopolis.samplers.framework.information.InformationStepResult;
//import org.junit.Test;

//import static org.junit.Assert.*;

/**
 * Created by Xavier on 06/11/2017.
 */
public class WorkflowTest {
    /*
    @Test
    public void validate() throws Exception {
        // TODO: Complete test
    }

    @Test
    public void nextStep() throws Exception {
        //options
        Step step0 = new InformationStep(0,"paso 0",1);
        Step step1 = new InformationStep(1,"paso 1",2);
        Step step2 = new InformationStep(2,"paso 2",3);
        Step step3 = new InformationStep(3,"paso 3",null);

        Workflow workflow = new Workflow();

        workflow.addStep(step0);
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);

        workflow.nextStep(new InformationStepResult(null)); //step 0
        Step testStep = workflow.nextStep(new InformationStepResult(step0)); //step 1

        assertEquals(1,workflow.getStepPosition());
        assertEquals(1, testStep.getId());
    }

    @Test
    public void previuosStep() throws Exception {
        //options
        Step step0 = new InformationStep(0,"step 0",1);
        Step step1 = new InformationStep(1,"step 1",2);
        Step step2 = new InformationStep(2,"step 2",3);
        Step step3 = new InformationStep(3,"step 3",null);

        Workflow workflow = new Workflow();

        workflow.addStep(step0);
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);

        workflow.nextStep(null); //step 0
        workflow.nextStep(new InformationStepResult(step0)); //step 1
        workflow.nextStep(new InformationStepResult(step1)); //step 2
        workflow.nextStep(new InformationStepResult(step2)); //step 3

        Step testStep = workflow.previuosStep(); //step 2

        assertEquals(2,workflow.getStepPosition());
        assertEquals(2, testStep.getId());
    }

    @Test
    public void outOfBounds () throws Exception {
        Step step0 = new InformationStep(0, "step 0",1);
        Step step1 = new InformationStep(1, "step 1",null);

        Workflow workflow = new Workflow();

        workflow.addStep(step0);
        workflow.addStep(step1);

        workflow.nextStep(null); //step 0
        workflow.nextStep(new InformationStepResult(step0)); //step 1
        workflow.nextStep(new InformationStepResult(step0)); //inexisting step
        workflow.nextStep(new InformationStepResult(step0)); //inexisting step
        //
        assertEquals(1, workflow.getStepPosition());
    }

     */
}