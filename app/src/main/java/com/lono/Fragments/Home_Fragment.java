package com.lono.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lono.R;

public class Home_Fragment extends Fragment implements View.OnClickListener {

    View rootview;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_home, viewGroup, false);

        return rootview;
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
