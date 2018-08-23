package com.lono.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lono.Models.Alerts_Model;
import com.lono.R;

import java.util.List;

public class Adapter_List_Alerts extends RecyclerView.Adapter<Adapter_List_Alerts.AlertsHolder> {

    Activity activity;
    List<Alerts_Model> list_alert;

    public Adapter_List_Alerts(Activity activity, List<Alerts_Model> list_alert){
        this.activity = activity;
        this.list_alert = list_alert;
    }

    @NonNull
    @Override
    public AlertsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_alerts, parent, false);
        return new Adapter_List_Alerts.AlertsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsHolder holder, int position) {
        Alerts_Model alertsModel = list_alert.get(position);

        holder.assunt.setText(alertsModel.getAssunt());
        holder.message.setText(alertsModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return list_alert != null ? list_alert.size() : 0;
    }

    public class AlertsHolder extends RecyclerView.ViewHolder {

        TextView assunt;
        TextView message;

        public AlertsHolder(View itemView) {
            super(itemView);

            assunt = itemView.findViewById(R.id.assunt);
            message = itemView.findViewById(R.id.message);

        }
    }
}
