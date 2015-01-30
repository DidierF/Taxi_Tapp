package intec.matdiscreta.taxitapp.api;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import intec.matdiscreta.taxitapp.TaxiTappAPI;
import intec.matdiscreta.taxitapp.session.Session;

/**
 * Created by Lou on 1/29/15.
 */
public class SendRegistrationIdRequest extends SpringAndroidSpiceRequest<EmptyResponse> {

    private Session currentSession;
    private String regId;

    public SendRegistrationIdRequest(Session session, String regId) {
        super(EmptyResponse.class);
        this.currentSession = session;
        this.regId = regId;
    }

    @Override
    public EmptyResponse loadDataFromNetwork() throws Exception {
        String url = TaxiTappAPI.rootUrl + "/users/" + currentSession.id;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

        parameters.set("id", currentSession.id);
        parameters.set("registration_id", regId);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, Object> queryParams = new HashMap<String, Object>();

        queryParams.put("id", Integer.valueOf(currentSession.id));
        queryParams.put("registration_id", regId);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, headers);

//        return getRestTemplate().postForObject(url, request, EmptyResponse.class);
        getRestTemplate().put(url, request);
        return null;
    }

    public String createCacheKey() {
        return "sendRegId.test";
    }
}
