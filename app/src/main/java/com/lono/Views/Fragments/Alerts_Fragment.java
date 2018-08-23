package com.lono.Views.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lono.Adapter.Adapter_List_Alerts;
import com.lono.Models.Alerts_Model;
import com.lono.R;
import com.lono.Service.Service_Alerts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Alerts_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    View rootview;
    ProgressBar progress_alerts;
    RecyclerView recycler_alerts;

    Service_Alerts serviceAlerts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_alerts, container, false);

        serviceAlerts = new Service_Alerts(getActivity());

        progress_alerts = rootview.findViewById(R.id.progress_alerts);

        recycler_alerts = rootview.findViewById(R.id.recycler_alerts);
        recycler_alerts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_alerts.setHasFixedSize(true);
        recycler_alerts.setNestedScrollingEnabled(false);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        serviceAlerts.list_alerts(recycler_alerts, progress_alerts);
    }

    @Override
    public void onClick(View v) {

    }
}
