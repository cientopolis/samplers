package org.cientopolis.samplers.framework.location;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.ui.ErrorMessaging;
import org.cientopolis.samplers.framework.base.StepFragment;
import org.cientopolis.samplers.framework.base.StepFragmentInteractionListener;

/**
 * Created by Xavier on 09/04/2017.
 * A simple {@link StepFragment} subclass to complete a LocationStep and retrive a Location (current or marked on the map)
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.*
 */

public class LocationFragment extends StepFragment {

    private static final String KEY_LOCATION = "org.cientopolis.samplers.LOCATION";
    private static final int REQUEST_LOCATION_PERMISSION = 10;

    private GoogleApiClient mGoogleApiClient;
    private GoogleApiConnectionCallbacks googleApiConnectionCallbacks;
    private Location mLocation = null;
    private TextView lb_latitude;
    private TextView lb_longitude;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Marker mMarker;


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

        if (savedInstanceState != null) {
            mLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

    }

    @Override
    public void onStart () {
        //mGoogleApiClient.connect();
        super.onStart();
        if (mMapView != null)
            mMapView.onStart();
    }

    @Override
    public void onStop () {
        //mGoogleApiClient.disconnect();
        super.onStop();
        if (mMapView != null)
            mMapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
}

    @Override
    public void onSaveInstanceState (Bundle outState) {
        // TODO check if this method is called
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LOCATION,mLocation);
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory () {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
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

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        //mMapView.onResume(); // needed to get the map to display immediately
        // dont needed, its called on onResume() of the fragment

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new MapReadyCallbacks());

        if (mLocation != null)
            updateLocationOnScreen();
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
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_take_location));
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new LocationStepResult(getStep().getId(),mLocation.getLatitude(), mLocation.getLongitude());
    }


    private void updateLocationOnScreen(){
        lb_latitude.setText(String.valueOf(mLocation.getLatitude()));
        lb_longitude.setText(String.valueOf(mLocation.getLongitude()));
    }


    private void requestLocation() {
        mGoogleApiClient.connect(); // onConnect retrives the location

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            //  result on onRequestPermissionsResult()

        } else { //we already have permission, instantiate the fragment

            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                // Send a message to the user that we need permissions to access the location to get the gps position
                ErrorMessaging.showValidationErrorMessage(getActivity().getApplicationContext(),getResources().getString(R.string.location_permissions_needed));
            }
        }
    }

    private class GetPositionClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Need to request permission at run time
                requestLocationPermission();
            }
            else {
                requestLocation();
            }
        }
    }

    private class GoogleApiConnectionCallbacks implements ConnectionCallbacks, OnConnectionFailedListener {

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

                updateLocationOnScreen();

                Log.e("MyLocationListener", "Latitude: "+String.valueOf(location.getLatitude()));
                Log.e("MyLocationListener", "Longitude: "+String.valueOf(location.getLongitude()));

                addMarker(location);
                /*
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMarker == null) {
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                            //.title("Marker Titlezzzz")
                            //.snippet("Marker Descriptionzzz")
                            .draggable(true));

                }
                else {
                    mMarker.setPosition(latLng);
                }

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
            mGoogleApiClient.disconnect();
        }
    }

    private void addMarker(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarker == null) {
            mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                    //.title("Marker Titlezzzz")
                    //.snippet("Marker Descriptionzzz")
                    .draggable(true));

        }
        else {
            mMarker.setPosition(latLng);
        }

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private class MapReadyCallbacks  implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mGoogleMap = googleMap;
            mGoogleMap.setOnMarkerDragListener(new MarkerDragListener());

            if (mLocation != null)
                addMarker(mLocation);

        }
    }

    private class MarkerDragListener implements GoogleMap.OnMarkerDragListener {

        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mLocation = new Location(LocationManager.PASSIVE_PROVIDER);
            mLocation.setLatitude(marker.getPosition().latitude);
            mLocation.setLongitude(marker.getPosition().longitude);

            updateLocationOnScreen();

            Log.e("MarkerDragListener", "Latitude: "+String.valueOf(mLocation.getLatitude()));
            Log.e("MarkerDragListener", "Longitude: "+String.valueOf(mLocation.getLongitude()));
        }
    }
}
