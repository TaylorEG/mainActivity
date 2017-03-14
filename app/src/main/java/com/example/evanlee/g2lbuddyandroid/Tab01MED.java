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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by evanlee on 2/2/17.
 */

public class Tab01MED extends Fragment implements Spinner.OnItemSelectedListener {

    public interface OnMedSubmitListen {
        public void onMedSubmit(String medName, String medAmount, String medUnit, String dateSend);
    }

    OnMedSubmitListen act;
    Activity a;
    EditText medName;
    EditText medAmount;
    Spinner medUnit;
    TextView userDate;
    TextView userTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            a=(Activity) context;
        }
        try {
            act = (OnMedSubmitListen) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMedSubmitListen");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab01_medicine, container, false);

        ((journalInput) getActivity())
                .setActionBarTitle("");
        medName = (EditText) rootView.findViewById(R.id.medName);
        medAmount = (EditText) rootView.findViewById(R.id.medAmount);
        medUnit = (Spinner) rootView.findViewById(R.id.medUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.medicineUnits, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medUnit.setAdapter(adapter);
        medUnit.setOnItemSelectedListener(this);
        userDate = (TextView) rootView.findViewById(R.id.userDate);
        userTime = (TextView) rootView.findViewById(R.id.userTime);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitButton : {
                sendMed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMed() {
        String medicineName = medName.getText().toString();
        String medicineAmount = medAmount.getText().toString();
        String medicineUnit = medUnit.getSelectedItem().toString();
        String dateString = userDate.getText().toString();
        String timeString = userTime.getText().toString();
        if(medicineName.isEmpty() || medicineAmount.isEmpty() || medicineUnit.equals("Unit")
                || dateString.isEmpty() || timeString.isEmpty()) {
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
        act.onMedSubmit(medicineName, medicineAmount, medicineUnit, dateSend);
    }
}

