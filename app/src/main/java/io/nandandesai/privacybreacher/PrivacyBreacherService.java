package io.nandandesai.privacybreacher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class PrivacyBreacherService extends Service {

    private static final String TAG = "PrivacyBreacherService";

    public static MutableLiveData<ArrayList<String>> eventDatabase = new MutableLiveData<>();

    private EventReceiver eventReceiver;

    public PrivacyBreacherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(R.string.app_name, getNotification(this));

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SHUTDOWN);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        eventReceiver = new EventReceiver();
        registerReceiver(eventReceiver, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(eventReceiver);
        eventDatabase = null; //garbage collect that object
    }

    public Notification getNotification(Context context) {

        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final String NOTIF_CHANNEL_ID = "privacybreacher";
        final String NOTIF_CHANNEL_NAME = "PrivacyBreacher";
        NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentTitle("PrivacyBreacher is running!")
                .setContentText("PrivacyBreacher is monitoring your activities.")
                .setPriority(2)
                .setDefaults(Notification.FLAG_FOREGROUND_SERVICE)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setShowWhen(false)
                .setOngoing(true)
                .setContentIntent(pendingNotificationIntent);
        return notificationBuilder.build();
    }
}
