package com.sawan.recipeone;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String userID;
    private int phone;
    private String dob;
    private String sex;

    public User(String name, String userID, int phone, String dob, String sex) {
        this.name = name;
        this.userID = userID;
        this.phone = phone;
        this.dob = dob;
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPhone() {
        return this.phone;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return this.dob;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return this.sex;
    }

}
