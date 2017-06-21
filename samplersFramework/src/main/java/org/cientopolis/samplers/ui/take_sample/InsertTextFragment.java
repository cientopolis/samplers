package org.cientopolis.samplers.ui.take_sample;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.InsertTextStep;
import org.cientopolis.samplers.model.StepResult;

/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class InsertTextFragment extends StepFragment {

    public InsertTextFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_insert_text;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        TextView textView = (TextView) rootView.findViewById(R.id.lb_it_text_to_show);
        textView.setText(getStep().getTextToShow());

        EditText editText = (EditText) rootView.findViewById(R.id.ed_it_text_to_enter);
        // Set de sample text
        editText.setHint(getStep().getSampleText());

        if (getStep().getMaxLength() > 0){
            // Adds an input filter to set the max length
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(getStep().getMaxLength())});
        }

        editText.setInputType(getAndroidInputType(getStep().getInputType()));

    }

    private int getAndroidInputType(InsertTextStep.InputType inputType) {
        // Maps [InsertTextStep.InputType] to [android.text.InputType]

        int result = android.text.InputType.TYPE_CLASS_TEXT;

        switch (inputType) {
            case TYPE_TEXT: result = android.text.InputType.TYPE_CLASS_TEXT; break;
            case TYPE_NUMBER: result = android.text.InputType.TYPE_CLASS_NUMBER; break;
            case TYPE_DATE: result = android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_DATE; break;
            case TYPE_TIME: result = android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_TIME; break;
        }

        return result;
    }

    @Override
    protected boolean validate() {
        // TODO: Complete validate
        return true;
    }

    @Override
    protected InsertTextStep getStep() {

        return (InsertTextStep) step;
    }

    @Override
    protected StepResult getStepResult() {
        // TODO: complete getStepResult
        return null;
    }
}
