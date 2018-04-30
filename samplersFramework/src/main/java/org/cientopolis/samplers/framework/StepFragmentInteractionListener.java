package org.cientopolis.samplers.framework;


/**
 * Created by Xavier on 01/03/2017.
 * The interaction listener of the {@link StepFragment}
 * Activities that contain subclasses of {@link StepFragment} must implement this interface
 */

public interface StepFragmentInteractionListener {
    /**
     * Called when the step is finished
     * @param stepResult The result collected
     */
    void onStepFinished(StepResult stepResult);
}
