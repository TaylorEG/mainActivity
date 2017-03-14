package com.example.blake.exportdata;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    static final String BLOODFILE = "bloodJournal";
    static final String ACTIVITYFILE = "activityJournal";
    static final String FOODFILE = "foodJournal";
    static final String MEDFILE = "medJournal";
    static final String AVERAGEFILE = "averageFile.txt";
    static final int BASENUM = 10;
    static final int ADDEDZERO = 10;

    CheckedTextView pdfCheck;
    CheckedTextView csvCheck;
    CustomAdapter customAdapter;
    List<String> months;
    String[] monthNames = {"Feb 2017", "Mar 2017", "Apr 2017", "May 2017", "Jun 2017", "Oct 2017"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addToMonths();
        fillWithDataMed();
        fillWithDataBG();
        fillWithDataAct();
        fillWithDataFood();
        fillWithDataAvg();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Export");
        String[] monthsOfData = getMonthsWithData();
        ListView listView = (ListView) findViewById(R.id.monthListView);
        customAdapter = new CustomAdapter(getApplicationContext(), monthsOfData);
        listView.setAdapter(customAdapter);
        pdfCheck = (CheckedTextView) findViewById(R.id.pdfCheck);
        csvCheck = (CheckedTextView) findViewById(R.id.csvCheck);
        pdfCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfCheck.isChecked()) {
                    pdfCheck.setCheckMarkDrawable(null);
                    pdfCheck.setChecked(false);
                } else {
                    pdfCheck.setCheckMarkDrawable(R.drawable.verify_sign);
                    pdfCheck.setChecked(true);
                }
            }
        });
        csvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(csvCheck.isChecked()) {
                    csvCheck.setCheckMarkDrawable(null);
                    csvCheck.setChecked(false);
                } else {
                    csvCheck.setCheckMarkDrawable(R.drawable.verify_sign);
                    csvCheck.setChecked(true);
                }
            }
        });
    }

    public String[] getMonthsWithData() {
        List<LineFormat> allAverages = new ArrayList<LineFormat>();
        File inFile = new File(getBaseContext().getFilesDir(), AVERAGEFILE);
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        while(fileRead.hasNextLine()) {
            String line = fileRead.nextLine();
            String[] splitOnSlash = line.split("/");
            String neededDay = "/20/";
            String neededTime = "/10/10";
            int month = parseInt(splitOnSlash[0], BASENUM);
            String dateFormat = splitOnSlash[0] + neededDay + splitOnSlash[1] +
                    neededTime;
            String restOfLine = months.get(month - 1) + " " + splitOnSlash[1];
            LineFormat averages = new LineFormat(dateFormat, restOfLine);
            allAverages.add(averages);
        }
        String[] returnArray = new String[allAverages.size()];
        Collections.sort(allAverages);
        int count = 0;
        for(int i = allAverages.size() - 1; i >= 0; i--) {
            returnArray[count] = allAverages.get(i).getInfo();
            count++;
        }
        return returnArray;
    }

    public void addToMonths() {
        months = new ArrayList<>();
        months.addAll(Arrays.asList("Jan","Feb","Mar","Apr","May","Jun"
        ,"Jul","Aug","Sep","Oct","Nov","Dec"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitButton : {
                sendEmail();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void fillWithDataMed() {
        String testString = "02/14/2017/11/15,Advil.5.mg";
        String fileName = "medJournal-02-2017.txt";
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(testString);
        pWriter.close();
    }

    public void fillWithDataBG() {
        String testString = "02/24/2017/10/15,500";
        String fileName = "bloodJournal-02-2017.txt";
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(testString);
        pWriter.close();
    }

    public void fillWithDataFood() {
        String testString = "02/14/2017/10/15,Burger.450";
        String fileName = "foodJournal-02-2017.txt";
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(testString);
        pWriter.close();
    }

    public void fillWithDataAct() {
        String testString = "03/10/2017/11/15,Running.50.10";
        String fileName = "activityJournal-03-2017.txt";
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(testString);
        pWriter.close();
    }

    public void fillWithDataAvg() {
        String testString = "02/2017/155,54605,1245,45,455,120\n" +
                 "03/2017/155,54605,1245,45,455,120\n" +
                "04/2017/155,54605,1245,45,455,120\n" +
                "07/2017/155,54605,1245,45,455,120\n" +
                "05/2017/155,54605,1245,45,455,120";
        String fileName = "averageFile.txt";
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(testString);
        pWriter.close();
    }

    public void sendEmail() {
        List<Boolean> checkMarks = customAdapter.getCheckers();
        List<String> checkedMonths = new ArrayList<>();
        for(int i = 0; i < checkMarks.size(); i++) {
            if(checkMarks.get(i)) {
                String[] splitOnSpace = customAdapter.getMonthValue(i).split(" ");
                int monthIndex = months.indexOf(splitOnSpace[0]) + 1;
                String finalCombine = "";
                if(monthIndex < ADDEDZERO) {
                    finalCombine += "0";
                }
                finalCombine += "" + monthIndex + splitOnSpace[1];
                checkedMonths.add(finalCombine);
            }
        }
        String dataSend = getDataToSend(checkedMonths);
        if(dataSend.equals("Empty")) {
            return;
        }
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);
        String currentDay = months.get(month) + " " + day + ", " + year;
        if (csvCheck.isChecked() && pdfCheck.isChecked()) {
            System.out.println("Both are checked");
        } else if (csvCheck.isChecked()) {
            String fileName = currentDay + " Export.csv";
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("application/csv");
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/download");
            dir.mkdirs();
            File data = new File(dir, fileName);
            createCSV(data, dataSend);
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(data));
            email.putExtra(Intent.EXTRA_SUBJECT, "G2L Buddy Export - " + currentDay);
            email.putExtra(Intent.EXTRA_TEXT, "Here's your G2L Buddy data export, generated on "
            + currentDay);
            startActivity(Intent.createChooser(email, "E-mail"));
        } else if(pdfCheck.isChecked()) {
            System.out.println("PDF Checked");
        }
        if(!pdfCheck.isChecked() && !csvCheck.isChecked()) {
            AlertDialog needForm = new AlertDialog.Builder(MainActivity.this).create();
            needForm.setTitle("Need Format to Export As");
            needForm.setMessage("You must select at least one format to export as.");
            needForm.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            needForm.show();
            return;
        }
    }

    public boolean monthIsChecked(List<String> checkedMonths, String file) {
        String[] monthCheckArray = file.split("-");
        String monthCheck = monthCheckArray[1] + monthCheckArray[2];
        return checkedMonths.contains(monthCheck);
    }

    public String getDataToSend(List<String> checkedMonths) {
        String total = "";
        String averageHeader = "Month,Glucose Avg.,Total Activity,Total Grams," +
                "Glucose (Lowest),Glucose (Highest)," +
                "Glucose (Avg. Deviation)\n";
        String dataHeader = "Date,Time,Type,Info,Amount,Unit\n";
        String path = getBaseContext().getFilesDir().getPath(); //folder path
        File folder = new File(path);
        File[] files = folder.listFiles();
        ArrayList<String> glucoseFiles = new ArrayList<>();
        ArrayList<String> activityFiles = new ArrayList<>();
        ArrayList<String> foodFiles = new ArrayList<>();
        ArrayList<String> medFiles = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (f != null) {
                    if (((f.getName().split("-"))[0]).equals(BLOODFILE)) {
                        if(monthIsChecked(checkedMonths, (f.getName().split("\\."))[0])) {
                            glucoseFiles.add(f.getName());
                        }
                    } else if (((f.getName().split("-"))[0]).equals(ACTIVITYFILE)) {
                        if(monthIsChecked(checkedMonths, (f.getName().split("\\."))[0])) {
                            activityFiles.add(f.getName());
                        }
                    } else if (((f.getName().split("-"))[0]).equals(FOODFILE)) {
                        if(monthIsChecked(checkedMonths, (f.getName().split("\\."))[0])) {
                            foodFiles.add(f.getName());
                        }
                    } else if (((f.getName().split("-"))[0]).equals(MEDFILE)) {
                        if(monthIsChecked(checkedMonths, (f.getName().split("\\."))[0])) {
                            medFiles.add(f.getName());
                        }
                    }
                }
            }
        }
        if (glucoseFiles.isEmpty() && activityFiles.isEmpty() && foodFiles.isEmpty() && medFiles.isEmpty())
        {
            return "Empty";
        }
        total += averageHeader;

        String averages = getAveragesString(checkedMonths) + "\n\n";
        total += averages;
        total += dataHeader;
        List<LineFormat> allData = new ArrayList<LineFormat>();
        for(int i = 0; i < glucoseFiles.size(); i++) {
            String fileName = glucoseFiles.get(i);
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Empty";
            }
            while(fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitOnComma = line.split(",");
                String dateSplit = splitOnComma[0];
                String[] splitOnSlash = splitOnComma[0].split("/");
                String onlyYear = splitOnSlash[2].substring(2);
                String finalString = splitOnSlash[0] + "/" + splitOnSlash[1] + "/" +
                        onlyYear + "," + splitOnSlash[3] + ":" + splitOnSlash[4] + "," +
                        "Reading,Blood glucose level," + splitOnComma[1] + ",mg/dL\n";
                LineFormat next = new LineFormat(dateSplit, finalString);
                allData.add(next);
            }
        }
        for(int i = 0; i < foodFiles.size(); i++) {
            String fileName = foodFiles.get(i);
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Empty";
            }
            while(fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitOnComma = line.split(",");
                String dateSplit = splitOnComma[0];
                String[] splitOnSlash = splitOnComma[0].split("/");
                String onlyYear = splitOnSlash[2].substring(2);
                String[] splitOnDot = splitOnComma[1].split("\\.");
                String finalString = splitOnSlash[0] + "/" + splitOnSlash[1] + "/" +
                        onlyYear + "," + splitOnSlash[3] + ":" + splitOnSlash[4] + ","
                        + "Food," + splitOnDot[0] + "," + splitOnDot[1] + ",grams\n";
                LineFormat next = new LineFormat(dateSplit, finalString);
                allData.add(next);
            }
        }
        for(int i = 0; i < medFiles.size(); i++) {
            String fileName = medFiles.get(i);
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Empty";
            }
            while(fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitOnComma = line.split(",");
                String dateSplit = splitOnComma[0];
                String[] splitOnSlash = splitOnComma[0].split("/");
                String onlyYear = splitOnSlash[2].substring(2);
                String[] splitOnDot = splitOnComma[1].split("\\.");
                String finalString = splitOnSlash[0] + "/" + splitOnSlash[1] + "/" +
                        onlyYear + "," + splitOnSlash[3] + ":" + splitOnSlash[4] + ","
                        + "Medication," + splitOnDot[0] + "," + splitOnDot[1] + "," + splitOnDot[2] + "\n";
                LineFormat next = new LineFormat(dateSplit, finalString);
                allData.add(next);
            }
        }
        for(int i = 0; i < activityFiles.size(); i++) {
            String fileName = activityFiles.get(i);
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Empty";
            }
            while(fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitOnComma = line.split(",");
                String dateSplit = splitOnComma[0];
                String[] splitOnSlash = splitOnComma[0].split("/");
                String onlyYear = splitOnSlash[2].substring(2);
                String[] splitOnDot = splitOnComma[1].split("\\.");
                String finalString = splitOnSlash[0] + "/" + splitOnSlash[1] + "/" +
                        onlyYear + "," + splitOnSlash[3] + ":" + splitOnSlash[4] + ","
                        + "Activity," + splitOnDot[0] + "," + splitOnDot[1] + ":" + splitOnDot[2] + ",time\n";
                LineFormat next = new LineFormat(dateSplit, finalString);
                allData.add(next);
            }
        }
        Collections.sort(allData);
        for(int i = 0; i < allData.size(); i++) {
            total += allData.get(i).getInfo();
        }
        return total;
    }

    public String getAveragesString(List<String> checkedMonths) {
        List<LineFormat> allAverages = new ArrayList<LineFormat>();
        File inFile = new File(getBaseContext().getFilesDir(), AVERAGEFILE);
        Scanner fileRead = null;
        try {
            fileRead = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Empty";
        }
        while(fileRead.hasNextLine()) {
            String line = fileRead.nextLine();
            String[] splitOnSlash = line.split("/");
            String neededDay = "/20/";
            String neededTime = "/10/10";
            int month = parseInt(splitOnSlash[0], BASENUM);
            String dateFormat = splitOnSlash[0] + neededDay + splitOnSlash[1] +
                    neededTime;
            String restOfLine = months.get(month - 1) + " " + splitOnSlash[1] + "," +
                    splitOnSlash[2] + "\n";
            LineFormat averages = new LineFormat(dateFormat, restOfLine);
            allAverages.add(averages);
        }
        Collections.sort(allAverages);
        String averageData = "";
        for(int i = 0; i < allAverages.size(); i++) {
            averageData += allAverages.get(i).getInfo();
        }
        return averageData;
    }

    public void createCSV(File fileName, String data) {

        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(fileName);
            fWriter.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
