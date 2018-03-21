package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/06/2017.
 */

public class PhotoStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String photoInstructions;

    public PhotoStepClassGenerator(int id, Integer nextStepId, String photoInstructions) {
        super(id);
        this.nextStepId = nextStepId;
        this.photoInstructions = photoInstructions;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varNameInstructions = "photoInstructions"+ String.valueOf(stepIndex);

        XMLManagement.addString(varNameInstructions, this.photoInstructions);

        output.add("    String "+varNameInstructions +" = getResources().getString(R.string."+varNameInstructions+"); ");
        output.add("    PhotoStep "+varNameStep+" = new PhotoStep("+String.valueOf(id)+","+varNameInstructions+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }
}
