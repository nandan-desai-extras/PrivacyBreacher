package io.nandandesai.privacybreacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneMonitorActivity extends AppCompatActivity {

    private static final String TAG = "PhoneMonitorActivity";
    private PhoneLogRecyclerAdapter phoneLogRecyclerAdapter;
    private RecyclerView phoneLogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_monitor);

        phoneLogRecyclerView = findViewById(R.id.phoneLogRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        phoneLogRecyclerView.setLayoutManager(layoutManager);
        phoneLogRecyclerAdapter = new PhoneLogRecyclerAdapter(this, new ArrayList<String>());
        phoneLogRecyclerView.setAdapter(phoneLogRecyclerAdapter);

        final TextView noLogText = findViewById(R.id.noLogText);
        final TextView headingText = findViewById(R.id.headingText);
        headingText.setVisibility(View.INVISIBLE);
        PrivacyBreacherService.eventDatabase.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> logEntries) {
                Log.i(TAG, "onChanged: event change received in PhoneMonitorActivity");
                noLogText.setVisibility(View.INVISIBLE);
                headingText.setVisibility(View.VISIBLE);
                Log.i(TAG, "LogEntries: " + logEntries);
                phoneLogRecyclerAdapter.setLogEntries(logEntries);
                phoneLogRecyclerAdapter.notifyDataSetChanged();
                phoneLogRecyclerView.scrollToPosition(phoneLogRecyclerAdapter.getItemCount() - 1);
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Phone activity monitoring");
        alertDialog.setMessage("Apps installed on your phone can monitor when you turn ON/OFF your screen without requesting any permissions. This way, they can know how many hours you used your phone today, from what time to what time you used your phone," +
                " how many times you charge your phone and are you charging with your Power Bank or AC Charging socket etc. This loophole can help bad guys build a pretty good spyware for your phone!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
