package intec.matdiscreta.taxitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Didier on 1/27/2015.
 */
public class DriverAlertDialog extends DialogFragment{

    private Bundle extras;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle extras = getArguments();
        builder.setMessage("You have a new request!\nRequest ID:" + extras.getString("call_id"))
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES! -- "But I'm le tired..."
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
