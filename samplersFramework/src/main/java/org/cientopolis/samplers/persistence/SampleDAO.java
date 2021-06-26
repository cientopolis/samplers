package org.cientopolis.samplers.persistence;

import org.cientopolis.samplers.framework.Sample;

import java.io.File;
import java.util.List;

//import androidx.room.Dao;
//import androidx.room.Query;

/**
 * Created by Xavier on 11/03/2017.
 */

//@Dao
public interface SampleDAO extends DAO<Sample, Long> {

    File getSampleDir(Sample sample);
    File getSamplesDir();

    List<Sample> getUnsentSamples();

    //@Override
    //@Query("select * from sample where id = :key")
    //Sample find(Long key);
}
