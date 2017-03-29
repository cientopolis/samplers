package org.cientopolis.samplers.ui.samples_list;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.Sample;
import org.cientopolis.samplers.network.SendSample;
import org.cientopolis.samplers.persistence.DAO_Factory;
import org.cientopolis.samplers.persistence.SampleDAO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SamplesListFragment extends Fragment implements SamplesListAdapter.SamplesListAdapterListener{

    private List<Sample> samples;
    private SamplesListAdapter adapter;

    public SamplesListFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (samples == null) {
            samples = DAO_Factory.getSampleDAO(getActivity().getApplicationContext()).list();
            Log.e("SamplesListFragment", "samples: "+String.valueOf(samples.size()));
        }

        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_samples_list, container, false);

        adapter = new SamplesListAdapter(getActivity(),samples, this);
        ListView list_samples = (ListView) rootView.findViewById(R.id.list_samples);
        list_samples.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onUploadSampleClick(Sample sample) {

        SendSample.sendSample(sample, getActivity().getApplicationContext());

    }

    @Override
    public void onDeleteSampleClick(Sample mySample) {

        DAO_Factory.getSampleDAO(getActivity().getApplicationContext()).delete(mySample);
        samples.remove(mySample);
        adapter.notifyDataSetChanged();

    }
}
