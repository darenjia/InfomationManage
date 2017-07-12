package com.bokun.bkjcb.infomationmanage.Domain;

/**
 * Created by DengShuai on 2017/7/12.
 */

public class Unit {
    private String quXian;
    private String address;
    private String tel;
    private String fax;
    private String zipCode;
    private int levelId;

    public String getQuXian() {
        return quXian;
    }

    public void setQuXian(String quXian) {
        this.quXian = quXian;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
}
