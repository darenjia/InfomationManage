package com.bokun.bkjcb.infomationmanage.Domain;

/**
 * Created by DengShuai on 2017/8/25.
 */

public class Emergency {
    private String Tel;
    private String Area;
    private String Unit;
    private String Remarks;

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        this.Tel = tel;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        this.Area = area;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        this.Remarks = remarks;
    }
}
