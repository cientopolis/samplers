package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 06/06/2016.
 */
public class InformationStep extends Step {

    private String textToShow;

    public InformationStep(String aTextToShow) {

        textToShow = aTextToShow;
    }

    public String getTextToShow(){

        return textToShow;
    }

}
