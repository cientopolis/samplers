package org.cientopolis.samplers.persistence;

import android.content.Context;

import org.cientopolis.samplers.modelo.Sample;

/**
 * Created by Xavier on 09/02/2017.
 */

public abstract class DAO_Factory {

    public static DAO<Sample, Long> getSampleDAO(Context context) {
        return new SampleDAOImpl(context);
    }
}
