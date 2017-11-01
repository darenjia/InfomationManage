package com.bokun.bkjcb.infomationmanage.Utils;

import android.text.TextUtils;

import com.bokun.bkjcb.infomationmanage.Domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2017/10/31.
 * Description :
 */

public class StringFilter {
    private ArrayList<User> list;

    public StringFilter(ArrayList<User> list) {
        this.list = list;
    }

    public List<User> filter(boolean flag, String key) {
        ArrayList<User> result;
        if (TextUtils.isEmpty(key)) {//当过滤的关键字为空的时候，我们则显示所有的数据
            result = list;
        } else {//否则把符合条件的数据对象添加到集合中
            result = new ArrayList<>();
            if (flag) {//true为按姓名查找
                for (User user : list) {
                    if (user.toString().contains(key.toLowerCase())) {
                        result.add(user);
                    }
                }
            } else {//false为按单位查找
                for (User user : list) {
                    String s = user.getUnitName() + Cn2Spell.getPinYin(user.getUnitName());
                    if (s.contains(key.toLowerCase())) {
                        result.add(user);
                    }
                }
            }
        }
        return result;
    }
}
