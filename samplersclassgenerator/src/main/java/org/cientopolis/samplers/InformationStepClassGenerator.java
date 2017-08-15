package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 02/06/2017.
 */

public class InformationStepClassGenerator implements StepClassGenerator {

    private int id;
    private Integer nextStepId;
    private String textToShow;

    public InformationStepClassGenerator(int id, Integer nextStepId, String textToShow) {
        this.id = id;
        this.nextStepId = nextStepId;
        this.textToShow = textToShow;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varName = "textToShow"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.textToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    "+workflow_var+".addStep(new InformationStep("+String.valueOf(id)+","+varName+","+String.valueOf(nextStepId)+")); ");

        return output;
    }

}
