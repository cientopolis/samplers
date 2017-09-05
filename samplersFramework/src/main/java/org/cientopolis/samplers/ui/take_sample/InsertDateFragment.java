package org.cientopolis.samplers.ui.take_sample;

/**
 * Created by Xavier on 01/09/2017.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.InsertDateStep;
import org.cientopolis.samplers.model.InsertDateStepResult;
import org.cientopolis.samplers.model.StepResult;

import java.util.GregorianCalendar;


/**
 * A simple {@link StepFragment} subclass.
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */
public class InsertDateFragment extends StepFragment {

    private DatePicker datePicker;

    public InsertDateFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_insert_date;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        TextView textView = (TextView) rootView.findViewById(R.id.lb_it_text_to_show);
        textView.setText(getStep().getTextToShow());

        datePicker = (DatePicker) rootView.findViewById(R.id.dp_date_to_select);

    }

    @Override
    protected boolean validate() {

        return true; // nothing to validate
    }

    @Override
    protected InsertDateStep getStep() {

        return (InsertDateStep) step;
    }

    @Override
    protected StepResult getStepResult() {
        GregorianCalendar calendar =new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),datePicker.getDayOfMonth());

        return new InsertDateStepResult(getStep().getId(),calendar.getTime());
    }


}
