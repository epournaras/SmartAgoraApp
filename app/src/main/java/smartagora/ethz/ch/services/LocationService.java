package smartagora.ethz.ch.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.prefrences.PreferencesKeys;
import smartagora.ethz.ch.utils.ExceptionalHandling;

/**
 * LocationService will run in the background to update user latitude and longitude
 * and other location updates for map.
 */

public class LocationService extends Service {

    private LocationManager mLocationManager;
    private LocationUpdatesListener mLocationUpdatesListener;
    private Context mContext;
    private final EventBus mEventBus = EventBus.getDefault();


    @Override
    public void onCreate() {
        try {
            Log.d("TEST", "Service onCreate()");
            super.onCreate();
            mContext = getApplicationContext();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.d("TEST", "Service onStartCommand()");
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationUpdatesListener = new LocationUpdatesListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationUpdatesListener);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationUpdatesListener);
            }

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                            0, mLocationUpdatesListener);
                else
                    mEventBus.postSticky(PreferencesKeys.GPS_OFF);


                if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                            30, mLocationUpdatesListener);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TEST", "Service onBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.d("TEST", "Service onDestroy()");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("TEST", "Service onDestroy() IN IF");
                if(mLocationManager != null)
                    mLocationManager.removeUpdates(mLocationUpdatesListener);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

    }

    class LocationUpdatesListener implements LocationListener {
        public void onLocationChanged(final Location location) {
            try {
                if (null != location) {
                    AppPreferences.saveLocation(mContext, new LatLng(location.getLatitude(), location.getLongitude()));
                    AppPreferences.saveAccuracy(mContext, location.getAccuracy());
                    mEventBus.postSticky(PreferencesKeys.LOCATION_UPDATED);
                }
            } catch (Exception ex) {
                ExceptionalHandling.logException(ex);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
