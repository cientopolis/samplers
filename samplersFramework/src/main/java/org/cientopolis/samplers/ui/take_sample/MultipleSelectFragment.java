package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.MultipleSelectStepResult;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.model.StepResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleSelectFragment extends StepFragment {

    private static final String ARG_STEP = "param_step";


    public MultipleSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step A MultipleSelectStep wich has the options to show as checkboxes.
     * @return A new instance of fragment MultipleSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultipleSelectFragment newInstance(MultipleSelectStep step) {
        MultipleSelectFragment fragment = new MultipleSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_multiple_select, container, false);

        LinearLayout vertical_layout = (LinearLayout) rootView.findViewById(R.id.vertical_layout);


        CheckBox checkBox;

        if (vertical_layout != null) {
            for (SelectOption option : getStep().getOptionsToSelect()) {
                checkBox = new CheckBox(getActivity());
                checkBox.setText(option.getTextToShow());
                checkBox.setTextSize(20);
                checkBox.setTag(option);
                checkBox.setChecked(option.isSelected());
                checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

                vertical_layout.addView(checkBox);
            }
        }
        else
            Log.e("MultipleSelectFragment", "NULLLLLL");

        return rootView;
    }
*/

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_multiple_select;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        LinearLayout vertical_layout = (LinearLayout) rootView.findViewById(R.id.vertical_layout);

        CheckBox checkBox;

        if (vertical_layout != null) {
            for (SelectOption option : getStep().getOptionsToSelect()) {
                checkBox = new CheckBox(getActivity());
                checkBox.setText(option.getTextToShow());
                checkBox.setTextSize(20);
                checkBox.setTag(option);
                checkBox.setChecked(option.isSelected());
                checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

                vertical_layout.addView(checkBox);
            }
        }
        else
            Log.e("MultipleSelectFragment", "NULLLLLL");
    }

    private MultipleSelectStep getStep() {

        return (MultipleSelectStep) step;
    }


    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SelectOption option = (SelectOption) buttonView.getTag();
            option.setSelected(isChecked);
        }
    }



    @Override
    protected StepResult getStepResult() {
        return new MultipleSelectStepResult(getStep().getSelectedOptions());
    }


}
