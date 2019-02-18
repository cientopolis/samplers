package org.cientopolis.samplers.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.SampleSentEvent;
import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.persistence.DAO_Factory;


import java.util.List;

/**
 * Created by Xavier on 06/03/2018.
 * The service responsible for checking if there are samples to send, and send them to the server
 * calling to the {@link SendSamplesService} service.
 * It's called when a sample is saved by {@link org.cientopolis.samplers.ui.TakeSampleActivity},
 * and also when wi-fi connection is detected by {@link WiFiBroadcastReceiver}
 */
public class SamplesShipmentService extends Service {

    public SamplesShipmentService() {
        Log.e("SamplesShipmentService", "created");
        // Register to the bus to receive messages
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        checkSamplesToSend();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void checkSamplesToSend(){
        Log.e("SamplesShipmentService", "checking samples to send");
        if (isWiFiConnected()) {
            List<Sample> samplesToSend = DAO_Factory.getSampleDAO(getApplicationContext()).getUnsentSamples();
            if (!samplesToSend.isEmpty()) {
                Sample sample = samplesToSend.get(0);

                Intent intent = new Intent(this, SendSamplesService.class);
                intent.putExtra(SendSamplesService.EXTRA_SAMPLE,sample);
                this.startService(intent);
            }
            else {
                stopSelf();
            }
        }
        else {
            stopSelf();
        }
    }

    private boolean isWiFiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    @Subscribe
    public void onSampleSent(SampleSentEvent sampleSentEvent) {
        if (sampleSentEvent.succed) {
            // Sample sent, continue with the next one if there is one
            checkSamplesToSend();
        }
        else {
            // Cant send a sample, quit
            stopSelf();
        }

    }
}
