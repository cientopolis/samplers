package org.cientopolis.samplers.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cientopolis.samplers.framework.Sample;

/**
 * Created by Xavier on 18/02/2018.
 * A Service to send a {@link Sample}. It receives a {@link Sample} as an extra param and sends it using the {@link SendSample} class.
 * It was conceived as a service because the need of sending samples in the background.
 */

public class SendSamplesService extends IntentService {

    public static final String EXTRA_SAMPLE = "org.cientopolis.samplers.SendSamplesService.EXTRA_SAMPLE";

    public SendSamplesService() {
        super("SendSamplesService");
    }

    @Override
    public void onCreate(){
        Log.e("SendSamplesService", "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        Log.e("SendSamplesService", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e("SendSamplesService", "onHandleIntent");
        if (intent != null) {
            Sample sample = (Sample) intent.getSerializableExtra(EXTRA_SAMPLE);

            SendSample sendSample = new SendSample();
            sendSample.sendSample(sample, getApplicationContext());
        }
    }
}
