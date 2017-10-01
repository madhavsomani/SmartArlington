package com.hnt.smartarlington.smartarlington;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class register extends AppCompatActivity  {

    //Creating views
    private EditText editname;
    private EditText editbloodtype;
    private EditText edituserphoneno;
    private EditText editgurdianname;
    private EditText editgurdianphoneno,editage;
    private EditText editadditonalinformation;

    private AppCompatButton buttonRegister;


    //Volley RequestQueue
    private RequestQueue requestQueue;

    //String variables to hold username password and phone
    private String name,age;
    private String bloodtype;
    private String userphoneno;
    private String gurdianname;
    private String gurdianphoneno;
    private String additonalinformation;

    String usernamekey;

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Initializing Views
        editname = (EditText) findViewById(R.id.editTextname);
        editbloodtype = (EditText) findViewById(R.id.bloodtype);
        edituserphoneno = (EditText) findViewById(R.id.userphoneno);
        editgurdianname = (EditText) findViewById(R.id.gurdianname);
        editgurdianphoneno = (EditText) findViewById(R.id.gurdianphoneno);
        editadditonalinformation = (EditText) findViewById(R.id.med);
        editage = (EditText) findViewById(R.id.editage);

        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);


        //Initializing the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        //Adding a listener to button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });




    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    //this method will register the user
    private void register() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);


        //Getting user data




        name = editname.getText().toString().trim();
        bloodtype = editbloodtype.getText().toString().trim();
        userphoneno = edituserphoneno.getText().toString().trim();
        gurdianname = editgurdianname.getText().toString().trim();
        gurdianphoneno = editgurdianphoneno.getText().toString().trim();
        additonalinformation =   editadditonalinformation.getText().toString().trim();
        age = editage.getText().toString().trim();
        //Again creating the string request
        String uri = String.format("http://18.221.200.182:8080/UtaHack/rest/Register/CreateUser?uname=%1$s&uph=%2$s&gph=%3$s&age=%4$s&med=%5$s&blood=%6$s&ugname=%7$s",
                name,userphoneno,gurdianphoneno,age,additonalinformation,bloodtype,gurdianname);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String id = jsonResponse.getString(Config.TAG_RESPONSE);
                            //Toast.makeText(register.this, id , Toast.LENGTH_LONG).show();

                            //If it is success
                            if(!id.isEmpty()){
                                //Asking user to confirm otp
                                Toast.makeText(register.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(register.this);
                                SharedPreferences.Editor editor = app_preferences.edit();
                                editor.putString("usernamekey",id);
                                editor.putString("phoneno1", "+1"+gurdianphoneno);
                                editor.putString("phonenoflag" , "1");
                                editor.putString("registerflag" , "1");
                                editor.commit();
                               // Toast.makeText(register.this, app_preferences.getString("register", "0"), Toast.LENGTH_LONG).show();
                                finish();

                            }else{
                                //If not successful user may already have registered
                                Toast.makeText(register.this, "Username or Phone number already registered", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                         //   Toast.makeText(register.this, "Error Message : "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(register.this, "Error Message : "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {


        };

        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }




}