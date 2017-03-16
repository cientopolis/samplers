package org.cientopolis.samplers.persistence;

import android.content.Context;

import org.cientopolis.samplers.model.Sample;

/**
 * Created by Xavier on 09/02/2017.
 */

public abstract class DAO_Factory {

    public static SampleDAO getSampleDAO(Context context) {
        return new SampleDAOImpl(context);
    }
}
