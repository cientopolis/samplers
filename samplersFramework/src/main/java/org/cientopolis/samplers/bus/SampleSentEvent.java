package org.cientopolis.samplers.bus;

import org.cientopolis.samplers.framework.Sample;

/**
 * Created by Xavier on 21/02/2018.
 */

public class SampleSentEvent {

    public Sample sample;
    public boolean succed;

    public SampleSentEvent(Sample sample, boolean succed) {

        this.sample = sample;
        this.succed = succed;
    }

}
