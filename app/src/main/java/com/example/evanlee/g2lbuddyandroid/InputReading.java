package com.example.evanlee.g2lbuddyandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class InputReading extends AppCompatActivity {

    public static final String LAUNCH_TAB = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);
    }

    /**
     * onClick listener on input_activity.xml
     * please implement the other input pages which have the fragments on top
     * @param view
     */
    public void inputBG(View view) {
        Intent next = new Intent(this, journalInput.class);
        next.putExtra(LAUNCH_TAB, 1);
        startActivity(next);
    }

    public void inputMed(View view) {
        Intent next = new Intent(this, journalInput.class);
        next.putExtra(LAUNCH_TAB,0);
        startActivity(next);
    }

    public void inputFood(View view) {
        Intent next = new Intent(this, journalInput.class);
        next.putExtra(LAUNCH_TAB,3);
        startActivity(next);
    }

    public void inputAct(View view) {
        Intent next = new Intent(this, journalInput.class);
        next.putExtra(LAUNCH_TAB,2);
        startActivity(next);
    }


}