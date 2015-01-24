package intec.matdiscreta.taxitapp;

import android.os.Debug;
import android.util.Log;

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

import intec.matdiscreta.taxitapp.api.NearbyTaxisRequest;
import intec.matdiscreta.taxitapp.api.TaxiList;
import intec.matdiscreta.taxitapp.ux.TaxiMarkerData;

/**
 * Created by Lou on 1/19/15.
 */
public class TaxiTappAPI {
    private static TaxiTappAPI ourInstance = new TaxiTappAPI();

    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private Map<Marker, TaxiMarkerData> taxiData = new HashMap<Marker, TaxiMarkerData>();

    public static TaxiTappAPI getInstance() {
        return ourInstance;
    }

    private TaxiTappAPI() {

    }

    public boolean callTaxi() {
        return false;
    }

    public boolean callTaxi(Marker marker) {
        return false;
    }

    public void getTaxis(final GoogleMap map) {
        NearbyTaxisRequest request = new NearbyTaxisRequest();


        RequestListener requestListener = new RequestListener() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.d("RoboSpice", spiceException.toString());
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

}
