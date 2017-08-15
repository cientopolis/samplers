package org.cientopolis.samplers.ui.take_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.SelectOneOption;
import org.cientopolis.samplers.model.SelectOneStepResult;
import org.cientopolis.samplers.model.StepResult;
import org.cientopolis.samplers.ui.ErrorMessaging;

/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class SelectOneFragment extends StepFragment {

    private static final String KEY_SELECTONE_SELECTED_OPTION = "org.cientopolis.samplers.KEY_SELECTONE_SELECTED_OPTION";

    private SelectOneOption selectedOption;

    public SelectOneFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_select_one;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        Log.e("SelectOneFragment","onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SELECTONE_SELECTED_OPTION,selectedOption);

    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        Log.e("SelectOneFragment", "onCreateViewStepFragment");
        if (savedInstanceState != null) {
            selectedOption = (SelectOneOption) savedInstanceState.getSerializable(KEY_SELECTONE_SELECTED_OPTION);
            Log.e("SelectOneFragment", "savedInstanceState");
        }

        // title
        TextView lb_select_one_title = (TextView) rootView.findViewById(R.id.lb_select_one_title);
        lb_select_one_title.setText(getStep().getTitle());

        // The radio group that will contain the radio buttons with the options to select
        RadioGroup radio_group = (RadioGroup) rootView.findViewById(R.id.radio_group);

        RadioButton radioButton;
        MyOnCheckedChangeListener myOnCheckedChangeListener = new MyOnCheckedChangeListener();

        if (radio_group != null) {
            // create a radio button for each option to show/select
            for (SelectOneOption option : getStep().getOptionsToSelect()) {
                radioButton = new RadioButton(getActivity());
                radioButton.setId(option.getId());
                radioButton.setText(option.getTextToShow());
                // // TODO: 02/03/2017 Parametrize style
                radioButton.setTextSize(20);
                radioButton.setTag(option);
                radioButton.setOnCheckedChangeListener(myOnCheckedChangeListener);

                radio_group.addView(radioButton);
            }

            if (selectedOption != null)
                radio_group.check(selectedOption.getId());

        }
        else
            Log.e("SelectOneFragment", "RadioGroup NULL");
    }


    @Override
    protected SelectOneStep getStep() {
        return (SelectOneStep) step;
    }


    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.e("MyOnCheckedChangeListener", "onCheckedChanged");
            if (isChecked) {
                selectedOption = (SelectOneOption) buttonView.getTag();
                Log.e("MyOnCheckedChangeListener", "isChecked:"+selectedOption.getTextToShow());
            }
        }
    }

    @Override
    protected boolean validate() {
        boolean ok = true;
        
        if (selectedOption == null) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_select_an_option));
        }
        
        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        Log.e("SelectOneFragment", "getStepResult:"+selectedOption.getTextToShow());
        Log.e("SelectOneFragment", "NextStepID:"+String.valueOf(selectedOption.getNextStepId()));
        return new SelectOneStepResult(getStep().getId(),selectedOption);
    }




}
