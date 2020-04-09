package io.nandandesai.privacybreacher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppListRecyclerAdapter extends RecyclerView.Adapter<AppListRecyclerAdapter.ViewHolder> {

    private ArrayList<App> apps;
    private Context context;

    public AppListRecyclerAdapter(Context context, ArrayList<App> apps) {
        this.apps = apps;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        App app = apps.get(position);

        viewHolder.appIcon.setImageDrawable(app.getIcon());
        viewHolder.appName.setText(app.getName());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.appIcon = itemView.findViewById(R.id.appIconImage);
            this.appName = itemView.findViewById(R.id.appName);
        }
    }


    public static class App {
        private String name;
        private Drawable icon;

        public App(String name, Drawable icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public Drawable getIcon() {
            return icon;
        }
    }

}
