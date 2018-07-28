package org.cientopolis.samplers.framework;

import java.io.Serializable;

/**
 * Created by Xavier on 07/02/2017.
 * The result of each {@link Step}.
 * Interface, there is a diferent StepResult subclass for the diferents {@link Step}.
 * See the diferent subclasses for details.
 */
public interface StepResult extends Serializable {


    /**
     * Returns the id of the {@link Step} that generated the {@link StepResult}.
     *
     * @return The id of the {@link Step} that generated the {@link StepResult}.
     */
    int getStepId();


    /**
     * Returns whether the {@link StepResult} has a multimedia file (like a photo or a sound) or not.
     *
     * @return true if the {@link StepResult} has a multimedia file, false otherwise.
     */
     boolean hasMultimediaFile();

    /**
     * Returns the filename of the multimedia file (like a photo or a sound) of the {@link StepResult}.
     *
     * @return the filename of the multimedia file.
     */
    String getMultimediaFileName();
}
