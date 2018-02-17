package org.cientopolis.samplers.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.persistence.DAO_Factory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
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
        /*
        id = Long.valueOf(0);
        //create sample
        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption(1,"Arboles"));
        optionsToSelect.add(new MultipleSelectOption(2,"Basura"));
        optionsToSelect.add(new MultipleSelectOption(3,"Arroyo"));
        optionsToSelect.add(new MultipleSelectOption(4,"Animales"));
        MultipleSelectStep multipleSelectStep = new MultipleSelectStep(1,optionsToSelect,"Seleccione lo que ve",2);

        ArrayList<MultipleSelectOption> optionsToOneSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption(1,"option0"));
        optionsToSelect.add(new MultipleSelectOption(2,"option1"));
        optionsToSelect.add(new MultipleSelectOption(3,"option2"));
        optionsToSelect.add(new MultipleSelectOption(4,"option3"));
        SelectOneStep selectOneStep = new SelectOneStep(2,optionsToOneSelect, "Seleccion uno solo");
        // create step results
        MultipleSelectStepResult multipleSelectStepResult = new MultipleSelectStepResult(multipleSelectStep.getSelectedOptions());
        multipleSelectStep.setStepResult(multipleSelectStepResult);

        SelectOneStepResult selectOneStepResult = new SelectOneStepResult(selectOneStep.getSelectedOption());
        selectOneStep.setStepResult(selectOneStepResult);

        sample = new Sample();

        sample.addStepResult(multipleSelectStepResult);
        sample.addStepResult(selectOneStepResult);
*/
    }

    @After
    public void tearDown() throws Exception {
        /*delete test sample*/
        DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).delete(sample);
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list().size(), is(0));
    }



    @Test
    public void getSamplesDir() throws Exception {
        File samplesDirectory = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).getSamplesDir();
        assertThat(samplesDirectory.getName(), equalToIgnoringCase("samples"));

    }

    @Test
    public void save() throws Exception {
        // Save the sample localy
        List<Sample> list = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list();
        id = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).save(this.sample);
        //assert if theres one or more samples
        DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).find(id);
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).list().size(), greaterThanOrEqualTo(list.size()+1));

    }

    @Test
    public void find() throws Exception {
        if(id == 0) {
            id = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).save(sample);
        }
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).find(id).getId(), equalTo(id));
    }

    @Test
    public void getSampleDir() throws Exception {
        if(id == 0) {
            id = DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).save(sample);
        }
        String sampleDirectoryName = "sample_"+String.valueOf(id);
        assertThat(DAO_Factory.getSampleDAO(InstrumentationRegistry.getContext()).getSampleDir(sample).getName(), equalToIgnoringCase(sampleDirectoryName));
    }

}