package io.nandandesai.privacybreacher;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneLogRecyclerAdapter extends RecyclerView.Adapter<PhoneLogRecyclerAdapter.ViewHolder> {
    private static final String TAG = "PhoneLogRecyclerAdapter";
    private Context context;
    private ArrayList<String> logEntries;

    PhoneLogRecyclerAdapter(Context context, ArrayList<String> logEntries) {
        this.context = context;
        this.logEntries = logEntries;
    }

    void setLogEntries(ArrayList<String> logEntries) {
        this.logEntries = logEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.phone_monitor_log_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.i(TAG, "onBindViewHolder: Adding log entry to TextView: " + logEntries.get(position));
        viewHolder.phoneMonitorLogItem.setText(logEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return logEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView phoneMonitorLogItem;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneMonitorLogItem = itemView.findViewById(R.id.phoneMonitorLogItem);
        }
    }
}
