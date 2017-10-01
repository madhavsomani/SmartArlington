package com.hnt.smartarlington.smartarlington;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapteralert extends BaseAdapter {
    Context c;
    ArrayList<dataobject> dataobject;
    LayoutInflater inflater;
    Float Latitude,Longitude;
    public CustomAdapteralert(Context c, ArrayList<dataobject> dataobject) {
        this.c = c;
        this.dataobject = dataobject;
        inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataobject.size();
    }
    @Override
    public Object getItem(int position) {
        return dataobject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.alertlistview,parent,false);
        }
        TextView editname= (TextView) convertView.findViewById(R.id.nameTxt);
        TextView editdate= (TextView) convertView.findViewById(R.id.date);
        TextView edittime= (TextView) convertView.findViewById(R.id.time);
        LinearLayout bodylayout = (LinearLayout)convertView.findViewById(R.id.bodylayout);

        TextView editbody= (TextView) convertView.findViewById(R.id.editbody);
        ImageView img = (ImageView)convertView.findViewById(R.id.mapimage);
        ImageView sosimagebutton = (ImageView)convertView.findViewById(R.id.sosimagebutton);


        //BIND DATA
        dataobject obj=dataobject.get(position);
       // editname.setText(obj.getname());
        String datetime[] = obj.getdateTime().split(" ");
        editdate.setText(datetime[0]);
        edittime.setText(datetime[1]);
        editbody.setText(obj.getmsg());
         Latitude = Float.valueOf(obj.getlocLat());
        Longitude = Float.valueOf(obj.getlocLong());
        if(obj.getsos().equalsIgnoreCase("yes")) {
            sosimagebutton.setVisibility(View.VISIBLE);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "http://maps.google.com/?q=" + Latitude + "," + Longitude ;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                c.startActivity(intent);

            }
        });


        return convertView;
    }
}