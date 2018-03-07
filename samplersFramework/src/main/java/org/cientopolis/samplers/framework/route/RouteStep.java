package org.cientopolis.samplers.framework.route;

import org.cientopolis.samplers.framework.base.BaseStep;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteStep extends BaseStep {

    private Integer nextStepId;
    private String textToShow;

    public RouteStep(int id, String aTextToShow, Integer nextStepId) {
        super(id);
        //stepFragmentClass = RouteFragment.class;
        textToShow = aTextToShow;
        this.nextStepId = nextStepId;
    }

    @Override
    public Integer getNextStepId() {
        return nextStepId;
    }
}
