package org.cientopolis.samplers.ui.samples_list;

import android.app.Activity;
import android.os.Bundle;
import org.cientopolis.samplers.R;

public class SamplesListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples_list);

        if (savedInstanceState == null) {
            SamplesListFragment fragment = new SamplesListFragment();

            getFragmentManager().beginTransaction()
                    .add(R.id.activity_samples_list, fragment).commit();
        }
    }
}
