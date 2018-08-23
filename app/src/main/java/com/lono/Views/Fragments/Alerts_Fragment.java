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

import com.lono.Adapter.Adapter_List_Alerts;
import com.lono.Models.Alerts_Model;
import com.lono.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Alerts_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    View rootview;
    RecyclerView recycler_alerts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_alerts, container, false);


        recycler_alerts = rootview.findViewById(R.id.recycler_alerts);
        recycler_alerts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_alerts.setHasFixedSize(true);
        recycler_alerts.setNestedScrollingEnabled(false);

        listAlerts();

        return rootview;
    }

    private void listAlerts(){
        sharedPreferences = getActivity().getSharedPreferences("all_alerts", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            try {
                JSONArray jsonArray = new JSONArray(sharedPreferences.getString("alerts", ""));
                List<Alerts_Model> list_alerts = new ArrayList<>();
                list_alerts.clear();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Alerts_Model alertsModel = new Alerts_Model(
                            jsonObject.getInt("id_notificacao"),
                            jsonObject.getInt("id_cliente"),
                            jsonObject.getString("mensagem"),
                            jsonObject.getString("assunto"),
                            jsonObject.getString("dat_cad"),
                            jsonObject.getString("elapsed_time"));
                    list_alerts.add(alertsModel);
                }
                Adapter_List_Alerts adapterListAlerts = new Adapter_List_Alerts(getActivity(), list_alerts);
                recycler_alerts.setAdapter(adapterListAlerts);
            }catch (JSONException e){}
        }
    }

    @Override
    public void onClick(View v) {

    }
}
