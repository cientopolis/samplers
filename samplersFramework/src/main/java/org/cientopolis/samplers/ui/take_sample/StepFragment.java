package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.MultipleSelectStepResult;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.model.Step;
import org.cientopolis.samplers.model.StepResult;

/**
 * Created by Xavier on 02/03/2017.
 */

public abstract class StepFragment extends Fragment {

    private static final String ARG_STEP = "param_step";

    protected Step step;
    protected StepFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("StepFragment", "entra onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.step = (Step) getArguments().getSerializable(ARG_STEP);
        }
    }

    @Override
    public void onAttach(Activity context) {
        Log.e("MultipleSelectFragment", "entra onAttach");
        super.onAttach(context);
        if (context instanceof StepFragmentInteractionListener) {
            mListener = (StepFragmentInteractionListener) context;
            Log.e("MultipleSelectFragment", "mListener asignado");
        } else {
            Log.e("MultipleSelectFragment", "mListener NO asignado");
            throw new RuntimeException(context.toString()
                    + " must implement StepFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(getLayoutResource(), container, false);

        Button bt_next = (Button) rootView.findViewById(R.id.bt_next);
        if (bt_next != null)
            bt_next.setOnClickListener(new NextClickListener());
        //else
        //    throw new Exception("Layout must include a Button with id: bt_next");

        onCreateViewStepFragment(rootView, savedInstanceState);

        return rootView;

    }

    protected abstract int getLayoutResource();
    protected abstract void onCreateViewStepFragment(View rootView, Bundle savedInstanceState);

    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                Log.e("NextClickListener", "OMG! mListener NULL !!!");
            }
            else {
                mListener.onStepFinished(getStepResult());
            }
        }
    }

    protected abstract StepResult getStepResult();


}
