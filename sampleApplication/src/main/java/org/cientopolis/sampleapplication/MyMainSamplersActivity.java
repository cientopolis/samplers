package org.cientopolis.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import org.cientopolis.samplers.ui.SamplersMainActivity;

/**
 * Created by Xavier on 02/02/2017.
 */

public class MyMainSamplersActivity  extends SamplersMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lb_main_titulo.setText("Bienvenido a la Aplicacion de Prueba!");
    }

    @Override
    protected void startTakeSampleActivity(){
        Intent intent = new Intent(this, MyTakeSampleActivity.class);
        startActivity(intent);
    }
}
