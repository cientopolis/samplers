package org.cientopolis.samplers.bus;

import org.cientopolis.samplers.framework.Sample;

/**
 * Created by Xavier on 05/03/2018.
 */

public class NewSampleSavedEvent {

    public Sample sample;

    public NewSampleSavedEvent(Sample sample) {
        this.sample = sample;
    }
}
