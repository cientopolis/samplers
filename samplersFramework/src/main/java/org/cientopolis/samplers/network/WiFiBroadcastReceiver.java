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

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                Log.e("WIFI", "Conected");
            } else {
                // wifi connection was lost
                Log.e("WIFI", "disconected");
            }
        }

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.
            Log.e("WIFI 2", "Conected");
        } else {
            // wifi connection was lost
            Log.e("WIFI 2", "disconected");
        }
    }
}
