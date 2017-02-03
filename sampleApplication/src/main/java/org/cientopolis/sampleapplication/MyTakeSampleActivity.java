package org.cientopolis.sampleapplication;

import android.os.Bundle;
import java.util.ArrayList;
import org.cientopolis.samplers.modelo.MultipleSelectOption;
import org.cientopolis.samplers.modelo.MultipleSelectStep;
import org.cientopolis.samplers.modelo.SelectOneStep;
import org.cientopolis.samplers.modelo.Workflow;
import org.cientopolis.samplers.ui.take_sample.TakeSampleActivity;



/**
 * Created by Xavier on 02/02/2017.
 */

public class MyTakeSampleActivity extends TakeSampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workflow = new Workflow();

        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption("Arboles", false));
        optionsToSelect.add(new MultipleSelectOption("Basura", false));
        optionsToSelect.add(new MultipleSelectOption("Arroyo", false));
        optionsToSelect.add(new MultipleSelectOption("Animales", false));
        workflow.addStep(new MultipleSelectStep(optionsToSelect));

        ArrayList<MultipleSelectOption> optionsToSelect2 = new ArrayList<MultipleSelectOption>();
        optionsToSelect2.add(new MultipleSelectOption("Opcion 1",false));
        optionsToSelect2.add(new MultipleSelectOption("Opcion 2",false));
        optionsToSelect2.add(new MultipleSelectOption("Opcion 3",false));
        optionsToSelect2.add(new MultipleSelectOption("Opcion 4",false));
        workflow.addStep(new SelectOneStep(optionsToSelect2));
    }

}
