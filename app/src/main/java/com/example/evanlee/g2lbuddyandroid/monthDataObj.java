package com.example.evanlee.g2lbuddyandroid;

/**
 * Created by Taylor on 1/24/2017.
 */

public class monthDataObj {
    String month;
    int glucoseAvg;
    int activitySum;
    int foodSum;
    int glucoseDeviation;

    public monthDataObj() {
        month = "";
        glucoseAvg = 0;
        activitySum = 0;
        foodSum = 0;
        glucoseDeviation = 0;
    }

    public monthDataObj(String month, int glucoseAvg, int activitySum,
                        int foodSum, int glucoseDeviation) {
        this.month = month;
        this.glucoseAvg = glucoseAvg;
        this.activitySum = activitySum;
        this.foodSum = foodSum;
        this.glucoseDeviation = glucoseDeviation;
    }

    public String getMonth() {
        return month;
    }

    public int getGlucoseAvg() {
        return glucoseAvg;
    }

    public int getActivitySum() {
        return activitySum;
    }

    public int getFoodSum() {
        return foodSum;
    }

    public int getGlucoseDeviation() {
        return glucoseDeviation;
    }
}
