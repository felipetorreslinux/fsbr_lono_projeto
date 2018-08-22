package com.lono.Views.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lono.R;
import com.lono.Service.Service_Terms_Journals;

public class Terms_Journals_Fragment extends Fragment implements View.OnClickListener {

    View rootview;
    Service_Terms_Journals serviceTermsJournals;
    ProgressBar progress_terms;

    LinearLayout layout_box_jornais;
    LinearLayout layout_box_termos;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_terms_journals, container,false);
        serviceTermsJournals = new Service_Terms_Journals(getActivity());

        layout_box_jornais = rootview.findViewById(R.id.layout_box_jornais);
        layout_box_termos = rootview.findViewById(R.id.layout_box_termos);

        recyclerView = rootview.findViewById(R.id.recycler_terms_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        progress_terms = rootview.findViewById(R.id.progress_terms);

        serviceTermsJournals.listTerms(recyclerView, progress_terms, layout_box_termos);

        return rootview;
    }

    @Override
    public void onClick(View v) {

    }
}
