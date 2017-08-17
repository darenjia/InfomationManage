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
    private Unit unit;
    private Level level;
    private String unitName;

    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母
//    private String Tag;//标签

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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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

    @Override
    public int compareTo(User another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }

    @Override
    public String toString() {
        return this.userName + this.tel + Cn2Spell.getPinYin(userName) + this.phoneNumber;
    }

//    public void changePinying(int flag) {
//        if (flag == 0) {
//            pinyin = Cn2Spell.getPinYin(userName); // 根据姓名称获取拼音
//        } else {
//            pinyin = Cn2Spell.getPinYin(unitName); // 根据单位名称获取拼音
//        }
//        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取单位名称首字母并转成大写
//        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
//            firstLetter = "#";
//        }
//    }
}
