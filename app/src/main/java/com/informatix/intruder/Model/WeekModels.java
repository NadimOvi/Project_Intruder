package com.informatix.intruder.Model;

public class WeekModels implements Comparable<WeekModels> {
    private  boolean isSectionHeader;
    private Integer day_id;
    private Integer week_day;
    private String start_time;
    private String end_time;
    private Integer device_id;

    public WeekModels(Integer day_id, Integer week_day, String start_time, String end_time, Integer device_id) {
        this.day_id = day_id;
        this.week_day = week_day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.device_id = device_id;
        isSectionHeader = false;
    }

    public WeekModels() {
    }

    public boolean isSectionHeader() {
        return isSectionHeader;
    }

   /* public void setSectionHeader(boolean sectionHeader) {
        isSectionHeader = sectionHeader;
    }*/

    public void setToSectionHeader() {
        isSectionHeader = true;
    }

    public Integer getDay_id() {
        return day_id;
    }

    public void setDay_id(Integer day_id) {
        this.day_id = day_id;
    }

    public Integer getWeek_day() {
        return week_day;
    }

    public void setWeek_day(Integer week_day) {
        this.week_day = week_day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    @Override
    public int compareTo(WeekModels weekModels) {
        return this.week_day.compareTo(weekModels.week_day);
    }
}
