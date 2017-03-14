package com.example.evanlee.g2lbuddyandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maxrhirata on 2/23/17.
 */

public class DailyDataAdapter extends BaseAdapter {
    private ArrayList<Object> dataSet;
    private LayoutInflater inflater;
    private static final int TYPE_INPUT_ENTRY = 0;
    private static final int TYPE_DAILY_HEADER = 1;

    public DailyDataAdapter(Context context, ArrayList<Object> dataSet) {
        this.dataSet = dataSet;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position){
        if(getItem(position) instanceof DailyInputObj){
            return TYPE_INPUT_ENTRY;
        }
        return TYPE_DAILY_HEADER;
    }

    @Override
    public boolean isEnabled(int position){
        return (getItemViewType(position) == TYPE_INPUT_ENTRY);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        //System.out.println("Here is the type: "+ type);

        if(convertView == null){
            switch (type){
                case TYPE_INPUT_ENTRY:
                    convertView = inflater.inflate(R.layout.daily_input_data_types, parent, false);
                    break;
                case TYPE_DAILY_HEADER:
                    convertView = inflater.inflate(R.layout.journal_daily_data_layout, parent, false);
                    break;
            }
        }

        switch (type){


            case TYPE_INPUT_ENTRY:
                convertView = inflater.inflate(R.layout.daily_input_data_types, parent, false);
                DailyInputObj entry = (DailyInputObj)getItem(position);

                ImageView icon = (ImageView)convertView.findViewById(R.id.data_type_icon);
                TextView inputTime = (TextView)convertView.findViewById(R.id.time_of_input);
                TextView text_data_type = (TextView)convertView.findViewById(R.id.text_data_type);
                TextView data_type_level = (TextView)convertView.findViewById(R.id.dataType_level);

                int icon_id = 0;

                if(entry.getType() == 1){
                    icon_id = R.drawable.blood_glucose;
                }else if(entry.getType() == 2){
                    icon_id = R.drawable.activity;
                }else if(entry.getType() == 3){
                    icon_id = R.drawable.food;
                }else{
                    icon_id = R.drawable.medication;
                }

                icon.setImageResource(icon_id);
                inputTime.setText(entry.getInput_time());
                text_data_type.setText(entry.getData_name());
                data_type_level.setText(entry.getData_level());
                break;
            case TYPE_DAILY_HEADER:
                convertView = inflater.inflate(R.layout.journal_daily_data_layout, parent, false);
                //dayDataObj dayData = (dayDataObj)getItem(position);
                DailyTotalDataObj dayData = (DailyTotalDataObj)getItem(position);

                TextView day = (TextView)convertView.findViewById(R.id.day_display);
                TextView month = (TextView)convertView.findViewById(R.id.month_display);
                TextView year = (TextView)convertView.findViewById(R.id.year_display);
                TextView bloodAvg = (TextView)convertView.findViewById(R.id.blood_avg);
                TextView activityTotal = (TextView)convertView.findViewById(R.id.total_activity_sum);
                TextView carbsTotal = (TextView)convertView.findViewById(R.id.carbs_total);

                //String readString = (String)getItem(position);
                //String[] headerLine = readString.split("-");

                day.setText(dayData.getDay());
                month.setText(dayData.getMonth());
                year.setText(dayData.getYear());
                bloodAvg.setText(dayData.getBlood_glucose_avg());
                activityTotal.setText(dayData.getActivity_total());
                carbsTotal.setText(dayData.getCarbs_total());
                break;
        }

        //System.out.println("END");


        return convertView;
    }
}
