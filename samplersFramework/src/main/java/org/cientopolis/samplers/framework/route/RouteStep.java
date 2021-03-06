package org.cientopolis.samplers.framework.route;

import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteStep extends BaseStep {

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
    public Integer getNextStepId() {
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
