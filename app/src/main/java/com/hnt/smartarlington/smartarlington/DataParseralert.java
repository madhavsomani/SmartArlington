package com.hnt.smartarlington.smartarlington;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Oclemy on 6/5/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class DataParseralert extends AsyncTask<Void,Void,Integer> {
    Context c;
    String jsonData;
    ListView lv;
    ProgressDialog pd;
    ArrayList<dataobject> dataobject = new ArrayList<>();
    public DataParseralert(Context c, String jsonData, ListView lv) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }
    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }
    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        pd.dismiss();
        if(result==0)
        {
            Toast.makeText(c,"Unable To Parse",Toast.LENGTH_SHORT).show();
        }else {
            //BIND DATA TO LISTVIEW
            CustomAdapteralert adapter=new CustomAdapteralert(c,dataobject);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();

            String json = gson.toJson(dataobject);

            editor.putString("arraylist", json);
            editor.commit();

            lv.setAdapter(adapter);

        }
    }
    private int parseData()
    {
        try
        {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;
            dataobject.clear();
            dataobject obj;
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                String dateTime=jo.getString("dateTime");
                String msg=jo.getString("msg");
                String sos=jo.getString("sos");
                String locLat=jo.getString("locLat");
                String alertID=jo.getString("alertID");
                String locLong=jo.getString("locLong");
              //  String name=jo.getString("name");
               // String discription=jo.getString("partydiscription");
                obj=new dataobject();
                //obj.setId(id);
                obj.setdateTime(dateTime);
                obj.setmsg(msg);
                obj.setsos(sos);
                obj.setlocLat(locLat);
                obj.setalertID(alertID);
                obj.setlocLong(locLong);
              //  obj.setname(name);
                //obj.setDiscription(discription);
                dataobject.add(obj);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
