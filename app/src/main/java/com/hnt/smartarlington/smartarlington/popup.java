package com.hnt.smartarlington.smartarlington;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by madhav on 12-03-2016.
 */
public class popup extends Activity {

    CountDownTimer count;
    String loc, Latitude, Longitude;

    TextView locationtext;
    MediaPlayer mPlayer;
    private RequestQueue requestQueue;

    private PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        mPlayer = MediaPlayer.create(popup.this, R.raw.beep);
        mPlayer.start();

        requestQueue = Volley.newRequestQueue(this);

        locationtext = (TextView) findViewById(R.id.popuplocation);
        final TextView countdown = (TextView) findViewById(R.id.countdown);
        Button stop = (Button) findViewById(R.id.stop);

        Toast.makeText(this, "Impact Detected! Countdown Started", Toast.LENGTH_SHORT).show();

        final SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        Latitude = spf.getString("Latitude", null);
        Longitude = spf.getString("Longitude", null);
        loc = spf.getString("loc", null);

        locationtext.setText("Latitude :"+Latitude+"\n Longitude : "+Longitude);

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        count = new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                countdown.setText("" + millisUntilFinished / 1000);
                v.vibrate(500);


            }

            @Override
            public void onFinish() {
                Toast.makeText(popup.this, "Sending Sms", Toast.LENGTH_SHORT).show();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(spf.getString("phoneno1", "+919999999999"), null, "User is in Danger ,\n Location :  " + loc + "\nGoogle map Link : http://maps.google.com/?q=" + Latitude + "," + Longitude, null, null);


                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(popup.this);
                String usernamekey = saved_values.getString("usernamekey", "1");

                //Getting user data
                //http://18.221.200.182:8080/UtaHack/rest/Alerts/setAlert?lat=%1$s&lon=%1$s&message=%1$s&uid=%1$s&sos=%1$s
                Calendar c = Calendar.getInstance();
                Date date = c.getTime(); //current date and time in UTC
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                String strDate = df.format(date);

              //  Toast.makeText(popup.this, strDate, Toast.LENGTH_LONG).show();
                String uri = String.format("http://18.221.200.182:8080/UtaHack/rest/Alerts/setAlert?lat=%1$s&lon=%2$s&message=%3$s&uid=%4$s&sos=%5$s&datetime=%6$s",
                        Latitude,Longitude,"User is in Danger",usernamekey,"y",strDate);



                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(popup.this, "Successfully Posted", Toast.LENGTH_LONG).show();

                                // Toast.makeText(register.this, app_preferences.getString("register", "0"), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(popup.this, "Error Message : "+error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }) {


                };

                //Adding request the the queue
                requestQueue.add(stringRequest);



                onDestroy();

                finish();
            }

        };
        count.start();

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDestroy();

                finish();


            }
        });

    }

    @Override
    protected void onStop() {

      /*  SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        int activityrunning = 0;
        edit.putInt("activity", activityrunning);
        edit.commit();
        count.cancel();
        mPlayer.stop(); */

        if (wl.isHeld())
            wl.release();


        super.onStop();
    }

    @Override
    protected void onDestroy() {

        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        int activityrunning = 0;
        edit.putInt("activity", activityrunning);
        edit.commit();

        count.cancel();
        mPlayer.stop();

        if (wl.isHeld())
            wl.release();

        super.onDestroy();
    }

    @Override
    protected void onStart() {

      /*  PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK), "TAG");
        wl.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard(); */


        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        SharedPreferences sfm = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sfm.edit();
        int activityrunning = 1;
        edit.putInt("activity", activityrunning);
        edit.commit();

        super.onStart();
    }

}