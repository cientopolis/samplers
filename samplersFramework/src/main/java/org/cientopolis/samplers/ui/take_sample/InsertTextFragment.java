package org.cientopolis.samplers.ui.take_sample;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.InsertTextStep;
import org.cientopolis.samplers.model.InsertTextStepResult;
import org.cientopolis.samplers.model.StepResult;
import org.cientopolis.samplers.ui.ErrorMessaging;

/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class InsertTextFragment extends StepFragment {

    private EditText ed_it_text_to_enter;

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

        ed_it_text_to_enter = (EditText) rootView.findViewById(R.id.ed_it_text_to_enter);
        // Set the sample text
        ed_it_text_to_enter.setHint(getStep().getSampleText());

        if (getStep().getMaxLength() > 0){
            // Adds an input filter to set the max length
            ed_it_text_to_enter.setFilters(new InputFilter[] {new InputFilter.LengthFilter(getStep().getMaxLength())});
        }

        // Set the input type
        ed_it_text_to_enter.setInputType(getAndroidInputType(getStep().getInputType()));

    }

    private int getAndroidInputType(InsertTextStep.InputType inputType) {
        // it Maps [InsertTextStep.InputType] to [android.text.InputType]

        int result = android.text.InputType.TYPE_CLASS_TEXT;

        switch (inputType) {
            case TYPE_TEXT: result = android.text.InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE; break;
            case TYPE_NUMBER: result = android.text.InputType.TYPE_CLASS_NUMBER; break;
            case TYPE_DECIMAL: result = android.text.InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL; break;
        }

        return result;
    }

    @Override
    protected boolean validate() {
        boolean ok = true;

        if (getStep().isOptional() && (ed_it_text_to_enter.getText().length() == 0)) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_insert_text));
        }

        return ok;
    }

    @Override
    protected InsertTextStep getStep() {

        return (InsertTextStep) step;
    }

    @Override
    protected StepResult getStepResult() {
        return new InsertTextStepResult(getStep().getId(),ed_it_text_to_enter.getText().toString());
    }
}
