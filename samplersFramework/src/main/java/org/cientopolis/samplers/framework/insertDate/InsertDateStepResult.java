package org.cientopolis.samplers.framework.insertDate;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

import java.util.Date;

/**
 * Created by Xavier on 01/09/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link InsertDateStep}
 */

public class InsertDateStepResult extends BaseStepResult {

    /**
     * The selected date.
     */
    private Date selected_date;


    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param selected_date The selected date.
     */
    public InsertDateStepResult(int stepId, Date selected_date) {
        super(stepId);
        this.selected_date = selected_date;
    }

    /**
     * Returns the selected date.
     *
     * @return The selected date.
     */
    public Date getSelected_date() {
        return selected_date;
    }
}
