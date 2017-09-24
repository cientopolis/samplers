package org.cientopolis.samplers.framework.insertText;

import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by Xavier on 23/06/2017.
 */

public class InsertTextStepResult extends StepResult {

    private String insertedText;

    public InsertTextStepResult(int stepId, String insertedText){
        super(stepId);
        this.insertedText = insertedText;
    }

    public String getInsertedText() {
        return insertedText;
    }
}
