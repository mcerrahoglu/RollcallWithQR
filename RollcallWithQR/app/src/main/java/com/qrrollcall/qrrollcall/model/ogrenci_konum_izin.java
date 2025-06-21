package com.qrrollcall.qrrollcall.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class ogrenci_konum_izin {
    public interface KonumBilgisiCallback {
        void konumGuncellendi(double enlem, double boylam);
    }

    private Context context;
    private double latitude;
    private double longitude;
    private ogrenci_konum_izin.KonumBilgisiCallback konumCallback;

    public ogrenci_konum_izin(Context context) {
        this.context = context;
        this.latitude = 0.0; // Varsayılan değerler
        this.longitude = 0.0;
        requestLocationUpdates();
    }

    public void requestLocationUpdates() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    /*
    Bu bölüm,
    Android cihazın konum hizmetlerini kullanabilmek için gerekli izinlerin kontrol edilmesini
    ve varsa izinlerin olup olmadığını doğrulamayı sağlar.
     */
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // İzinler alınmadıysa
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.d("LocationListener", "Latitude: " + latitude + ", Longitude: " + longitude);

                if (konumCallback != null) {
                    konumCallback.konumGuncellendi(latitude, longitude);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // NETWORK_PROVIDER veya GPS_PROVIDER'dan konum güncellemelerini isteyin
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setKonumCallback(KonumBilgisiCallback callback) {
        this.konumCallback = callback;
    }
}
