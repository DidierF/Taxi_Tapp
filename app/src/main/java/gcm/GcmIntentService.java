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

import com.google.android.gms.gcm.GoogleCloudMessaging;

import intec.matdiscreta.taxitapp.HomeActivity;
import intec.matdiscreta.taxitapp.R;
import intec.matdiscreta.taxitapp.StartActivity;

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
                publishNotification();
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void publishNotification() {

        String contentText = "Your taxi is on it's way!";
        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

//        Intent notificationIntent = new Intent(this, HomeActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(HomeActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent homePendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
                PendingIntent.getActivity(this, 0, new Intent(this, StartActivity.class), 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Taxi Tapp");
        builder.setContentText(contentText);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLights(Color.YELLOW, 1000, 1000);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);

        builder.setContentIntent(homePendingIntent);
        Notification n = builder.build();

        manager.notify(7, n);
    }
}
