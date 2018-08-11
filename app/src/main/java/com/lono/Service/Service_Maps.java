package com.lono.Service;

import android.app.Activity;
import android.location.Location;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lono.R;
import com.lono.Views.View_Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service_Maps {

    public static void open (final Activity activity, final GoogleMap googleMap, final MapView mapView, final Location location, final TextView textView){
        String url = makeURL(location.getLatitude(), location.getLongitude(), -8.0459274, -34.8891792);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray routeArray = response.getJSONArray("routes");
                            JSONObject routes = routeArray.getJSONObject(0);
                            JSONObject address = routes.getJSONArray("legs").getJSONObject(0);

                            String start_address = address.getString("start_address");
                            String[] end_address = address.getString("end_address").split("-");
                            String distance_address = address.getJSONObject("distance").getString("text");
                            String duration_address = address.getJSONObject("duration").getString("text");

                            textView.setText(end_address[0]+"\nVocê está a "+distance_address+" de nós.\nCerca de "+duration_address+" para chegar");

                            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                            String encodedString = overviewPolylines.getString("points");
                            List<LatLng> list = decodePoly(encodedString);
                            for (int z = 0; z < list.size() - 1; z++) {
                                LatLng src = list.get(z);
                                LatLng dest = list.get(z + 1);
                                googleMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                                        .width(5).color(activity.getResources().getColor(R.color.colorBlack)).geodesic(true));
                            }
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap mMap) {
                                    mMap = googleMap;

                                    final LatLng latLng = new LatLng(-8.0459274,-34.8891792);

                                    final MarkerOptions marker = new MarkerOptions()
                                            .position(latLng)
                                            .flat(false)
                                            .draggable(false)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_home));

                                    final MarkerOptions you = new MarkerOptions()
                                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                            .flat(false)
                                            .draggable(false)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you_map));

                                    googleMap.addMarker(marker);
                                    googleMap.addMarker(you);
                                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    googleMap.setBuildingsEnabled(true);
                                    googleMap.setIndoorEnabled(true);
                                    googleMap.setTrafficEnabled(false);
                                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style));
                                    CameraPosition cameraPosition = new CameraPosition
                                            .Builder()
                                            .target(latLng)
                                            .zoom(12).build();
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                }
                            });

                        }catch (JSONException e){}
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        System.out.println(urlString);
        return urlString.toString();
    }

}
