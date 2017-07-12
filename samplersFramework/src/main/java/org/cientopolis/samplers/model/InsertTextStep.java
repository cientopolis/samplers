package org.cientopolis.samplers.model;

import android.text.InputType;

import org.cientopolis.samplers.ui.take_sample.InsertTextFragment;

/**
 * Created by Xavier on 20/06/2017.
 */

public class InsertTextStep extends BaseStep {

    private String textToShow;
    private String sampleText;
    private int maxLength;
    private InsertTextStep.InputType inputType;
    private boolean optional;
    private Integer nextStepId;

    public InsertTextStep(String textToShow, String sampleText, int maxLength, InsertTextStep.InputType inputType, boolean optional) {
        stepFragmentClass = InsertTextFragment.class;
        this.textToShow = textToShow;
        this.sampleText = sampleText;
        this.maxLength = maxLength;
        this.inputType = inputType;
        this.optional = optional;
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
    public Integer getNextStepId() {
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
