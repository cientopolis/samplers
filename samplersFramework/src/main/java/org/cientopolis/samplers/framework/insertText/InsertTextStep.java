package org.cientopolis.samplers.framework.insertText;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 20/06/2017.
 */

public class InsertTextStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_insert_text";

    private String textToShow;
    private String sampleText;
    private int maxLength;
    private InsertTextStep.InputType inputType;
    private boolean optional;
    private Integer nextStepId;

    public InsertTextStep(int id, String textToShow, String sampleText, int maxLength, InsertTextStep.InputType inputType, boolean optional, Integer nextStepId) {
        super(id);
        this.stepFragmentClass = InsertTextFragment.class;
        this.textToShow = textToShow;
        this.sampleText = sampleText;
        this.maxLength = maxLength;
        this.inputType = inputType;
        this.optional = optional;
        this.nextStepId = nextStepId;
    }

    public String getTextToShow(){

        return textToShow;
    }

    public String getSampleText() {
        return sampleText;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isOptional() {
        return optional;
    }

    public InsertTextStep.InputType getInputType() {
        return inputType;
    }

    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        return nextStepId;
    }

    public void setNextStepId(Integer nextStepId) {
        this.nextStepId = nextStepId;
    }

    public enum InputType {
        TYPE_TEXT,
        TYPE_NUMBER,
        TYPE_DECIMAL;
    }
}
