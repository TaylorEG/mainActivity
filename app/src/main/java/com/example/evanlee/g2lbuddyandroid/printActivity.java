package com.example.evanlee.g2lbuddyandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class printActivity extends AppCompatActivity {

    public static final String monthTest = "02";
    public static final String yearTest = "2017";


    private TextView tobePrint;
    private TextView tobePrint2;
    private TextView tobePrint3;
    private TextView tobePrint4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        tobePrint = (TextView) findViewById(R.id.testPrint);
        tobePrint2 = (TextView) findViewById(R.id.testPrint2);
        tobePrint3 = (TextView) findViewById(R.id.testPrint3);
        tobePrint4 = (TextView) findViewById(R.id.testPrint4);
    }

    protected void onStart() {
        super.onStart();
        String first = setTextFunc("med");
        tobePrint.setText(first);
        String second = setTextFunc("blood");
        tobePrint2.setText(second);
        String third = setTextFunc("activity");
        tobePrint3.setText(third);
        String last = setTextFunc("food");
        tobePrint4.setText(last);
    }

    private String setTextFunc(String file) {
//        System.out.println("This is the output from medicine submission");
        String fileName = file + "Journal" + "-" + monthTest + "-" + yearTest + ".txt";
        File inFile = new File(getBaseContext().getFilesDir(), fileName);
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        String lastline = "";
        while(fileRead.hasNextLine()) {
            String line = fileRead.nextLine();
            lastline = line;
        }
        System.out.println(lastline);
        fileRead.close();
        return lastline;
    }
}
