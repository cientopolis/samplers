package org.cientopolis.sampleapplication;

import android.os.Bundle;

import org.cientopolis.samplers.model.InformationStep;
import org.cientopolis.samplers.model.InsertTextStep;
import org.cientopolis.samplers.model.LocationStep;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.PhotoStep;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.SelectOption;
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
        workflow.addStep(new InformationStep(1, "Informacion de prueba para ver que se muestra bien",4));

        // InsertTextStep
        //workflow.addStep(new InsertTextStep(2, "Escriba algo","cualquier cosa",50, InsertTextStep.InputType.TYPE_TEXT, true, 3));

        // LocationStep
        //workflow.addStep(new LocationStep(3, "Seleccione la posicion de la muestra",4));

        // PhotoStep
        workflow.addStep(new PhotoStep(4, "Instrucciones para mostrar","",5));

        // MultipleSelectStep
        ArrayList<SelectOption> optionsToSelect = new ArrayList<SelectOption>();
        optionsToSelect.add(new SelectOption(1,"Arboles"));
        optionsToSelect.add(new SelectOption(2,"Basura"));
        optionsToSelect.add(new SelectOption(3,"Arroyo"));
        optionsToSelect.add(new SelectOption(4,"Animales"));
        workflow.addStep(new MultipleSelectStep(5, optionsToSelect, "Seleccione si observa algo de esto",6));

        // SelectOneStep
        ArrayList<SelectOption> optionsToSelect2 = new ArrayList<SelectOption>();
        optionsToSelect2.add(new SelectOption(1,"Opcion 1"));
        optionsToSelect2.add(new SelectOption(2,"Opcion 2"));
        optionsToSelect2.add(new SelectOption(3,"Opcion 3"));
        optionsToSelect2.add(new SelectOption(4,"Opcion 4"));
        workflow.addStep(new SelectOneStep(6, optionsToSelect2, "Seleccione solo uno"));

        return workflow;
    }
}
