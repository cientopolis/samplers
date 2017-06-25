package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 02/06/2017.
 */

public class LocationStepClassGenerator implements StepClassGenerator {

    private String textToShow;

    public LocationStepClassGenerator(String textToShow) {
        this.textToShow = textToShow;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();

        String varName = "textToShow"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.textToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    "+workflow_var+".addStep(new LocationStep("+varName+")); ");

        return output;
    }

}
