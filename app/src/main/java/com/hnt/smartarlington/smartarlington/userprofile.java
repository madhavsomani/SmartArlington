package com.hnt.smartarlington.smartarlington;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by madhav on 9/30/2017.
 */

public class userprofile extends Activity {


        public static final String DATA_URL = "http://18.221.200.182:8080/UtaHack/rest/Alerts/getUserInfo?uid=";


        public static final String JSON_ARRAY = "result";

        String usernamekey;


        TextView tvname,tvage,tvuserphoneno,tvbloodtype,tvgurdianname,tvgurdianphoneno;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.userprofile);

            tvname = (TextView)findViewById(R.id.profilename);
            tvage = (TextView)findViewById(R.id.age);
            tvbloodtype = (TextView)findViewById(R.id.displaybloodtype);
            tvuserphoneno = (TextView)findViewById(R.id.displayuserphoneno);
            tvgurdianname = (TextView)findViewById(R.id.gurdianname);
            tvgurdianphoneno = (TextView)findViewById(R.id.guardianphoneno);




            SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(userprofile.this);
            usernamekey = saved_values.getString("usernamekey", "1");
            Toast.makeText(userprofile.this, usernamekey,Toast.LENGTH_LONG).show();
            //Fetch Image
           // Picasso.with(getApplicationContext()).load("http://hntdatabase.16mb.com/PhotoUpload/"+usernamekey+".png").into(image);

            //fetch userinfo
            getData();
        }


    private void getData() {


        if (usernamekey.equals("")) {
            Toast.makeText(this, "Please enter an username", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = DATA_URL+usernamekey;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
              //  Toast.makeText(userprofile.this, response.toString(),Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(userprofile.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
         String name = "";
         String age= "";
         String bloodtype= "";
         String userphoneno= "";
         String gurdianname= "";
         String gurdianphoneno= "";

        try {



            JSONObject jsonObject = new JSONObject(response);

            name = jsonObject.getString("userName");
            age = jsonObject.getString("userAge");
            bloodtype = jsonObject.getString("userBloodGroup");
            userphoneno = jsonObject.getString("userPhone");
            gurdianname = jsonObject.getString("userGuardianName");
            gurdianphoneno = jsonObject.getString("userGuardianPhone");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvname.setText("Name : "+name);
        tvage.setText("Age : "+age);
        tvbloodtype.setText("Blood Group : "+bloodtype);
        tvuserphoneno.setText("User phone No : "+userphoneno);
        tvgurdianname.setText("Guardian Name : "+gurdianname);
       tvgurdianphoneno.setText("Guardian Phone No : "+gurdianphoneno);

    }

}