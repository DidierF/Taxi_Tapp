package intec.matdiscreta.taxitapp.api;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

import intec.matdiscreta.taxitapp.TaxiTappAPI;
import intec.matdiscreta.taxitapp.UserActivity;
import intec.matdiscreta.taxitapp.session.Session;

/**
 * Created by Lou on 1/30/15.
 */
public class UpdateLocationRequest extends SpringAndroidSpiceRequest<EmptyResponse> {

    Session currentSession;
    UserActivity userActivity;


    public UpdateLocationRequest(Session session, UserActivity userActivity) {
        super(EmptyResponse.class);
        this.currentSession = session;
        this.userActivity = userActivity;
    }

    @Override
    public EmptyResponse loadDataFromNetwork() throws Exception {
        String url = TaxiTappAPI.rootUrl + "/users/" + currentSession.id;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

        Location location = userActivity.getCurrentLocation();

        parameters.set("latitude", userActivity.getCurrentLocation().getLatitude());
        parameters.set("longitude", userActivity.getCurrentLocation().getLongitude());

        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, Object> queryParams = new HashMap<String, Object>();

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, headers);

        getRestTemplate().put(url, request);
        return null;
    }

    public String createCacheKey() {
        return "updateLocation.test";
    }

}
