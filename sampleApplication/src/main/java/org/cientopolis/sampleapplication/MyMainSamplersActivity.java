package org.cientopolis.sampleapplication;


import android.os.Bundle;
import org.cientopolis.samplers.authentication.AuthenticationManager;
import org.cientopolis.samplers.framework.information.InformationStep;
import org.cientopolis.samplers.framework.insertDate.InsertDateStep;
import org.cientopolis.samplers.framework.insertText.InsertTextStep;
import org.cientopolis.samplers.framework.insertTime.InsertTimeStep;
import org.cientopolis.samplers.framework.location.LocationStep;
import org.cientopolis.samplers.framework.multipleSelect.MultipleSelectStep;
import org.cientopolis.samplers.framework.photo.PhotoStep;
import org.cientopolis.samplers.framework.route.RouteStep;
import org.cientopolis.samplers.framework.selectOne.SelectOneOption;
import org.cientopolis.samplers.framework.selectOne.SelectOneStep;
import org.cientopolis.samplers.framework.multipleSelect.MultipleSelectOption;
import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.Workflow;
import org.cientopolis.samplers.framework.soundRecord.SoundRecordStep;
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
        NetworkConfiguration.setURL("http://192.168.0.10/samplers/upload.php");
        NetworkConfiguration.setPARAM_NAME_SAMPLE("sample");
        NetworkConfiguration.setPARAM_NAME_USER_ID("user_id");
        NetworkConfiguration.setPARAM_NAME_AUTHENTICATION_TYPE("authentication_type");

        // Set the authentication configuration
        AuthenticationManager.setAuthenticationEnabled(true);
        AuthenticationManager.setAuthenticationOptional(true);
    }


    @Override
    protected Workflow getWorkflow() {
        Workflow workflow = new Workflow();
        Step step;

        // InformationStep
        workflow.addStep(new InformationStep(1, "Informacion de prueba para ver que se muestra bien",3));

        // Insert Time
        workflow.addStep(new InsertTimeStep(101, "Seleccione la Hora de la muestra",102));

        // Insert Date
        workflow.addStep(new InsertDateStep(102, "Seleccione la Fecha de la muestra",103));

        // Sound
        workflow.addStep(new SoundRecordStep(103, "Grabe algo",6));

        // SelectOneStep
        ArrayList<SelectOneOption> optionsToSelect2 = new ArrayList<SelectOneOption>();
        optionsToSelect2.add(new SelectOneOption(1,"SI", 3));
        optionsToSelect2.add(new SelectOneOption(2,"NO", 4));
        workflow.addStep(new SelectOneStep(2, optionsToSelect2, "¿Quiere sacar una foto?"));

        // PhotoStep
        PhotoStep photoStep = new PhotoStep(3, "Saque una foto de su gato",null);
        // set help resource
        photoStep.setHelpResourseId(R.raw.photohelp);
        workflow.addStep(photoStep);

        // InsertTextStep
        workflow.addStep(new InsertTextStep(4, "Escriba algo","cualquier cosa",50, InsertTextStep.InputType.TYPE_TEXT, true, 5));

        // LocationStep
        workflow.addStep(new LocationStep(5, "Seleccione la posicion de la muestra",6));

        // RouteStep
        RouteStep routeStep = new RouteStep(55, "Marque el recorrido", null);
        routeStep.setInterval(2000);  // default is 5000
        routeStep.setMapZoom(12);  // default is 15
        workflow.addStep(routeStep);

        // MultipleSelectStep
        ArrayList<MultipleSelectOption> optionsToSelect = new ArrayList<MultipleSelectOption>();
        optionsToSelect.add(new MultipleSelectOption(1,"Arboles"));
        optionsToSelect.add(new MultipleSelectOption(2,"Basura"));
        optionsToSelect.add(new MultipleSelectOption(3,"Arroyo"));
        optionsToSelect.add(new MultipleSelectOption(4,"Animales"));
        workflow.addStep(new MultipleSelectStep(6, optionsToSelect, "Seleccione si observa algo de esto",null));



/*
        // SelectOneStep
        optionsToSelect2 = new ArrayList<SelectOneOption>();
        optionsToSelect2.add(new SelectOneOption(1,"Opcion 1", null));
        optionsToSelect2.add(new SelectOneOption(2,"Opcion 2", null));
        optionsToSelect2.add(new SelectOneOption(3,"Opcion 3", null));
        optionsToSelect2.add(new SelectOneOption(4,"Opcion 4", null));
        workflow.addStep(new SelectOneStep(7, optionsToSelect2, "Seleccione solo uno"));
*/


        return workflow;
    }

    @Override
    protected Integer getMainHelpResourceId() {
        return R.raw.mainhelp;
    }
}
