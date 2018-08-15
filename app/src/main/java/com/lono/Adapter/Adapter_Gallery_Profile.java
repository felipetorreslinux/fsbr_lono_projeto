package com.lono.Adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lono.Models.Gallery_Images;
import com.lono.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Adapter_Gallery_Profile extends RecyclerView.Adapter<Adapter_Gallery_Profile.GalleryHolder>{

    Activity activity;
    List<Gallery_Images> list_image;

    public Adapter_Gallery_Profile(Activity activity, List<Gallery_Images> list_image){
        this.activity = activity;
        this.list_image = list_image;
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_profile, parent, false);
        return new Adapter_Gallery_Profile.GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        Gallery_Images galleryImages = list_image.get(position);
        Transformation transformation = new RoundedCornersTransformation(0,2);
        Picasso.with(activity)
                .load(new File(galleryImages.getImages()))
                .resize(150,150)
                .transform(transformation)
                .into(holder.image_holder_gallery_profile);
    }

    @Override
    public int getItemCount() {
        return list_image != null ? list_image.size() : 0;
    }

    public class GalleryHolder extends RecyclerView.ViewHolder{

        ImageView image_holder_gallery_profile;

        public GalleryHolder(View itemView) {
            super(itemView);
            image_holder_gallery_profile = itemView.findViewById(R.id.image_holder_gallery_profile);
        }
    }
}