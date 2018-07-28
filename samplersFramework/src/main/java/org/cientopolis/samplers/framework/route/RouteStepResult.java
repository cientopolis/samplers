package org.cientopolis.samplers.framework.route;

import android.location.Location;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

import java.util.List;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteStepResult extends BaseStepResult {


    private List<Location> route;

    public RouteStepResult(int stepId, List<Location> route) {
        super(stepId);
        this.route = route;
    }

    public List<Location> getRoute() {
        return route;
    }
}
