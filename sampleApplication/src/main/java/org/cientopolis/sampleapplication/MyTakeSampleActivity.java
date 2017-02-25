package org.cientopolis.sampleapplication;

import android.os.Bundle;
import java.util.ArrayList;
import org.cientopolis.samplers.modelo.SelectOption;
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

        ArrayList<SelectOption> optionsToSelect = new ArrayList<SelectOption>();
        optionsToSelect.add(new SelectOption(1,"Arboles", false));
        optionsToSelect.add(new SelectOption(2,"Basura", false));
        optionsToSelect.add(new SelectOption(3,"Arroyo", false));
        optionsToSelect.add(new SelectOption(4,"Animales", false));
        workflow.addStep(new MultipleSelectStep(optionsToSelect));

        ArrayList<SelectOption> optionsToSelect2 = new ArrayList<SelectOption>();
        optionsToSelect2.add(new SelectOption(1,"Opcion 1",false));
        optionsToSelect2.add(new SelectOption(2,"Opcion 2",false));
        optionsToSelect2.add(new SelectOption(3,"Opcion 3",false));
        optionsToSelect2.add(new SelectOption(4,"Opcion 4",false));
        workflow.addStep(new SelectOneStep(optionsToSelect2));
    }

}
