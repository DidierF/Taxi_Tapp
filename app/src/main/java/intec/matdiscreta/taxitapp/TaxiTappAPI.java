package intec.matdiscreta.taxitapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.os.Debug;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;
import java.util.Map;

import intec.matdiscreta.taxitapp.api.CallTaxiRequest;
import intec.matdiscreta.taxitapp.api.NearbyTaxisRequest;
import intec.matdiscreta.taxitapp.api.SendRegistrationIdRequest;
import intec.matdiscreta.taxitapp.api.TaxiList;
import intec.matdiscreta.taxitapp.api.UserTaxiCall;
import intec.matdiscreta.taxitapp.session.Session;
import intec.matdiscreta.taxitapp.ux.TaxiMarkerData;

/**
 * Created by Lou on 1/19/15.
 */
public class TaxiTappAPI {
    private static TaxiTappAPI ourInstance = new TaxiTappAPI();

    private Session session = Session.FIRST_USER;
//    public static String rootUrl = "http://192.168.1.115:3000";
    public static String rootUrl = "http://10.0.0.19:3000";


    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private Map<Marker, TaxiMarkerData> taxiData = new HashMap<Marker, TaxiMarkerData>();
    private UserTaxiCall currentTaxiCall;
    private Activity context;

    public static TaxiTappAPI getInstance() {
        return ourInstance;
    }

    private TaxiTappAPI() {

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

    public boolean callTaxi(Marker marker) {
        return false;
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

                for (TaxiMarkerData taxiMarkerData : (TaxiList) o) {
                    LatLng latLng = new LatLng(taxiMarkerData.latitude, taxiMarkerData.longitude);
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(taxiMarkerData.name));
                    taxiData.put(marker, taxiMarkerData);
                }

                Log.d("RoboSpice", o.toString());
            }
        };

        spiceManager.execute(request, request.createCacheKey(), 10 * DurationInMillis.ONE_SECOND, requestListener);
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

    public void setCurrentTaxiCall(boolean successfullyAskedForCab, UserTaxiCall currentTaxiCall) {
        this.currentTaxiCall = currentTaxiCall;
        if(!successfullyAskedForCab) {
            Button tappBtn = (Button) context.findViewById(R.id.tapp_btn);
            tappBtn.setEnabled(true);
        }
    }

    public void setContext(Activity activity) {
        this.context = activity;
    }
}
