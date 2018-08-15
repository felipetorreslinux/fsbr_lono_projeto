package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lono.Adapter.Adapter_Gallery_Profile;
import com.lono.Models.Gallery_Images;
import com.lono.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DCIM;

public class View_Gallery_Profile extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RecyclerView recyclerview_gallery;
    List<Gallery_Images> list_images = new ArrayList<Gallery_Images>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_gallery_profile);
        createToolbar(toolbar);

        recyclerview_gallery = (RecyclerView) findViewById(R.id.recyclerview_gallery);
        recyclerview_gallery.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview_gallery.setHasFixedSize(true);
        recyclerview_gallery.setNestedScrollingEnabled(false);

        list_images.clear();

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File file[] = storageDir.listFiles();

        for (int i=0; i < file.length; i++) {
            String image = file[i].getParent()+"/"+file[i].getName();
            String name = file[i].getParent()+"/"+file[i].getName();
            Gallery_Images galleryImages = new Gallery_Images(
                    i,
                    image,
                    name);
            list_images.add(galleryImages);
        };

        Adapter_Gallery_Profile adapterGalleryProfile = new Adapter_Gallery_Profile(this, list_images);
        recyclerview_gallery.setAdapter(adapterGalleryProfile);

    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_gallery_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Galeria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}