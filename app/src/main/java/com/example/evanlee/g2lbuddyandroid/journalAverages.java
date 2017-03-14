package com.example.evanlee.g2lbuddyandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class journalAverages extends AppCompatActivity {

    static final int NUM_DATA_TYPES = 3;
    static final String KEY_MONTH_YEAR = "MM-yyyy";
    static final String KEY_MONTH = "month";
    static final String KEY_YEAR = "year";
    static final String KEY_GLUCOSE = "glucose";
    static final String KEY_GLUCOSE_HIGH = "high";
    static final String KEY_GLUCOSE_LOW = "low";
    static final String KEY_ACTIVITY = "activity";
    static final String KEY_FOOD = "food";
    static final String KEY_DEVIATION = "deviation";

    static final String BLOODFILE = "bloodJournal";
    static final String ACTIVITYFILE = "activityJournal";
    static final String FOODFILE = "foodJournal";

    static final String[] MONTHNAMES = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    String path;
    ListView list;
    LazyAdapter adapter;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_averages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6096ba")));
        fab.setImageResource(R.drawable.add_white24);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), InputReading.class));
                finish();
            }
        });

        path = getBaseContext().getFilesDir().getPath(); //folder path
        File folder = new File(path);
        File[] files = folder.listFiles();

        ArrayList<String> glucoseFiles = new ArrayList<>();
        ArrayList<String> activityFiles = new ArrayList<>();
        ArrayList<String> foodFiles = new ArrayList<>();


        if (files != null) {
            for (File f : files) {
                if (f != null) {
                    if (((f.getName().split("-"))[0]).equals(BLOODFILE))
                        glucoseFiles.add(f.getName());
                    else if (((f.getName().split("-"))[0]).equals(ACTIVITYFILE))
                        activityFiles.add(f.getName());
                    else if (((f.getName().split("-"))[0]).equals(FOODFILE))
                        foodFiles.add(f.getName());
                }
            }
        }

        message = (TextView)findViewById(R.id.emptyMessage);
        if (glucoseFiles.isEmpty() && activityFiles.isEmpty() && foodFiles.isEmpty())
        {
            message.setText("Error: No Save Data Found");
            return;
        }

        list=(ListView)findViewById(R.id.homeScreenList);

        adapter=new LazyAdapter(this, getDataList(glucoseFiles, activityFiles, foodFiles));
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> adaptMap = adapter.getData().get(position);
                Intent intent = new Intent(view.getContext(), DailyData.class);
                System.out.println("BoomBoomBap: " + adaptMap.get(KEY_MONTH_YEAR));
                intent.putExtra(KEY_MONTH_YEAR, adaptMap.get(journalAverages.KEY_MONTH_YEAR));

                startActivity(intent);

            }
        });

    }

    public ArrayList<HashMap<String, String>> getDataList(ArrayList<String> glucoseFiles, ArrayList<String> activityFiles, ArrayList<String> foodFiles)
    {

        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

        Date oldestDate = getLatest(glucoseFiles, activityFiles, foodFiles);
        Date latestDate = getOldest(glucoseFiles, activityFiles, foodFiles);


        ArrayList<Integer> allGlucose;
        int sumActivity;
        int sumFood;

        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.US);
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.US);

        String line;
        int sumGlucose;
        int count;
        while (oldestDate.after(latestDate)) {
            count = 0;
            sumActivity = 0;
            sumFood = 0;
            sumGlucose = 0;
            allGlucose = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(path + '/' + BLOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt"))) {
                while ((line = br.readLine()) != null)
                {
                    System.out.println("THIS IS THE FUCKING LINE: " + line);
                    if(!line.isEmpty()) {
                        allGlucose.add(parseGlucose(line));
                        sumGlucose += allGlucose.get(allGlucose.size() - 1);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFound: " + BLOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            } catch (IOException e) {
                System.out.println("IOException: " + BLOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(path + '/' + ACTIVITYFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt"))) {
                while ((line = br.readLine()) != null)
                {
                    String[] arr = line.split("\\.");
                    sumActivity += (Integer.parseInt(arr[1]) * 60) + Integer.parseInt(arr[2]);
                }
            } catch (FileNotFoundException f) {
                System.out.println("FileNotFound: " + ACTIVITYFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            } catch (IOException f) {
                System.out.println("IOException: " + ACTIVITYFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(path + '/' + FOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt"))) {
                while ((line = br.readLine()) != null) {
                    sumFood += Integer.parseInt(line.split("\\.")[1]);
                }
            } catch (FileNotFoundException g) {
                System.out.println("FileNotFound: " + FOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            } catch (IOException g) {
                System.out.println("IOException: " + FOODFILE + '-' + month.format(oldestDate) + '-' + year.format(oldestDate) + ".txt");
                count++;
            }

            if (count < NUM_DATA_TYPES)
                dataList.add(getMap(oldestDate,allGlucose,sumGlucose,sumActivity,sumFood));

            oldestDate = addMonths(oldestDate, -1);
            allGlucose = null;
        }

        return dataList;
    }

    public HashMap<String, String> getMap(Date oldestDate, ArrayList<Integer> allGlucose, int sumGlucose, int sumActivity, int sumFood)
    {
        int avgGlucose = 0;

        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.US);
        SimpleDateFormat month = new SimpleDateFormat("MM", Locale.US);

        HashMap<String, String> map = new HashMap<>();

        map.put(KEY_MONTH_YEAR, month.format(oldestDate) + '-' + year.format(oldestDate));
        map.put(KEY_MONTH, MONTHNAMES[Integer.parseInt(month.format(oldestDate)) - 1]);
        map.put(KEY_YEAR, year.format(oldestDate));

        if(allGlucose != null && allGlucose.size() > 0) {
            avgGlucose = sumGlucose / allGlucose.size();
            System.out.println("ALL GLUCOSE: " + allGlucose.toString());
        }

        map.put(KEY_GLUCOSE, Integer.toString(avgGlucose));
        map.put(KEY_GLUCOSE_HIGH, getHighestGlucose(allGlucose));
        map.put(KEY_GLUCOSE_LOW, getLowestGlucose(allGlucose));
        map.put(KEY_ACTIVITY, formTimeString(sumActivity));
        map.put(KEY_FOOD, Integer.toString(sumFood));
        map.put(KEY_DEVIATION, Integer.toString(getDeviation(allGlucose)));

        return map;
    }

    public Date addMonths(Date d, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public String getFile(ArrayList<String> l, Date date) {
        if (l.size() > 0 && !l.isEmpty())
            for (int i = 0; i < l.size(); i++) {
                if (parseDateTitle(l.get(0)).compareTo(date) == 0) {
                    return l.get(i);
                }
            }

        return null;
    }

    public Date getOldest(ArrayList<String> glucose, ArrayList<String> activity, ArrayList<String> food) {
        Date gDate = new Date();
        Date aDate = new Date();
        Date fDate = new Date();

        Date oldest = new Date();

            if (glucose != null && glucose.size() > 0)
                gDate = parseDateTitle(glucose.get(glucose.size() - 1));
            if (activity != null && activity.size() > 0)
                aDate = parseDateTitle(activity.get(activity.size() - 1));
            if (food != null && food.size() > 0)
                fDate = parseDateTitle(food.get(food.size() - 1));

            gDate = (glucose == null ? (activity == null ? (food == null ? null : fDate) : (food == null ? aDate : earliest(aDate, fDate))) : earliest(gDate, earliest(aDate, fDate)));
            oldest = (oldest == null ? gDate : earliest(gDate, oldest));
        return oldest;
    }

    public Date getLatest(ArrayList<String> glucose, ArrayList<String> activity, ArrayList<String> food) {
        Date gDate = new Date();
        Date aDate = new Date();
        Date fDate = new Date();

        Date earliest = new Date();

        int size = 0;
        if (glucose != null) {
            if (activity != null) {
                if (food != null) {
                    size = Math.max(glucose.size(), Math.max(activity.size(), food.size()));
                } else {
                    size = Math.max(glucose.size(), activity.size());
                }
            } else if (food != null) {
                size = Math.max(glucose.size(), food.size());
            }
        } else if (activity != null) {
            if (food != null) {
                size = Math.max(activity.size(), food.size());
            }
        } else if (food != null) {
            size = food.size();
        }

        for (int i = 0; i < size; i++) {
            if (!glucose.isEmpty() && glucose.size() > 0)
                gDate = parseDateTitle(glucose.get(0));
            if (!activity.isEmpty() && activity.size() > 0)
                aDate = parseDateTitle(activity.get(0));
            if (!food.isEmpty() && food.size() > 0)
                fDate = parseDateTitle(food.get(0));

            gDate = (glucose == null ? (activity == null ? (food == null ? null : fDate) : (food == null ? aDate : latest(aDate, fDate))) : latest(gDate, latest(aDate, fDate)));
            earliest = (earliest == null ? gDate : latest(gDate, earliest));
            //gDate = null;
            //aDate = null;
            //fDate = null;
        }
        return earliest;
    }

    public Date parseDateTitle(String line) {
        if (line == null || line.isEmpty())
            return null;

        String[] arr = line.split("-");
        String date = arr[1] + '-' + arr[2].substring(0, 4);
        try
        {
            return new SimpleDateFormat("MM-yyyy", Locale.US).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date earliest(Date a, Date b) {
        return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
    }

    public Date latest(Date a, Date b) {
        return a == null ? b : (b == null ? a : (a.before(b) ? b : a));
    }

    public int parseGlucose(String line) {
        return Integer.parseInt((line.split(","))[1]);
    }

    public int parseActivity(String line) {
        return Integer.parseInt((line.split("."))[1]);
    }

    public int parseFood(String line) {
        return Integer.parseInt((line.split("."))[1]);
    }

    public int getDeviation(ArrayList<Integer> allGlucose) {
        if (allGlucose == null || allGlucose.size() == 0)
            return 0;

        int mean, sum;
        mean = sum = 0;

        for (int i = 0; i < allGlucose.size(); i++)
        {
            mean += allGlucose.get(i);
        }
        mean = mean / allGlucose.size();

        for (int i = 0; i < allGlucose.size(); i++)
        {
            sum += (allGlucose.get(i) - mean) * (allGlucose.get(i) - mean);
        }

        return (int)Math.sqrt(sum / allGlucose.size());
    }

    public String getHighestGlucose(ArrayList<Integer> allGlucose) {
        if (allGlucose.size() == 0)
            return "0";

        int highest = 0;
        for (int i = 0; i < allGlucose.size(); i++)
        {
            if (allGlucose.get(i) > highest)
                highest = allGlucose.get(i);
        }

        System.out.println("HIGHEST GLUCOSE: " + highest);
        return Integer.toString(highest);
    }

    public String getLowestGlucose(ArrayList<Integer> allGlucose) {
        if (allGlucose.size() == 0)
            return "0";

        int lowest = allGlucose.get(0);
        for (int i = 0; i < allGlucose.size(); i++)
        {
            if (allGlucose.get(i) < lowest)
                lowest = allGlucose.get(i);
        }

        System.out.println("LOWEST GLUCOSE: " + lowest);
        return Integer.toString(lowest);
    }

    public String formTimeString(int time) {
        String full = "";
        String hours = Integer.toString((int)Math.floor(time / 3600));
        String mins = Integer.toString((int)Math.floor((time % 3600) / 60));
        String sec = Integer.toString(time % 60);
        if (hours.length() < 2)
            full += '0' + hours + ':';
        else
            full += hours + ':';
        if (mins.length() < 2)
            full += '0' + mins + ':';
        else
            full += mins + ':';
        if (sec.length() < 2)
            full += '0' + sec;
        else
            full += sec;

        return full;
    }

}
