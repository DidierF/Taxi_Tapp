package intec.matdiscreta.taxitapp.api;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.web.client.RestTemplate;

import intec.matdiscreta.taxitapp.TaxiTappAPI;

/**
 * Created by Lou on 1/20/15.
 */
public class NearbyTaxisRequest extends SpringAndroidSpiceRequest<TaxiList> {
    private double latitude;
    private double longitude;
    private boolean omwToMode;

    public NearbyTaxisRequest(boolean omwToMode) {
        super(TaxiList.class);
        this.omwToMode = omwToMode;
    }

    public NearbyTaxisRequest(double latitude, double longitude) {
        super(TaxiList.class);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NearbyTaxisRequest(LatLng latLng) {
        super(TaxiList.class);
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    @Override
    public TaxiList loadDataFromNetwork() throws Exception {
//        String specificResource = omwToMode ? String.valueOf(TaxiTappAPI.getInstance().getSession().id) : "";
//        Log.d("RoboSpiceCancel", "Check requesting..." + String.valueOf(specificResource));
        String url = TaxiTappAPI.rootUrl + "/taxis/";
        Log.d("RoboSpiceCancel", url);

        TaxiList taxiList= getRestTemplate().getForObject(url, TaxiList.class);

        return taxiList;
    }

    public String createCacheKey() {
        return "nearbyTaxis.test";
    }
}
