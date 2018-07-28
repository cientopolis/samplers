package org.cientopolis.samplers.framework.location;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by Xavier on 09/04/2017.
 * Subclass of {@link StepResult}.
 * The result of each {@link LocationStep}
 */

public class LocationStepResult extends BaseStepResult {

    /**
     * The latitude of the position collected.
     */
    private double latitude;
    /**
     * The longitude of the position collected.
     */
    private double longitude;

    /**
     * Constructor.
     *
     * @param stepId The id of the {@link Step} that generated the StepResult.
     * @param latitude The latitude of the position collected.
     * @param longitude The longitude of the position collected.
     */
    public LocationStepResult(int stepId, double latitude, double longitude) {
        super(stepId);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude of the position collected.
     *
     * @return The latitude of the position collected.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of the position collected.
     *
     * @return The longitude of the position collected.
     */
    public double getLongitude() {
        return longitude;
    }
}
