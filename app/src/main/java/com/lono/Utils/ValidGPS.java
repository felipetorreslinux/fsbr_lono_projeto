package com.lono.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.lono.R;

import static android.content.Context.LOCATION_SERVICE;

public class ValidGPS {

    static AlertDialog.Builder builder;

    public static void enable(final Activity activity){
        builder = new AlertDialog.Builder(activity);
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            builder.setTitle(R.string.app_name);
            builder.setMessage("Necessário que seu gps esteja ativo");
            builder.setCancelable(false);
            builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            });
            builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            builder.create().show();
        }
    }
}
