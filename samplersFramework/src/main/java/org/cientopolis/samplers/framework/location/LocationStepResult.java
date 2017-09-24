package org.cientopolis.samplers.framework.location;

import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStepResult extends StepResult {

    private double latitude;
    private double longitude;

    public LocationStepResult(int stepId, double latitude, double longitude) {
        super(stepId);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
