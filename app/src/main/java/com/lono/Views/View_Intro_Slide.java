package com.lono.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lono.Adapter.Slide_Intro_Adapter;
import com.lono.Models.Slides_Intro_Model;
import com.lono.R;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class View_Intro_Slide extends Activity implements View.OnClickListener {

    UltraViewPager ultraviewpager_intro;
    List<Slides_Intro_Model> list_slides = new ArrayList<>();
    Button button_slide_intro;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_intro_slide);

        ultraviewpager_intro = (UltraViewPager) findViewById(R.id.ultraviewpager_intro);
        button_slide_intro = (Button) findViewById(R.id.button_slide_intro);
        button_slide_intro.setOnClickListener(this);
        button_slide_intro.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slidesIntro ();
    }

    private void slidesIntro (){
        list_slides.clear();
        Slides_Intro_Model slide01 = new Slides_Intro_Model(R.drawable.post, "O LONO é um sistema que pesquisa de forma automática nomes ou termos previamente cadastrados, em jornais e publicações como: diários oficiais, diários da justiça dos estados e tribunais superiores.");
        Slides_Intro_Model slide02 = new Slides_Intro_Model(R.drawable.journal, "Pesquise a palavra desejada em centenas de jornais");
        Slides_Intro_Model slide03 = new Slides_Intro_Model(R.drawable.smartphone, "Interface fácil de usar e acessível por qualquer dispositivo.");
        Slides_Intro_Model slide04 = new Slides_Intro_Model(R.drawable.envelope, "Receba as matérias diretamente no seu correio eletrônico.");
        list_slides.clear();
        list_slides.add(slide01);
        list_slides.add(slide02);
        list_slides.add(slide03);
        list_slides.add(slide04);

        Slide_Intro_Adapter adapter = new Slide_Intro_Adapter(this, list_slides);
        ultraviewpager_intro.setAdapter(adapter);
        ultraviewpager_intro.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraviewpager_intro.initIndicator();
        ultraviewpager_intro.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(getResources().getColor(R.color.colorPrimary))
                .setNormalColor(getResources().getColor(R.color.colorGray ))
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        ultraviewpager_intro.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraviewpager_intro.getIndicator().setMargin(0,25, 0 ,25);
        ultraviewpager_intro.getIndicator().build();
        ultraviewpager_intro.setInfiniteLoop(false);
        ultraviewpager_intro.setPageTransformer(false, new UltraScaleTransformer());

        ultraviewpager_intro.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 3:
                        button_slide_intro.setVisibility(View.VISIBLE);
                        break;
                    default:
                        button_slide_intro.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_slide_intro:
                Intent intent = new Intent(this, View_Intro.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
