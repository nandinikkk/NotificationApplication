package com.location.reminder.model;


public class ReminderModel {

    private int id;
    private String userid;
    private String latitude;
    private String longitude;
    private String title;
    private int fromtime;
    private int totime;
    private int repeat;
    private int repeatinterval = 0;
    private String remindercreatedby;
    private String Status;
    private String reminderinfo;
    private String location_type;
    private String assignedto;
    private long fromdate = 0;
    private long todate = 0;

    private String locationid;

    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    private String reminderby;

    public String getReminderby() {
        return reminderby;
    }

    public void setReminderby(String reminderby) {
        this.reminderby = reminderby;
    }

    public long getFromdate() {
        return fromdate;
    }

    public void setFromdate(long fromdate) {
        this.fromdate = fromdate;
    }

    public long getTodate() {
        return todate;
    }

    public void setTodate(long todate) {
        this.todate = todate;
    }

    public String getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(String assignedto) {
        this.assignedto = assignedto;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getReminderinfo() {
        return reminderinfo;
    }

    public void setReminderinfo(String reminderinfo) {
        this.reminderinfo = reminderinfo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemindercreatedby() {
        return remindercreatedby;
    }

    public void setRemindercreatedby(String remindercreatedby) {
        this.remindercreatedby = remindercreatedby;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFromtime() {
        return fromtime;
    }

    public void setFromtime(int fromtime) {
        this.fromtime = fromtime;
    }

    public int getTotime() {
        return totime;
    }

    public void setTotime(int totime) {
        this.totime = totime;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRepeatinterval() {
        return repeatinterval;
    }

    public void setRepeatinterval(int repeatinterval) {
        this.repeatinterval = repeatinterval;
    }
}
