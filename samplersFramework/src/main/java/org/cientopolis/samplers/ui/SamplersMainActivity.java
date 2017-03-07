package org.cientopolis.samplers.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.ui.samples_list.SamplesListActivity;
import org.cientopolis.samplers.ui.take_sample.TakeSampleActivity;


public class SamplersMainActivity extends Activity {
    protected TextView lb_main_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lb_main_titulo = (TextView) findViewById(R.id.lb_main_titulo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // // TODO: 27/02/2017 Refactor: use case statement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.action_samples) {
            startSamplesListActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void takeSample(View view){

        startTakeSampleActivity();
    }

    protected void startTakeSampleActivity() {
        //Intent intent = new Intent(this, TakeSampleActivity.class);
        //startActivity(intent);
    }

    protected void startSamplesListActivity() {
        Intent intent = new Intent(this, SamplesListActivity.class);
        startActivity(intent);
    }

}
