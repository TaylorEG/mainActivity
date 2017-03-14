package com.example.blake.exportdata;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Blake on 2/25/17.
 */

public class LineFormat implements Comparable<LineFormat>{

    private String dateString;
    private String info;

    public LineFormat(String dateInput, String infoInput) {
        dateString = dateInput;
        info = infoInput;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public int compareTo(LineFormat other) {
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
        return date.compareTo(dateOther);
    }
}
