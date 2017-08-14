package org.cientopolis.samplers.ui.take_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.MultipleSelectOption;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.MultipleSelectStepResult;
import org.cientopolis.samplers.model.StepResult;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.util.ArrayList;

/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class MultipleSelectFragment extends StepFragment {

    private static final String KEY_MULTIPLE_SELECT_FRAGMENT_SELECTED_OPTIONS = "org.cientopolis.samplers.MULTIPLE_SELECT_FRAGMENT_SELECTED_OPTIONS";

    private ArrayList<MultipleSelectOption> selectedOptions;

    public MultipleSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedOptions = (ArrayList<MultipleSelectOption>) savedInstanceState.getSerializable(KEY_MULTIPLE_SELECT_FRAGMENT_SELECTED_OPTIONS);
        }
        else {
            selectedOptions = new ArrayList<>();
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_MULTIPLE_SELECT_FRAGMENT_SELECTED_OPTIONS,selectedOptions);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_multiple_select;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        // title
        TextView lb_multiple_select_title = (TextView) rootView.findViewById(R.id.lb_multiple_select_title);
        lb_multiple_select_title.setText(getStep().getTitle());

        // layout for the checkboxes
        LinearLayout vertical_layout = (LinearLayout) rootView.findViewById(R.id.vertical_layout);

        CheckBox checkBox;
        // Checkboxes
        if (vertical_layout != null) {
            for (MultipleSelectOption option : getStep().getOptionsToSelect()) {
                checkBox = new CheckBox(getActivity());
                checkBox.setText(option.getTextToShow());
                checkBox.setTextSize(20);
                checkBox.setTag(option);
                if (selectedOptions.contains(option))
                    checkBox.setChecked(true);
                checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

                vertical_layout.addView(checkBox);
            }
        }
        else
            Log.e("MultipleSelectFragment", "NULLLLLL");
    }

    @Override
    protected MultipleSelectStep getStep() {

        return (MultipleSelectStep) step;
    }


    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MultipleSelectOption option = (MultipleSelectOption) buttonView.getTag();
            if (isChecked) {
                // don't duplicate
                if (!selectedOptions.contains(option))
                    selectedOptions.add(option);
            }
            else
                selectedOptions.remove(option);

        }
    }


    @Override
    protected boolean validate() {
        boolean ok = true;

        if (selectedOptions.size() == 0) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_select_an_option_at_least));
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new MultipleSelectStepResult(getStep().getId(), selectedOptions);
    }


}
