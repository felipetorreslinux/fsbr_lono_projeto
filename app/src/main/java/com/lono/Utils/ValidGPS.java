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


    public static boolean enable(final Activity activity){
        LocationManager service = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            return false;
        }else{
            return true;
        }
    }
}
