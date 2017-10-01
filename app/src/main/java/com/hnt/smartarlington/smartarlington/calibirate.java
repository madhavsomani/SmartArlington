package com.hnt.smartarlington.smartarlington;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by madhav on 12-03-2016.
 */
public class calibirate extends Activity implements SensorEventListener {
    LineGraphSeries<DataPoint> mSeries2;
    double pow2 = 1;
    double maxvalue = 0;
    double counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibirate);
        final Button calibirate = (Button)findViewById(R.id.button3);
        //Animaion
        ImageView img = (ImageView) findViewById(R.id.imageView2);
        Animation an = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.shake);
        img.startAnimation(an);

        //Flag
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean("Calibrate", true);
        edit.commit();


        Snackbar snack = Snackbar.make(findViewById(R.id.snackbarPosition3), "Shake Your Phone and Press Back key!", Snackbar.LENGTH_INDEFINITE);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        snack.show();


        Sensor gravity;
        SensorManager manger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        gravity = manger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manger.registerListener(this, gravity, SensorManager.SENSOR_DELAY_UI);

        GraphView graph1 = (GraphView)findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<DataPoint>();
        mSeries2.setColor(Color.parseColor("#f49e00"));
        graph1.addSeries(mSeries2);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(500);


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph1);
       // staticLabelsFormatter.setHorizontalLabels(new String[]{"", "", ""});
       // staticLabelsFormatter.setVerticalLabels(new String[]{"", "", ""});
        graph1.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);





        calibirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(maxvalue<40)
            {
                maxvalue = 50;
            }
                SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(calibirate.this);
                final SharedPreferences.Editor edit = spf.edit();
                edit.putInt("maxvalue", (int) maxvalue);
                edit.commit();
                Toast.makeText(calibirate.this, "Value updated", Toast.LENGTH_SHORT).show();
                finish();
            }


        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        pow2 = Math.sqrt(Math.pow(event.values[0] , 2) + Math.pow(event.values[1], 2)+ Math.pow(event.values[2] , 2));
        counter = counter + 1;
        if(maxvalue<pow2)
        {
            maxvalue=pow2;


        }

        mSeries2.appendData(new DataPoint(counter, pow2), true, 500);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
