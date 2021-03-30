package com.example.shellpest_android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Localizacion implements LocationListener {
    activity_Monitoreo mainActivity;
    public activity_Monitoreo getMainActivity() {
        return mainActivity;
    }
    public void setMainActivity(activity_Monitoreo mainActivity) {
        this.mainActivity = mainActivity;

    }
    @Override
    public void onLocationChanged(Location loc) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion
        loc.getLatitude();
        loc.getLongitude();
        String sLatitud = String.valueOf(loc.getLatitude());
        String sLongitud = String.valueOf(loc.getLongitude());
        Toast.makeText(mainActivity,sLatitud+","+sLongitud,Toast.LENGTH_SHORT).show();
        //CoorX.setText(sLatitud);
        //longitud.setText(sLongitud);
        //this.mainActivity.setLocation(loc);
        this.mainActivity.CoorX=sLatitud;
        this.mainActivity.CoorY=sLongitud;
    }


    @Override
    public void onProviderDisabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es desactivado
        //latitud.setText("GPS Desactivado");
        Toast.makeText(mainActivity,"GPS Desactivado",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es activado
        //latitud.setText("GPS Activado");
        Toast.makeText(mainActivity,"GPS Activado",Toast.LENGTH_SHORT).show();
    }

    public void ubicacionActual(Location loc){

        loc.getLatitude();
        loc.getLongitude();
        String sLatitud = String.valueOf(loc.getLatitude());
        String sLongitud = String.valueOf(loc.getLongitude());
        //Toast.makeText(mainActivity,sLatitud+","+sLongitud,Toast.LENGTH_SHORT).show();
        //CoorX.setText(sLatitud);
        //longitud.setText(sLongitud);
        //this.mainActivity.setLocation(loc);
        //this.mainActivity.CoorX=sLatitud;
        //this.mainActivity.CoorY=sLongitud;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

}
