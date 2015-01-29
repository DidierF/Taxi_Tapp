package intec.matdiscreta.taxitapp.api;

/**
 * Created by Lou on 1/24/15.
 */
public class UserTaxiCall {
    private static final long serialVersionUID = 8192333539004718470L;
    private int id, user_id, taxi_id;
    private boolean pending;

    public boolean getPending() {
        return pending;
    }
}
