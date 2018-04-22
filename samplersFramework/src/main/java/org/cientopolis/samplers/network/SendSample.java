package org.cientopolis.samplers.network;

import android.content.Context;
import android.util.Log;

import org.cientopolis.samplers.authentication.AuthenticationManager;
import org.cientopolis.samplers.authentication.User;
import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.SampleSentEvent;
import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.persistence.DAO_Factory;
import org.cientopolis.samplers.persistence.ZipUtilities;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Xavier on 14/03/2017.
 */

class SendSample {

    private static final String MEDIA_TYPE = "application/zip";

    private OkHttpClient client = new OkHttpClient();

    public boolean sendSample(Sample sample, Context context) {
        boolean succeeded = false;

        Log.e("SendSample", "sample:" + sample.toString());

        File samplesDir = DAO_Factory.getSampleDAO(context).getSamplesDir();
        File sampleDir = DAO_Factory.getSampleDAO(context).getSampleDir(sample);
        File zipFile = null;

        if (sampleDir != null) {
            try {

                String zipFileName = "sample_"+String.valueOf(sample.getId())+".zip";
                zipFile = new File(samplesDir,zipFileName);

                Log.e("SendSample", "try to zip...");
                ZipUtilities.zipFilesInDirectory(sampleDir,zipFile.getAbsolutePath());
                Log.e("SendSample", "ziped");

                succeeded = sendFile(zipFile,context);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
            Log.e("SendSample", "sample dir NULL !!");

        if (succeeded) {
            sample.setSent();
            DAO_Factory.getSampleDAO(context).save(sample);
        }

        // Delete the zip file
        if (zipFile != null) {
            zipFile.delete();
        }


        BusProvider.getInstance().post(new SampleSentEvent(sample,succeeded));

        return succeeded;
    }

    public void onSampleSent(Sample sample) {

    }

    private boolean sendFile(File file, Context context) {
        boolean succeeded = false;

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            // add the sample param
            builder.addFormDataPart(NetworkConfiguration.getPARAM_NAME_SAMPLE(), file.getName(),
                    RequestBody.create(MediaType.parse(MEDIA_TYPE), file));

            // add the user params
            if (AuthenticationManager.isAuthenticationEnabled()) {
                User user = AuthenticationManager.getUser(context);
                if (user != null) {
                    builder.addFormDataPart(NetworkConfiguration.getPARAM_NAME_USER_ID(), user.getUserId())
                            .addFormDataPart(NetworkConfiguration.getPARAM_NAME_AUTHENTICATION_TYPE(), user.getAuthenticationType());
                    Log.e("SendFile", "USER INFO ADDED");
                }
            }


            RequestBody body = builder.build();


            Request request = new Request.Builder()
                    .url(NetworkConfiguration.getURL())
                    .post(body)
                    .build();


            Log.e("request", "request:" + request.toString());

            Response response = client.newCall(request).execute();
            String stringResponse = response.body().string();

            Log.e("SendFile", "ok:" + stringResponse);


            succeeded = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return succeeded;
    }
}
