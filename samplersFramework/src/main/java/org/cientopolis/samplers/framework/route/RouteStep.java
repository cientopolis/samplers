package org.cientopolis.samplers.framework.route;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteStep extends BaseStep {

    private static final String STEP_RESOURCE_NAME = "step_route";

    private Integer nextStepId;
    private String textToShow;
    private long interval;
    private int mapZoom;

    public RouteStep(int id, String aTextToShow, Integer nextStepId) {
        super(id);
        stepFragmentClass = RouteFragment.class;
        textToShow = aTextToShow;
        this.nextStepId = nextStepId;
        this.interval = 5000;
        this.mapZoom = 15;
    }

    @Override
    public String getStepResourceName() {
        return STEP_RESOURCE_NAME;
    }

    @Override
    public Integer getNextStepId(StepResult stepResult) {
        return nextStepId;
    }

    public String getTextToShow() {
        return textToShow;
    }

    public long getInterval() {
        return interval;
    }

    public int getMapZoom() {
        return mapZoom;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setMapZoom(int mapZoom) {
        this.mapZoom = mapZoom;
    }
}
