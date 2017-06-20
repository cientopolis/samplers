package org.cientopolis.samplers.ui.take_sample;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.*;
import org.cientopolis.samplers.persistence.DAO_Factory;
import org.cientopolis.samplers.ui.ErrorMessaging;


import java.util.Date;

public class TakeSampleActivity extends Activity implements StepFragmentInteractionListener {

    public static final String EXTRA_WORKFLOW = "org.cientopolis.samplers.WORKFLOW";

    private static final String KEY_SAMPLE = "org.cientopolis.samplers.SAMPLE";

    private TextView lb_step_count;
    protected Workflow workflow;
    protected Sample sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        // Get the Intent that started this activity and extract the workflow passed as parameter
        Intent intent = getIntent();
        workflow = (Workflow) intent.getSerializableExtra(EXTRA_WORKFLOW);

        // if not passed as parameter, then create a new empty workflow
        if (workflow == null)
            workflow = new Workflow();


        getFragmentManager().addOnBackStackChangedListener(new MyOnBackStackChangedListener());

        lb_step_count = (TextView) findViewById(R.id.lb_step_count);

        if (savedInstanceState == null) { // First execution
            sample = new Sample();
            nextStep();
        }

        //refreshStepStateOnScreen();
    }

    @Override
    protected void onStart () {
        super.onStart();
        Log.e("TakeSampleActivity", "onStart");
    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.e("TakeSampleActivity", "onStop");
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SAMPLE, sample);

        Log.e("TakeSampleActivity", "onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            sample = (Sample) savedInstanceState.getSerializable(KEY_SAMPLE);

        refreshStepStateOnScreen();

        Log.e("TakeSampleActivity", "onRestoreInstanceState");
    }

    private void nextStep() {
        if (workflow != null) {
            if (!workflow.isEnd()) {
                Step step = workflow.nextStep();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                StepFragment fragment = StepFragment.newInstance(step.getStepFragmentClass(), step);

                if (fragment != null) {

                    transaction.replace(R.id.container, fragment);
                    Log.e("TakeSampleActivity", "replaced");
                    if (workflow.getStepPosition() > 0) {
                        transaction.addToBackStack(null);
                        Log.e("TakeSampleActivity", "addToBackStack");
                    }
                    transaction.commit();
                }

                refreshStepStateOnScreen();

            } else {
                // Finish...

                // Set end date time of the sample
                sample.setEndDateTime(new Date());

                // Save the sample localy
                DAO_Factory.getSampleDAO(getApplicationContext()).save(sample);

                ErrorMessaging.showInfoMessage(this, getResources().getString(R.string.message_sample_saved));
                Log.e("TakeSample", "finish");
                this.finish();
            }
        }

    }

    private void previuosStep() {
        workflow.previuosStep();
        refreshStepStateOnScreen();
    }


    private void refreshStepStateOnScreen() {
        lb_step_count.setText(String.valueOf(workflow.getStepPosition()+1) + "/" + String.valueOf(workflow.getStepCount()));
    }


    @Override
    public void onStepFinished(StepResult stepResult) {
        sample.addStepResult(stepResult);

        // go to the next step
        nextStep();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("TakeSampleActivity","KEYCODE_BACK");

            previuosStep();
        }
        return super.onKeyDown(keyCode, event);
    }


    private class MyOnBackStackChangedListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            Log.e("onBackStackChanged","onBackStackChanged: "+ String.valueOf(getFragmentManager().getBackStackEntryCount()));

            // +1 xq el primer fragment no se agrega al BackStack
            //proximoPaso = getSupportFragmentManager().getBackStackEntryCount()+1;


            //workflow.setStepPosition(getFragmentManager().getBackStackEntryCount());

            //refreshStepStateOnScreen();
        }
    }
}
