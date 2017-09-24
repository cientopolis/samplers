package org.cientopolis.samplers.framework.insertTime;

import org.cientopolis.samplers.framework.StepResult;

import java.util.Date;

/**
 * Created by Xavier on 03/09/2017.
 */

public class InsertTimeStepResult extends StepResult {

    private Date selected_time;

    public InsertTimeStepResult(int stepId, Date selected_time) {
        super(stepId);
        this.selected_time = selected_time;
    }

    public Date getSelected_date() {
        return selected_time;
    }
}