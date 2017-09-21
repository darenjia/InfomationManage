package com.bokun.bkjcb.infomationmanage.Domain;

public class DB_User {
    private int id;

    private int Unitid;

    private String UserName;

    private String LoginName;

    private String Password;

    private String U_Tel;

    private String TEL;

    private String Duty;

    private int Role_A;

    private int Role_B;

    private int Role_C;

    private int Role_D;

    private int Flag;

    public DB_User() {
    }

    public int getId() {
        return id;
    }

    public int getUnitid() {
        return Unitid;
    }

    public String getUserName() {
        return UserName;
    }

    public String getLoginName() {
        return LoginName;
    }

    public String getPassword() {
        return Password;
    }

    public String getU_Tel() {
        return U_Tel;
    }

    public String getTEL() {
        return TEL;
    }

    public String getDuty() {
        return Duty;
    }

    public int getRole_A() {
        return Role_A;
    }

    public int getRole_B() {
        return Role_B;
    }

    public int getRole_C() {
        return Role_C;
    }

    public int getRole_D() {
        return Role_D;
    }

    public int getFlag() {
        return Flag;
    }
}
