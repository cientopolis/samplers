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

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.modelo.SelectOneStep;
import org.cientopolis.samplers.modelo.SelectOption;
import org.cientopolis.samplers.modelo.SelectOneStepResult;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnOneOptionSelectedListener} interface
 * to handle interaction events.
 * Use the {@link SelectOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectOneFragment extends Fragment {

    private static final String ARG_PARAM1 = "param_options";


    private SelectOneStep step;
    private OnOneOptionSelectedListener mListener;

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
        args.putSerializable(ARG_PARAM1, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.step = (SelectOneStep) getArguments().getSerializable(ARG_PARAM1);
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

                    mListener.onOneOptionSelected(selectOneStepResult);
                }

            }
        }
    }

    private boolean validate() {
        // TODO validate that the user select one option
        return true;
    }

    @Override
    public void onAttach(Activity context) {
        Log.e("SelectOneFragment", "entra onAttach");
        super.onAttach(context);
        if (context instanceof OnOneOptionSelectedListener) {
            mListener = (OnOneOptionSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOneOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnOneOptionSelectedListener {
        void onOneOptionSelected(SelectOneStepResult selectOneStepResult);
    }
}
