package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 03/09/2017.
 */

public class InsertTextStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String textToShow;
    private String sampleText;
    private int maxLength;
    private String inputType;
    private boolean optional;


    public InsertTextStepClassGenerator(int id, Integer nextStepId, String textToShow, String sampleText, int maxLength, String inputType, boolean optional) {
        super(id);

        // Checks for valid inputType
        if ((!inputType.equals("text")) && (!inputType.equals("number")) && (!inputType.equals("decimal"))) {
            throw new RuntimeException("Invalid inputType: "+inputType+". InputType mut be one of [text, number, decimal]");
        }

        this.nextStepId = nextStepId;
        this.textToShow = textToShow;
        this.sampleText = sampleText;
        this.maxLength = maxLength;
        this.inputType = inputType;
        this.optional = optional;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);
        String varNameTextToShow = "textToShowInsertText"+ String.valueOf(stepIndex);
        String varNameSampleText = "sampleTextInsertText"+ String.valueOf(stepIndex);


        XMLManagement.addString(varNameTextToShow, this.textToShow);
        XMLManagement.addString(varNameSampleText, this.sampleText);

        output.add("    String "+varNameTextToShow +" = getResources().getString(R.string."+varNameTextToShow+"); ");
        output.add("    String "+varNameSampleText +" = getResources().getString(R.string."+varNameSampleText+"); ");
        output.add("    InsertTextStep "+varNameStep+" = new InsertTextStep("+String.valueOf(id)+","+varNameTextToShow+","+varNameSampleText+","+String.valueOf(maxLength)+","+inputTypeToEnum(inputType)+","+String.valueOf(optional)+","+String.valueOf(nextStepId)+"); ");

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }

    private String inputTypeToEnum(String inputType) {
        String result = "text";

        if (inputType.equals("text"))
            result = "InsertTextStep.InputType.TYPE_TEXT";
        else if (inputType.equals("number"))
            result = "InsertTextStep.InputType.TYPE_NUMBER";
        else if (inputType.equals("decimal"))
            result = "InsertTextStep.InputType.TYPE_DECIMAL";


        return result;
    }
}
