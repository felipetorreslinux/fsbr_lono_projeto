package com.lono.Views.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lono.R;
import com.lono.Service.Service_Terms;
import com.lono.Service.Service_Terms_Journals;

public class TermsJournals_Fragment extends Fragment implements View.OnClickListener {

    View rootview;
    Service_Terms_Journals serviceTermsJournals;
    ProgressBar progress_terms;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_terms_journals, container,false);
        serviceTermsJournals = new Service_Terms_Journals(getActivity());

        recyclerView = rootview.findViewById(R.id.recycler_terms_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        progress_terms = rootview.findViewById(R.id.progress_terms);

        serviceTermsJournals.listTerms(recyclerView, progress_terms);

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
