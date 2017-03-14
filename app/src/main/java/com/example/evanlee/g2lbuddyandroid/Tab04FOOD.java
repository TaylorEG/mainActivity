package com.example.evanlee.g2lbuddyandroid;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by evanlee on 2/2/17.
 */

public class Tab04FOOD extends Fragment {

    public interface OnFoodSubmitListen {
        public void onFoodSubmit(String foodName, String calorieCount, String dateSend);
    }

    OnFoodSubmitListen act;
    EditText foodName;
    EditText calorieCount;
    TextView userDate;
    TextView userTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }
        try {
            act = (OnFoodSubmitListen) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFoodSubmitListen");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab04_food, container, false);
        foodName = (EditText) rootView.findViewById(R.id.foodName);
        calorieCount = (EditText) rootView.findViewById(R.id.calorieCount);
        userDate = (TextView) rootView.findViewById(R.id.userDate);
        userTime = (TextView) rootView.findViewById(R.id.userTime);
        ((journalInput) getActivity()).setActionBarTitle("");
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitButton : {
                sendFood();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendFood() {
        String foodNam = foodName.getText().toString();
        String calorie = calorieCount.getText().toString();
        String dateString = userDate.getText().toString();
        String timeString = userTime.getText().toString();
        if(foodNam.isEmpty() || calorie.isEmpty() || dateString.isEmpty() || timeString.isEmpty()) {
            AlertDialog needInput = new AlertDialog.Builder(getContext()).create();
            needInput.setTitle("Missing fields");
            needInput.setMessage("Please fill out all required fields");
            needInput.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            needInput.show();
            return;
        }
        String[] timeSplit = timeString.split(":");
        String dateSend = "" + dateString + "/" + timeSplit[0] + "/" + timeSplit[1];
        act.onFoodSubmit(foodNam, calorie, dateSend);
    }
}
