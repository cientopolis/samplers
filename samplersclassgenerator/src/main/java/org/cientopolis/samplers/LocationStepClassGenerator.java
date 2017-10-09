package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 02/06/2017.
 */

public class LocationStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String textToShow;

    public LocationStepClassGenerator(int id, Integer nextStepId, String textToShow) {
        super(id);
        this.nextStepId = nextStepId;
        this.textToShow = textToShow;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);

        String varName = "textToShowLocation"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.textToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    LocationStep "+varNameStep+" = new LocationStep("+String.valueOf(id)+","+varName+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }

}
