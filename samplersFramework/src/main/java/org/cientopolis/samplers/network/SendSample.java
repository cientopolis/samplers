package org.cientopolis.samplers.network;

import android.content.Context;
import android.util.Log;

import org.cientopolis.samplers.model.Sample;
import org.cientopolis.samplers.persistence.DAO_Factory;
import org.cientopolis.samplers.persistence.ZipUtilities;

import java.io.File;
import java.io.IOException;

/**
 * Created by Xavier on 14/03/2017.
 */

public class SendSample {

    // TODO: 14/03/2017 refactor class, use strategy and singleton patterns

    public static void sendSample(Sample sample, Context context) {

        File samplesDir = DAO_Factory.getSampleDAO(context).getSamplesDir();
        File sampleDir = DAO_Factory.getSampleDAO(context).getSampleDir(sample);
        if (sampleDir != null) {
            try {
                File zipFile = new File(samplesDir,"sample.zip");

                Log.e("onSampleClick", "try to zip...");
                ZipUtilities.zipFilesInDirectory(sampleDir,zipFile.getAbsolutePath());
                Log.e("onSampleClick", "ziped");

                new SendFileAsyncTask(context).execute(zipFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
            Log.e("onSampleClick", "sample dir NULL !!");

        Log.e("onSampleClick", "sample:" + sample.toString());
    }
}
