package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/06/2017.
 */

public class PhotoStepClassGenerator implements StepClassGenerator {

    private int id;
    private Integer nextStepId;
    private String photoInstructions;
    private String overload_image_name;

    public PhotoStepClassGenerator(int id, Integer nextStepId, String photoInstructions, String overload_image_name) {
        this.id = id;
        this.nextStepId = nextStepId;
        this.photoInstructions = photoInstructions;
        this.overload_image_name = overload_image_name;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameInstructions = "photoInstructions"+ String.valueOf(stepIndex);
        String varNameImageToOverly = "photoImageToOverly"+ String.valueOf(stepIndex);

        XMLManagement.addString(varNameInstructions, this.photoInstructions);
        XMLManagement.addString(varNameImageToOverly, this.overload_image_name);

        output.add("    String "+varNameInstructions +" = getResources().getString(R.string."+varNameInstructions+"); ");
        output.add("    String "+varNameImageToOverly +" = getResources().getString(R.string."+varNameImageToOverly+"); ");
        output.add("    "+workflow_var+".addStep(new PhotoStep("+String.valueOf(id)+","+varNameInstructions+","+varNameImageToOverly+","+String.valueOf(nextStepId)+")); ");

        return output;
    }
}
