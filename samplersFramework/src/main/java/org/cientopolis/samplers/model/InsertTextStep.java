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

    public InsertTextStep(String textToShow, String sampleText, int maxLength, InsertTextStep.InputType inputType) {
        stepFragmentClass = InsertTextFragment.class;
        this.textToShow = textToShow;
        this.sampleText = sampleText;
        this.maxLength = maxLength;
        this.inputType = inputType;
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

    public InsertTextStep.InputType getInputType() {
        return inputType;
    }

    public enum InputType {
        TYPE_TEXT,
        TYPE_NUMBER,
        TYPE_DATE,
        TYPE_TIME;

    }
}
