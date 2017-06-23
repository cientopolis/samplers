package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 23/06/2017.
 */

public class InsertTextStepResult extends StepResult {

    private String insertedText;

    public InsertTextStepResult(String insertedText){
        this.insertedText = insertedText;
    }

    public String getInsertedText() {
        return insertedText;
    }
}
