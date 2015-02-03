package intec.matdiscreta.taxitapp.ux;

import android.location.Location;
import android.util.Log;

/**
 * Created by Lou on 1/16/15.
 */
public class TaxiMarkerData  {

    public int id;
    public double latitude, longitude;
    public boolean available = true;
    public String name;
    public String updated_at;
    public String created_at;
    public String url;

    public TaxiMarkerData() {
    }
}
