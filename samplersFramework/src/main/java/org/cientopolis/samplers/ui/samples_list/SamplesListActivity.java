package org.cientopolis.samplers.ui.samples_list;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.cientopolis.samplers.R;

public class SamplesListActivity extends Activity {

    private static final String FRAMENT_TAG = "org.cientopolis.samplers.TAG_SamplesListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples_list);

        if (savedInstanceState == null) {
            SamplesListFragment fragment = new SamplesListFragment();

            getFragmentManager().beginTransaction()
                    .add(R.id.activity_samples_list, fragment, FRAMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_samples_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete_sent_samples) {
            deleteSentSamples();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteSentSamples() {
        SamplesListFragment fragment = (SamplesListFragment) getFragmentManager().findFragmentByTag(FRAMENT_TAG);
        fragment.deleteSentSamples();
    }
}
