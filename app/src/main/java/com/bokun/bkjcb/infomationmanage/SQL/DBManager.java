package com.bokun.bkjcb.infomationmanage.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.Unit;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Utils.L;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/7/11.
 */

public class DBManager {
    private SQLiteDatabase database;
    private Context context;
    private static DBManager manager;
    private Cursor cursor;

    private DBManager(Context context) {
        this.context = context;
        database = SQLUtil.newInstance(context).getDatabase();
    }

    public static DBManager newInstance(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    public User queryUserByLoginName(String loginName) {
        User user = null;
        cursor = database.query("User", null, "LoginName=?", new String[]{loginName}, null, null, null);
        if (cursor.moveToNext()) {
            user = new User();
            user.setUnitId(cursor.getInt(cursor.getColumnIndex("Unitid")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
        }
        return user;
    }

    public ArrayList<User> queryAllUser() {
        User user = null;
        ArrayList<User> users = new ArrayList<>();
        cursor = database.rawQuery("select * from User", null);
        while (cursor.moveToNext()) {
            user = new User();
            user.setUnitId(cursor.getInt(cursor.getColumnIndex("Unitid")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("U_Tel")));
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
            user.setUnit(queryUnit(user.getUnitId()));
            user.setLevel(queryLevel(user.getUnit().getLevelId()));
            user.setUnitName(user.getLevel().getDepartmentName());
            users.add(user);
        }
        return users;
    }

    public Unit queryUnit(int unitId) {
        Unit unit = null;
        Cursor cursor = database.rawQuery("SELECT a.Address,a.Fax,a.Tel,a.Zipcode,a.LevelID,b.NewName FROM Unit a LEFT JOIN z_Quxian b ON b.id = a.Quxian where a.id = ?",
                new String[]{String.valueOf(unitId)});
        if (cursor.moveToNext()) {
            unit = new Unit();
            unit.setQuXian(cursor.getString(cursor.getColumnIndex("NewName")));
            unit.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            unit.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
            unit.setTel(cursor.getString(cursor.getColumnIndex("Tel")));
            unit.setZipCode(cursor.getString(cursor.getColumnIndex("Zipcode")));
            unit.setLevelId(cursor.getInt(cursor.getColumnIndex("LevelID")));
        }
        return unit;
    }

    public Level queryLevel(int levelId) {
        Level level = null;
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level where id = ?",
                new String[]{String.valueOf(levelId)});
        if (cursor.moveToNext()) {
            level = new Level();
            level.setQuxin(cursor.getInt(cursor.getColumnIndex("Quxian")));
            level.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            level.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            level.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            level.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            level.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
        }
        return level;
    }

    public ArrayList<String> queryAllUnitName(String level) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level=? AND Kind2 IS NULL", new String[]{level});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryAllQuName() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Quxian", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("NewName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByQu(int quId) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Quxian = ? AND Kind2 IS Null ", new String[]{String.valueOf(quId)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByUnitId(int unitId) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 2 AND Kind1 = ? AND Kind2 IS NOT NULL", new String[]{String.valueOf(unitId)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByQu(int quID, int kind1) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 1 AND Quxian = ? AND Kind1 = ? AND Kind2 is NOT NULL AND Kind3 IS NULL", new String[]{String.valueOf(quID), String.valueOf(kind1)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByQu(int quID, int kind1, int kind2) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 1 AND Quxian = ? AND Kind1 = ? AND Kind2 = ? AND Kind3 IS NOT NULL", new String[]{String.valueOf(quID), String.valueOf(kind1), String.valueOf(kind2)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByKind1(int kind1) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 0 AND Kind1 = ? AND Kind2 is NOT NULL AND Kind3 IS NULL", new String[]{String.valueOf(kind1)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

}
