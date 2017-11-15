package com.location.reminder.util;

public class Constants {

    public static final String sharedPreferences = "locationremindersSP";

   public static final String HOSTNAME = "link of aws";

 

    public static final String PORT = "8080";

    public static final String WEB_SERVER_URL = HOSTNAME + "/Reminderapp/Reminderapp/";
   //public static final String WEB_SERVER_URL = HOSTNAME + "/notifications/";
    public static final String JAVA_URL = HOSTNAME + ":" + PORT + "/reminder/";
    public static final int STAY_TRESHOLD_TIME_SECS = 0;
    public static final int STAY_TRESHOLD_DISTANCE_METERS = 50;
    public static final int DEFAULT_PREFLOC_DISTANCE = 2500;


}
