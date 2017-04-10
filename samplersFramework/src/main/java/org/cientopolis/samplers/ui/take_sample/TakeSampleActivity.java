package org.cientopolis.samplers.ui.take_sample;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.*;
import org.cientopolis.samplers.persistence.DAO_Factory;


import java.util.Date;

public class TakeSampleActivity extends Activity implements StepFragmentInteractionListener {

    public static final String EXTRA_WORKFLOW = "org.cientopolis.samplers.WORKFLOW";

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

        sample = new Sample();

        getFragmentManager().addOnBackStackChangedListener(new MyOnBackStackChangedListener());

        lb_step_count = (TextView) findViewById(R.id.lb_step_count);
    }

    @Override
    protected void onStart () {
        super.onStart();

        nextStep();
        refreshStepStateOnScreen();
    }

    private void nextStep() {
        if (workflow != null) {
            if (!workflow.isEnd()) {
                Step step = workflow.nextStep();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                Fragment fragment;

                // TODO Apply a pattern
                if (InformationStep.class.isInstance(step)) {
                    fragment = StepFragment.newInstance(InformationFragment.class, step);
                } else if (PhotoStep.class.isInstance(step)) {
                    fragment = StepFragment.newInstance(PhotoFragment.class, step);
                }else if (MultipleSelectStep.class.isInstance(step)) {
                    fragment = StepFragment.newInstance(MultipleSelectFragment.class, step);
                } else if (SelectOneStep.class.isInstance(step)) {
                    fragment = StepFragment.newInstance(SelectOneFragment.class, step);
                } else if (LocationStep.class.isInstance(step)) {
                    fragment = StepFragment.newInstance(LocationFragment.class, step);
                } else
                    fragment = null;

                if (fragment != null) {

                    transaction.replace(R.id.container, fragment);
                    Log.e("TakeSampleActivity", "replaced");
                    if (workflow.getStepPosition() > 0) {
                        transaction.addToBackStack(null);
                        Log.e("TakeSampleActivity", "addToBackStack");
                    }
                    transaction.commit();
                }

            } else {
                // Finish...

                // Set end date time of the sample
                sample.setEndDateTime(new Date());

                // Save the sample localy
                DAO_Factory.getSampleDAO(getApplicationContext()).save(sample);

                Toast.makeText(this, "Coool!!", Toast.LENGTH_LONG).show();
                Log.e("TakeSample", "finish");
                this.finish();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO remove test code
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("TakeSampleActivity","KEYCODE_BACK");
            //Fragment f =  getFragmentManager().findFragmentById(R.id.container);
            /*
            if (f instanceof OnBackKeyDown) {

                ((OnBackKeyDown) f).onBackKeyDown();
                //return true;
            }*/
        }
        return super.onKeyDown(keyCode, event);
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

    private class MyOnBackStackChangedListener implements FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            Log.e("onBackStackChanged","onBackStackChanged: "+ String.valueOf(getFragmentManager().getBackStackEntryCount()));

            // +1 xq el primer fragment no se agrega al BackStack
            //proximoPaso = getSupportFragmentManager().getBackStackEntryCount()+1;


            workflow.setStepPosition(getFragmentManager().getBackStackEntryCount());

            refreshStepStateOnScreen();
        }
    }
}
