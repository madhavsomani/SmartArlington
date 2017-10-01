package com.hnt.smartarlington.smartarlington;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by madhav on 16-02-2016.
 */
public class settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.settings , container , false);

        Button update = (Button)rootview.findViewById(R.id.update);
        Button cal = (Button)rootview.findViewById(R.id.cal);
        final EditText phoneno = (EditText)rootview.findViewById(R.id.phoneno1);
        TextView maxvalue = (TextView)rootview.findViewById(R.id.calvalue);
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = spf.edit();


        phoneno.setText(spf.getString("phoneno1" , ""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   edit.putString("phoneno1", phoneno.getText().toString());
                    edit.putString("phonenoflag" , "1");

                   edit.commit();
                   Toast.makeText(getActivity(), "Phone No Updated", Toast.LENGTH_SHORT).show();
            }
        });

        int maxvaluecal = spf.getInt("maxvalue" , 1);

        maxvalue.setText("Calibrated value : " + maxvaluecal);

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity() , calibirate.class);
                startActivity(i);

            }
        });

        return rootview;
    }
}
