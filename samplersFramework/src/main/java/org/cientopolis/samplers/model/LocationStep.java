package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStep extends Step {

    private String textToShow;

    public LocationStep(String aTextToShow) {

        textToShow = aTextToShow;
    }

    public String getTextToShow(){

        return textToShow;
    }
}
