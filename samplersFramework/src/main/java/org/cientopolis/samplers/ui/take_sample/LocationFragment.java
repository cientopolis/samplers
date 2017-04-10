package org.cientopolis.samplers.ui.take_sample;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.model.LocationStep;
import org.cientopolis.samplers.model.LocationStepResult;
import org.cientopolis.samplers.model.StepResult;

/**
 * Created by Xavier on 09/04/2017.
 * A simple {@link StepFragment} subclass to complete a LocationStep and retrive a Location (current or marked on the map)
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.*
 */

public class LocationFragment extends StepFragment {

    private GoogleApiClient mGoogleApiClient;
    private GoogleApiConnectionCallbacks googleApiConnectionCallbacks;
    private Location mLocation = null;
    private TextView lb_latitude;
    private TextView lb_longitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            Context context = getActivity().getApplicationContext();
            googleApiConnectionCallbacks = new GoogleApiConnectionCallbacks();

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(googleApiConnectionCallbacks)
                    .addOnConnectionFailedListener(googleApiConnectionCallbacks)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onStart () {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop () {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_location;
    }

    @Override
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        TextView textView = (TextView) rootView.findViewById(R.id.lb_text_to_show);
        textView.setText(getStep().getTextToShow());

        lb_latitude = (TextView) rootView.findViewById(R.id.lb_latitude);
        lb_longitude = (TextView) rootView.findViewById(R.id.lb_longitude);

        Button bt_get_position = (Button) rootView.findViewById(R.id.bt_get_position);
        bt_get_position.setOnClickListener(new GetPositionClickListener());

    }

    @Override
    protected LocationStep getStep() {
        return (LocationStep) step;
    }

    @Override
    protected boolean validate() {
        boolean ok = true;

        if (mLocation == null) {
            ok = false;
            // TODO: 10/04/2017 Unify messages to show
            Toast.makeText(getActivity(), getResources().getString(R.string.error_must_take_location), Toast.LENGTH_LONG).show();
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new LocationStepResult(mLocation.getLatitude(), mLocation.getLongitude());
    }

    private class GoogleApiConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(1);

            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest ,new MyLocationListener());
            }
            catch (SecurityException e) {
                Log.e("LocationFragment", "SecurityException accesing Location");
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e("LocationFragment", "Google API Connection Failed");
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocation = location;

                lb_latitude.setText(String.valueOf(location.getLatitude()));
                lb_longitude.setText(String.valueOf(location.getLongitude()));

                Log.e("LocationFragment", "Latitude: "+String.valueOf(location.getLatitude()));
                Log.e("LocationFragment", "Longitude: "+String.valueOf(location.getLongitude()));
            }
            mGoogleApiClient.disconnect();
        }
    }

    private class GetPositionClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mGoogleApiClient.connect(); // onConnect retrives the location
        }
    }


}
