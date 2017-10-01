package com.hnt.smartarlington.smartarlington;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by madhav on 9/30/2017.
 */

public class alertfragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    ImageView sendbutton;
    private RequestQueue requestQueue;
    LocationManager locationManager;
    String Latitude ,Longitude , loc ;
    Location mLastLocation;
    EditText edittype;
    GoogleApiClient mGoogleApiClient;

    ListView lv;
    String urlAddress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alertfragment,container,false);

        sendbutton = (ImageView)view.findViewById(R.id.sendbutton);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        edittype = (EditText)view.findViewById(R.id.edittype);
          lv= (ListView) view.findViewById(R.id.alerlsitview);


        //Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Reseting all values
        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sfm.edit();
        edit.putString("Longitude", null);
        edit.putString("Latitude", null);
        edit.commit();




        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edittype.getText().toString().isEmpty()) {
                    //Displaying a progress dialog
                    final ProgressDialog loading = ProgressDialog.show(getActivity(), "Sending", "Please wait...", false, false);

                    SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String usernamekey = saved_values.getString("usernamekey", "1");
                    String messagebody = edittype.getText().toString();

              /*  final SharedPreferences spff = PreferenceManager.getDefaultSharedPreferences(this);
                Latitude = spff.getString("Latitude", null);
                Longitude = spff.getString("Longitude", null);
                loc = spff.getString("loc", null); */

                    //Getting user data
                    //http://18.221.200.182:8080/UtaHack/rest/Alerts/setAlert?lat=%1$s&lon=%1$s&message=%1$s&uid=%1$s&sos=%1$s
                    Calendar c = Calendar.getInstance();
                    Date date = c.getTime(); //current date and time in UTC
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                    String strDate = df.format(date);
                   // Toast.makeText(getActivity(), strDate, Toast.LENGTH_LONG).show();
                    String uri = String.format("http://18.221.200.182:8080/UtaHack/rest/Alerts/setAlert?lat=%1$s&lon=%2$s&message=%3$s&uid=%4$s&sos=%5$s&datetime=%6$s",
                            Latitude, Longitude, messagebody, usernamekey, "n" , strDate);


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();
                                    Toast.makeText(getActivity(), "Successfully Posted", Toast.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loading.dismiss();
                                    Toast.makeText(getActivity(), "Error Message : " + error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {


                    };

                    //Adding request the the queue
                    requestQueue.add(stringRequest);


                }else
                {
                    Toast.makeText(getActivity(), "Type Something" , Toast.LENGTH_LONG).show();

                }

            }
        });

        return view;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor edit = sfm.edit();
            edit.putString("Longitude", Longitude);
            edit.putString("Latitude", Latitude);
            edit.putString("loc", loc);
            edit.commit();

             urlAddress="http://18.221.200.182:8080/UtaHack/rest/Alerts/getAlerts?lat="+Latitude+"&lon="+Longitude;
            Toast.makeText(getActivity(), urlAddress, Toast.LENGTH_LONG).show();

            new Downloaderalert(getActivity(),urlAddress,lv).execute();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {


        mGoogleApiClient.disconnect();

        super.onDestroy();

    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
}