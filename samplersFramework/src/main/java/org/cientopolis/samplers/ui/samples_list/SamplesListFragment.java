package org.cientopolis.samplers.ui.samples_list;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.Sample;
import org.cientopolis.samplers.persistence.DAO_Factory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SamplesListFragment extends Fragment {

    private List<Sample> samples;

    public SamplesListFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (samples == null)
            samples = DAO_Factory.getSampleDAO(getActivity().getApplicationContext()).list();

        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_samples_list, container, false);

        SamplesListAdapter adapter = new SamplesListAdapter(getActivity(),samples);
        ListView list_samples = (ListView) rootView.findViewById(R.id.list_samples);
        list_samples.setAdapter(adapter);

        return rootView;
    }

}
