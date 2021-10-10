package com.informatix.intruder.Model;

import java.util.ArrayList;

public class MainDayModels {
    private ArrayList<DayModels> dates;

    public MainDayModels(ArrayList<DayModels> dates) {
        this.dates = dates;
    }

    public MainDayModels() {
    }

    public ArrayList<DayModels> getDates() {
        return dates;
    }

    public void setDates(ArrayList<DayModels> dates) {
        this.dates = dates;
    }
}
