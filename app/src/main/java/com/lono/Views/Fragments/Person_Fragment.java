package com.lono.Views.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class Person_Fragment extends Fragment implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    View rootview;

    TextView status_account;
    ImageView image_profile;
    TextView name_profile;
    TextView email_profile;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_person, container, false);

        return rootview;
    }

    @Override
    public void onResume() {
        infoProfile();
        super.onResume();
    }

    private void infoProfile() {
        sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        status_account = (TextView) rootview.findViewById(R.id.status_account);
        image_profile = (ImageView) rootview.findViewById(R.id.image_profile);
        image_profile.setOnClickListener(this);
        name_profile = (TextView) rootview.findViewById(R.id.name_profile);
        email_profile = (TextView) rootview.findViewById(R.id.email_profile);

        Picasso.with(getActivity())
                .load(sharedPreferences.getString("avatar_url", String.valueOf(R.drawable.eu)))
                .placeholder(R.drawable.place_profile)
                .error(R.drawable.place_profile)
                .resize(150,150)
                .transform(new CropCircleTransformation())
                .into(image_profile);

        if(sharedPreferences != null){
            name_profile.setText(sharedPreferences.getString("name", null));
            email_profile.setText(sharedPreferences.getString("email", null));
            String status = sharedPreferences.getString("situacao_cad", null);

            System.out.println(sharedPreferences.getBoolean("view_notifications", false));

            if(status.equals("A")){
                status_account.setBackgroundColor(getActivity().getResources().getColor(R.color.colorGreenLight));
                status_account.setText("Sua conta está ativa");
            }else{
                status_account.setText("Sua conta pendente");
                status_account.setBackgroundColor(getActivity().getResources().getColor(R.color.colorRed));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_profile:

                break;
        }
    }
}
