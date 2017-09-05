package org.cientopolis.sampleapplication;

import android.os.Bundle;

import org.cientopolis.samplers.model.InformationStep;
import org.cientopolis.samplers.model.InsertDateStep;
import org.cientopolis.samplers.model.InsertTimeStep;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.SelectOneOption;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.MultipleSelectOption;
import org.cientopolis.samplers.model.Step;
import org.cientopolis.samplers.model.Workflow;
import org.cientopolis.samplers.network.NetworkConfiguration;
import org.cientopolis.samplers.ui.SamplersMainActivity;

import java.util.ArrayList;

/**
 * Created by Xavier on 02/02/2017.
 */

public class MyMainSamplersActivity  extends SamplersMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the network configuration
        NetworkConfiguration.setURL("http://192.168.1.10/samplers/upload.php");
        NetworkConfiguration.setPARAM_NAME("sample");

        lb_main_welcome_message.setText("Bienvenido a la Aplicacion de Prueba!");
    }


    @Override
    protected Workflow getWorkflow() {
        Workflow workflow = new Workflow();
        Step step;

        // InformationStep
        workflow.addStep(new InformationStep(1, "Informacion de prueba para ver que se muestra bien",101));

        // Insert Time
        workflow.addStep(new InsertTimeStep(101, "Seleccione la Hora de la muestra",102));

        // Insert Date
        workflow.addStep(new InsertDateStep(102, "Seleccione la Fecha de la muestra",2));

        // SelectOneStep
        ArrayList<SelectOneOption> optionsToSelect2 = new ArrayList<SelectOneOption>();
        optionsToSelect2.add(new SelectOneOption(1,"SI", 5));
        optionsToSelect2.add(new SelectOneOption(2,"NO", 6));
        workflow.addStep(new SelectOneStep(2, optionsToSelect2, "¿Quiere sacar una foto?"));

        // InsertTextStep
        //workflow.addStep(new InsertTextStep(3, "Escriba algo","cualquier cosa",50, InsertTextStep.InputType.TYPE_TEXT, true, 3));

        // LocationStep
        //workflow.addStep(new LocationStep(4, "Seleccione la posicion de la muestra",4));

        // PhotoStep
        workflow.addStep(new PhotoStep(5, "Instrucciones para mostrar","",6));

        // MultipleSelectStep
        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption(1,"Arboles"));
        optionsToSelect.add(new MultipleSelectOption(2,"Basura"));
        optionsToSelect.add(new MultipleSelectOption(3,"Arroyo"));
        optionsToSelect.add(new MultipleSelectOption(4,"Animales"));
        workflow.addStep(new MultipleSelectStep(6, optionsToSelect, "Seleccione si observa algo de esto",7));

        // SelectOneStep
        optionsToSelect2 = new ArrayList<SelectOneOption>();
        optionsToSelect2.add(new SelectOneOption(1,"Opcion 1", null));
        optionsToSelect2.add(new SelectOneOption(2,"Opcion 2", null));
        optionsToSelect2.add(new SelectOneOption(3,"Opcion 3", null));
        optionsToSelect2.add(new SelectOneOption(4,"Opcion 4", null));
        workflow.addStep(new SelectOneStep(7, optionsToSelect2, "Seleccione solo uno"));

        return workflow;
    }
}
