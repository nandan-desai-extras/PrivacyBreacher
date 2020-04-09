package io.nandandesai.privacybreacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhoneInfoActivity extends AppCompatActivity {

    private static final String TAG = "PhoneInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);
        new ActivityLoadTask(this).execute();
    }

    private static String getFormattedDataMeasure(long bytes) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String dataFormat = bytes + " B";
        float speed = 0;
        if (bytes >= 1024) {
            speed = bytes / 1024; //KB
            speed = Float.valueOf(decimalFormat.format(speed));
            if (speed >= 1024) {
                speed = speed / 1024; //MB
                speed = Float.valueOf(decimalFormat.format(speed));
                if (speed >= 1024) {
                    speed = speed / 1024; //GB
                    speed = Float.valueOf(decimalFormat.format(speed));
                    dataFormat = speed + " GB";
                } else {
                    dataFormat = speed + " MB";
                }
            } else {
                dataFormat = speed + " KB";
            }
        }

        return dataFormat;
    }

    private static class ActivityLoadTask extends AsyncTask<Void, Void, Void> {

        private TextView productView;
        private TextView modelView;
        private TextView manufacturerView;
        private TextView manufacturedOn;
        private TextView brandView;
        private TextView deviceView;
        private TextView deviceUptimeView;
        private TextView mobileDataView;
        private TextView wifiDataView;

        private RecyclerView appListRecyclerView;

        private LinearLayout phoneInfoView;
        private ProgressBar progressBar;

        //Build
        private String product;
        private String model;
        private String manufacturer;
        private String buildTime;
        private String brand;
        private String device;

        //uptime
        private String uptimeString;

        //data used
        private long mobileDataUsed;
        private long wifiDataUsed;

        //apps installed
        private ArrayList<AppListRecyclerAdapter.App> apps = new ArrayList<>();

        private AppCompatActivity activity;

        ActivityLoadTask(AppCompatActivity activity) {
            this.activity = activity;
            productView = activity.findViewById(R.id.product);
            modelView = activity.findViewById(R.id.model);
            manufacturerView = activity.findViewById(R.id.manufacturer);
            manufacturedOn = activity.findViewById(R.id.manufacturedOn);
            brandView = activity.findViewById(R.id.brand);
            deviceView = activity.findViewById(R.id.device);
            deviceUptimeView = activity.findViewById(R.id.deviceUptime);
            mobileDataView = activity.findViewById(R.id.mobileDataUsed);
            wifiDataView = activity.findViewById(R.id.wifiDataUsed);

            appListRecyclerView = activity.findViewById(R.id.appListRecyclerView);

            phoneInfoView = activity.findViewById(R.id.phoneInfoView);
            progressBar = activity.findViewById(R.id.progressBar);
        }

        @Override
        protected void onPreExecute() {
            phoneInfoView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //build info
            product = Build.PRODUCT;
            model = Build.MODEL;
            manufacturer = Build.MANUFACTURER;
            buildTime = new Date(Build.TIME).toString();
            brand = Build.BRAND;
            device = Build.DEVICE;

            //SystemClock, get the uptime of the device since boot
            long uptime = SystemClock.uptimeMillis();
            SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm:ss", Locale.ENGLISH);
            Date date = new Date(uptime);
            uptimeString = formatter.format(date);

            //Data used
            mobileDataUsed = TrafficStats.getMobileRxBytes() + TrafficStats.getTotalTxBytes();
            long totalDataUsed = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
            wifiDataUsed = totalDataUsed - mobileDataUsed;

            //apps installed
            final PackageManager pm = activity.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {
                String appName = pm.getApplicationLabel(packageInfo).toString();
                Drawable appIcon = pm.getApplicationIcon(packageInfo);
                apps.add(new AppListRecyclerAdapter.App(appName, appIcon));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

            productView.setText(product);
            modelView.setText(model);
            manufacturerView.setText(manufacturer);
            manufacturedOn.setText(buildTime);
            brandView.setText(brand);
            deviceView.setText(device);
            deviceUptimeView.setText(uptimeString + " in (dd:hh:mm:ss) format");
            mobileDataView.setText(getFormattedDataMeasure(mobileDataUsed));
            wifiDataView.setText(getFormattedDataMeasure(wifiDataUsed));

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            appListRecyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(appListRecyclerView.getContext(), layoutManager.getOrientation());
            appListRecyclerView.addItemDecoration(dividerItemDecoration);

            appListRecyclerView.setNestedScrollingEnabled(false);
            appListRecyclerView.setFocusable(false);

            AppListRecyclerAdapter appListRecyclerAdapter = new AppListRecyclerAdapter(activity, apps);
            appListRecyclerView.setAdapter(appListRecyclerAdapter);

            phoneInfoView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
