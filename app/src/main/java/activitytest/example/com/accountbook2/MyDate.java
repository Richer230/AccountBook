package activitytest.example.com.accountbook2;

import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

    public static String getTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        Date d = new Date();
//        d.setTime(time);
        return format.format(time);
    }
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public int getYear(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getMonth(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getDay(long time){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getHour(long time){
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getMinute(long time){
        SimpleDateFormat format = new SimpleDateFormat("mm");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
}
