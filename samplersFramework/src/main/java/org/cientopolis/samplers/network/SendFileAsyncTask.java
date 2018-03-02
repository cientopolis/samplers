package org.cientopolis.samplers.network;

import android.os.AsyncTask;
import android.util.Log;

import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.SampleSentEvent;
import org.cientopolis.samplers.framework.Sample;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Xavier on 08/03/2017.
 */

public class SendFileAsyncTask extends AsyncTask<File,Void,Boolean> {


    private static final String MEDIA_TYPE = "application/zip";

    private Sample sample;

    private OkHttpClient client = new OkHttpClient();

    public SendFileAsyncTask(Sample sample) {
        this.sample = sample;
    }


    @Override
    protected Boolean doInBackground(File... params) {

        Boolean succeeded = false;

        try {
            for (File file: params) {

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(NetworkConfiguration.getPARAM_NAME(), file.getName(),
                                RequestBody.create(MediaType.parse(MEDIA_TYPE), file))
                        .build();

                Request request = new Request.Builder()
                        .url(NetworkConfiguration.getURL())
                        .post(body)
                        .build();

                Log.e("request", "request:" + request.toString());

                Response response = client.newCall(request).execute();
                String stringResponse = response.body().string();

                Log.e("SendFile", "ok:" + stringResponse);
            }

            succeeded = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return succeeded;
    }

    @Override
    protected void onPostExecute(Boolean succeeded) {
        if (succeeded) {
            Log.e("SendFileAsyncTask", "Mestra enviada!!");

        }
        else
            Log.e("SendFileAsyncTask","Error al envier la muestra!!");

        BusProvider.getInstance().post(new SampleSentEvent(sample,succeeded));
    }
}
