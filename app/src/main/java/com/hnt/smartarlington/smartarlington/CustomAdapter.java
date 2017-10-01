package com.hnt.smartarlington.smartarlington;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<dataobject> dataobject;
    LayoutInflater inflater;
    public CustomAdapter(Context c, ArrayList<dataobject> dataobject) {
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
            convertView=inflater.inflate(R.layout.notilistview,parent,false);
        }
        TextView editbody= (TextView) convertView.findViewById(R.id.edittitle);
        TextView edittitle= (TextView) convertView.findViewById(R.id.editbody);

        //BIND DATA
        dataobject obj=dataobject.get(position);
        editbody.setText(obj.getfeedHeading());
        edittitle.setText(obj.getfeedContent());
        return convertView;
    }
}