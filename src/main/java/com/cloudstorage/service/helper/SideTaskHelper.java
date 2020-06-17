package com.cloudstorage.service.helper;


import java.text.SimpleDateFormat;
import java.util.Date;

public class SideTaskHelper {

    public static String longToDateString(long timestamp){
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(timestamp);
    }

    public static String currentDateString(){
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date());
    }

}
