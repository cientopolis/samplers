package org.cientopolis.samplers.ui.take_sample;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.InformationStep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    private static final String ARG_STEP = "texto";

    private InformationStep step;
    private StepFragmentInteractionListener mListener;

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step a InformationStep with the Text to show.
     * @return A new instance of fragment InformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(InformationStep step) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.step = (InformationStep) getArguments().getSerializable(ARG_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.lb_text_to_show);
        textView.setText(step.getTextToShow());

        Button bt_siguiente = (Button) rootView.findViewById(R.id.bt_next);
        bt_siguiente.setOnClickListener(new NextClickListener());

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof StepFragmentInteractionListener) {
            mListener = (StepFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement StepFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class NextClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                Log.e("NextClickListener", "mListener NULL !!!");
            }
            else {
                // // TODO: 01/03/2017 see stepResult of InformationStep
                mListener.onStepFinished(null);
            }
        }
    }


}
