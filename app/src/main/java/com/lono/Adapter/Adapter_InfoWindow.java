package com.lono.Adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.lono.R;

public class Adapter_InfoWindow implements GoogleMap.InfoWindowAdapter {

    Activity activity;
    final View myContentsView;

    String[] distance;
    String[] time;
    String[] start;
    String[] end;

    public Adapter_InfoWindow(Activity activity, String distance,String time, String start, String end){
        this.start = start.split("-");
        this.end = end.split("-");
        this.distance = distance.split(" ");
        this.time = time.split(" ");
        this.activity = activity;
        myContentsView = activity.getLayoutInflater().inflate(R.layout.info_window_map, null);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        TextView time_text = myContentsView.findViewById(R.id.info_time);
        TextView distance_text = myContentsView.findViewById(R.id.distance_text);
        switch (marker.getTitle()){
            case "Lono":
                time_text.setVisibility(View.GONE);
                distance_text.setText("Lono - Localizador de Nomes\n"+end[0]);
                break;
            default:
                time_text.setVisibility(View.VISIBLE);
                time_text.setText(time[0]+"\n"+time[1].replace("s", ""));
                if(Double.parseDouble(distance[0]) < 1){
                    distance_text.setText("Distância de "+distance[0]+" metros\n"+start[0]);
                }else{
                    distance_text.setText("Distância de "+distance[0]+" km\n"+start[0]);
                }
                break;
        }





        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
