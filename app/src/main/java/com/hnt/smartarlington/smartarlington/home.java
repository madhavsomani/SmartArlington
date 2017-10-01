package com.hnt.smartarlington.smartarlington;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by madhav on 15-02-2016.
 */
public class home extends Fragment implements SensorEventListener {
    TextView display;
    Button start,stop;
    double impactsetat;
    private LineGraphSeries<DataPoint> mSeries2;
    double counter = 1;
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
     double pow2 = 1;
    GoogleApiClient googleApiClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.home , container , false);



        Sensor gravity;
        SensorManager manger = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = sfm.edit();


        start = (Button)rootview.findViewById(R.id.button);
        stop = (Button)rootview.findViewById(R.id.button2);

        int maxvaluecal = sfm.getInt("maxvalue", 50);

         impactsetat = maxvaluecal*0.55;

        //TextView display4 =(TextView)rootview.findViewById(R.id.senstivity);
       // display4.setText("Impact set at : " + String.format("%.2f", impactsetat));
            final Intent i = new Intent(getActivity() , service.class);


        if(sfm.getInt("flagofstart",0) == 0)
        {
            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
        }else{
            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
        }



        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                //snackbar
                Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.snackbarPosition2), "Starting Service!", Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.parseColor("#FFFFFF"));

                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                snack.show();



                start.setVisibility(View.GONE);
               stop.setVisibility(View.VISIBLE);

                getActivity().startService(i);






            }
        });


        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //snackbar
                Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.snackbarPosition2), "Stoping Service!", Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setGravity(Gravity.CENTER);
                snack.show();




                  stop.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                getActivity().stopService(i);



            }
        });




    gravity = manger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manger.registerListener(this, gravity, SensorManager.SENSOR_DELAY_UI);

        GraphView graph1 = (GraphView) rootview.findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<DataPoint>();
        mSeries2.setColor(Color.parseColor("#f49e00"));

        graph1.addSeries(mSeries2);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(500);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph1);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", ""});
        staticLabelsFormatter.setVerticalLabels(new String[]{"", "", ""});

        graph1.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);




         return rootview;

    }


    @Override
    public void onSensorChanged( SensorEvent event) {

         pow2 = Math.sqrt(Math.pow(event.values[0] , 2) + Math.pow(event.values[1], 2)+ Math.pow(event.values[2] , 2));


        counter = counter + 1;
        mSeries2.appendData(new DataPoint(counter, pow2), true, 500);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {



    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS enabled

        }else{
            //GPS disabled
            Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.snackbarPosition2), "Enable Location From Settings!", Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            snack.show();

        }





        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String phonedumbno = "0";

        String phoneno = sfm.getString("phonenoflag", "0");
        //Set No
        if(phonedumbno.equals(phoneno)) {
            //snackbar
            Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.snackbarPosition2), "Guardian No is Empty, Please Update the No by going to Settings Tab!", Snackbar.LENGTH_INDEFINITE);
            View view = snack.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            snack.show();

        }


    }
}
