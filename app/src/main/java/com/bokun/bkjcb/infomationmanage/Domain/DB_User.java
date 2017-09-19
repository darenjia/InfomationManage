package com.bokun.bkjcb.infomationmanage.Domain;

public class DB_User {
    private final int id;

    private final int unitid;

    private final String userName;

    private final String loginName;

    private final String password;

    private final String uTel;

    private final String tel;

    private final String duty;

    private final int roleA;

    private final int roleB;

    private final int roleC;

    private final int roleD;

    private final int flag;

    public DB_User(int id, int unitid, String userName, String loginName, String password,
                   String uTel, String tel, String duty, int roleA, int roleB, int roleC,
                   int roleD, int flag) {
        this.id = id;
        this.unitid = unitid;
        this.userName = userName;
        this.loginName = loginName;
        this.password = password;
        this.uTel = uTel;
        this.tel = tel;
        this.duty = duty;
        this.roleA = roleA;
        this.roleB = roleB;
        this.roleC = roleC;
        this.roleD = roleD;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public int getUnitid() {
        return unitid;
    }

    public String getUserName() {
        return userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public String getUTel() {
        return uTel;
    }

    public String getTel() {
        return tel;
    }

    public String getDuty() {
        return duty;
    }

    public int getRoleA() {
        return roleA;
    }

    public int getRoleB() {
        return roleB;
    }

    public int getRoleC() {
        return roleC;
    }

    public int getRoleD() {
        return roleD;
    }

    public int getFlag() {
        return flag;
    }
}
