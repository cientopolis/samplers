package org.cientopolis.samplers;

import org.cientopolis.samplers.framework.information.InformationStep;
import org.cientopolis.samplers.framework.multipleSelect.MultipleSelectOption;
import org.cientopolis.samplers.framework.multipleSelect.MultipleSelectStep;
import org.cientopolis.samplers.framework.photo.PhotoStep;
import org.cientopolis.samplers.framework.selectOne.SelectOneOption;
import org.cientopolis.samplers.framework.selectOne.SelectOneStep;
import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.Workflow;
//import org.junit.Test;

import java.util.ArrayList;

//import static org.junit.Assert.assertEquals;

/**
 * Created by lilauth on 3/14/17.
 */

public class SamplersModelUnitTest {
/*
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
        PhotoStep photoStep = new PhotoStep(4, "instructions",null);
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

*/



}
