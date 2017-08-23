package com.bokun.bkjcb.infomationmanage.Domain;

import com.bokun.bkjcb.infomationmanage.Utils.Cn2Spell;

import java.io.Serializable;

/**
 * Created by DengShuai on 2017/7/11.
 */

public class User implements Serializable, Comparable<User> {
    private int unitId;
    private String userName;
    private String loginName;
    private String password;
    private String tel;
    private String phoneNumber;
    private int flag;
    private String unitName;
    private int role_a;
    private int role_b;
    private int role_c;
    private int role_d;
    private String duty;

    private String pinyin; // 单位名称对应的拼音
    private String firstLetter; // 单位的首字母
//    private String Tag;//标签

    String py;
    String letter;

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        py = Cn2Spell.getPinYin(userName); // 根据姓名获取拼音
        letter = py.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!letter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            letter = "#";
        }
    }

//    public String getTag() {
//        if (Tag == null) {
//            return firstLetter.toUpperCase();
//        }
//        return Tag;
//    }
//
//    public void setTag(String tag) {
//        Tag = tag;
//    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passWord) {
        this.password = passWord;
    }

    public String getTel() {
        return tel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
        pinyin = Cn2Spell.getPinYin(unitName); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    public int getRole_a() {
        return role_a;
    }

    public void setRole_a(int role_a) {
        this.role_a = role_a;
    }

    public int getRole_b() {
        return role_b;
    }

    public void setRole_b(int role_b) {
        this.role_b = role_b;
    }

    public int getRole_c() {
        return role_c;
    }

    public void setRole_c(int role_c) {
        this.role_c = role_c;
    }

    public int getRole_d() {
        return role_d;
    }

    public void setRole_d(int role_d) {
        this.role_d = role_d;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Override
    public int compareTo(User another) {
        //按拼音排序
//       /* if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
//            return 1;
//        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
//            return -1;
//        } else {
//            return pinyin.compareToIgnoreCase(another.getPinyin());
//        }*/

        //按姓名拼音排序
        if (getRole_a() > 0
                || getRole_b() > 0 && another.getRole_a() <= 0
                || getRole_c() > 0 && (another.getRole_a() <= 0 && another.getRole_b() <= 0)
                || getRole_d() > 0 && (another.getRole_a() <= 0 && another.getRole_b() <= 0 && another.getRole_c() <= 0)
                || letter.equals("#") && !letter.equals("#")) {
            return -1;
        } else if (another.getRole_a() > 0
                || another.getRole_b() > 0
                || another.getRole_c() > 0
                || another.getRole_d() > 0
                || !letter.equals("#") && letter.equals("#")) {
            return 1;
        } else {
            return py.compareToIgnoreCase(another.py);
        }
    }

    @Override
    public String toString() {
        return this.userName + this.tel + Cn2Spell.getPinYin(userName) + this.phoneNumber;
    }

    private int quxin;
    private String departmentName;
    private int level;
    private int kind1;
    private int kind2;
    private int kind3;
    private String departmentNameA;

    public int getQuxin() {
        return quxin;
    }

    public void setQuxin(int quxin) {
        this.quxin = quxin;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKind1() {
        return kind1;
    }

    public void setKind1(int kind1) {
        this.kind1 = kind1;
    }

    public int getKind2() {
        return kind2;
    }

    public void setKind2(int kind2) {
        this.kind2 = kind2;
    }

    public int getKind3() {
        return kind3;
    }

    public void setKind3(int kind3) {
        this.kind3 = kind3;
    }

    public String getDepartmentNameA() {
        return departmentNameA;
    }

    public void setDepartmentNameA(String departmentNameA) {
        this.departmentNameA = departmentNameA;
    }

    private String quXian;
    private String address;
    private String tel_U;
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

    public String getTel_U() {
        return tel_U;
    }

    public void setTel_U(String tel) {
        this.tel_U = tel;
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
