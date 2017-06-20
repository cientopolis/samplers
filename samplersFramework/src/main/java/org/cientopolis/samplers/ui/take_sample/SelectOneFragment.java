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
import org.cientopolis.samplers.model.SelectOption;
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


    public SelectOneFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_select_one;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        // title
        TextView lb_select_one_title = (TextView) rootView.findViewById(R.id.lb_select_one_title);
        lb_select_one_title.setText(getStep().getTitle());

        // The radio group that will contain the radio buttons with the options to select
        RadioGroup radio_group = (RadioGroup) rootView.findViewById(R.id.radio_group);

        RadioButton radioButton;

        if (radio_group != null) {
            // create a radio button for each option to show/select
            for (SelectOption option : getStep().getOptionsToSelect()) {
                radioButton = new RadioButton(getActivity());
                radioButton.setText(option.getTextToShow());
                // // TODO: 02/03/2017 Parametrize style
                radioButton.setTextSize(20);
                radioButton.setTag(option);
                radioButton.setChecked(option.isSelected());
                radioButton.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

                radio_group.addView(radioButton);
            }
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
            SelectOption option = (SelectOption) buttonView.getTag();
            option.setSelected(isChecked);
        }
    }

    @Override
    protected boolean validate() {
        boolean ok = true;
        
        if (getStep().getSelectedOption() == null) {
            ok = false;
            // TODO: 02/03/2017 Unify messages to show
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_select_an_option));
        }
        
        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new SelectOneStepResult(getStep().getSelectedOption());
    }




}
