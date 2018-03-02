package org.cientopolis.samplers.ui.samples_list;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.squareup.otto.Subscribe;
import org.cientopolis.samplers.R;
import org.cientopolis.samplers.bus.BusProvider;
import org.cientopolis.samplers.bus.SampleSentEvent;
import org.cientopolis.samplers.framework.Sample;
import org.cientopolis.samplers.network.SendSamplesService;
import org.cientopolis.samplers.persistence.DAO_Factory;
import org.cientopolis.samplers.ui.ErrorMessaging;

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
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register to the bus to receive messages
        BusProvider.getInstance().register(this);
    }

    public void onDestroy () {
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onSampleSent(SampleSentEvent sampleSentEvent) {
        if (sampleSentEvent.succed)
            ErrorMessaging.showInfoMessage(getActivity().getApplicationContext(), getResources().getString(R.string.send_sample_success_message));

        else
            ErrorMessaging.showErrorMessage(getActivity().getApplicationContext(), getResources().getString(R.string.send_sample_error_message));

        adapter.notifyDataSetChanged();
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

        Intent intent = new Intent(getActivity(), SendSamplesService.class);
        intent.putExtra(SendSamplesService.EXTRA_SAMPLE,sample);
        getActivity().startService(intent);
    }

    @Override
    public void onDeleteSampleClick(Sample mySample) {
        String message =  getResources().getString(R.string.delete_sample_confirmation_message);
        String yesString = getResources().getString(R.string.yesString);
        String noString = getResources().getString(R.string.noString);

        // Ask for confirmation before deleting
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setPositiveButton(yesString, new ConfirmationYesOnClickListener(mySample))
                .setNegativeButton(noString, null).show();


    }

    private class ConfirmationYesOnClickListener implements DialogInterface.OnClickListener {

        private Sample sample;

        public ConfirmationYesOnClickListener(Sample sample) {
            this.sample = sample;
        }


        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                DAO_Factory.getSampleDAO(getActivity().getApplicationContext()).delete(this.sample);
                samples.remove(this.sample);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
