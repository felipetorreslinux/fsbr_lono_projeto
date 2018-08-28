package com.lono.Views.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.lono.R;
import com.lono.Views.View_Edit_Profile;
import com.lono.Views.View_My_Plan_Profile;
import com.squareup.picasso.Picasso;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class Person_Fragment extends Fragment implements View.OnClickListener{

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    View rootview;

    TextView status_account;
    public static ImageView image_profile;
    TextView name_profile;
    TextView email_profile;

    LinearLayout item_edit_profile;
    LinearLayout item_my_plam;
    LinearLayout item_payment;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_person, container, false);
        sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        builder = new AlertDialog.Builder(getActivity());

        item_edit_profile = (LinearLayout) rootview.findViewById(R.id.item_edit_profile);
        item_edit_profile.setOnClickListener(this);
        item_my_plam = (LinearLayout) rootview.findViewById(R.id.item_my_plam);
        item_my_plam.setOnClickListener(this);
        item_payment = (LinearLayout) rootview.findViewById(R.id.item_payment);
        item_payment.setOnClickListener(this);

        infoProfile();
        return rootview;
    }

    private void infoProfile() {

        status_account = (TextView) rootview.findViewById(R.id.status_account);
        image_profile = (ImageView) rootview.findViewById(R.id.image_profile);
        image_profile.setOnClickListener(this);
        name_profile = (TextView) rootview.findViewById(R.id.name_profile);
        email_profile = (TextView) rootview.findViewById(R.id.email_profile);

        if(sharedPreferences != null){
            Picasso.with(getActivity())
                .load(sharedPreferences.getString("avatar_url", String.valueOf(R.drawable.place_profile)))
                .resize(150,150)
                .transform(new CropCircleTransformation())
                .into(image_profile);
            name_profile.setText(sharedPreferences.getString("name", null));
            email_profile.setText(sharedPreferences.getString("email", null));
            String status = sharedPreferences.getString("sit_cad", null);

            if(status.equals("A")){
                status_account.setBackgroundColor(getActivity().getResources().getColor(R.color.colorGreenLight));
                status_account.setText("Sua conta está ativa");
            }else{
                status_account.setText("Sua conta pendente");
                status_account.setBackgroundColor(getActivity().getResources().getColor(R.color.colorRed));
            }
            status_account.setVisibility(View.GONE);
        }

        if(sharedPreferences.getString("name_plan", "").equals("Plus")){
            item_payment.setVisibility(View.GONE);
        }else{
            item_payment.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_profile:
                editImageProfile();
                break;

            case R.id.item_edit_profile:
                Intent edit_profile = new Intent(getActivity(), View_Edit_Profile.class);
                getActivity().startActivityForResult(edit_profile, 1001);
                break;

            case R.id.item_my_plam:
                Intent my_plan = new Intent(getActivity(), View_My_Plan_Profile.class);
                getActivity().startActivity(my_plan);
                break;

            case R.id.item_payment:
                Snackbar.make(getActivity().getWindow().getDecorView(), "Em Desenvolvimento", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void editImageProfile(){
        ImagePicker.create(getActivity())
                .returnMode(ReturnMode.ALL)
                .folderMode(true)
                .toolbarFolderTitle("Albuns")
                .toolbarImageTitle("Escolha")
                .includeVideo(false)
                .single()
                .limit(1)
                .showCamera(true)
                .imageDirectory("Camera")
                .enableLog(false)
                .start();
    }
}
