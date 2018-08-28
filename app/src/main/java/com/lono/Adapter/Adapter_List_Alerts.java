package com.lono.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.Models.Alerts_Model;
import com.lono.R;
import com.lono.Service.Service_Alerts;

import java.util.List;

public class Adapter_List_Alerts extends RecyclerView.Adapter<Adapter_List_Alerts.AlertsHolder> {

    Activity activity;
    List<Alerts_Model> list_alert;
    AlertDialog.Builder builder;
    Service_Alerts serviceAlerts;

    public Adapter_List_Alerts(Activity activity, List<Alerts_Model> list_alert){
        this.activity = activity;
        this.list_alert = list_alert;
        this.builder = new AlertDialog.Builder(activity);
        this.serviceAlerts = new Service_Alerts(activity);
    }

    @NonNull
    @Override
    public AlertsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_alerts, parent, false);
        return new Adapter_List_Alerts.AlertsHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AlertsHolder holder, int position) {
        final Alerts_Model alertsModel = list_alert.get(position);

        holder.assunt.setText(alertsModel.getAssunt());
        holder.message.setText(alertsModel.getMessage());
        holder.recurring_time.setText(alertsModel.getRecurring_time());

        if(alertsModel.isRead()){
            holder.item_alerts.setBackgroundColor(activity.getResources().getColor(R.color.colorFundo));
            holder.image_read.setVisibility(View.VISIBLE);
            holder.item_alerts.setElevation(0f);
        }else{
            holder.item_alerts.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
            holder.image_read.setVisibility(View.GONE);
        }

        holder.item_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(alertsModel.getAssunt());
                builder.setMessage(alertsModel.getMessage()+"\n\n"+alertsModel.getRecurring_time());
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!alertsModel.isRead()){
                            serviceAlerts.readMessage(alertsModel);
                            alertsModel.setRead(true);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_alert != null ? list_alert.size() : 0;
    }

    public class AlertsHolder extends RecyclerView.ViewHolder {

        LinearLayout item_alerts;
        TextView assunt;
        TextView message;
        TextView recurring_time;
        ImageView image_read;

        public AlertsHolder(View itemView) {
            super(itemView);

            item_alerts = itemView.findViewById(R.id.item_alerts);
            assunt = itemView.findViewById(R.id.assunt);
            message = itemView.findViewById(R.id.message);
            recurring_time = itemView.findViewById(R.id.recurring_time);
            image_read = itemView.findViewById(R.id.image_read);

        }
    }
}
