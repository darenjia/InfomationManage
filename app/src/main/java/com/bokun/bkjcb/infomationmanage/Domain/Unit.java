package com.bokun.bkjcb.infomationmanage.Domain;

/**
 * Created by DengShuai on 2017/7/12.
 */

public class Unit {
    private int id;
    private int Quxian;
    private String Address;
    private String Tel;
    private String Fax;
    private String Zipcode;
    private int LevelID;
    private int IsShow;
    private String QuxianName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuxian() {
        return Quxian;
    }

    public void setQuxian(int quxian) {
        Quxian = quxian;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public int getLevelID() {
        return LevelID;
    }

    public void setLevelID(int levelID) {
        LevelID = levelID;
    }

    public int getIsShow() {
        return IsShow;
    }

    public void setIsShow(int isShow) {
        IsShow = isShow;
    }

    public String getQuxianName() {
        return QuxianName;
    }

    public void setQuxianName(String quxianName) {
        QuxianName = quxianName;
    }
}
