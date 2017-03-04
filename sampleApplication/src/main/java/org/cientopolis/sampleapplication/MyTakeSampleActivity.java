package org.cientopolis.sampleapplication;

import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.cientopolis.samplers.model.InformationStep;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.Workflow;
import org.cientopolis.samplers.ui.take_sample.TakeSampleActivity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Xavier on 02/02/2017.
 */

public class MyTakeSampleActivity extends TakeSampleActivity {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        workflow.addStep(new InformationStep("Informacion de prueba para ver que se muestra bien"));

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
/*

        File fileSample;
        try {
            File fileDir = new File(getApplicationContext().getFilesDir(),"samples");
            String[] samples = fileDir.list();


            for (String sampleName: samples) {
                fileSample = new File(fileDir, sampleName);
                //post("http://192.168.0.107/service/upload.php",fileSample);
                new SendFile().execute(fileSample);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private String post(String url, File json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
