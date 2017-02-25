package org.cientopolis.samplers.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Xavier on 07/02/2017.
 */

public class Sample {

    private Long id;
    private List<StepResult> stepResults;
    private Date startDateTime;
    private Date endDateTime;

    public Sample(){
        startDateTime = new Date();
        stepResults = new ArrayList<StepResult>();
    }

    public void addStepResult(StepResult aStepResult) {
        stepResults.add(aStepResult);
    }

    public List<StepResult> getStepResults() {
        return stepResults;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
