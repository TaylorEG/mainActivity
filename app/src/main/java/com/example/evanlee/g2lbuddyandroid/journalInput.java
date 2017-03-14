package com.example.evanlee.g2lbuddyandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import android.support.v7.app.AlertDialog;

import static com.example.evanlee.g2lbuddyandroid.InputReading.LAUNCH_TAB;

public class journalInput extends AppCompatActivity implements Tab01MED.OnMedSubmitListen,
        Tab02BG.OnBloodSubmitListen, Tab03ACT.OnActivitySubmitListen, Tab04FOOD.OnFoodSubmitListen,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    public static final int DEF_VAL = 0;
    public static final int EXTRAZERONEEDED = 10;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0); // clear all scroll flag for this activity
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        int setPage = getIntent().getIntExtra(LAUNCH_TAB, DEF_VAL);
        mViewPager.setCurrentItem(setPage);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Discard changes?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        journalInput.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void showDatePickerDialog(View v){
        DatePickerFragment dpFragment = new DatePickerFragment();
        dpFragment.show(getSupportFragmentManager(), "datepicker");

    }

    public void showTimePickerDialog(View v){
        TimePickerFragment tpFragment = new TimePickerFragment();
        tpFragment.show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance

        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        TextView DateText = (TextView)findViewById(R.id.userDate);
        monthOfYear += 1;
        String daySet = "";
        if(dayOfMonth < EXTRAZERONEEDED) {
            daySet += "0";
        }
        daySet += "" + dayOfMonth;
        if(monthOfYear < EXTRAZERONEEDED) {
            DateText.setText("0" + monthOfYear + "/" + daySet + "/" + year);
        } else {
            DateText.setText(monthOfYear + "/" + daySet + "/" + year);
        }
        // showTimePickerDialog(findViewById(R.id.userDate));//this might not work
    }

    public void onTimeSet(TimePicker view, int hour, int minute){
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        TextView TimeText = (TextView)findViewById(R.id.userTime);
        String hourSet = "";
        if(hour < EXTRAZERONEEDED) {
            hourSet += "0";
        }
        hourSet += "" + hour;
        if(minute < EXTRAZERONEEDED) {
            TimeText.setText(hourSet + ":0" + minute);
        } else {
            TimeText.setText(hourSet + ":" + minute);
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = { R.drawable.medication, R.drawable.blood_glucose, R.drawable.activity, R.drawable.food};
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    public void onMedSubmit(String medName, String medAmount, String medUnit, String dateSend) {

        //String dateUser = "02/04/2017/11/14";
        String[] splitOnSlash = dateSend.split("/");
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(dateSend);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        String currentEntry = "";
        currentEntry += dateSend + "," + medName + "." + medAmount + "." + medUnit;
        String fileName = "medJournal" + "-" + splitOnSlash[0] + "-" + splitOnSlash[2] + ".txt";
        fileWrite(fileName, currentEntry, date);
    }

    public void onBloodSubmit(String bloodGlucoseNum, String dateSend) {

//        String dateUser = "02/04/2017/11/14";
        String[] splitOnSlash = dateSend.split("/");
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(dateSend);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        String currentEntry = "";
        currentEntry += dateSend + "," + bloodGlucoseNum;
        String fileName = "bloodJournal" + "-" + splitOnSlash[0] + "-" + splitOnSlash[2] + ".txt";
        fileWrite(fileName, currentEntry, date);

    }

    public void onActivitySubmit(String activityName, String activityDurationMin,
                                 String activityDurationSec, String dateSend) {
        //String dateUser = "02/05/2017/11/14";
        String[] splitOnSlash = dateSend.split("/");
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(dateSend);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        String currentEntry = "";
        currentEntry += dateSend + "," + activityName + "." + activityDurationMin
                + "." + activityDurationSec;
        String fileName = "activityJournal" + "-" + splitOnSlash[0] + "-" + splitOnSlash[2] + ".txt";
        fileWrite(fileName, currentEntry, date);
    }

    public void onFoodSubmit(String foodName, String calorieCount, String dateSend) {

        //String dateUser = "02/04/2017/11/14";
        String[] splitOnSlash = dateSend.split("/");
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(dateSend);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        String currentEntry = "";
        currentEntry += dateSend + "," + foodName + "." + calorieCount;
        String fileName = "foodJournal" + "-" + splitOnSlash[0] + "-" + splitOnSlash[2] + ".txt";
        fileWrite(fileName, currentEntry, date);

    }

    private void fileWrite(String fileName, String currentEntry, Date date) {
        String total = "";
        boolean exists = false;
        File f = new File(getBaseContext().getFilesDir(), fileName);
        FileWriter fWriter = null;
        if(f.exists()) {
            exists = true;
            Scanner fileRead = null;
            try {
                fileRead = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            String next = "";
            boolean found = false;
            boolean past = false;
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                if (date.after(findDate(line))) {
                    past = true;
                }
                if (found || !past) {
                    total += line + "\n";
                } else {
                    total += currentEntry + "\n" + line + "\n";
                    found = true;
                }
            }
            if(!past) {
                total += currentEntry + "\n";
            }
        }
        try {
            fWriter = new FileWriter(f);
            if(!exists) {
                total = currentEntry;
            }
            System.out.println("This is the total end before print " + total);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String finalTotal = total;
        if(exists) {
            finalTotal = total.substring(0, total.length() - 1);
        }
        PrintWriter pWriter = new PrintWriter(fWriter);
        pWriter.println(finalTotal);
        pWriter.close();
        Intent intent = new Intent(this, journalAverages.class);
        startActivity(intent);
    }

    private Date findDate(String passedDate) {
        String[] splitOnComma = passedDate.split(",");
        String dateUser = splitOnComma[0];
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy/HH/mm", Locale.US);
        try {
            date = format.parse(dateUser);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            //case R.id.submitButton:
            // User chose the "save" action, determine which fragment is the current one and call its save interface


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.Tab01MED, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

            // returning current tab
            switch (position){
                case 0:
                    Tab01MED tab01 = new Tab01MED();
                    return tab01;
                case 1:
                    Tab02BG tab02 = new Tab02BG();
                    return tab02;
                case 2:
                    Tab03ACT tab03 = new Tab03ACT();
                    return tab03;
                case 3:
                    Tab04FOOD tab04 = new Tab04FOOD();
                    return tab04;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
