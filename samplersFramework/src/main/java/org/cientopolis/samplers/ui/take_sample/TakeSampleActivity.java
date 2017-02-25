package org.cientopolis.samplers.ui.take_sample;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.modelo.InformationStep;
import org.cientopolis.samplers.modelo.SelectOption;
import org.cientopolis.samplers.modelo.MultipleSelectStep;
import org.cientopolis.samplers.modelo.PhotoStep;
import org.cientopolis.samplers.modelo.Sample;
import org.cientopolis.samplers.modelo.SelectOneStep;
import org.cientopolis.samplers.modelo.SelectOneStepResult;
import org.cientopolis.samplers.modelo.Step;
import org.cientopolis.samplers.modelo.Workflow;
import org.cientopolis.samplers.persistence.DAO_Factory;


import java.util.ArrayList;
import java.util.Date;

public class TakeSampleActivity extends Activity implements InformationFragment.OnInformationFragmentInteractionListener,
                                                                     PhotoCameraFragment.OnPhotoCameraFragmentInteractionListener,
                                                                     MultipleSelectFragment.OnFragmentInteractionListener,
                                                                     SelectOneFragment.OnOneOptionSelectedListener {


    private TextView lb_nro_paso;
    protected Workflow workflow;
    protected Sample sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_sample);

        sample = new Sample();

        getFragmentManager().addOnBackStackChangedListener(new MyOnBackStackChangedListener());

        lb_nro_paso = (TextView) findViewById(R.id.lb_nro_paso);
        Gson gson = new Gson();

        for (Sample aSample : DAO_Factory.getSampleDAO(getApplicationContext()).list()) {

            Toast.makeText(this, gson.toJson(aSample), Toast.LENGTH_LONG).show();
        }
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
                    InformationStep informationStep = (InformationStep) step;
                    fragment = InformationFragment.newInstance(informationStep.getTextToShow());
                } else if (PhotoStep.class.isInstance(step)) {
                    PhotoStep photoStep = (PhotoStep) step;
                    fragment = PhotoCameraFragment.newInstance(photoStep.getInstructionsToShow(), photoStep.getPathToImageToOverlay());
                } else if (MultipleSelectStep.class.isInstance(step)) {
                    MultipleSelectStep multipleSelectStep = (MultipleSelectStep) step;
                    fragment = MultipleSelectFragment.newInstance(multipleSelectStep.getOptionsToSelect());
                } else if (SelectOneStep.class.isInstance(step)) {
                    SelectOneStep selectOneStep = (SelectOneStep) step;
                    fragment = SelectOneFragment.newInstance(selectOneStep.getOptionsToSelect());
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

    @Override
    public void  onPhotoCameraFragmentInteraction(Uri uri) {
        Log.e("TakeSampleActivity", "onPhotoCameraFragmentInteraction");
        nextStep();
    }

    @Override
    public void onInformationReaded() {
        Log.e("TakeSampleActivity", "onInformationReaded");
        nextStep();
    }

    private void refreshStepStateOnScreen() {
        lb_nro_paso.setText(String.valueOf(workflow.getStepPosition()+1) + "/" + String.valueOf(workflow.getStepCount()));
    }


    @Override
    public void onOptionsSelected(ArrayList<SelectOption> aOptionsToShow) {
        for (SelectOption option : aOptionsToShow) {
            Log.e("onOptionsSelected", option.getTextToShow() +":" + String.valueOf(option.isSelected()));
        }

        nextStep();
    }

    @Override
    public void onOneOptionSelected(SelectOneStepResult selectOneStepResult) {
        // add the step result to the sample
        sample.addStepResult(selectOneStepResult);

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
