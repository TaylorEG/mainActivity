package com.example.evanlee.g2lbuddyandroid;

/**
 * Created by maxrhirata on 2/20/17.
 */

public class DailyTotalDataObj {

    //int id;

    private String day;
    private String month;
    private String year;

    //combines the day + month + and year
    // -note: might not be nessarry later on still just testing something new
    private String date;

    private String blood_glucose_avg;
    private String activity_total;
    private String carbs_total;

    public DailyTotalDataObj(){

        //id = 0;
        day = "1";
        month = "January";
        year = "2017";

        //DailyData = new ArrayList<DailyInputObj>();

        date = day + " " + month + " " + year;

        blood_glucose_avg = "";
        activity_total = "";
        carbs_total = "";
    }

    public DailyTotalDataObj(String day_, String month_, String year_, String blood_glucose_avg_, String activity_total_, String carbs_total_)
    {
        //this.id = id_;
        this.day = day_;
        this.month = month_;
        this.year = year_;
        this.date = day_ + " " + month_ + " " + year_;
        this.blood_glucose_avg = blood_glucose_avg_;
        this.activity_total = activity_total_;
        this.carbs_total = carbs_total_;
    }

    //public int getId(){return id;}

    // helper function for input_data function by adjusting the data
    // every time new data is included

    /*
    private void calc_data(){
        for(DailyInputObj i : DailyData){

            // reset after adding new data
            blood_glucose_avg = 0;
            activity_total = 0;
            carbs_total = 0;

            int count_BloodGlucose = 0;
            int BloodGlucose_data = 0;
            //calculate the data from the data ArrayList
            if (i.getData_name() == "Blood Glucose"){
                BloodGlucose_data += i.getData_level();
            }
            else if(i.getData_name() == "Activity"){
                activity_total += i.getData_level();
            }
            else if(i.getData_name() == "Carbs"){
                carbs_total += i.getData_level();
            }

            blood_glucose_avg = BloodGlucose_data/count_BloodGlucose;
        }
    }


    public void input_data(DailyInputObj data){

        //Set date to the first item input for that day
        if(DailyData.size() == 0)
            date = data.getDate();

        //If data does not match date, do nothing
        if(date != data.getDate())
            return;

        DailyData.add(data);
        calc_data();
    }*/


    //setter functions
    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBlood_glucose_avg(String blood_glucose_avg) {
        this.blood_glucose_avg = blood_glucose_avg;
    }

    public void setActivity_total(String activity_total) {
        this.activity_total = activity_total;
    }

    public void setCarbs_total(String carbs_total) {
        this.carbs_total = carbs_total;
    }

    // getter functions
    public String getDay(){return day;}

    public String getMonth(){return  month;}

    public String getYear(){return year;}

    public String getDate() { return date;}

    public String getBlood_glucose_avg(){return blood_glucose_avg;}

    public String getActivity_total() { return activity_total;}

    public String getCarbs_total() { return carbs_total;}


}
