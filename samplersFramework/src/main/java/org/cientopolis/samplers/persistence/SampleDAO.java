package org.cientopolis.samplers.persistence;

import org.cientopolis.samplers.framework.Sample;

import java.io.File;
import java.util.List;


/**
 * Created by Xavier on 11/03/2017.
 */

public interface SampleDAO extends DAO<Sample, Long> {

    File getSampleDir(Sample sample);
    File getSamplesDir();

    List<Sample> getUnsentSamples();
}
