package intec.matdiscreta.taxitapp;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;
import java.util.Map;

import intec.matdiscreta.taxitapp.api.CallTaxiRequest;
import intec.matdiscreta.taxitapp.api.NearbyTaxisRequest;
import intec.matdiscreta.taxitapp.api.SendRegistrationIdRequest;
import intec.matdiscreta.taxitapp.api.TaxiList;
import intec.matdiscreta.taxitapp.api.UpdateLocationRequest;
import intec.matdiscreta.taxitapp.api.UserTaxiCall;
import intec.matdiscreta.taxitapp.session.Session;
import intec.matdiscreta.taxitapp.ux.TaxiMarkerData;

/**
 * Created by Lou on 1/19/15.
 */
public class TaxiTappAPI {
    public static String rootUrl = "https://taxitapp-intec.herokuapp.com";
    private static TaxiTappAPI ourInstance = new TaxiTappAPI();
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private Session session = Session.FIRST_USER;
    private Map<Marker, TaxiMarkerData> taxiData = new HashMap<Marker, TaxiMarkerData>();
    private UserTaxiCall currentTaxiCall;
    private UserActivity context;

    private TaxiTappAPI() {

    }

    public static TaxiTappAPI getInstance() {
        return ourInstance;
    }

    public void callTaxi(Location location) {
        CallTaxiRequest request = new CallTaxiRequest(session, location);

        RequestListener requestListener = new RequestListener() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("RoboSpice", e.toString());
                setCurrentTaxiCall(false, null);
            }

            @Override
            public void onRequestSuccess(Object o) {
                Log.d("RoboSpice", String.valueOf(((UserTaxiCall) o).getPending()));
                setCurrentTaxiCall(true, (UserTaxiCall) o);
            }
        };

        spiceManager.execute(request, request.createCacheKey(), DurationInMillis.ALWAYS_RETURNED, requestListener);
    }

    public void callTaxi(Location location, Marker marker) {
        TaxiMarkerData taxiMarkerData = taxiData.get(marker);
        int taxiId = taxiMarkerData.id;

        if(taxiMarkerData.available) {
            CallTaxiRequest request = new CallTaxiRequest(session, location, taxiId);

            RequestListener requestListener = new RequestListener() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.d("RoboSpice", e.toString());
                    setCurrentTaxiCall(false, null);
                }

                @Override
                public void onRequestSuccess(Object o) {
                    Log.d("RoboSpice", String.valueOf(((UserTaxiCall) o).getPending()));
                    setCurrentTaxiCall(true, (UserTaxiCall) o);
                }
            };

            spiceManager.execute(request, request.createCacheKey(), DurationInMillis.ALWAYS_RETURNED, requestListener);
        } else {
            setCurrentTaxiCall(false, null);
        }
    }

    public void getTaxis(final GoogleMap map) {
        NearbyTaxisRequest request = new NearbyTaxisRequest();


        RequestListener requestListener = new RequestListener() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("RoboSpice", e.toString());
            }

            @Override
            public void onRequestSuccess(Object o) {
                map.clear();
                taxiData.clear();

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_marker);

                for (TaxiMarkerData taxiMarkerData : (TaxiList) o) {
                    LatLng latLng = new LatLng(taxiMarkerData.latitude, taxiMarkerData.longitude);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(taxiMarkerData.name)
                            .icon(icon));
                    taxiData.put(marker, taxiMarkerData);
                }

                Log.d("RoboSpice", o.toString());
            }
        };

        spiceManager.execute(request, request.createCacheKey(), 8 * DurationInMillis.ONE_SECOND, requestListener);
    }


    public void sendRegistrationId(String regId) {
        SendRegistrationIdRequest request = new SendRegistrationIdRequest(session, regId);

        RequestListener requestListener = new RequestListener() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.d("RoboSpice", e.toString());
            }

            @Override
            public void onRequestSuccess(Object o) {
                Log.d("RoboSpice", "Successfully registered device.");
            }
        };

        spiceManager.execute(request, request.createCacheKey(), DurationInMillis.ALWAYS_RETURNED, requestListener);

    }

    public void updateCurrentLocation() {
        String contextClassName = context.getLocalClassName();
            UpdateLocationRequest request = new UpdateLocationRequest(session, context);

            RequestListener requestListener = new RequestListener() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.d("RoboSpice", e.toString());
                }

                @Override
                public void onRequestSuccess(Object o) {
                    Log.d("RoboSpice", "Successfully updated user location.");
                }
            };

            spiceManager.execute(request, request.createCacheKey(), 8 * DurationInMillis.ONE_SECOND, requestListener);
    }



    public void setCurrentTaxiCall(boolean successfullyAskedForCab, UserTaxiCall currentTaxiCall) {
        this.currentTaxiCall = currentTaxiCall;
        if(!successfullyAskedForCab) {
            Button tappBtn = (Button) context.findViewById(R.id.tapp_btn);
            tappBtn.setEnabled(true);
        }
    }

    public void setContext(UserActivity activity) {
        this.context = activity;
    }

    public Session getSession(){
        return session;
    }

}
