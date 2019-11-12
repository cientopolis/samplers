package org.cientopolis.samplers.framework;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.multipleSelect.MultipleSelectFragment;

/**
 * Created by Xavier on 02/03/2017.
 * Represents the view of a {@link Step}.
 * Given a {@link Step} it's the UI to generate the correspondent {@link StepResult}
 *
 * Activities that contain subclasses of this fragment must implement the
 * {@link StepFragmentInteractionListener} interface to handle interaction events.
 *
 * Use the {@link MultipleSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class StepFragment extends Fragment {

    private static final String ARG_STEP = "org.cientopolis.samplers.StepFragment.PARAM_STEP";

    // The given Step, passed as a parameter
    protected Step step;
    // The listener to interact when the Step is finished
    protected StepFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of subclasses of this fragment
     * using the provided parameters.
     *
     * @param type The subclass of StepFragment to instantiate.
     * @param step A subclass of Step
     * @return A new instance of the subclass of StepFragment provided.
     */
    public static <T extends StepFragment> T newInstance(Class<T> type, Step step) {
        T fragment;
        try {
            fragment = type.newInstance();
            Bundle args = new Bundle();
            args.putSerializable(ARG_STEP, step);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while instantiating a new StepFragment");
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.step = (Step) getArguments().getSerializable(ARG_STEP);
        }
    }

    private void myOnAttach(Context context) {
        /*
        * ATTENTION:
        * this method is called twice on API 23-25 because both onAttach(Activity) and onAttach(Context) are executed
        * Don't put creation code here (e.g. new SomeClass())
        */

        if (context instanceof StepFragmentInteractionListener) {
            mListener = (StepFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepFragmentInteractionListener");
        }
    }

    @SuppressWarnings("deprecation") // This method is needed when running on API Levels < 23
    @Override
    public void onAttach(Activity activity) {
        Log.e("StepFragment", "onAttach(Activity)");
        super.onAttach(activity);
        myOnAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        Log.e("StepFragment", "onAttach(Context)");
        super.onAttach(context);
        myOnAttach(context);
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

        // Set the "Next Button" onClick listener
        Button bt_next = (Button) rootView.findViewById(R.id.bt_next);
        if (bt_next != null) {
            // Set the onClick listener
            bt_next.setOnClickListener(new NextClickListener());
            // Set the caption
            bt_next.setText(getResources().getString(R.string.bt_next));
            // Set the image icon
            bt_next.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_36dp, 0);
        }
        else
            throw new RuntimeException("Layout must include a Button with id: bt_next");

        // Call onCreateView callback of the subclass
        onCreateViewStepFragment(rootView, savedInstanceState);

        // return the inflated layout
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
     * @param savedInstanceState The savedInstanceState provided in the {@link Fragment#onCreateView}  method.
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


    /**
     * OnClick listener of the Next Button
     */
    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // check for missing listener
            if (mListener != null) {
                // Validate if the step is complete. It's a subclass task
                if (validate())
                    mListener.onStepFinished(getStepResult());
            }
            else {
                throw new RuntimeException("StepFragmentInteractionListener is NULL. You probably overrode the onAttach() method without calling super.onAttach().");
            }
        }
    }




}