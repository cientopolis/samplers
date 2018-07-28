package org.cientopolis.samplers.framework.insertTime;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

import java.util.Date;

/**
 * Created by Xavier on 03/09/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link InsertTimeStep}
 */

public class InsertTimeStepResult extends BaseStepResult {

    /**
     * The selected time.
     */
    private Date selected_time;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param selected_time The selected time.
     */
    public InsertTimeStepResult(int stepId, Date selected_time) {
        super(stepId);
        this.selected_time = selected_time;
    }

    /**
     * Returns the selected time.
     *
     * @return The selected time.
     */
    public Date getSelected_date() {
        return selected_time;
    }
}