package org.cientopolis.sampleapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Laura on 27/02/2017.
 */

public class SendFile extends AsyncTask<File,Void,Void> {

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected Void doInBackground(File... params) {

        MediaType JSON = MediaType.parse("application/pdf; charset=utf-8");
        String url = "http://192.168.0.107/service/upload.php";

        try {
            for (File file: params) {

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("fileToUpload", file.getName(),
                                RequestBody.create(MediaType.parse("text/csv"), file))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Log.e("request", "request:" + request.toString());

                Response response = client.newCall(request).execute();
                String respuesta = response.body().string();

                Log.e("SendFile", "ok:" + respuesta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
