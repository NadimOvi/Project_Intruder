package com.informatix.intruder.Model;

import java.util.ArrayList;

public class MainWeekModels {
    private ArrayList<WeekModels> weekdays;

    public MainWeekModels() {
    }

    public MainWeekModels(ArrayList<WeekModels> weekdays) {
        this.weekdays = weekdays;
    }

    public ArrayList<WeekModels> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<WeekModels> weekdays) {
        this.weekdays = weekdays;
    }
}
