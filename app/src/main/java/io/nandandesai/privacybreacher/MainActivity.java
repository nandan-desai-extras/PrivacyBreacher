package io.nandandesai.privacybreacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!isMyServiceRunning(PrivacyBreacherService.class)) {
            //start the foreground service
            Intent serviceIntent = new Intent(this, PrivacyBreacherService.class);
            startService(serviceIntent);
        }

        //on click listeners
        ImageButton optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        Button physicalMonitorButton = findViewById(R.id.physicalMonitorButton);
        physicalMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhysicalMonitorActivity.class);
                startActivity(intent);
            }
        });

        Button phoneMonitorButton = findViewById(R.id.phoneMonitorButton);
        phoneMonitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhoneMonitorActivity.class);
                startActivity(intent);
            }
        });

        Button phoneInfoButton = findViewById(R.id.phoneInfoButton);
        phoneInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhoneInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.home_menu, menu);
        MenuItem aboutMenu = menu.findItem(R.id.menu_about);
        aboutMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);

                return false;
            }
        });

        MenuItem exitMenu = menu.findItem(R.id.menu_exit);
        exitMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                exitApp();
                return false;
            }
        });
        popup.show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void exitApp() {
        Intent serviceIntent = new Intent(MainActivity.this, PrivacyBreacherService.class);
        MainActivity.this.stopService(serviceIntent);
        finish();
    }
}
