package org.cientopolis.samplers.framework.insertTime;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.StepFragment;

import java.util.GregorianCalendar;

/**
 * Created by Xavier on 03/09/2017.
 */

public class InsertTimeFragment extends StepFragment {

    private TimePicker timePicker;

    public InsertTimeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_insert_time;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

        TextView textView = (TextView) rootView.findViewById(R.id.lb_it_text_to_show);
        textView.setText(getStep().getTextToShow());

        timePicker = (TimePicker) rootView.findViewById(R.id.tp_time_to_select);

    }

    @Override
    protected boolean validate() {

        return true; // nothing to validate
    }

    @Override
    protected InsertTimeStep getStep() {

        return (InsertTimeStep) step;
    }


    @Override
    @SuppressWarnings("deprecation")
    protected StepResult getStepResult() {
        int hour;
        int minute;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        else {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        }

        GregorianCalendar calendar =new GregorianCalendar(0,0,0,hour, minute,0);

        return new InsertTimeStepResult(getStep().getId(),calendar.getTime());
    }

}
