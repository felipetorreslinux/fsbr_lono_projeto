package com.lono.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lono.Models.Slides_Intro_Model;
import com.lono.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Slide_Intro_Adapter extends PagerAdapter {

    List<Slides_Intro_Model> list_slide_intro;
    View view;
    Activity activity;

    public Slide_Intro_Adapter(final Activity activity, final List<Slides_Intro_Model> list_slide_intro){
        this.list_slide_intro = list_slide_intro;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list_slide_intro != null ? list_slide_intro.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(activity).inflate(R.layout.item_slide_intro, container, false);
        Slides_Intro_Model slide = list_slide_intro.get(position);

        ImageView imageview_slide_intro = view.findViewById(R.id.imageview_slide_intro);
        TextView textview_slide_intro = view.findViewById(R.id.textview_slide_intro);

        imageview_slide_intro.setImageResource(slide.getImage());
        textview_slide_intro.setText(slide.getText());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }

}
