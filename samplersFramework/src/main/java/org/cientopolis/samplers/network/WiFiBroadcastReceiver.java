package org.cientopolis.samplers.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Xavier on 18/02/2018.
 */

public class WiFiBroadcastReceiver extends BroadcastReceiver {

    public WiFiBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.
            Log.e("WIFI", "Conected");

            Intent serviceIntent = new Intent(context, SamplesShipmentService.class);
            context.startService(serviceIntent);
        } else {
            // wifi connection was lost
            Log.e("WIFI", "disconected");
        }
    }
}
