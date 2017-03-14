package com.example.evanlee.g2lbuddyandroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maxrhirata on 2/23/17.
 */

public class DailyInputObj implements Comparable<DailyInputObj> {

    //private Date date;
    //int data_icon;
    //static final int BLOOD_GLUCOSE = 1;
    //static final int ACTIVITY = 2;
    //static final int FOOD = 3;
    //static final int MEDICATION = 4;

    private int type;
    private String month;
    private String day;
    private String year;
    private String hour;
    private String minutes;
    private String input_time;
    private String data_name;

    //For Comparable
    private String data_level;

    private String dateString;

    public DailyInputObj()
    {
        //date = new Date();
        input_time = "";
        data_name = "";
        data_level = "0";
    }

    public DailyInputObj(int idType, String month, String day, String year, String hour, String minutes, String data_name, String data_level) {

        this.type = idType;
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.minutes = minutes;
        this.input_time = hour + ":" + minutes;
        this.data_name = data_name;
        this.data_level = data_level;

        this.dateString = month + "/" + day + "/" + year + "/" + hour + "/" + minutes;
    }

    public int getType() {
        return type;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getHour() {
        return hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getInput_time() {
        return input_time;
    }

    public String getData_name() {
        return data_name;
    }

    public String getData_level() {
        return data_level;
    }

    public String getDateString() { return dateString; }

    public String getStdDate(){
        String[] splitDate = dateString.split("/");
        String date = splitDate[0]+ "/" + splitDate[1] + "/" + splitDate[2];
        return date;
    }

    @Override
    public int compareTo(DailyInputObj other){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(this.dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        Date dateOther = null;
        SimpleDateFormat formatOther = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            dateOther = format.parse(other.dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return dateOther.compareTo(date);
    }

}


