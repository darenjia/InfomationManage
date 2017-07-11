package com.bokun.bkjcb.infomationmanage.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.infomationmanage.Domain.User;

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
            user.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
            users.add(user);
        }
        return users;
    }
}
