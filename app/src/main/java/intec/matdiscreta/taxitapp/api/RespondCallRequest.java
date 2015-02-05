package intec.matdiscreta.taxitapp.api;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import intec.matdiscreta.taxitapp.TaxiTappAPI;
import intec.matdiscreta.taxitapp.session.Session;

/**
 * Created by Lou on 2/4/15.
 */
public class RespondCallRequest extends SpringAndroidSpiceRequest<EmptyResponse> {

    private int callId;
    private boolean response;
    private Session currentSession;

    public RespondCallRequest(Session session, int callId, boolean response) {
        super(EmptyResponse.class);
        this.callId = callId;
        this.response = response;
        this.currentSession = session;
    }

    @Override
    public EmptyResponse loadDataFromNetwork() throws Exception {
        String url = TaxiTappAPI.rootUrl + "/user_taxi_calls/" + callId;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

        parameters.set("taxi_id", currentSession.id);
        parameters.set("accepted", response);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, headers);

        getRestTemplate().put(url, request);
        return null;
    }

    public String createCacheKey() {
        return "respondCall.test";
    }

}
