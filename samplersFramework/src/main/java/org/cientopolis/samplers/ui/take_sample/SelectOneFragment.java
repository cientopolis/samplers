package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.SelectOneStep;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.model.SelectOneStepResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectOneFragment extends Fragment {

    private static final String ARG_STEP = "param_options";


    private SelectOneStep step;
    private StepFragmentInteractionListener mListener;

    public SelectOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step A SelectOneStep wich has the list of options to show as checkboxes.
     * @return A new instance of fragment MultipleSelectFragment.
     */
    public static SelectOneFragment newInstance(SelectOneStep step) {
        SelectOneFragment fragment = new SelectOneFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.step = (SelectOneStep) getArguments().getSerializable(ARG_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_one, container, false);

        // The radio group that will contain the radio buttons with the options to select
        RadioGroup radio_group = (RadioGroup) rootView.findViewById(R.id.radio_group);

        RadioButton radioButton;

        if (radio_group != null) {
            // create a radio button for each option to show/select
            for (SelectOption option : step.getOptionsToSelect()) {
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

        Button bt_next = (Button) rootView.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new NextClickListener());

        return rootView;
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SelectOption option = (SelectOption) buttonView.getTag();
            option.setSelected(isChecked);
        }
    }


    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                Log.e("NextClickListener", "mListener NULL !!!");
            }
            else {
                if (validate()) {
                    SelectOneStepResult selectOneStepResult = new SelectOneStepResult(step.getSelectedOption());

                    mListener.onStepFinished(selectOneStepResult);
                }

            }
        }
    }

    private boolean validate() {
        boolean ok = true;
        
        if (step.getSelectedOption() == null) {
            ok = false;
            // TODO: 02/03/2017 Unify messages to show
            Toast.makeText(getActivity(), getResources().getString(R.string.error_must_select_an_option), Toast.LENGTH_LONG).show();
        }
        
        return ok;
    }

    @Override
    public void onAttach(Activity context) {
        Log.e("SelectOneFragment", "entra onAttach");
        super.onAttach(context);
        if (context instanceof StepFragmentInteractionListener) {
            mListener = (StepFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
