package io.nandandesai.privacybreacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EventReceiver extends BroadcastReceiver {

    private static final String TAG = "EventReceiver";

    private ArrayList<String> eventList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.i(TAG, "onReceive: Screen is active.");
            eventList.add("\uD83D\uDD06 Phone screen turned ON at " + getFormattedDate(System.currentTimeMillis()));
            PrivacyBreacherService.eventDatabase.setValue(eventList);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.i(TAG, "onReceive: Screen is inactive.");
            eventList.add("\uD83D\uDD05 Phone screen turned OFF at " + getFormattedDate(System.currentTimeMillis()));
            PrivacyBreacherService.eventDatabase.setValue(eventList);
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Toast.makeText(context, "PrivacyBreacher just saw that you are switching OFF your phone!", Toast.LENGTH_LONG).show();
        } else if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.i(TAG, "onReceive: Phone charger is connected");
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            // How are we charging?
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if (usbCharge) {
                eventList.add("⚡ You started charging your phone using USB cable connected to a laptop/PC at " + getFormattedDate(System.currentTimeMillis()));
                PrivacyBreacherService.eventDatabase.setValue(eventList);
            } else if (acCharge) {
                eventList.add("\uD83D\uDD0C You started charging your phone using a Power Bank or AC Charging adapter at " + getFormattedDate(System.currentTimeMillis()));
                PrivacyBreacherService.eventDatabase.setValue(eventList);
            }

            Log.i(TAG, "usbCharge: " + usbCharge);
            Log.i(TAG, "acCharging: " + acCharge);
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.i(TAG, "onReceive: Phone charger is disconnected.");
            eventList.add("\uD83D\uDD0B You disconnected your phone charger at " + getFormattedDate(System.currentTimeMillis()));
            PrivacyBreacherService.eventDatabase.setValue(eventList);
        } else if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            if (!isInitialStickyBroadcast()) {
                Log.i(TAG, "onReceive: Headphone action detected!");
                int state = intent.getIntExtra("state", -1);
                if (state == 1) {
                    eventList.add("\uD83C\uDFA7 You plugged headphones at " + getFormattedDate(System.currentTimeMillis()));
                    PrivacyBreacherService.eventDatabase.setValue(eventList);
                } else if (state == 0) {
                    eventList.add("\uD83D\uDD0A You unplugged headphones at " + getFormattedDate(System.currentTimeMillis()));
                    PrivacyBreacherService.eventDatabase.setValue(eventList);
                }
            }
        }

    }

    private String getFormattedDate(long unixTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss aa", Locale.US);
        Date date = new Date(unixTime);
        return formatter.format(date);
    }
}
