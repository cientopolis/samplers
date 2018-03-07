package org.cientopolis.samplers.framework.route;

import android.os.Bundle;
import android.view.View;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.StepFragment;

/**
 * Created by Xavier on 07/03/2018.
 */

public class RouteFragment extends StepFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_route;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {

    }

    @Override
    protected RouteStep getStep() {
        return (RouteStep) step;
    }

    @Override
    protected boolean validate() {
        return true;
    }

    @Override
    protected StepResult getStepResult() {
        return new RouteStepResult(getStep().getId());
    }
}
