package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.Step;
import org.cientopolis.samplers.model.StepResult;

/**
 * Created by Xavier on 02/03/2017.
 * A simple {@link Fragment} subclass.
 *
 * Activities that contain subclasses of this fragment must implement the
 * {@link StepFragmentInteractionListener} interface to handle interaction events.
 *
 * Use the {@link MultipleSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class StepFragment extends Fragment {

    private static final String ARG_STEP = "param_step";

    protected Step step;
    protected StepFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of subclasses of this fragment
     * using the provided parameters.
     *
     * @param type The subclass of StepFragment to instantiate.
     * @param step A subclass of Step
     * @return A new instance of the subclass of StepFragment provided.
     */
    public static <T extends StepFragment> StepFragment newInstance(Class<T> type, Step step) {
        StepFragment fragment = null;
        try {
            fragment = type.newInstance();
            Bundle args = new Bundle();
            args.putSerializable(ARG_STEP, step);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("StepFragment", "entra onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.step = (Step) getArguments().getSerializable(ARG_STEP);
        }
    }

    @Override
    public void onAttach(Activity context) {
        // TODO: 04/03/2017 analyze, sometimes this method is not called, depending on the api version, its called the onAttach(Context) instead
        Log.e("MultipleSelectFragment", "entra onAttach");
        super.onAttach(context);
        if (context instanceof StepFragmentInteractionListener) {
            mListener = (StepFragmentInteractionListener) context;
            Log.e("MultipleSelectFragment", "mListener asignado");
        } else {
            Log.e("MultipleSelectFragment", "mListener NO asignado");
            throw new RuntimeException(context.toString()
                    + " must implement StepFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(getLayoutResource(), container, false);

        Button bt_next = (Button) rootView.findViewById(R.id.bt_next);
        if (bt_next != null)
            bt_next.setOnClickListener(new NextClickListener());

        // TODO: 04/03/2017 Analize if it must throw an exception or what
        //else
        //    throw new Exception("Layout must include a Button with id: bt_next");

        onCreateViewStepFragment(rootView, savedInstanceState);

        return rootView;

    }

    /**
     * @return the layout resource of the fragment
     * e.g. return R.layout.fragment_multiple_select;
     */
    protected abstract int getLayoutResource();


    /**
     * This method is called internally in {@link StepFragment#onCreateView}
     * after the inflate of the layout
     * Use this method to interact with the recently created layout
     *
     * @param rootView The rootView provided in the {@link Fragment#onCreateView}  method.
     * @param savedInstanceState The rootView provided in the {@link Fragment#onCreateView}  method.
     */
    protected abstract void onCreateViewStepFragment(View rootView, Bundle savedInstanceState);


    /**
     * Getter of the Step to access it casted
     * @return the casted Step
     * e.g. return (MultipleSelectStep) step;
     */
    protected abstract <T extends Step> T getStep();


    /**
     * This method is called internally in {@link NextClickListener#onClick}
     * before finish the step fragment
     * Use it to validate the user imput
     * @return true if the validation is ok, false otherwise
     */
    protected abstract boolean validate();

    /**
     * This method is called internally in {@link NextClickListener#onClick}
     * after {@link StepFragment#validate} te get the result to pass to the {@link StepFragment#mListener}
     * @return a instance of a subclass of StepResult
     */
    protected abstract StepResult getStepResult();



    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if (validate())
                    mListener.onStepFinished(getStepResult());
            }
            else {
                Log.e("NextClickListener", "OMG! mListener NULL !!!");
            }
        }
    }




}
