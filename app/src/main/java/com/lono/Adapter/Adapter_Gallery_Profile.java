package com.lono.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lono.Models.Gallery_Images;
import com.lono.R;
import com.lono.Service.Service_Profile;
import com.lono.Utils.Alerts;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Adapter_Gallery_Profile extends RecyclerView.Adapter<Adapter_Gallery_Profile.GalleryHolder> {
    Activity activity;
    List<Gallery_Images> list_image;
    Service_Profile serviceProfile;

    public Adapter_Gallery_Profile(Activity activity, List<Gallery_Images> list_image){
        this.activity = activity;
        this.list_image = list_image;
        this.serviceProfile = new Service_Profile(activity);
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_profile, parent, false);
        return new Adapter_Gallery_Profile.GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryHolder holder, int position) {
        final Gallery_Images galleryImages = list_image.get(position);
        Transformation transformation = new RoundedCornersTransformation(0,2);

        Picasso.with(activity)
                .load(new File(galleryImages.getImages()))
                .resize(150,150)
                .transform(transformation)
                .into(holder.image_holder_gallery_profile);

        holder.image_holder_gallery_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Alerts.progress_open(activity, null, "Salvando imagem", false);
//                serviceProfile.uploadImage(new File(galleryImages.getImages()));
                CropImage.activity(Uri.fromFile(new File(galleryImages.getImages())))
                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(250,250)
                        .start(activity);
            }
        });
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