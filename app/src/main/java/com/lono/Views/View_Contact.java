package com.lono.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Bounds;
import com.lono.Adapter.Adapter_InfoWindow;
import com.lono.R;
import com.lono.Service.Service_Contact;
import com.lono.Utils.Alerts;

import com.lono.Utils.ValidGPS;
import com.lono.Utils.Valitations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class View_Contact extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    
    MapView mapView;
    private GoogleMap googleMap;
    FusedLocationProviderClient mFusedLocationClient;
    TextView info_location;
    TextView info_location_user;

    LinearLayout box_loading_map;

    LinearLayout item_call_lono;
    LinearLayout item_chat_lono;
    LinearLayout item_mail_lono;
    LinearLayout item_drive_lono;

    Service_Contact serviceContact;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact);

        serviceContact = new Service_Contact(this);

        createToolbar(toolbar);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        box_loading_map = (LinearLayout) findViewById(R.id.box_loading_map);

        info_location = (TextView) findViewById(R.id.info_location);
        info_location_user = (TextView) findViewById(R.id.info_location_user);
        info_location.setText("Carregando local do Lono");
        info_location_user.setText("Carregando sua localização");

        item_call_lono = (LinearLayout) findViewById(R.id.item_call_lono);
        item_call_lono.setOnClickListener(this);
        item_chat_lono = (LinearLayout) findViewById(R.id.item_chat_lono);
        item_chat_lono.setOnClickListener(this);
        item_mail_lono = (LinearLayout)findViewById(R.id.item_mail_lono);
        item_mail_lono.setOnClickListener(this);
        item_drive_lono = (LinearLayout) findViewById(R.id.item_drive_lono);
        item_drive_lono.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 1);

        verificaGPS();

    }

    private void verificaGPS(){
        if(ValidGPS.enable(this) == true){
            try {
                mapView.setVisibility(View.GONE);
                box_loading_map.setVisibility(View.VISIBLE);
                MapsInitializer.initialize(getApplicationContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localeProfile();
                    }
                }, 1000);
            } catch (Exception e) {}
        }else{
            mapView.setVisibility(View.GONE);
            box_loading_map.setVisibility(View.GONE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Ative seu GPS para que possamos traçar o melhor trajeto e informar a você sua localização, distância e tempo até nós");
            builder.setCancelable(false);
            builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 2000);
                }
            });
            builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }

    private void createToolbar(Toolbar toolbar) {
        Drawable backIconActionBar = getResources().getDrawable(R.drawable.ic_back_white);
        toolbar = (Toolbar) findViewById(R.id.actionbar_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backIconActionBar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
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

                if (nome.isEmpty()) {

                    layout_name.setErrorEnabled(true);
                    layout_email.setErrorEnabled(false);
                    layout_message.setErrorEnabled(false);
                    layout_name.setError("Infome seu nome e sobrenome");
                    name.requestFocus();

                } else if (mail.isEmpty()) {

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(true);
                    layout_message.setErrorEnabled(false);
                    layout_email.setError("Infome seu email");
                    email.requestFocus();

                } else if (Valitations.email(mail) == false) {

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(true);
                    layout_message.setErrorEnabled(false);
                    layout_email.setError("Email inválido");
                    email.requestFocus();

                } else if (msg.isEmpty()) {

                    layout_name.setErrorEnabled(false);
                    layout_email.setErrorEnabled(false);
                    layout_message.setErrorEnabled(false);
                    layout_message.setError("Escreva algo para gente");
                    message.requestFocus();

                } else {

                    Alerts.progress_open(View_Contact.this, null, "Enviando...", false);
                    serviceContact.sendEmail(nome, mail, msg, alertDialog);

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

                                String[] start_address = address.getString("start_address").split("-");
                                String[] end_address = address.getString("end_address").split("-");
                                String distance_address = address.getJSONObject("distance").getString("text");
                                String duration_address = address.getJSONObject("duration").getString("text");

                                info_location_user.setText(start_address[0]);
                                info_location.setText(end_address[0]);

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

                                final Marker destin_marker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("Lono")
                                        .flat(false)
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_home)));

                                final Marker you_marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .title("Você")
                                        .flat(false)
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_you_map)));
                                you_marker.showInfoWindow();

                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                googleMap.getUiSettings().setMapToolbarEnabled(false);
                                googleMap.setBuildingsEnabled(true);
                                googleMap.setIndoorEnabled(true);
                                googleMap.setTrafficEnabled(false);

                                googleMap.setInfoWindowAdapter(new Adapter_InfoWindow(View_Contact.this,
                                        distance_address, duration_address,
                                        start_address[0], end_address[0]));

                                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(View_Contact.this, R.raw.map_style));

                                LatLng norhtEast = SphericalUtil.computeOffset(new LatLng(location.getLatitude(), location.getLongitude()), 0, 45);
                                LatLng southWest = SphericalUtil.computeOffset(new LatLng(-8.0459274, -34.8891792), 0, 225);

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(norhtEast);
                                builder.include(southWest);

                                final LatLngBounds bounds = builder.build();
                                final int zoomWidth = getResources().getDisplayMetrics().widthPixels / 5;
                                final int zoomHeight = getResources().getDisplayMetrics().heightPixels / 5;
                                final int zoomPadding = (int) (zoomWidth * 0.05);

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,zoomHeight,zoomPadding));
                                mapView.setVisibility(View.VISIBLE);
                                box_loading_map.setVisibility(View.GONE);
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

    private void writeContact(String displayName, String number) {
        ArrayList contentProviderOperations = new ArrayList();
        //insert raw contact using RawContacts.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        //insert contact display name using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName).build());
        //insert mobile number using Data.CONTENT_URI
        contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            getApplicationContext().getContentResolver().
                    applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void openChat() {
        writeContact("Lono Atendimento", "+558187059867");
        try{
            String number = "558187059867";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
            sendIntent.putExtra("jid", number + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
        catch(Exception e) {
            Snackbar.make(getWindow().getDecorView(),
                    "Você não tem o Whatsapp instalado",
                    Snackbar.LENGTH_SHORT).show();
        }

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
                    }
                }
            });

    }

    @SuppressLint("MissingPermission")
    private void callContactPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:81 3132-0664"));
        startActivity(callIntent);
    }

    private void comoChegarMaps() {
        Uri uri = Uri.parse("google.navigation:q=-8.0458145,-34.8895672");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_call_lono:
                callContactPhone();
                break;

            case R.id.item_chat_lono:
                openChat();
                break;

            case R.id.item_mail_lono:
                openMessageContact();
                break;

            case R.id.item_drive_lono:
                comoChegarMaps();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2000:
                verificaGPS();
                break;
        }
    }
}
