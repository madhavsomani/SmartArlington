package com.hnt.smartarlington.smartarlington;

/**
 * Created by madhav on 2/18/2017.
 */

public class dataobject {

    int id;
    String feedContent,dateTime,msg,sos,locLat,alertID,locLong,name;

    String feedHeading;

    public String getfeedContent() {
        return feedContent;
    }
    public void setfeedContent(String feedContent) {
        this.feedContent = feedContent;
    }
    public String getfeedHeading() {
        return feedHeading;
    }
    public void setfeedHeading(String feedHeading) {
        this.feedHeading = feedHeading;
    }

    public String getdateTime() {
        return dateTime;
    }
    public void setdateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getmsg() {
        return msg;
    }
    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String getsos() {
        return sos;
    }
    public void setsos( String sos) {
        this.sos = sos;
    }
    public String getlocLat() {
        return locLat;
    }
    public void setlocLat(String locLat) {
        this.locLat = locLat;
    }

    public String getalertID() {
        return alertID;
    }
    public void setalertID(String alertID) {
        this.alertID = alertID;

    }

    public String getlocLong() {
        return locLong;
    }
    public void setlocLong(String locLong) {
        this.locLong = locLong;

    }

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.locLong = name;

    }
}
