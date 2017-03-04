package org.cientopolis.samplers.ui.take_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.MultipleSelectStep;
import org.cientopolis.samplers.model.MultipleSelectStepResult;
import org.cientopolis.samplers.model.SelectOption;
import org.cientopolis.samplers.model.StepResult;

/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class MultipleSelectFragment extends StepFragment {

    public MultipleSelectFragment() {
        // Required empty public constructor
    }


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

    @Override
    protected MultipleSelectStep getStep() {

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
    protected boolean validate() {
        boolean ok = true;

        if (getStep().getSelectedOptions().size() == 0) {
            ok = false;
            // TODO: 02/03/2017 Unify messages to show
            Toast.makeText(getActivity(), getResources().getString(R.string.error_must_select_an_option_at_least), Toast.LENGTH_LONG).show();
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new MultipleSelectStepResult(getStep().getSelectedOptions());
    }


}
