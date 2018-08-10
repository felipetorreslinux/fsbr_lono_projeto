package com.lono.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.lono.R;
import com.lono.Service.Service_Contact;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.ValidGPS;
import com.lono.Utils.Valitations;

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

    double MyLag = 0.0;
    double MyLng = 0.0;

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
        mapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    localeProfile();
                }
            }, 1000);
        } catch (Exception e) {}


        message_contact = (FloatingActionButton) findViewById(R.id.message_contact);
        call_contact = (FloatingActionButton) findViewById(R.id.call_contact);
        drive_contact = (FloatingActionButton) findViewById(R.id.drive_contact);
        message_contact.setOnClickListener(this);
        call_contact.setOnClickListener(this);
        drive_contact.setOnClickListener(this);

    }

    private void mapReady(final Location location){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng latLng = new LatLng(-8.045934,-34.8912007);

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
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                decodedPath.add(new LatLng(location.getLatitude(),location.getLongitude()));
                decodedPath.add(latLng);
                googleMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLACK));

            }
        });
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
                    }
                }, 2000);
                break;

            case R.id.drive_contact:
                comoChegarMaps();
                break;
        }
    }

    private void openMessageContact() {
    }

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

                            LatLng posicaoInicial = new LatLng(location.getLatitude(),location.getLongitude());
                            LatLng posicaiFinal = new LatLng(-8.045934,-34.8912007);

                            double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
                            formatNumber(distance);

                            geocoder = new Geocoder(View_Contact.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String logradouro = addresses.get(0).getThoroughfare();
                                String estado = addresses.get(0).getAdminArea();
                                String number = addresses.get(0).getFeatureName();
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                    }
                });
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1000) {
            distance /= 1000;
            unit = "km";
        }else{
            distance *= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
