package com.hnt.smartarlington.smartarlington;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by madhav on 15-02-2016.
 */
public class service extends Service implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    double impactsetat;
    int counter = 0 ;
    double pow2 = 0;
    SensorManager manger;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;
    String Latitude ,Longitude , loc ;
    Location mLastLocation;

    GoogleApiClient mGoogleApiClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Sensor gravity;
        manger = (SensorManager)getSystemService(SENSOR_SERVICE);
        gravity = manger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manger.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);



        PowerManager mgr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

        //Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }






        //Reseting all values
        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        edit.putString("Longitude", null);
        edit.putString("Latitude", null);
        edit.commit();


        counter = sfm.getInt("activity", 0);

        int maxvaluecal = sfm.getInt("maxvalue", 1);

        impactsetat = maxvaluecal*0.55;


        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        pow2 = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2));


        if ( pow2 > impactsetat)
        {
            SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
            counter = sfm.getInt("activity", 0);

            if(counter == 0)
            {

                counter = 5;
                Intent i = new Intent(getBaseContext(), popup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);



            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {

        Toast.makeText(service.this, "Service Stoped!", Toast.LENGTH_SHORT).show();
        manger.unregisterListener(this);
        mGoogleApiClient.disconnect();

        wakeLock.release();
        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        edit.putInt("flagofstart",0);
        edit.commit();


        super.onDestroy();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        mGoogleApiClient.connect();
        super.onStart(intent, startId);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            Latitude = String.valueOf(mLastLocation.getLatitude());
            Longitude = String.valueOf(mLastLocation.getLongitude());
            loc = "Latitude :"+ String.valueOf(mLastLocation.getLatitude())+" Longitude : "+ String.valueOf(mLastLocation.getLongitude());
            SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor edit = sfm.edit();
            edit.putString("Longitude", Longitude);
            edit.putString("Latitude", Latitude);
            edit.putString("loc", loc);
            edit.commit();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        edit.putInt("flagofstart",1);
        edit.commit();

        Intent notificationIntent = new Intent(this, sosmainactivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logofinal)
                .setContentTitle("SmartSOS")
                .setContentText("Tracking Location and Impact!")
                .setContentIntent(pendingIntent).build();

        startForeground(1337, notification);
    }



}
