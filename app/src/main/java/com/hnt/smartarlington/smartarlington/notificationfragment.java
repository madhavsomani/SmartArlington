package com.hnt.smartarlington.smartarlington;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by madhav on 9/30/2017.
 */

public class notificationfragment extends Fragment{



    final static String urlAddress="http://18.221.200.182:8080/UtaHack/rest/FeedWS/getFeeds";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificationfragment,container,false);

        final ListView lv= (ListView) view.findViewById(R.id.notficationlistview);
        new Downloader(getActivity(),urlAddress,lv).execute();

        return view;
    }
}
