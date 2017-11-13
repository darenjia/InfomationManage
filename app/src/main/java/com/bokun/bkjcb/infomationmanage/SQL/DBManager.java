package com.bokun.bkjcb.infomationmanage.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bokun.bkjcb.infomationmanage.Domain.DB_User;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.LevelBind;
import com.bokun.bkjcb.infomationmanage.Domain.Unit;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
import com.bokun.bkjcb.infomationmanage.Domain.Z_Quxian;
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
    //private Cursor cursor;

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

    public void close() {
        if (database != null) {
            database.close();
            manager = null;
        }
    }

    public ArrayList<User> queryUserByLoginName(String loginName) {
        ArrayList<User> users = new ArrayList<>();
        User user = null;
//        Cursor cursor = database.query("User", null, "LoginName=?", new String[]{loginName}, null, null, null);
        Cursor cursor = database.rawQuery("SELECT " +
                "a.id," +
                "a.UserName," +
                "a.LoginName," +
                "a.Password," +
                "a.U_Tel," +
                "a.TEL," +
                "a.Duty," +
                "b.Address," +
                "b.Quxian," +
                "c.NewName," +
                "d.DepartmentNameA " +
                "FROM " +
                "User a " +
                "LEFT JOIN Unit b ON a.Unitid = b.id " +
                "LEFT JOIN z_Quxian c ON c.id = b.Quxian " +
                "LEFT JOIN z_Level d ON b.LevelID = d.id " +
                "WHERE a.LoginName=?", new String[]{loginName});
        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("UserName")));
            user.setLoginName(cursor.getString(cursor.getColumnIndex("LoginName")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            user.setTel(cursor.getString(cursor.getColumnIndex("TEL")));
            user.setDuty(cursor.getString(cursor.getColumnIndex("Duty")));
            user.setTel_U(cursor.getString(cursor.getColumnIndex("U_Tel")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            user.setQuXian(cursor.getString(cursor.getColumnIndex("NewName")));
            user.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        L.i(users.size());
        return users;
    }

    public User queryUserById(int id) {
        User user = null;
//        cursor = database.query("User", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Cursor cursor = database.rawQuery("SELECT " +
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
        Cursor cursor = database.rawQuery("SELECT " +
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
                        "Where b.IsShow = 0 and a.Flag = 0 and d.flag = 0 " +
                        "ORDER BY d.DepartmentNameA COLLATE LOCALIZED ASC,d.Quxian ASC"
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

            user.setUnitName(user.getDepartmentNameA());
            //去重
           /* if (users.contains(user)) {
                L.i(user.getUserName());
                continue;
            }*/
            users.add(user);
        }
        return users;
    }

    public Unit queryUnit(int unitId) {
        Unit unit = null;
        Cursor cursor = database.rawQuery("SELECT a.Address,a.Fax,a.Tel,a.Zipcode,a.LevelID,a.Quxian,b.NewName FROM Unit a LEFT JOIN z_Quxian b ON b.id = a.Quxian where a.id = ?",
                new String[]{String.valueOf(unitId)});
        if (cursor.moveToNext()) {
            unit = new Unit();
            unit.setQuxian(cursor.getInt(cursor.getColumnIndex("Quxian")));
            unit.setQuxianName(cursor.getString(cursor.getColumnIndex("NewName")));
            unit.setAddress(cursor.getString(cursor.getColumnIndex("Address")));
            unit.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
            unit.setTel(cursor.getString(cursor.getColumnIndex("Tel")));
            unit.setZipcode(cursor.getString(cursor.getColumnIndex("Zipcode")));
            unit.setLevelID(cursor.getInt(cursor.getColumnIndex("LevelID")));
        }
        return unit;
    }

    public Level queryLevel(int levelId) {
        Level level = null;
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level where id = ?",
                new String[]{String.valueOf(levelId)});
        if (cursor.moveToNext()) {
            level = new Level();
            level.setQuxian(cursor.getInt(cursor.getColumnIndex("Quxian")));
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
        StringBuilder sql = new StringBuilder("SELECT * FROM z_Level where level = ? and flag = 0 ");
        para.add(String.valueOf(level_l));
        if (level_l == 1 && quxianId != -1) {
            sql.append("and Quxian = ? ");
            para.add(String.valueOf(quxianId));
        }

        if (kind1 == -1) {
            sql.append("and Kind1 != 0 AND Kind2 = 0 AND Kind3 = 0");
        } /*else if (kind2 == 0) {
            sql.append("and Kind1 = ? and Kind2 IS NOT NULL AND Kind3 IS NULL");
            para.add(String.valueOf(kind1));
        }*/ else if (kind2 != -1 && kind3 == -1) {
            sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 != 0");
            para.add(String.valueOf(kind1));
            para.add(String.valueOf(kind2));
        } else if (kind3 != -1) {
            sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 = ?");
            para.add(String.valueOf(kind1));
            para.add(String.valueOf(kind2));
            para.add(String.valueOf(kind3));
            return levels;
        } else {
            sql.append("and Kind1 = ? AND Kind2 != 0 AND Kind3 = 0");
            para.add(String.valueOf(kind1));
        }
        String[] strings = new String[para.size()];
//        L.i(sql.toString());
        Cursor cursor = database.rawQuery(sql.toString(), para.toArray(strings));
        while (cursor.moveToNext()) {
            level = new Level();
            level.setId(cursor.getInt(cursor.getColumnIndex("id")));
            level.setQuxian(cursor.getInt(cursor.getColumnIndex("Quxian")));
            level.setDepartmentName(cursor.getString(cursor.getColumnIndex("DepartmentName")));
            level.setDepartmentNameA(cursor.getString(cursor.getColumnIndex("DepartmentNameA")));
            level.setLevel(cursor.getInt(cursor.getColumnIndex("Level")));
            level.setKind1(cursor.getInt(cursor.getColumnIndex("Kind1")));
            level.setKind2(cursor.getInt(cursor.getColumnIndex("Kind2")));
            level.setKind3(cursor.getInt(cursor.getColumnIndex("Kind3")));
            if (level.getDepartmentNameA().equals("")) {
                level.setDepartmentNameA(level.getDepartmentName());
            }
            levels.add(level);
        }
        L.i(levels.size() + "：区县列表");
        return levels;
    }

    public ArrayList<String> queryAllUnitName(String level) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level=? AND Kind2 IS NULL", new String[]{level});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<Level> queryAllQuName() {
        ArrayList<Level> list = new ArrayList<>();
        Level level = null;
        Cursor cursor = database.rawQuery("SELECT * FROM z_Quxian", null);
        while (cursor.moveToNext()) {
            level = new Level();
            String name = cursor.getString(cursor.getColumnIndex("NewName"));
            level.setLevel(1);
            level.setQuxian(cursor.getInt(cursor.getColumnIndex("id")));
            level.setDepartmentNameA(name);
            list.add(level);
        }
        return list;
    }

    public Level queryLevelId(Level level) {
        ArrayList<Level> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        ArrayList<String> para = new ArrayList<>();
        builder.append("SELECT a.id FROM z_Level a where a.Level=? and a.Kind1=? and a.DepartMentNameA=?");
        para.add("" + level.getLevel());
        para.add("" + level.getKind1());
        para.add(level.getDepartmentNameA());
        if (level.getQuxian() != 0) {
            builder.append(" and a.Quxian=?");
            para.add("" + level.getQuxian());
        }
        String[] strings = new String[para.size()];
        Cursor cursor = database.rawQuery(builder.toString(),
                para.toArray(strings));
        while (cursor.moveToNext()) {
            level.setId(cursor.getInt(0));
        }
        return level;
    }

    public ArrayList<String> queryNameByQu(int quId) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Quxian = ? AND Kind2 IS Null ", new String[]{String.valueOf(quId)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByUnitId(int unitId) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 2 AND Kind1 = ? AND Kind2 IS NOT NULL", new String[]{String.valueOf(unitId)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByQu(int quID, int kind1) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 1 AND Quxian = ? AND Kind1 = ? AND Kind2 is NOT NULL AND Kind3 IS NULL", new String[]{String.valueOf(quID), String.valueOf(kind1)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByQu(int quID, int kind1, int kind2) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 1 AND Quxian = ? AND Kind1 = ? AND Kind2 = ? AND Kind3 IS NOT NULL", new String[]{String.valueOf(quID), String.valueOf(kind1), String.valueOf(kind2)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            list.add(name);
        }
        return list;
    }

    public ArrayList<String> queryNameByKind1(int kind1) {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部");
        Cursor cursor = database.rawQuery("SELECT * FROM z_Level WHERE Level = 0 AND Kind1 = ? AND Kind2 is NOT NULL AND Kind3 IS NULL", new String[]{String.valueOf(kind1)});
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
        if (level == null || level.getId() == -1) {
            return users;
        }
        builder = new StringBuilder("SELECT " +
                "a.id as userId," +
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
                "where b.IsShow = 0 and a.Flag = 0 and d.id = ? ");
       /* if (level.getLevel() == 1) {
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
        }*/
        builder.append(" ORDER BY d.DepartmentNameA COLLATE LOCALIZED ASC");
//        L.i(level.getId() + ":::id");
        Cursor cursor = database.rawQuery(builder.toString(), new String[]{String.valueOf(level.getId())});
        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt((cursor.getColumnIndex("userId"))));
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
        Collections.sort(users);
        return users;
    }

    public ArrayList<Emergency> getAllEmergency() {
        ArrayList<Emergency> list = new ArrayList<>();
        Emergency emergency = null;
        Cursor cursor = database.rawQuery("SELECT * FROM Emergency", null);
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
    public boolean insertUsers(ArrayList<DB_User> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("User", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertUser(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertUser(DB_User user) {
        ContentValues values = new ContentValues();
        values.put("id", user.getId());
        values.put("LoginName", user.getLoginName());
        values.put("UserName", user.getUserName());
        values.put("Unitid", user.getUnitid());
        values.put("Password", user.getPassword());
        values.put("U_Tel", user.getU_Tel());
        values.put("TEL", user.getTEL());
        values.put("Duty", user.getDuty());
        values.put("Role_A", user.getRole_A());
        values.put("Role_B", user.getRole_B());
        values.put("Role_C", user.getRole_C());
        values.put("Role_D", user.getRole_D());
        values.put("Flag", user.getFlag());
        long flag = database.insert("User", null, values);
        return flag > 0;
    }

    public boolean insertUnits(ArrayList<Unit> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("Unit", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertUnit(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertUnit(Unit unit) {
        ContentValues values = new ContentValues();
        values.put("id", unit.getId());
        values.put("Quxian", unit.getQuxian());
        values.put("Address", unit.getAddress());
        values.put("Tel", unit.getTel());
        values.put("Fax", unit.getFax());
        values.put("Zipcode", unit.getZipcode());
        values.put("LevelId", unit.getLevelID());
        values.put("IsShow", unit.getIsShow());
        long flag = database.insert("Unit", null, values);
        return flag > 0;
    }

    public boolean insertEmergency(ArrayList<Emergency> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("Emergency", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertEmergency(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertEmergency(Emergency emergency) {
        ContentValues values = new ContentValues();
        values.put("id", emergency.getId());
        values.put("Tel", emergency.getTel());
        values.put("Area", emergency.getArea());
        values.put("Unit", emergency.getUnit());
        values.put("Remarks", emergency.getRemarks());
        long flag = database.insert("Emergency", null, values);
        return flag > 0;
    }

    public boolean insertLevel(ArrayList<Level> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("z_Level", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertLevel(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertLevel(Level level) {
        ContentValues values = new ContentValues();
        values.put("id", level.getId());
        values.put("Quxian", level.getQuxian());
        values.put("DepartmentName", level.getDepartmentName());
        values.put("DepartmentNameA", level.getDepartmentNameA());
        values.put("Level", level.getLevel());
        values.put("Kind1", level.getKind1());
        values.put("Kind2", level.getKind2());
        values.put("Kind3", level.getKind3());
        values.put("flag", level.getFlag());
        long flag = database.insert("z_Level", null, values);
        return flag > 0;
    }

    public boolean insertLevelBind(ArrayList<LevelBind> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("levelbind", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertLevelBind(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertLevelBind(LevelBind level) {
        ContentValues values = new ContentValues();
        values.put("id", level.getId());
        values.put("G_LevelID", level.getGLevelid());
        values.put("Q_LevelID", level.getQLevelid());
        long flag = database.insert("levelbind", null, values);
        return flag > 0;
    }

    public boolean insertQuxian(ArrayList<Z_Quxian> list) {
        boolean flag = false;
        database.beginTransaction();
        try {
            database.delete("z_Quxian", null, null);
            for (int i = 0; i < list.size(); i++) {
                if (insertQuxian(list.get(i))) {
                    continue;
                } else {
                    throw new SQLiteException();
                }
            }
            database.setTransactionSuccessful();
            flag = true;
        } catch (SQLiteException e) {
            L.i(e.getMessage());
        } finally {
            database.endTransaction();
        }

        return flag;
    }

    private boolean insertQuxian(Z_Quxian quxian) {
        ContentValues values = new ContentValues();
        values.put("id", quxian.getId());
        values.put("NewName", quxian.getNewName());
        values.put("SortSeq", quxian.getSortSeq());
        long flag = database.insert("z_Quxian", null, values);
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

    public VersionInfo getVersionInfo() {
        VersionInfo versionInfo = null;
        Cursor cursor = database.query("z_version", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            versionInfo = new VersionInfo();
            versionInfo.setDataV(cursor.getString(cursor.getColumnIndex("data_V")));
            versionInfo.setProgramV(cursor.getString(cursor.getColumnIndex("program_V")));
        }
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo info) {
        ContentValues values = new ContentValues();
        values.put("data_V", info.getDataV());
        values.put("program_V", info.getProgramV());
        database.update("z_version", values, "id = ?", new String[]{String.valueOf(1)});
    }

    public ArrayList<HistoryItem> getAllHistoryItem(String limit) {
        ArrayList<HistoryItem> items = new ArrayList<>();
        HistoryItem item = null;
        Cursor cursor;
        try {
            cursor = database.query("history", null, null, null, null, null, "time DESC", limit);
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

    public ArrayList<Level> queryLevel(Level level) {
        ArrayList<Level> list = new ArrayList<>();
        Level l = null;
        Cursor cursor = database.query("levelbind", null, "G_LevelID = ?", new String[]{String.valueOf(level.getId())}, null, null, null);
        while (cursor.moveToNext()) {
            l = new Level();
            l.setId(cursor.getInt(cursor.getColumnIndex("Q_LevelID")));
            list.add(l);
        }
        return list;
    }

    public ArrayList<Level> queryLevel_G(Level level) {
        ArrayList<Level> list = new ArrayList<>();
        Level l = null;
        Cursor cursor = database.query("levelbind", null, "Q_LevelID = ?", new String[]{String.valueOf(level.getId())}, null, null, null);
        while (cursor.moveToNext()) {
            l = new Level();
            l.setId(cursor.getInt(cursor.getColumnIndex("G_LevelID")));
            list.add(l);
        }
        return list;
    }
}
