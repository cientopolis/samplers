package org.cientopolis.samplers.persistence;

import org.cientopolis.samplers.framework.Sample;

import java.io.File;

/**
 * Created by Xavier on 11/03/2017.
 */

public interface SampleDAO extends DAO<Sample, Long> {

    public File getSampleDir(Sample sample);
    public File getSamplesDir();
}
