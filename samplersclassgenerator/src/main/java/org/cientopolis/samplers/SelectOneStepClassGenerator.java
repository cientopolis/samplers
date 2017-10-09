package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 09/06/2017.
 */

public class SelectOneStepClassGenerator extends BaseStepClassGenerator {

    private List<SelectOneOption> optionsToShow;
    private String title;


    public SelectOneStepClassGenerator(int id, String title) {
        super(id);
        this.optionsToShow = new ArrayList<>();
        this.title = title;
    }

    public void addOptionToSelect(int id, String textToShow, Integer nextStepId) {
        SelectOneOption selectOption = new SelectOneOption(id, textToShow, nextStepId);
        optionsToShow.add(selectOption);
    }

    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varNameTitle = "selectOneTitle"+ String.valueOf(stepIndex);
        String varNameOptions = "optionsToSelectOne" + String.valueOf(stepIndex);

        XMLManagement.addString(varNameTitle, this.title);

        output.add("    String "+varNameTitle +" = getResources().getString(R.string."+varNameTitle+"); ");
        output.add("    ArrayList<SelectOneOption> "+varNameOptions +" = new ArrayList<SelectOneOption>();");

        for (int i=0; i<optionsToShow.size(); i++) {
            SelectOneOption option = optionsToShow.get(i);

            String varNameOptionText = "selectOneOptionText" + String.valueOf(stepIndex)+"_"+String.valueOf(i);
            XMLManagement.addString(varNameOptionText, option.getTextToShow());

            output.add("    String "+varNameOptionText +" = getResources().getString(R.string."+varNameOptionText+"); ");
            output.add("    "+varNameOptions +".add(new SelectOneOption("+option.getId()+","+varNameOptionText+", "+String.valueOf(option.getNextStepId())+"));");
        }

        output.add("    SelectOneStep "+varNameStep+" = new SelectOneStep("+String.valueOf(id)+","+varNameOptions+","+varNameTitle+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }
}
