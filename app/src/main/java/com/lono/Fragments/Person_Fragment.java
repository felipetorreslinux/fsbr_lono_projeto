package com.lono.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.R;

public class Person_Fragment extends Fragment implements View.OnClickListener{

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    View rootView;
    ImageView imageview_menu_person;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_person, viewGroup, false);

        editor = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit();
        sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
        imageview_menu_person = rootView.findViewById(R.id.imageview_menu_person);
        imageview_menu_person.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageview_menu_person:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setCancelable(true);
                View v = getActivity().getLayoutInflater().inflate(R.layout.bottomsweet_menu_person, null);
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();

                TextView exit_app = v.findViewById(R.id.text_view_exit_app);
                exit_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putString("token", null);
                        editor.commit();
                        getActivity().finish();
                    }
                });
                break;
        }
    }
}
