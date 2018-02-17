package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 30/11/2017.
 */

public class SoundRecordStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String instructionsToShow;

    public SoundRecordStepClassGenerator(int id, Integer nextStepId, String instructionsToShow) {
        super(id);
        this.nextStepId = nextStepId;
        this.instructionsToShow = instructionsToShow;
    }

    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varName = "instructionsToShow"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.instructionsToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    SoundRecordStep "+varNameStep+" = new SoundRecordStep("+String.valueOf(id)+","+varName+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }

}
