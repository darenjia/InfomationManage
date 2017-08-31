package com.bokun.bkjcb.infomationmanage.SQL;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.Unit;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Utils.L;

import java.util.ArrayList;
import java.util.Collections;

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

    public User queryUserById(int id) {
        User user = null;
//        cursor = database.query("User", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor = database.rawQuery("SELECT " +
                        "a.id as userid," +
                        "a.Unitid," +
                        "a.UserName," +
                        "a.LoginName," +
                        "a.Password," +
                        "a.U_Tel," +
                        "a.TEL," +
                        "a.Role_A," +
                        "a.Role_B," +
                        "a.Role_C," +
                        "a.Role_D," +
                        "a.Flag," +
                        "a.Duty," +
                        "b.Address," +
                        "b.id," +
                        "b.Fax," +
                        "b.Quxian," +
                        "b.LevelID," +
                        "b.Tel as Number," +
                        "b.Zipcode," +
                        "c.NewName," +
                        "d.id," +
                        "d.DepartmentName," +
                        "d.Level," +
                        "d.Kind1," +
                        "d.Kind2," +
                        "d.Kind3, " +
                        "d.DepartmentNameA " +
                        "FROM " +
                        "User a " +
                        "LEFT JOIN Unit b ON a.Unitid = b.id " +
                        "LEFT JOIN z_Quxian c ON c.id = b.Quxian " +
                        "LEFT JOIN z_Level d ON b.LevelID = d.id " +
                        "where a.id = ?"
                , new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("userid")));
            user.setUnitId(cursor.getInt(cursor.getColumnIndex("Unitid")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("U_Tel")));
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
            user.setDuty(cursor.getString(cursor.getColumnIndex("Duty")));
            user.setRole_a(cursor.getInt(cursor.getColumnIndex("Role_A")));
            user.setRole_b(cursor.getInt(cursor.getColumnIndex("Role_B")));
            user.setRole_c(cursor.getInt(cursor.getColumnIndex("Role_C")));
            user.setRole_d(cursor.getInt(cursor.getColumnIndex("Role_D")));

            user.setQuXian(cursor.getString(cursor.getColumnIndex("NewName")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            user.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
            user.setTel_U(cursor.getString(cursor.getColumnIndex("Number")));
            user.setZipCode(cursor.getString(cursor.getColumnIndex("Zipcode")));
            user.setLevelId(cursor.getInt(cursor.getColumnIndex("LevelID")));

            user.setQuxin(cursor.getInt(cursor.getColumnIndex("Quxian")));
            user.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            user.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            user.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            user.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            user.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
            user.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
        }
        return user;
    }

    public ArrayList<User> queryAllUser() {
        User user = null;
        Unit unit = null;
        Level level = null;
        ArrayList<User> users = new ArrayList<>();
        cursor = database.rawQuery("SELECT " +
                        "a.id as userid," +
                        "a.Unitid," +
                        "a.UserName," +
                        "a.LoginName," +
                        "a.Password," +
                        "a.U_Tel," +
                        "a.TEL," +
                        "a.Role_A," +
                        "a.Role_B," +
                        "a.Role_C," +
                        "a.Role_D," +
                        "a.Flag," +
                        "a.Duty," +
                        "b.Address," +
                        "b.id," +
                        "b.Fax," +
                        "b.Quxian," +
                        "b.LevelID," +
                        "b.Tel as Number," +
                        "b.Zipcode," +
                        "c.NewName," +
                        "d.id," +
                        "d.DepartmentName," +
                        "d.Level," +
                        "d.Kind1," +
                        "d.Kind2," +
                        "d.Kind3, " +
                        "d.DepartmentNameA " +
                        "FROM " +
                        "User a " +
                        "LEFT JOIN Unit b ON a.Unitid = b.id " +
                        "LEFT JOIN z_Quxian c ON c.id = b.Quxian " +
                        "LEFT JOIN z_Level d ON b.LevelID = d.id " +
                        "ORDER BY d.DepartmentNameA COLLATE LOCALIZED ASC"
                , null);
        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("userid")));
            user.setUnitId(cursor.getInt(cursor.getColumnIndex("Unitid")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("U_Tel")));
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
            user.setDuty(cursor.getString(cursor.getColumnIndex("Duty")));
            user.setRole_a(cursor.getInt(cursor.getColumnIndex("Role_A")));
            user.setRole_b(cursor.getInt(cursor.getColumnIndex("Role_B")));
            user.setRole_c(cursor.getInt(cursor.getColumnIndex("Role_C")));
            user.setRole_d(cursor.getInt(cursor.getColumnIndex("Role_D")));

            user.setQuXian(cursor.getString(cursor.getColumnIndex("NewName")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            user.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
            user.setTel_U(cursor.getString(cursor.getColumnIndex("Number")));
            user.setZipCode(cursor.getString(cursor.getColumnIndex("Zipcode")));
            user.setLevelId(cursor.getInt(cursor.getColumnIndex("LevelID")));

            user.setQuxin(cursor.getInt(cursor.getColumnIndex("Quxian")));
            user.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            user.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            user.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            user.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            user.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
            user.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
//            L.i(user.getUserName());
            user.setUnitName(user.getDepartmentNameA());
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

    public ArrayList<Level> queryLevel(int level_l, int kind1, int kind2, int kind3, int quxianId) {
        ArrayList<Level> levels = new ArrayList<>();
        Level level = null;
        ArrayList<String> para = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM z_Level where level = ? ");
        para.add(String.valueOf(level_l));
        if (level_l == 1 && quxianId != -1) {
            sql.append("and Quxian = ? ");
            para.add(String.valueOf(quxianId));
        }

        if (kind1 == -1) {
            sql.append("and Kind1 is NOT NULL AND Kind2 IS NULL AND Kind3 IS NULL");
        } /*else if (kind2 == 0) {
            sql.append("and Kind1 = ? and Kind2 IS NOT NULL AND Kind3 IS NULL");
            para.add(String.valueOf(kind1));
        }*/ else if (kind2 != -1) {
            sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 IS NOT NULL");
            para.add(String.valueOf(kind1));
            para.add(String.valueOf(kind2));
        } else if (kind3 != -1) {
            sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 IS NOT NULL");
            para.add(String.valueOf(kind1));
            para.add(String.valueOf(kind2));
        } else {
            sql.append("and Kind1 = ? AND Kind2 IS NOT NULL AND Kind3 IS NULL");
            para.add(String.valueOf(kind1));
        }
        String[] strings = new String[para.size()];
        L.i(sql.toString());
        Cursor cursor = database.rawQuery(sql.toString(), para.toArray(strings));
        while (cursor.moveToNext()) {
            level = new Level();
            level.setQuxin(cursor.getInt(cursor.getColumnIndex("Quxian")));
            level.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            level.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
            level.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            level.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            level.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            level.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
            levels.add(level);
        }
        L.i(levels.size());
        return levels;
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

    public ArrayList<Level> queryAllQuName() {
        ArrayList<Level> list = new ArrayList<>();
        Level level = null;
        cursor = database.rawQuery("SELECT * FROM z_Quxian", null);
        while (cursor.moveToNext()) {
            level = new Level();
            String name = cursor.getString(cursor.getColumnIndex("NewName"));
            level.setLevel(1);
            level.setQuxin(cursor.getInt(cursor.getColumnIndex("id")));
            level.setDepartmentNameA(name);
            list.add(level);
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

    public ArrayList<User> queryUser(Level level) {
        ArrayList<User> users = new ArrayList<>();
        User user = null;
        StringBuilder builder = null;
        builder = new StringBuilder("SELECT " +
                "a.id," +
                "a.Unitid," +
                "a.UserName," +
                "a.LoginName," +
                "a.Password," +
                "a.U_Tel," +
                "a.TEL," +
                "a.Role_A," +
                "a.Role_B," +
                "a.Role_C," +
                "a.Role_D," +
                "a.Flag," +
                "a.Duty," +
                "b.Address," +
                "b.id," +
                "b.Fax," +
                "b.Quxian," +
                "b.LevelID," +
                "b.Tel as Number," +
                "b.Zipcode," +
                "c.NewName," +
                "d.id," +
                "d.DepartmentName," +
                "d.Level," +
                "d.Kind1," +
                "d.Kind2," +
                "d.Kind3, " +
                "d.DepartmentNameA " +
                "FROM " +
                "User a " +
                "LEFT JOIN Unit b ON a.Unitid = b.id " +
                "LEFT JOIN z_Quxian c ON c.id = b.Quxian " +
                "LEFT JOIN z_Level d ON b.LevelID = d.id " +
                "where d.Level = ? ");
        if (level.getLevel() == 1) {
            builder.append(" and d.Quxian = " + level.getQuxin());
        }
        if (level.getKind1() != 0) {
            builder.append(" and d.Kind1 = " + level.getKind1());
        } else {
            builder.append(" and d.kind1 IS Null ");
        }
        if (level.getKind2() != 0) {
            builder.append(" and d.Kind2 = " + level.getKind2());
        } else {
            builder.append(" and d.Kind2 IS Null");
        }
        if (level.getKind3() != 0) {
            builder.append(" and d.Kind3 = " + level.getKind3());
        } else {
            builder.append(" and d.Kind3 IS Null");
        }
        builder.append(" ORDER BY d.DepartmentNameA COLLATE LOCALIZED ASC");
        cursor = database.rawQuery(builder.toString(), new String[]{String.valueOf(level.getLevel())});
        while (cursor.moveToNext()) {
            user = new User();
            user.setUnitId(cursor.getInt(cursor.getColumnIndex("Unitid")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("U_Tel")));
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
            user.setDuty(cursor.getString(cursor.getColumnIndex("Duty")));
            user.setRole_a(cursor.getInt(cursor.getColumnIndex("Role_A")));
            user.setRole_b(cursor.getInt(cursor.getColumnIndex("Role_B")));
            user.setRole_c(cursor.getInt(cursor.getColumnIndex("Role_C")));
            user.setRole_d(cursor.getInt(cursor.getColumnIndex("Role_D")));

            user.setQuXian(cursor.getString(cursor.getColumnIndex("NewName")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            user.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
            user.setTel_U(cursor.getString(cursor.getColumnIndex("Number")));
            user.setZipCode(cursor.getString(cursor.getColumnIndex("Zipcode")));
            user.setLevelId(cursor.getInt(cursor.getColumnIndex("LevelID")));

            user.setQuxin(cursor.getInt(cursor.getColumnIndex("Quxian")));
            user.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            user.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            user.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            user.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            user.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
            user.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
            user.setUnitName(user.getDepartmentNameA());
            users.add(user);
        }
        L.i("啥啥啥：" + users.size());
        Collections.sort(users);
        return users;
    }

    public ArrayList<Emergency> getAllEmergency() {
        ArrayList<Emergency> list = new ArrayList<>();
        Emergency emergency = null;
        cursor = database.rawQuery("SELECT * FROM Emergency", null);
        while (cursor.moveToNext()) {
            emergency = new Emergency();
            emergency.setTel(cursor.getString(cursor.getColumnIndex("Tel")));
            emergency.setArea(cursor.getString(cursor.getColumnIndex("Area")));
            emergency.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
            emergency.setRemarks(cursor.getString(cursor.getColumnIndex("Remarks")));
            list.add(emergency);
        }
        return list;
    }

    //insert更新数据
    public boolean insertUsers(ArrayList<User> list) {

        for (int i = 0; i < list.size(); i++) {

        }
        return true;
    }

    private boolean insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("LoginName", user.getLoginName());
        values.put("UserName", user.getUserName());
        values.put("Unitid", user.getUnitId());
        values.put("Password", user.getPassword());
        values.put("U_Tel", user.getPhoneNumber());
        values.put("TEL", user.getTel());
        values.put("Duty", user.getDuty());
        values.put("Role_A", user.getRole_a());
        values.put("Role_B", user.getRole_b());
        values.put("Role_C", user.getRole_c());
        values.put("Role_D", user.getRole_d());
        values.put("Flag", user.getFlag());
        long flag = database.insert("User", "id", values);
        return flag > 0;
    }

    private boolean insertUnit(Unit unit) {
        ContentValues values = new ContentValues();
        values.put("Quxian", unit.getQuXian());
        values.put("Address", unit.getAddress());
        values.put("Tel", unit.getTel());
        values.put("Fax", unit.getFax());
        values.put("Zipcode", unit.getZipCode());
        values.put("LevelId", unit.getLevelId());
        long flag = database.insert("Unit", "id", values);
        return flag > 0;
    }

    private void createHistory() {
        database.execSQL("Create table history(" +
                "id Integer primary key," +
                "userid int(6)," +
                "time long(20)," +
                "username char(10)," +
                "tel char(20)" +
                ")");
    }

    public void insertHistory(HistoryItem item) {
        database.execSQL("insert into history(userid,time,tel,username) values (?,?,?,?)", new String[]{String.valueOf(item.getUserId()), String.valueOf(item.getTime()), item.getTel(), item.getUserName()});
    }

    private void updateHistory(HistoryItem item) {
        ContentValues values = new ContentValues();
        values.put("userid", item.getUserId());
        values.put("time", item.getTime());
        database.update("history", values, "id = ?", new String[]{String.valueOf(1)});
    }

    public ArrayList<HistoryItem> getAllHistoryItem() {
        ArrayList<HistoryItem> items = new ArrayList<>();
        HistoryItem item = null;
        try {
            cursor = database.query("history", null, null, null, null, null, "time DESC", "5");
        } catch (SQLiteException e) {
            createHistory();
            return items;
        }
        while (cursor.moveToNext()) {
            item = new HistoryItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("id")));
            item.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
            item.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            item.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            item.setTel(cursor.getString(cursor.getColumnIndex("tel")));
            items.add(item);
        }
        return items;
    }

    public boolean deleteHistory() {
        int flag = database.delete("history", null, null);
        return flag > 0;
    }
}
