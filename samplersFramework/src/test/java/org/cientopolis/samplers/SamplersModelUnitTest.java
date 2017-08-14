package org.cientopolis.samplers;

import org.cientopolis.samplers.model.InformationStep;
import org.cientopolis.samplers.model.MultipleSelectOption;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.SelectOneOption;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.Step;
import org.cientopolis.samplers.model.Workflow;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by lilauth on 3/14/17.
 */

public class SamplersModelUnitTest {

    private MultipleSelectStep multipleSelectStep;

    @Test
    public void informationStep() throws Exception {
        InformationStep is = new InformationStep(1, "an informative text",null);
        assertEquals("an informative text", is.getTextToShow());
        assertEquals(1, is.getId());
    }

    @Test
    public void emptyMultipleSelectStep() throws Exception {
        multipleSelectStep = new MultipleSelectStep(2,"title",null);
        assertEquals(0, multipleSelectStep.getOptionsToSelect().size());
        assertEquals(2, multipleSelectStep.getId());
    }

    @Test
    public void MultipleSelectStepWithSelectedOptions() throws Exception {
        //options
        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<>();
        optionsToSelect.add(new MultipleSelectOption(1,"Option 1"));
        optionsToSelect.add(new MultipleSelectOption(2,"Option 2"));
        MultipleSelectOption selectedOption1 = new MultipleSelectOption(3,"Option 3");
        optionsToSelect.add(selectedOption1);
        MultipleSelectOption selectedOption2 = new MultipleSelectOption(4,"Option 4");
        optionsToSelect.add(selectedOption2);
        // MultipleSelectStep
        multipleSelectStep = new MultipleSelectStep(3, optionsToSelect,"Seleccione lo que ve",null);
        //
        assertEquals(4, multipleSelectStep.getOptionsToSelect().size());
        assertEquals(3, multipleSelectStep.getId());
    }

    @Test
    public void PhotoStepTest () throws Exception {
        PhotoStep photoStep = new PhotoStep(4, "instructions","path to image",null);
        assertEquals("instructions", photoStep.getInstructionsToShow());
        assertEquals(4, photoStep.getId());
    }

    @Test
    public void SelectOneStepTestWithSelection () throws Exception {
        //options
        ArrayList<SelectOneOption> optionsToSelect = new ArrayList<>();
        optionsToSelect.add(new SelectOneOption(1,"Option 1",null));
        optionsToSelect.add(new SelectOneOption(2,"Option 2",null));
        SelectOneOption selectedOption1 = new SelectOneOption(3,"Option 3", null);
        optionsToSelect.add(selectedOption1);
        SelectOneOption selectedOption2 = new SelectOneOption(4,"Option 4",null);
        optionsToSelect.add(selectedOption2);

        SelectOneStep selectOneStep = new SelectOneStep(5,optionsToSelect, "Seleccione uno solo");
        //returns the first selected
        assertEquals("Seleccione uno solo", selectOneStep.getTitle());
        assertEquals(5, selectOneStep.getId());
    }

    @Test
    public void workflowTestNextStep () throws Exception {
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

        workflow.nextStep(); //step 0
        Step testStep = workflow.nextStep(); //step 1

        assertEquals(1,workflow.getStepPosition());
        assertEquals(1, testStep.getId());
    }

    @Test
    public void workflowTestOutOfBounds () throws Exception {
        Step step0 = new InformationStep(0, "step 0",1);
        Step step1 = new InformationStep(1, "step 1",null);

        Workflow workflow = new Workflow();

        workflow.addStep(step0);
        workflow.addStep(step1);

        workflow.nextStep(); //step 0
        workflow.nextStep(); //step 1
        workflow.nextStep(); //inexisting step
        workflow.nextStep(); //inexisting step
        //
        assertEquals(1, workflow.getStepPosition());
    }

    @Test
    public void workflowTestPriortStep () throws Exception {
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

        workflow.nextStep(); //step 0
        workflow.nextStep(); //step 1
        workflow.nextStep(); //step 2
        workflow.nextStep(); //step 3

        Step testStep = workflow.previuosStep(); //step 2

        assertEquals(2,workflow.getStepPosition());
        assertEquals(2, testStep.getId());
    }
}
