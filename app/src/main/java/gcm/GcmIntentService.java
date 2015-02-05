package gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import intec.matdiscreta.taxitapp.DriverActivity;
import intec.matdiscreta.taxitapp.HomeActivity;
import intec.matdiscreta.taxitapp.R;
import intec.matdiscreta.taxitapp.StartActivity;
import intec.matdiscreta.taxitapp.TaxiTappAPI;

/**
 * Created by Lou on 1/29/15.
 */
public class GcmIntentService extends IntentService {

    private NotificationManager manager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()) {
            if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                publishNotification(extras);
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void publishNotification(Bundle extras) {

        String contentText = extras.getString("message");
        Log.d("Bundle", contentText);
        Log.d("Bundle", String.valueOf(extras.get("call_id")));
        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, TaxiTappAPI.getInstance().getSession().isTaxi ? DriverActivity.class : HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(extras);

//        Intent notificationIntent = new Intent(this, HomeActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(HomeActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Taxi Tapp");
        builder.setContentText(contentText);
        builder.setSmallIcon(R.drawable.ic_taxi_marker);
        builder.setLights(Color.YELLOW, 1000, 1000);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        builder.setContentIntent(pendingIntent);
        Notification n = builder.build();

        manager.notify(7, n);
    }
}
