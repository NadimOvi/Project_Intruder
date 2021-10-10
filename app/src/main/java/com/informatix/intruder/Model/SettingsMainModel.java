package com.informatix.intruder.Model;

import java.util.ArrayList;

public class SettingsMainModel {
    private ArrayList<SettingsModel> settings;

    public SettingsMainModel() {
    }

    public SettingsMainModel(ArrayList<SettingsModel> settings) {
        this.settings = settings;
    }

    public ArrayList<SettingsModel> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<SettingsModel> settings) {
        this.settings = settings;
    }
}
