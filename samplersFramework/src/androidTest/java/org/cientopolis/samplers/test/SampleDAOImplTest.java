package org.cientopolis.samplers.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.MultipleSelectStepResult;
import org.cientopolis.samplers.model.Sample;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.SelectOneStepResult;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.persistence.DAO_Factory;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lilauth on 3/16/17.
 */
@RunWith(AndroidJUnit4.class)
public class SampleDAOImplTest {
    private Sample sample;
    private Long id;
    @Before
    public void setUp() throws Exception {
        id = Long.valueOf(0);
        //create sample
        ArrayList<SelectOption> optionsToSelect = new ArrayList<SelectOption>();
        optionsToSelect.add(new SelectOption(1,"Arboles", true));
        optionsToSelect.add(new SelectOption(2,"Basura", false));
        optionsToSelect.add(new SelectOption(3,"Arroyo", true));
        optionsToSelect.add(new SelectOption(4,"Animales", false));
        MultipleSelectStep multipleSelectStep = new MultipleSelectStep(optionsToSelect);

        ArrayList<SelectOption> optionsToOneSelect = new ArrayList<SelectOption>();
        optionsToSelect.add(new SelectOption(1,"option0", false));
        optionsToSelect.add(new SelectOption(2,"option1", false));
        optionsToSelect.add(new SelectOption(3,"option2", true));
        optionsToSelect.add(new SelectOption(4,"option3", false));
        SelectOneStep selectOneStep = new SelectOneStep(optionsToOneSelect);
        /*create step results*/
        MultipleSelectStepResult multipleSelectStepResult = new MultipleSelectStepResult(multipleSelectStep.getSelectedOptions());
        SelectOneStepResult selectOneStepResult = new SelectOneStepResult(selectOneStep.getSelectedOption());

        sample = new Sample();

        sample.addStepResult(multipleSelectStepResult);
        sample.addStepResult(selectOneStepResult);

    }

    @After
    public void tearDown() throws Exception {
        /*delete all samples*/
        List<Sample> samples = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list();
        for(Sample sample : samples){
            DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).delete(sample);
        }
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list().size(), is(0));
    }



    @Test
    public void getSamplesDir() throws Exception {

    }

    @Test
    public void save() throws Exception {
        // Save the sample localy
        id = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).save(this.sample);
        //assert if theres one or more samples
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list().size(), greaterThanOrEqualTo(1));

    }

    @Test
    public void find() throws Exception {

    }

    @Test
    public void getSampleDir() throws Exception {

    }

}