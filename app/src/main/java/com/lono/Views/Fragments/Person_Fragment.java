package com.lono.Views.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lono.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class Person_Fragment extends Fragment implements View.OnClickListener{

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    View rootview;

    TextView status_account;
    ImageView image_profile;
    TextView name_profile;
    TextView email_profile;

    LinearLayout item_edit_profile;
    LinearLayout item_my_plam;
    LinearLayout item_payment;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_person, container, false);
        editor = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        builder = new AlertDialog.Builder(getActivity());

        item_edit_profile = (LinearLayout) rootview.findViewById(R.id.item_edit_profile);
        item_edit_profile.setOnClickListener(this);
        item_my_plam = (LinearLayout) rootview.findViewById(R.id.item_my_plam);
        item_my_plam.setOnClickListener(this);
        item_payment = (LinearLayout) rootview.findViewById(R.id.item_payment);
        item_payment.setOnClickListener(this);

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

            case R.id.item_edit_profile:

                break;

            case R.id.item_my_plam:

                break;

            case R.id.item_payment:

                break;

            case R.id.item_exit_app:
                builder.setTitle(R.string.app_name);
                builder.setMessage("Deseja realmente sair do Lono?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("token", "");
                        editor.commit();
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Não", null);
                builder.create().show();
                break;
        }
    }
}
