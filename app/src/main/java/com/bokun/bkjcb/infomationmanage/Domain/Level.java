package com.bokun.bkjcb.infomationmanage.Domain;

import java.io.Serializable;

/**
 * Created by DengShuai on 2017/7/12.
 */

public class Level implements Serializable{
    private int id;
    private int Quxian;
    private String DepartmentName;
    private String DepartmentNameA;
    private int Level;
    private int Kind1;
    private int Kind2;
    private int Kind3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuxian() {
        return Quxian;
    }

    public void setQuxian(int quxin) {
        this.Quxian = quxin;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.DepartmentName = departmentName;
    }

    public String getDepartmentNameA() {
        return DepartmentNameA;
    }

    public void setDepartmentNameA(String departmentNameA) {
        this.DepartmentNameA = departmentNameA;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        this.Level = level;
    }

    public int getKind1() {
        return Kind1;
    }

    public void setKind1(int kind1) {
        this.Kind1 = kind1;
    }

    public int getKind2() {
        return Kind2;
    }

    public void setKind2(int kind2) {
        this.Kind2 = kind2;
    }

    public int getKind3() {
        return Kind3;
    }

    public void setKind3(int kind3) {
        this.Kind3 = kind3;
    }
}
