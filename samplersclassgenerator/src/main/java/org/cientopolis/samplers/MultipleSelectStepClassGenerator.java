package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 07/06/2017.
 */

public class MultipleSelectStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private List<MultipleSelectOption> optionsToShow;
    private String title;

    public MultipleSelectStepClassGenerator(int id, Integer nextStepId, String title) {
        super(id);
        this.nextStepId = nextStepId;
        this.optionsToShow = new ArrayList<>();
        this.title = title;
    }

    public void addOptionToSelect(int id, String textToShow) {
        MultipleSelectOption selectOption = new MultipleSelectOption(id, textToShow);
        optionsToShow.add(selectOption);
    }

    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varNameTitle = "multipleSelectTitle"+ String.valueOf(stepIndex);
        String varNameOptions = "optionsToSelect" + String.valueOf(stepIndex);

        XMLManagement.addString(varNameTitle, this.title);
        
        output.add("    String "+varNameTitle +" = getResources().getString(R.string."+varNameTitle+"); ");
        output.add("    ArrayList<MultipleSelectOption> "+varNameOptions +" = new ArrayList<MultipleSelectOption>();");

        for (int i=0; i<optionsToShow.size(); i++) {
            MultipleSelectOption option = optionsToShow.get(i);

            String varNameOptionText = "multipleSelectOptionText" + String.valueOf(stepIndex)+"_"+String.valueOf(i);
            XMLManagement.addString(varNameOptionText, option.getTextToShow());

            output.add("    String "+varNameOptionText +" = getResources().getString(R.string."+varNameOptionText+"); ");
            output.add("    "+varNameOptions +".add(new MultipleSelectOption("+option.getId()+","+varNameOptionText+"));");
        }

        output.add("    MultipleSelectStep "+varNameStep+" = new MultipleSelectStep("+String.valueOf(id)+","+varNameOptions+","+varNameTitle+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }


}
