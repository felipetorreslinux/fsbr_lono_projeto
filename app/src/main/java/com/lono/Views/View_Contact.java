package com.lono.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.lono.R;
import com.lono.Service.Service_Contact;
import com.lono.Utils.Alerts;
import com.lono.Utils.Keyboard;
import com.lono.Utils.ValidGPS;
import com.lono.Utils.Valitations;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class View_Contact extends AppCompatActivity implements View.OnClickListener {


    MapView mapView;
    private GoogleMap googleMap;
    FusedLocationProviderClient mFusedLocationClient;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        ValidGPS.enable(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
            mapReady();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    localeProfile();
                }
            }, 1000);
        } catch (Exception e) {}

    }

    private void mapReady(){
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
                googleMap.setMinZoomPreference(14);
                googleMap.addMarker(marker);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
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
                            System.out.println("asdasdasdasdasd");
                        }
                    }
                });
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
