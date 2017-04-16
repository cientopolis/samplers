package org.cientopolis.samplers.model;

import org.cientopolis.samplers.ui.take_sample.LocationFragment;


/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStep extends BaseStep {

    private String textToShow;


    public LocationStep(String aTextToShow) {
        stepFragmentClass = LocationFragment.class;
        textToShow = aTextToShow;
    }

    public String getTextToShow(){

        return textToShow;
    }


}
