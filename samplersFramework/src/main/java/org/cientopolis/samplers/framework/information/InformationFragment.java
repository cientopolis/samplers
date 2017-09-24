package org.cientopolis.samplers.framework.information;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.StepFragment;
import org.cientopolis.samplers.framework.base.StepFragmentInteractionListener;


/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class InformationFragment extends StepFragment {

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_information;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        TextView textView = (TextView) rootView.findViewById(R.id.lb_text_to_show);
        textView.setText(getStep().getTextToShow());

    }

    @Override
    protected boolean validate() {
        // No validations needed
        return true;
    }

    @Override
    protected InformationStep getStep() {

        return (InformationStep) step;
    }

    @Override
    protected StepResult getStepResult() {
        // TODO: 01/03/2017 see stepResult of InformationStep
        return null;
    }




}
