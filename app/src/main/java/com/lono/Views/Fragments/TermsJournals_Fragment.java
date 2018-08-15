package com.lono.Views.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lono.R;

public class TermsJournals_Fragment extends Fragment implements View.OnClickListener {

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_terms_journals, container,false);

        return rootview;
    }

    @Override
    public void onClick(View v) {

    }
}
