package com.example.evanlee.g2lbuddyandroid;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private HashMap<String, String> entry;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<HashMap<String, String>> getData() { return data; }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.journal_month_data_layout, null);

        TextView month = (TextView) vi.findViewById(R.id.month_display);
        TextView year = (TextView) vi.findViewById(R.id.year_display);
        TextView glucose = (TextView) vi.findViewById(R.id.blood_data);
        TextView high = (TextView) vi.findViewById(R.id.highest_reading);
        TextView low = (TextView) vi.findViewById(R.id.lowest_reading);
        TextView activity = (TextView) vi.findViewById(R.id.activity_data);
        TextView food = (TextView) vi.findViewById(R.id.carb_data);
        TextView deviation = (TextView) vi.findViewById(R.id.blood_dev_data);

        entry = data.get(position);

        // Setting all values in listview
        month.setText(entry.get(journalAverages.KEY_MONTH));
        year.setText(entry.get(journalAverages.KEY_YEAR));
        glucose.setText(entry.get(journalAverages.KEY_GLUCOSE));
        //if (journalAverages.KEY_GLUCOSE != "0") {
            glucose.setTextColor(Color.parseColor("#D63053"));
            ImageView blood = (ImageView) vi.findViewById(R.id.blood_icon);
            blood.setImageResource(R.drawable.journal_blood);

            high.setText(entry.get(journalAverages.KEY_GLUCOSE_HIGH));
       //     ImageView high_icon = (ImageView) vi.findViewById(R.id.highest_reading_icon);
       //     high_icon.setImageResource(R.drawable.journal_blood_high);
            low.setText(entry.get(journalAverages.KEY_GLUCOSE_LOW));
        //    ImageView low_icon = (ImageView) vi.findViewById(R.id.lowest_reading_icon);
       //     low_icon.setImageResource(R.drawable.journal_blood_low);
        //}

        activity.setText(entry.get(journalAverages.KEY_ACTIVITY));
       /* if (journalAverages.KEY_ACTIVITY != "00:00:00") {
            activity.setTextColor(Color.parseColor("#30AED6"));
            ImageView time = (ImageView) vi.findViewById(R.id.imageView2);
            time.setImageResource(R.drawable.journal_activity_empty);
        }*/

        food.setText(entry.get(journalAverages.KEY_FOOD));
        /*if (journalAverages.KEY_FOOD != "0") {
            food.setTextColor(Color.parseColor("#FAD937"));
            ImageView cals = (ImageView) vi.findViewById(R.id.imageView4);
            cals.setImageResource((R.drawable.journal_carb_empty));
        }*/

        deviation.setText(entry.get(journalAverages.KEY_DEVIATION));
        //if (journalAverages.KEY_DEVIATION != "0") {
        //    deviation.setTextColor(Color.parseColor("#dfe1e0"));
        //    ImageView dev = (ImageView) vi.findViewById(R.id.blood_dev_icon);
        //    dev.setImageResource(R.drawable.journal_blood_dev);
        //}
        return vi;
    }
}