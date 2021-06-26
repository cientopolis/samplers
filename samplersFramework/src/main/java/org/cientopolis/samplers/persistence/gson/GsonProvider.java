package org.cientopolis.samplers.persistence.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by Xavier on 22/04/2018.
 */

public abstract class GsonProvider {

    public static Gson newGsonInstance() {

        return new GsonBuilder()
                .registerTypeAdapter(StepResult.class, new InterfaceAdapter<StepResult>())
                .registerTypeAdapter(Step.class, new InterfaceAdapter<Step>())
                .registerTypeAdapter(Class.class, new ClassAdapter())
                .create();
    }

}
