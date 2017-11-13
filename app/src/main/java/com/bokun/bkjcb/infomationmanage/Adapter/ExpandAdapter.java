package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.github.zagum.expandicon.ExpandIconView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class ExpandAdapter extends BaseExpandableListAdapter {

    protected List<User> list = new ArrayList<>();
    private List<User> parentList = new ArrayList<>();
    private List<String> unitName;
    private List<List<User>> users;
    protected Context mContext;
    private ExpandableListView listView;

    public ExpandAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list.addAll(list);
        initUserData(list);
    }

    private void initUserData(List<User> list_p) {
        users = new ArrayList<>();
        unitName = new ArrayList<>();
        List<User> userList = null;
        for (User u : list_p) {
            if (u.getLevel() == 1 && (u.getKind1() == 2 || u.getKind1() == 3) && u.getKind2() != 0) {
                if (!unitName.contains(u.getUnitName() + "(" + u.getQuXian() + ")")) {
                    unitName.add(u.getUnitName() + "(" + u.getQuXian() + ")");
                    if (userList != null) {
                        Collections.sort(userList);
                        users.add(userList);
                    }
                    userList = new ArrayList<>();
                    userList.add(u);
                } else {
                    if (userList != null) {
                        if (!userList.contains(u)) {//去重
                            userList.add(u);
                        }
                    }
                }
            } else {
                if (!unitName.contains(u.getUnitName())) {
                    unitName.add(u.getUnitName());
                    if (userList != null) {
                        Collections.sort(userList);
                        users.add(userList);
                    }
                    userList = new ArrayList<>();
                    userList.add(u);
                } else {
                    if (userList != null) {
                        if (!userList.contains(u)) {//去重
                            userList.add(u);
                        }
                    }
                }
            }
        }
        if (userList != null) {
            Collections.sort(userList);
            users.add(userList);
        }
    }

    @Override
    public int getGroupCount() {
        return unitName == null ? 0 : unitName.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return users == null ? 0 : users.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return unitName == null ? null : unitName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return users == null ? null : users.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.parent_view, null);
        TextView name_view = (TextView) view.findViewById(R.id.catalog);
        TextView num = (TextView) view.findViewById(R.id.num);
        ExpandIconView icon = (ExpandIconView) view.findViewById(R.id.expand_icon);
        name_view.setText(unitName.get(groupPosition));
        num.setText(String.valueOf(users.get(groupPosition).size()));
        if (isExpanded) {
            icon.setState(ExpandIconView.LESS, false);
        } else {
            icon.setState(ExpandIconView.MORE, false);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.child_view, null);
//        SuperTextView tv = (SuperTextView) view.findViewById(R.id.info_tv);
        TextView tv = (TextView) view.findViewById(R.id.info_tv);
        TextView bm = (TextView) view.findViewById(R.id.info_dz);
        TextView qx = (TextView) view.findViewById(R.id.info_qx);
        ImageView role_a = (ImageView) view.findViewById(R.id.class_a);
        ImageView role_b = (ImageView) view.findViewById(R.id.class_b);
        ImageView role_c = (ImageView) view.findViewById(R.id.class_c);
        ImageView role_d = (ImageView) view.findViewById(R.id.class_d);
        AvatarImageView iv = (AvatarImageView) view.findViewById(R.id.item_avatar);
        User user = users.get(groupPosition).get(childPosition);
        tv.setText(user.getUserName());
        iv.setTextAndColor(tv.getText().toString().substring(0, 1), getColor(mContext, childPosition));
        qx.setText(user.getQuXian());
        bm.setText(user.getDuty());
        if (user.getRole_a() != 0) {
            role_a.setVisibility(View.VISIBLE);
        }
        if (user.getRole_b() != 0) {
            role_b.setVisibility(View.VISIBLE);
        }
        if (user.getRole_c() != 0) {
            role_c.setVisibility(View.VISIBLE);
        }
        if (user.getRole_d() != 0) {
            role_d.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getUnitListCount() {
        return unitName.size();
    }

    public ExpandableListView getListView() {
        return listView;
    }

    public void setListView(ExpandableListView listView) {
        this.listView = listView;
    }

    public List<User> getData() {
        return list;
    }

    public void replaceData(List<User> l) {
        list.clear();
        list.addAll(l);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        initUserData(list);
        super.notifyDataSetChanged();
        openOrClose();
    }

    public User getUser(int group, int child) {
        return users.get(group).get(child);
    }

    public static int getColor(Context context, int position) {
        int[] colors = {R.color.color_type_0, R.color.color_type_1, R.color.color_type_2, R.color.color_type_3};
        return context.getResources().getColor(colors[position % 4]);
    }

    private void openOrClose() {
        if (getUnitListCount() == 1) {
            listView.expandGroup(0);
        } else if (getUnitListCount() > 1) {
            for (int i = 0; i < getUnitListCount(); i++) {
                listView.collapseGroup(i);
            }
        }
    }
}