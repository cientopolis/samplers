package org.cientopolis.samplers.framework.route;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.cientopolis.samplers.R;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.StepFragment;
import org.cientopolis.samplers.framework.StepFragmentInteractionListener;
import org.cientopolis.samplers.ui.ErrorMessaging;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Xavier on 07/03/2018.
 * A simple {@link StepFragment} subclass to complete a RouteStep and retrive a List<Location>
 * Activities that contain this fragment must implement the {@link StepFragmentInteractionListener}
 * interface to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to create an instance of this fragment.
 */

public class RouteFragment extends StepFragment {

    private static final String KEY_ROUTE = "org.cientopolis.samplers.ROUTE";
    private static final String KEY_STATE = "org.cientopolis.samplers.RouteFragment_State";

    private static final int REQUEST_LOCATION_PERMISSION = 10;

    private static final int START_REQUESTING_LOCATION_UPDATES_RESOURSE_ID = R.drawable.ic_my_location_black_36dp;
    private static final int STOP_REQUESTING_LOCATION_UPDATES_RESOURSE_ID = R.drawable.ic_stop_black_36dp;

    private ImageButton bt_get_position;

    private GoogleApiClient mGoogleApiClient;
    private MyLocationListener myLocationListener;

    private boolean receivingLocationUpdates;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private List<Location> mRoute;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            Context context = getActivity().getApplicationContext();
            GoogleApiConnectionCallbacks googleApiConnectionCallbacks = new GoogleApiConnectionCallbacks();

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(googleApiConnectionCallbacks)
                    .addOnConnectionFailedListener(googleApiConnectionCallbacks)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (savedInstanceState != null) {
            mRoute = (ArrayList<Location>) savedInstanceState.getSerializable(KEY_ROUTE);
            receivingLocationUpdates = savedInstanceState.getBoolean(KEY_STATE);

        }
        else {
            mRoute = new ArrayList<>();
            receivingLocationUpdates = false;
        }

        if (receivingLocationUpdates)
            requestLocations();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_route;
    }

    @Override
    public void onStart () {
        super.onStart();
        if (mMapView != null)
            mMapView.onStart();
    }

    @Override
    public void onStop () {
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

        if (receivingLocationUpdates) {
            stopRequestingLocations();
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ROUTE, (ArrayList<Location>) mRoute);
        outState.putBoolean(KEY_STATE, receivingLocationUpdates);

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
    protected void onCreateViewStepFragment(View rootView, Bundle savedInstanceState) {
        TextView textView = (TextView) rootView.findViewById(R.id.lb_text_to_show);
        textView.setText(getStep().getTextToShow());

        bt_get_position = (ImageButton) rootView.findViewById(R.id.bt_get_position);
        if (receivingLocationUpdates)
            bt_get_position.setImageResource(STOP_REQUESTING_LOCATION_UPDATES_RESOURSE_ID);
        else
            bt_get_position.setImageResource(START_REQUESTING_LOCATION_UPDATES_RESOURSE_ID);
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

    }

    @Override
    protected RouteStep getStep() {
        return (RouteStep) step;
    }

    @Override
    protected boolean validate() {
        boolean ok = true;

        if (mRoute.isEmpty()) {
            ok = false;
            ErrorMessaging.showValidationErrorMessage(getActivity(), getResources().getString(R.string.error_must_take_route));
        }

        return ok;
    }

    @Override
    protected StepResult getStepResult() {
        return new RouteStepResult(getStep().getId(), mRoute);
    }


    private void startRecordingRoute() {
        // Clear posible old route
        mRoute.clear();
        mGoogleMap.clear();

        requestLocations();

        receivingLocationUpdates = true;

        if (bt_get_position != null)
            bt_get_position.setImageResource(STOP_REQUESTING_LOCATION_UPDATES_RESOURSE_ID);


    }

    private void requestLocations() {

        mGoogleApiClient.connect(); // onConnect retrives the location

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            //  result on onRequestPermissionsResult()

        } else { //we already have permission, start requesting locations updates

            startRecordingRoute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecordingRoute();
            } else {
                // Send a message to the user that we need permissions to access the location to get the gps position
                ErrorMessaging.showValidationErrorMessage(getActivity().getApplicationContext(),getResources().getString(R.string.location_permissions_needed_route));
            }
        }
    }

    private void addMarker(Location location){

        if (mGoogleMap != null) {
            // clear the map and repaint all the route
            mGoogleMap.clear();
            Polyline mPolyline = mGoogleMap.addPolyline(new PolylineOptions().clickable(false));

            LatLng latLng;
            Marker mMarker;
            List<LatLng> puntos = new ArrayList<>();

            for (Location routeLocation: mRoute) {
                latLng = new LatLng(routeLocation.getLatitude(), routeLocation.getLongitude());

                mMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                        //.title("Marker Titlezzzz")
                        //.snippet("Marker Descriptionzzz")
                        .draggable(false));

                puntos.add(mMarker.getPosition());
            }

            // Polyline to represent the route
            mPolyline.setPoints(puntos);

            // Zoom to the position received
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(getStep().getMapZoom()).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private class MapReadyCallbacks  implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mGoogleMap = googleMap;

            if (!mRoute.isEmpty()) {
                // Add the last position to repaint the route
                addMarker(mRoute.get(mRoute.size()-1));
            }

        }
    }

    private class GetPositionClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (!receivingLocationUpdates) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Need to request permission at run time
                    requestLocationPermission();
                } else {
                    startRecordingRoute();
                }
            }
            else {
                stopRecordingRoute();
            }
        }
    }

    private void stopRecordingRoute() {

        Log.e("RouteFragment","stopRecordingRoute");

        stopRequestingLocations();

        receivingLocationUpdates = false;

        bt_get_position.setImageResource(START_REQUESTING_LOCATION_UPDATES_RESOURSE_ID);
    }

    private void stopRequestingLocations() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, myLocationListener);
        mGoogleApiClient.disconnect();
    }


    private class GoogleApiConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(getStep().getInterval());

            myLocationListener = new MyLocationListener();
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest , myLocationListener);
            }
            catch (SecurityException e) {
                Log.e("RouteFragment", "SecurityException accesing Location");
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e("RouteFragment", "Google API Connection Failed");
        }
        
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //mLocation = location;

                //updateLocationOnScreen();

                Log.e("MyLocationListener", "Latitude: "+String.valueOf(location.getLatitude()));
                Log.e("MyLocationListener", "Longitude: "+String.valueOf(location.getLongitude()));

                mRoute.add(location);
                Log.e("MyLocationListener", "Cant Locations: "+String.valueOf(mRoute.size()));
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
            //mGoogleApiClient.disconnect();
        }
    }


}
