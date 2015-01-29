package gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOError;
import java.io.IOException;

import intec.matdiscreta.taxitapp.HomeActivity;
import intec.matdiscreta.taxitapp.TaxiTappAPI;

/**
 * Created by Lou on 1/29/15.
 */
public class GcmManager {
    private static GcmManager ourInstance = new GcmManager();

    GoogleCloudMessaging gcm;

    private static String PROPERTY_REG_ID = "registration_id";
    private static String PROPERTY_APP_VERSION = "1";
    private String SENDER_ID = "71686379121";

    private Context context;
    private String regId;

    SharedPreferences sharedPrefs;

    public static GcmManager getInstance() {
        return ourInstance;
    }

    public void startGcm(Context context) {
        this.context = context;
        this.gcm = GoogleCloudMessaging.getInstance(context);
        this.regId = getRegistrationId(context);

        if(regId.isEmpty()) {
            registerInBackground();
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String regId = prefs.getString(PROPERTY_REG_ID, "");

        if(regId.isEmpty()) {
            Log.i("GCM", "Registration not found.");
            return "";
        }

        return regId;
    }

    private SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(HomeActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private void storeRegistrationId(Context context, String regId) {

    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {
                    if(gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered! Registration ID: " + regId;

                    sendRegistrationIdToBackend();

                    storeRegistrationId(context, regId);
                } catch(IOException ex) {
                    msg = "Error: " + ex.getMessage();
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
        }.execute(null, null, null);

    }

    private void sendRegistrationIdToBackend() {
        TaxiTappAPI.getInstance().sendRegistrationId(regId);
    }


}
