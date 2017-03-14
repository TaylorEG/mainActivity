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

public class Tab02BG extends Fragment {

    public interface OnBloodSubmitListen {
        public void onBloodSubmit(String bloodGlucoseNum, String dateSend);
    }

    OnBloodSubmitListen act;
    EditText bloodGlucoseNum;
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
            act = (OnBloodSubmitListen) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnBloodSubmitListen");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab02_bloodglucose, container, false);
        ((journalInput) getActivity())
                .setActionBarTitle("");
        bloodGlucoseNum = (EditText) rootView.findViewById(R.id.bloodGlucoseNum);
        userDate = (TextView) rootView.findViewById(R.id.userDate);
        userTime = (TextView) rootView.findViewById(R.id.userTime);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitButton : {
                sendBlood();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void sendBlood() {
        String bloodGlucoselvl = bloodGlucoseNum.getText().toString();
        String dateString = userDate.getText().toString();
        String timeString = userTime.getText().toString();
        if(bloodGlucoselvl.isEmpty() || dateString.isEmpty() || timeString.isEmpty()) {
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
        act.onBloodSubmit(bloodGlucoselvl, dateSend);
    }
}

