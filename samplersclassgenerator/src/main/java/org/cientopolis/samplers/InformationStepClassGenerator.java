package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 02/06/2017.
 */

public class InformationStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String textToShow;

    public InformationStepClassGenerator(int id, Integer nextStepId, String textToShow) {
        super(id);
        this.nextStepId = nextStepId;
        this.textToShow = textToShow;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varName = "textToShow"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.textToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    InformationStep "+varNameStep+" = new InformationStep("+String.valueOf(id)+","+varName+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }

}
