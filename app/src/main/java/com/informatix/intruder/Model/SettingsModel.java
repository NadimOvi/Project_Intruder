package com.informatix.intruder.Model;

public class SettingsModel {
    private String phone1;
    private String phone2;
    private String phone3;
    private int buzzer;
    private int relay;

    public SettingsModel() {
    }

    public SettingsModel(String phone1, String phone2, String phone3, int buzzer, int relay) {
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.buzzer = buzzer;
        this.relay = relay;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public int getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(int buzzer) {
        this.buzzer = buzzer;
    }

    public int getRelay() {
        return relay;
    }

    public void setRelay(int relay) {
        this.relay = relay;
    }
}
