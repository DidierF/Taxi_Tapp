package intec.matdiscreta.taxitapp.api;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import intec.matdiscreta.taxitapp.TaxiTappAPI;
import intec.matdiscreta.taxitapp.session.Session;

/**
 * Created by Lou on 1/24/15.
 */
public class CallTaxiRequest extends SpringAndroidSpiceRequest<UserTaxiCall> {

    Session currentSession;
    Location currentLocation;
    int taxiId;

    public CallTaxiRequest(Session session, Location location) {
        super(UserTaxiCall.class);
        this.currentSession = session;
        this.currentLocation = location;
    }

    public CallTaxiRequest(Session session, Location location, int taxiId) {
        super(UserTaxiCall.class);
        this.currentSession = session;
        this.currentLocation = location;
        this.taxiId = taxiId;
    }

    @Override
    public UserTaxiCall loadDataFromNetwork() throws Exception {
        String url = TaxiTappAPI.rootUrl + "/user_taxi_calls/";
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

        parameters.set("user_id", currentSession.id);
        parameters.set("taxi_id", taxiId);
        parameters.set("latitude", currentLocation.getLatitude());
        parameters.set("longitude", currentLocation.getLongitude());

        MultiValueMap<String, Object> parametersRoot = new LinkedMultiValueMap<String, Object>();
        parametersRoot.set("user_taxi_call", parameters);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parametersRoot, headers);

        return getRestTemplate().postForObject(url, request, UserTaxiCall.class);
    }

    public String createCacheKey() {
        return "callTaxi.test";
    }
}
