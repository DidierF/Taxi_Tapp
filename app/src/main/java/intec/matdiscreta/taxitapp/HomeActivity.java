package intec.matdiscreta.taxitapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends FragmentActivity implements MainOverlayFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates = true;
    private String mLastUpdateTime;
    private Marker mUserMarker;

    private Marker mTaxiMarker;
    //Used to test
    private Geocoder mGeoC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buildGoogleApiClient();
        setUpMapIfNeeded();

    }

    @Override
    protected void onStart() {
        super.onStart();
        TaxiTappAPI.getInstance().spiceManager.start(this);
        TaxiTappAPI.getInstance().getTaxis(mMap);

        mGeoC = new Geocoder(this);

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        TaxiTappAPI.getInstance().spiceManager.shouldStop();
//        spiceManager.shouldStop();

        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mCurrentLocation = lastLocation;
        try {
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            try {
                updateUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUI() throws IOException {
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if (mMap != null && mUserMarker != null) {
            //mUserMarker.setPosition(latLng);

        } else {
            if (mMap != null) {

                //mUserMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                //Modified
                //Moves the map camera to the users location and zooms it so the streets can be seen.
                //The camera update tells the camera where to go with the given LatLong and zooms to
                //the given level (0 = whole world, 21+ street and specific buildings.
                //CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                //mMap.animateCamera(update);
            }
        }

        ArrayList<Address> addresses = new ArrayList<Address>();
        addresses = (ArrayList<Address>) mGeoC.getFromLocation(latLng.latitude, latLng.longitude, 10);
        MainOverlayFragment overlayFragment = (MainOverlayFragment) this.getSupportFragmentManager().findFragmentById(R.id.overlay_fragment);
        ((TextView) overlayFragment.getView().findViewById(R.id.addressLabel)).setText(addresses.get(0).getAddressLine(0));
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        createLocationRequest();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                TaxiTappAPI.getInstance().callTaxi(marker);
            }
        };


    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void onFragmentInteraction(Uri uri) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



}
