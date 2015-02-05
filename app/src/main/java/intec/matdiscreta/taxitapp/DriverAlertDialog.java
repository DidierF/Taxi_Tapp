package intec.matdiscreta.taxitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Didier on 1/27/2015.
 */
public class DriverAlertDialog extends DialogFragment{

    boolean mRemoved;
    int mBackStackId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Bundle extras = getArguments();
        final LatLng latLng = new LatLng(Double.valueOf(extras.getString("latitude")), Double.valueOf(extras.getString("longitude")));
        builder.setMessage("You have a new request!\nRequest ID:" + extras.getString("call_id"))
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES! -- "But I'm le tired..."
                        TaxiTappAPI.getInstance().respondCall(Integer.valueOf(extras.getString("call_id")), true, latLng);
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        TaxiTappAPI.getInstance().respondCall(Integer.valueOf(extras.getString("call_id")), false, latLng);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
