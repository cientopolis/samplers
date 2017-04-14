package org.cientopolis.samplers.model;

/**
 * Created by Xavier on 09/04/2017.
 */

public class LocationStepResult extends StepResult {

    private double latitude;
    private double longitude;

    public LocationStepResult(double latitude, double longitude) {
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
