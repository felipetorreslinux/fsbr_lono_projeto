package com.lono.Views.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_Publications;


public class Publications_Fragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    View rootview;
    ProgressBar progress_publications;

    TextView info_response_pub;
    RecyclerView recycler_pubs;

    Service_Publications servicePublications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_publications, container, false);
        sharedPreferences = getActivity().getSharedPreferences("search_pub", Context.MODE_PRIVATE);

        servicePublications = new Service_Publications(getActivity());

        progress_publications = rootview.findViewById(R.id.progress_publications);

        info_response_pub = rootview.findViewById(R.id.info_response_pub);

        recycler_pubs = rootview.findViewById(R.id.recycler_pubs);
        recycler_pubs.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_pubs.setHasFixedSize(true);
        recycler_pubs.setNestedScrollingEnabled(false);

        servicePublications.listOcorrencyToday(recycler_pubs, progress_publications, info_response_pub);

        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            String status = savedInstanceState.getString("status");
        }

        return rootview;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}
