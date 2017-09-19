package com.bokun.bkjcb.infomationmanage.Http;


/**
 * Created by BKJCB on 2017/3/16.
 */

import com.bokun.bkjcb.infomationmanage.Domain.DB_User;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.LevelBind;
import com.bokun.bkjcb.infomationmanage.Domain.Unit;
import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
import com.bokun.bkjcb.infomationmanage.Domain.Z_Quxian;
import com.bokun.bkjcb.infomationmanage.Utils.L;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by BKJCB on 2017/3/16.
 */

public class JsonParser {

    public static String parseJSON(String result, String name) {
        String json = null;
        try {
            JSONObject object = new JSONObject(result);
            json = object.get(name).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static ArrayList<VersionInfo> getResultData(String json) {
        L.i(json);
        json = parseJSON(json, "GetyearResult");
        L.i(json);
        ArrayList<VersionInfo> results = new ArrayList<>();
        if (json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            VersionInfo result = gson.fromJson(element, VersionInfo.class);
            results.add(result);
        }
        return results;
    }

    public static ArrayList<Emergency> getEmergency(String json) {
//        L.i(json);
        json = parseJSON(json, "emergencyResult");
        ArrayList<Emergency> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
        L.i(json);
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Emergency result = gson.fromJson(element, Emergency.class);
            results.add(result);
        }
        return results;
    }

    public static ArrayList<LevelBind> getLevelBind(String json) {
        L.i("levelbindResult:" + json);
        json = parseJSON(json, "levelbindResult");
        ArrayList<LevelBind> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
//        L.i(json);
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            LevelBind result = gson.fromJson(element, LevelBind.class);
            results.add(result);
        }
        return results;
    }

    public static ArrayList<Unit> getUnit(String json) {
        L.i("unitResult:" + json);
        json = parseJSON(json, "unitResult");
//        L.i(json + "");
        ArrayList<Unit> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Unit result = gson.fromJson(element, Unit.class);
            results.add(result);
        }
        return results;
    }

    public static ArrayList<Z_Quxian> getZ_Quxian(String json) {
        L.i("z_quxianResult:" + json);
        json = parseJSON(json, "z_quxianResult");
        ArrayList<Z_Quxian> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Z_Quxian result = gson.fromJson(element, Z_Quxian.class);
            results.add(result);
        }
        return results;
    }
    public static ArrayList<Level> getLevel(String json) {
        L.i("z_levelResult:" + json);
        json = parseJSON(json, "z_levelResult");
        ArrayList<Level> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            Level result = gson.fromJson(element, Level.class);
            results.add(result);
        }
        return results;
    }
    public static ArrayList<DB_User> getUser(String json) {
        L.i("userResult:" + json);
        json = parseJSON(json, "userResult");
        ArrayList<DB_User> results = new ArrayList<>();
        if (json == null || json.equals("{}")) {
            return results;
        }
        //将JSON的String 转成一个JsonArray对象
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : array) {
            DB_User result = gson.fromJson(element, DB_User.class);
            results.add(result);
        }
        return results;
    }
}

