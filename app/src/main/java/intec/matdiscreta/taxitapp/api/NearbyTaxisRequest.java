package intec.matdiscreta.taxitapp.api;

import com.google.android.gms.maps.model.LatLng;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Created by Lou on 1/20/15.
 */
public class NearbyTaxisRequest extends SpringAndroidSpiceRequest<TaxiList> {
    private double latitude;
    private double longitude;

    public NearbyTaxisRequest() {
        super(TaxiList.class);
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
        String url = "http://10.0.0.15:3000/taxis/";

        return getRestTemplate().getForObject(url, TaxiList.class);
    }

    public String createCacheKey() {
        return "nearbyTaxis.test";
    }
}