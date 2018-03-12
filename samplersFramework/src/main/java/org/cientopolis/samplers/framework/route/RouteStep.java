package org.cientopolis.samplers.framework.route;

import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteStep extends BaseStep {

    private Integer nextStepId;
    private String textToShow;
    private long interval;

    public RouteStep(int id, String aTextToShow, long interval, Integer nextStepId) {
        super(id);
        stepFragmentClass = RouteFragment.class;
        textToShow = aTextToShow;
        this.nextStepId = nextStepId;
        this.interval = interval;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public long getInterval() {
        return interval;
    }
}
