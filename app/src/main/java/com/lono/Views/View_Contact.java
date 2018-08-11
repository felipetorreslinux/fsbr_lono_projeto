package com.lono.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.EditText;

import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.lono.R;
import com.lono.Service.Service_Contact;
import com.lono.Utils.Alerts;

import com.lono.Utils.ValidGPS;
import com.lono.Utils.Valitations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class View_Contact extends AppCompatActivity implements View.OnClickListener {


    MapView mapView;
    private GoogleMap googleMap;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    List<Address> addresses;
    List<LatLng> decodedPath = new ArrayList<LatLng>();

    TextView info_location;

    FloatingActionButton message_contact;
    FloatingActionButton call_contact;
    FloatingActionButton drive_contact;

    Service_Contact serviceContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        ValidGPS.enable(this);
        serviceContact = new Service_Contact(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        info_location = (TextView) findViewById(R.id.info_location);
        info_location.setText("Carregando informações...");
        message_contact = (FloatingActionButton) findViewById(R.id.message_contact);
        call_contact = (FloatingActionButton) findViewById(R.id.call_contact);
        drive_contact = (FloatingActionButton) findViewById(R.id.drive_contact);
        message_contact.setOnClickListener(this);
        call_contact.setOnClickListener(this);
        drive_contact.setOnClickListener(this);

        try {
            MapsInitializer.initialize(getApplicationContext());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    localeProfile();
                }
            }, 1000);
        } catch (Exception e) {}

        mapView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_contact:
                openMessageContact();
                break;

            case R.id.call_contact:
                Alerts.progress_open(this, null, "Ligando para Lono", false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callContactPhone();
                        Alerts.progress_clode();
                    }
                }, 2000);
                break;

            case R.id.drive_contact:
                comoChegarMaps();
                break;
        }
    }

    private void openMessageContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_send_message, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final TextInputLayout layout_name = view.findViewById(R.id.layout_name_message);
        final TextInputLayout layout_email = view.findViewById(R.id.layout_email_message);
        final TextInputLayout layout_message = view.findViewById(R.id.layout_message_contact);

        final EditText name = view.findViewById(R.id.name_contact);
        final EditText email = view.findViewById(R.id.email_contact);
        final EditText message = view.findViewById(R.id.text_message_contact);

        FloatingActionButton button_send = view.findViewById(R.id.button_send_message);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = name.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String msg = message.getText().toString().trim();

                if(nome.isEmpty()){

                    layout_name.setErrorEnabled(true);
                    layout_email.setErrorEnabled(false);
                    layout_message.setErrorEnabled(false);
                    layout_name.setError("Infome seu nome e sobrenome");
                    name.requestFocus();

                }else if(mail.isEmpty()){

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(true);
                    layout_message.setErrorEnabled(false);
                    layout_email.setError("Infome seu email");
                    email.requestFocus();

                }else if(Valitations.email(mail) == false){

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(true);
                    layout_message.setErrorEnabled(false);
                    layout_email.setError("Email inválido");
                    email.requestFocus();

                }else if(msg.isEmpty()){

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(false);
                    layout_message.setErrorEnabled(false);
                    layout_message.setError("Escreva algo para gente");
                    message.requestFocus();

                }else {

                    Alerts.progress_open(View_Contact.this, null, "Enviando...", false);
                    serviceContact.sendEmail(nome, mail, msg, alertDialog);

                }
            }
        });

    };

    @SuppressLint("MissingPermission")
    private void callContactPhone(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:81 3132-0664"));
        startActivity(callIntent);
    }

    private void comoChegarMaps(){
        Uri uri = Uri.parse("google.navigation:q=-8.0458145,-34.8895672");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void localeProfile() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if (location != null) {
                            mapReady(location);
                            geocoder = new Geocoder(View_Contact.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(-8.0459254,-34.8891508, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String logradouro = addresses.get(0).getThoroughfare();
                                String estado = addresses.get(0).getAdminArea();
                                String number = addresses.get(0).getFeatureName();
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                            } catch (IOException e) {}
                        }else{

                        }
                    }
                });
    }

    private void mapReady(final Location location){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

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

                                info_location.setText(end_address[0]+"\nVocê está a "+distance_address+" de nós.\nCerca de "+duration_address+" para chegar");

                                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                                String encodedString = overviewPolylines.getString("points");
                                List<LatLng> list = decodePoly(encodedString);
                                for (int z = 0; z < list.size() - 1; z++) {
                                    LatLng src = list.get(z);
                                    LatLng dest = list.get(z + 1);
                                    googleMap.addPolyline(new PolylineOptions()
                                            .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                                            .width(5).color(getResources().getColor(R.color.colorBlack)).geodesic(true));
                                }
                                googleMap.addMarker(marker);
                                googleMap.addMarker(you);
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                googleMap.setBuildingsEnabled(true);
                                googleMap.setIndoorEnabled(true);
                                googleMap.setTrafficEnabled(false);
                                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(View_Contact.this, R.raw.map_style));
                                CameraPosition cameraPosition = new CameraPosition
                                        .Builder()
                                        .target(latLng)
                                        .zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mapView.setVisibility(View.VISIBLE);
                                mapView.onResume();
                            }catch (JSONException e){}
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            }
        });
    }

    private List<LatLng> decodePoly(String encoded) {

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

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
