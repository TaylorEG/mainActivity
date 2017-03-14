package com.example.blake.exportdata;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blake on 2/27/17.
 */

public class CustomAdapter extends BaseAdapter {
    private String[] months;
    private LayoutInflater adapterInflater;
    private List<Boolean> checkers;



    public CustomAdapter(Context context, String[] months) {
        this.months = months;
        adapterInflater = (LayoutInflater.from(context));
        checkers = new ArrayList<Boolean>();
        for(int i = 0; i < months.length; i++) {
            checkers.add(true);
        }
    }

    @Override
    public int getCount() {
        return months.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<Boolean> getCheckers() {
        List<Boolean> checkersDup = checkers;
        return checkersDup;
    }

    public String getMonthValue(int pos) {
        return months[pos];
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = adapterInflater.inflate(R.layout.list_view_months, null);
        final CheckedTextView monthCheckTextView = (CheckedTextView) view.findViewById(R.id.month_lists);
        monthCheckTextView.setText(months[position]);
        monthCheckTextView.setChecked(checkers.get(position));
        if(monthCheckTextView.isChecked()) {
            monthCheckTextView.setCheckMarkDrawable(R.drawable.verify_sign);
        } else {
            monthCheckTextView.setCheckMarkDrawable(0);
        }
        monthCheckTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthCheckTextView.isChecked()) {
                    monthCheckTextView.setCheckMarkDrawable(0);
                    monthCheckTextView.setChecked(false);
                    checkers.set(position, false);
                } else {
                    monthCheckTextView.setCheckMarkDrawable(R.drawable.verify_sign);
                    monthCheckTextView.setChecked(true);
                    checkers.set(position, true);
                }
            }
        });
        return view;
    }
}
