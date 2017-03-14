package com.example.evanlee.g2lbuddyandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class DailyData extends AppCompatActivity {

    static final String KEY_MONTH_YEAR = "MM-yyyy";
    static final String KEY_MONTH = "month";
    static final String KEY_YEAR = "year";
    static final String KEY_GLUCOSE = "glucose";
    static final String KEY_ACTIVITY = "activity";
    static final String KEY_FOOD = "food";
    static final String KEY_DEVIATION = "deviation";

    static final String BLOODFILE = "bloodJournal";
    static final String ACTIVITYFILE = "activityJournal";
    static final String FOODFILE = "foodJournal";
    static final String MEDFILE = "medJournal";

    static final int BLOOD_TYPE = 1;
    static final int ACTIVITY_TYPE = 2;
    static final int FOOD_TYPE = 3;
    static final int MEDICATION_TYPE = 4;


    static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String monthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_data);
        //fillWithDataAct();
        //fillWithDataBG();
        //fillWithDataFood();
        //fillWithDataMed();

        monthYear = getIntent().getStringExtra(KEY_MONTH_YEAR);

        ListView listView = (ListView) findViewById(R.id.dailyData_ListView);


        //ArrayList<DailyTotalDataObj> headers = new ArrayList<>();

        //get locations of data folder and files
        String path = getBaseContext().getFilesDir().getPath(); //folder path
        File folder = new File(path);
        File[] files = folder.listFiles();

        ArrayList<String> bloodGlucoseFiles = new ArrayList<>();
        ArrayList<String> activityFiles = new ArrayList<>();
        ArrayList<String> foodFiles = new ArrayList<>();
        ArrayList<String> medFiles = new ArrayList<>();

        if (files != null) {
            for (File f : files) {
                if (f != null) {
                    String[] arr = f.getName().split("-");
                    if (arr[0].equals(BLOODFILE) ){//&& (arr[1] + '-' + arr[2]) == (monthYear + ".txt")) {
                        System.out.println("KILLER: " + arr[1] + arr[2] + "\t" + monthYear);
                        bloodGlucoseFiles.add(f.getName());
                    } else if (arr[0].equals(ACTIVITYFILE) ){//&& (arr[1] + '-' + arr[2]) == (monthYear + ".txt")) {
                        activityFiles.add(f.getName());
                    } else if (arr[0].equals(FOODFILE) ){//&& (arr[1] + '-' + arr[2]) == (monthYear + ".txt")) {
                        foodFiles.add(f.getName());
                    } else if (arr[0].equals(MEDFILE) ){//&& (arr[1] + '-' + arr[2]) == (monthYear + ".txt")) {
                        medFiles.add(f.getName());
                    }
                }
            }
        }

        if (bloodGlucoseFiles.isEmpty() && activityFiles.isEmpty() && foodFiles.isEmpty() && medFiles.isEmpty()) {
            System.out.println("THere is no DATA......");
            //return;
        }

        //create an ArrayList DailyInputObj each type of entry
        ArrayList<DailyInputObj> entries = new ArrayList<>();

        //fill entries with blood glucose DailyInputObjs
        for (String dataLine : bloodGlucoseFiles) {
            System.out.println("FILE: " + dataLine);

            String fileName = dataLine;
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //return "Empty";
            }
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                //System.out.println("Here is the data: " + line);
                entries.add(generateBloodEntry(line));
            }
        }

        //fill entries with activity data
        for (String dataLine : activityFiles) {
            System.out.println("FILE: " + dataLine);

            String fileName = dataLine;
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //return "Empty";
            }
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                //System.out.println("Here is the data: " + line);
                entries.add(generateActivityEntry(line));
            }
        }

        //fill entries with food data
        for (String dataLine : foodFiles) {
            System.out.println("FILE: " + dataLine);

            String fileName = dataLine;
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //return "Empty";
            }
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                //System.out.println("Here is the data: " + line);
                entries.add(generateFoodEntry(line));
            }
        }

        //fill entries with medication data
        for (String dataLine : medFiles) {
            System.out.println("FILE: " + dataLine);

            String fileName = dataLine;
            File inFile = new File(getBaseContext().getFilesDir(), fileName);
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //return "Empty";
            }
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                //System.out.println("Here is the data: " + line);
                entries.add(generateMedEntry(line));
            }
        }


        Collections.sort(entries);

        //Checking if entries is good
        for(DailyInputObj entry: entries){
            System.out.println(entry.getDateString() + " " + entry.getType());
        }

        int BloodCount = 0;
        int Blood_Total = 0;
        int activity_hours = 0;
        int activity_min = 0;
        int totalCarbs = 0;

        //starting point for gathering data for that day
        String strtDate = entries.get(0).getStdDate();

        /*
        String[] splitDate = strtDate.split("/");
        String Month = splitDate[0];
        String Day = splitDate[1];
        String Year = splitDate[2];
*/
        //System.out.println("Start Data: " + strtDate);

        ArrayList<Object> listOrder = new ArrayList<>();


        ArrayList<DailyInputObj> holder = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            //listOrder.add(entries.get(i));


            //strtDate = entries.get(i).getStdDate();

            System.out.println("Entry Data: " + entries.get(i).getStdDate());

            if (strtDate.equals(entries.get(i).getStdDate())) {

                if (entries.get(i).getType() == BLOOD_TYPE) {
                    BloodCount++;
                    Blood_Total += Integer.parseInt(entries.get(i).getData_level());
                } else if (entries.get(i).getType() == ACTIVITY_TYPE) {

                    String[] splitActivityTime = entries.get(i).getData_level().split(":");
                    activity_hours += Integer.parseInt(splitActivityTime[0]);
                    activity_min += Integer.parseInt(splitActivityTime[1]);

                } else if (entries.get(i).getType() == FOOD_TYPE) {
                    totalCarbs += Integer.parseInt(entries.get(i).getData_level());
                } else //MEDICATION TYPE
                {
                    //Do Nothing Just add the entry to the list
                }

                //add the entry to a holder for data for that day
                holder.add(entries.get(i));
                //System.out.println("Holder size: " + holder.size());

                //When dealing with the last data entry
                if(i == entries.size()-1){
                    String[] splitDate = strtDate.split("/");
                    String Month = splitDate[0];
                    String Day = splitDate[1];
                    String Year = splitDate[2];

                    int BloodGlucoseAvg = 0;
                    if (BloodCount > 0)
                        BloodGlucoseAvg = Blood_Total / BloodCount;

                    if (activity_min >= 60) {
                        int addedHours = activity_min / 60;
                        activity_hours += addedHours;
                        activity_min = activity_min % 60;
                    }
                    String ActivityTotal = Integer.toString(activity_hours) + ":" + Integer.toString(activity_min);

                    DailyTotalDataObj last_header = new DailyTotalDataObj(Day, getMonthName(Month), Year, String.valueOf(BloodGlucoseAvg), ActivityTotal, Integer.toString(totalCarbs));

                    listOrder.add(last_header);
                    for (DailyInputObj j : holder)
                        listOrder.add(j);
                    holder.clear();
                }

            } else {

                // When data finally moves on to a different day
                // Create a header from all the calculated data from that day
                // add that header to a listOrder ArrayList and then the contents of the holder
                // reset all vaiables and clear holder
                // move on to next days data

                String[] splitDate = strtDate.split("/");
                String Month = splitDate[0];
                String Day = splitDate[1];
                String Year = splitDate[2];

                int BloodGlucoseAvg = 0;
                if (BloodCount > 0)
                    BloodGlucoseAvg = Blood_Total / BloodCount;

                if (activity_min >= 60) {
                    int addedHours = activity_min / 60;
                    activity_hours += addedHours;
                    activity_min = activity_min % 60;
                }
                String ActivityTotal = Integer.toString(activity_hours) + ":" + Integer.toString(activity_min);

                DailyTotalDataObj header = new DailyTotalDataObj(Day, getMonthName(Month), Year, String.valueOf(BloodGlucoseAvg), ActivityTotal, Integer.toString(totalCarbs));

                listOrder.add(header);
                for (DailyInputObj j : holder)
                    listOrder.add(j);

                //reset values
                holder.clear();
                strtDate = entries.get(i).getStdDate();
                Blood_Total = 0;
                BloodCount = 0;
                activity_hours = 0;
                activity_min = 0;
                totalCarbs = 0;

                //Case: When dealing with the last data entry
                if (strtDate.equals(entries.get(i).getStdDate())) {

                    if (entries.get(i).getType() == BLOOD_TYPE) {
                        BloodCount++;
                        Blood_Total += Integer.parseInt(entries.get(i).getData_level());
                    } else if (entries.get(i).getType() == ACTIVITY_TYPE) {

                        String[] splitActivityTime = entries.get(i).getData_level().split(":");
                        activity_hours += Integer.parseInt(splitActivityTime[0]);
                        activity_min += Integer.parseInt(splitActivityTime[1]);

                    } else if (entries.get(i).getType() == FOOD_TYPE) {
                        totalCarbs += Integer.parseInt(entries.get(i).getData_level());
                    } else //MEDICATION TYPE
                    {
                        //Do Nothing Just add the entry to the list
                    }

                    //add the entry to a holder for data for that day
                    holder.add(entries.get(i));

                }

                if(i == entries.size()-1){
                    splitDate = strtDate.split("/");
                    Month = splitDate[0];
                    Day = splitDate[1];
                    Year = splitDate[2];

                    BloodGlucoseAvg = 0;
                    if (BloodCount > 0)
                        BloodGlucoseAvg = Blood_Total / BloodCount;

                    if (activity_min >= 60) {
                        int addedHours = activity_min / 60;
                        activity_hours += addedHours;
                        activity_min = activity_min % 60;
                    }
                    ActivityTotal = Integer.toString(activity_hours) + ":" + Integer.toString(activity_min);

                    DailyTotalDataObj last_header = new DailyTotalDataObj(Day, getMonthName(Month), Year, String.valueOf(BloodGlucoseAvg), ActivityTotal, Integer.toString(totalCarbs));

                    listOrder.add(last_header);
                    for (DailyInputObj j : holder)
                        listOrder.add(j);
                    holder.clear();
                }


            }
        }

            listView.setAdapter(new DailyDataAdapter(this, listOrder));
    }


    //use this to sort all data by date and time
    public Date parseDate(String inputDataString) {
        String[] data = inputDataString.split("/");
        //String minutes = data[4].split(",")[0];

        String date = data[0] + "-" + data[1] + "-" + data[2];

        try {
            return new SimpleDateFormat("MM-dd-yyyy", Locale.US).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public DailyInputObj generateBloodEntry(String inputDataString)
    {
        //String testStringBlood = "02/24/2017/10/15,500";
        String[] data = inputDataString.split("/");
        String month = data[0];
        String day = data[1];
        String year = data[2];
        String hours = data[3];
        String minutes = data[4].split(",")[0];
        String bloodGlucose_level = data[4].split(",")[1];

        //System.out.println(month + " " + day + " " + year + " " + hours + " " + day + " " + "Blood Glucose" + " " + bloodGlucose_level);

        DailyInputObj BloodGlucose = new DailyInputObj(1,month, day, year, hours, minutes, "Blood Glucose", bloodGlucose_level);
        return BloodGlucose;
    }

    public DailyInputObj generateActivityEntry(String inputDataString){

        //String testStringActivity = "02/10/2017/11/15,Running.50.10";
        String[] data = inputDataString.split("/");
        String minutes = data[4].split(",")[0];


        String[] activtyData = (data[4].split(",")[1]).split("\\.");
        String exerciseDes = activtyData[0];
        String exercise_hours = activtyData[1];
        String exercise_min = activtyData[2];
        String exercise_time = exercise_hours + ":" + exercise_min;

        //System.out.println(data[0] + " " + data[1] + " " + data[2] + " "+ exercise_time + " " + exerciseDes + " " + exercise_time);

        DailyInputObj Activity = new DailyInputObj(2,data[0],data[1],data[2],data[3],minutes,exerciseDes, exercise_time);
        return  Activity;
    }

    public DailyInputObj generateFoodEntry(String inputDataString){

        //String testStringFood = "02/14/2017/10/15,Burger.450";
        String[] data = inputDataString.split("/");
        String minutes = data[4].split(",")[0];
        String[] foodData = (data[4].split(",")[1]).split("\\.");

        String foodDes = foodData[0];
        String carbs = foodData[1];

        DailyInputObj Food = new DailyInputObj(3,data[0],data[1],data[2],data[3],minutes,foodDes,carbs);
        return Food;
    }

    public DailyInputObj generateMedEntry(String inputDataString){

        //String testStringMed = "02/14/2017/11/15,Advil.5.mg";
        String[] data = inputDataString.split("/");
        String minutes = data[4].split(",")[0];

        String[] MedicationData = (data[4].split(",")[1]).split("\\.");
        String MedicationName = MedicationData[0];
        String amount = MedicationData[1];
        String units = MedicationData[2];
        String dosage = amount + " " + units;

        DailyInputObj Medication = new DailyInputObj(4,data[0],data[1],data[2],data[3], minutes, MedicationName, dosage);
        return Medication;
    }

    public String getMonthName(String index){

        int monthIndex = 0;

        if(index.charAt(0) == '0') {
            monthIndex = Character.getNumericValue(index.charAt(1));
        }else{
            monthIndex = Integer.parseInt(index);
        }

        return MONTH_NAMES[monthIndex-1];
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
        String testString = "02/24/2017/10/15,500\n02/24/2017/11/16,808\n02/14/2017/14/15,700\n02/10/2017/12/13,500\n02/08/2017/10/19,666";
        String fileName = "bloodJournal-02-2017.txt";
        System.out.println("DIRECTORY "+getBaseContext().getFilesDir());
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        //pWriter.write(testString);
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
        String testString = "02/10/2017/11/15,Running.50.10";
        String fileName = "activityJournal-02-2017.txt";
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

}
