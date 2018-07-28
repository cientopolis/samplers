package org.cientopolis.samplers.framework.insertText;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by Xavier on 23/06/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link InsertTextStep}
 */

public class InsertTextStepResult extends BaseStepResult {

    /**
     * The inserted text.
     */
    private String insertedText;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param insertedText The inserted text.
     */
    public InsertTextStepResult(int stepId, String insertedText){
        super(stepId);
        this.insertedText = insertedText;
    }

    /**
     * Returns the inserted text.
     *
     * @return The inserted text.
     */
    public String getInsertedText() {
        return insertedText;
    }
}
