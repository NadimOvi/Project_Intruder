package com.informatix.intruder.Model;

public class DayModels implements Comparable<DayModels>{
    private  boolean isSectionHeader;
    private int date_id;
    private String date;
    private String start_time;
    private String end_time;
    private int device_id;

    public DayModels() {
    }

    public DayModels(int date_id, String date, String start_time, String end_time, int device_id) {
        this.date_id = date_id;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.device_id = device_id;
        isSectionHeader = false;
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

    public int getDate_id() {
        return date_id;
    }

    public void setDate_id(int date_id) {
        this.date_id = date_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    @Override
    public int compareTo(DayModels dayModels) {
        return this.date.compareTo(dayModels.date);
    }
}
