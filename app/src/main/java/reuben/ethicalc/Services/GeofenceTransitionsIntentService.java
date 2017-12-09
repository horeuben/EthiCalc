package reuben.ethicalc.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import reuben.ethicalc.Fragment.GetNearbyShopsFragment;
import reuben.ethicalc.R;

public class GeofenceTransitionsIntentService extends IntentService {
    long oldenterOneStop;
    long oldexitOneStop;
    private static final String TAG = "GeofenceTransitions";
    long timeNow;
    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Handler h = new Handler(Looper.getMainLooper());
        Log.i(TAG, "onHandleIntent");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        String id = geofencingEvent.getTriggeringGeofences().get(0).getRequestId();
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }
        timeNow = System.currentTimeMillis();

        // Get the transition type.eh
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        Log.i(TAG, "geofenceTransition = " + geofenceTransition + " Enter : " + Geofence.GEOFENCE_TRANSITION_ENTER + "Exit : " + Geofence.GEOFENCE_TRANSITION_EXIT);
        Log.i(TAG,geofencingEvent.getTriggeringLocation().getLatitude()+"+"+geofencingEvent.getTriggeringLocation().getLongitude())              ;
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
            showNotification("Entered "+id,geofencingEvent.getTriggeringLocation().getLatitude()+":"+geofencingEvent.getTriggeringLocation().getLongitude());
        }
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                showNotification("Exited  "+id,geofencingEvent.getTriggeringLocation().getLatitude()+":"+geofencingEvent.getTriggeringLocation().getLongitude());
        } else {
            // Log the error.
            showNotification("Error","Error");
            Log.e(TAG, "here"+ String.valueOf(geofenceTransition));
        }
    }

    public void showNotification(String text,String message) {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, GetNearbyShopsFragment.class);
        //intent.putExtra(MainActivity2.KEY,message);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Geofence")
                .setContentText(text)
                .setContentIntent(pendingNotificationIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}
