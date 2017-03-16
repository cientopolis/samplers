package org.cientopolis.sampleapplication;

import android.content.Intent;
import android.os.Bundle;

import org.cientopolis.samplers.network.NetworkConfiguration;
import org.cientopolis.samplers.ui.SamplersMainActivity;

/**
 * Created by Xavier on 02/02/2017.
 */

public class MyMainSamplersActivity  extends SamplersMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the network configuration
        NetworkConfiguration.setURL("http://192.168.1.10/samplers/upload.php");
        NetworkConfiguration.setPARAM_NAME("sample");

        lb_main_titulo.setText("Bienvenido a la Aplicacion de Prueba!");
    }

    @Override
    protected void startTakeSampleActivity(){
        Intent intent = new Intent(this, MyTakeSampleActivity.class);
        startActivity(intent);
    }
}
