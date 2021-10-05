package com.informatix.intruder.Model;

public class LoginModel {
    private String name;
    private Integer device_id;
    private String valid_till;

    public LoginModel() {
    }

    public LoginModel(String name, Integer device_id, String valid_till) {
        this.name = name;
        this.device_id = device_id;
        this.valid_till = valid_till;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Integer device_id) {
        this.device_id = device_id;
    }

    public String getValid_till() {
        return valid_till;
    }

    public void setValid_till(String valid_till) {
        this.valid_till = valid_till;
    }
}
