package org.cientopolis.samplers.model;

import java.util.Date;

/**
 * Created by Xavier on 01/09/2017.
 */

public class InsertDateStepResult extends StepResult {

    private Date selected_date;

    public InsertDateStepResult(int stepId, Date selected_date) {
        super(stepId);
        this.selected_date = selected_date;
    }

    public Date getSelected_date() {
        return selected_date;
    }
}
