package org.cientopolis.samplers.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class SendFileAsyncTask extends AsyncTask<File,Void,Void> {


    private Context context;
    private static final String MEDIA_TYPE = "application/zip";


    private OkHttpClient client = new OkHttpClient();

    public SendFileAsyncTask(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(File... params) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // TODO: 14/03/2017 use listeners to show finish status
        Toast.makeText(context, "Mestra enviada!!", Toast.LENGTH_LONG).show();
    }
}
