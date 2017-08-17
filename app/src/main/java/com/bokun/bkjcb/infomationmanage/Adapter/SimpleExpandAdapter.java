package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.Utils.Cn2Spell;
import com.github.zagum.expandicon.ExpandIconView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class SimpleExpandAdapter extends BaseExpandableListAdapter implements Filterable {

    protected List<User> list = null;
    private List<User> list1 = new ArrayList<>();
    private List<User> list2 = new ArrayList<>();
    private List<User> list3 = new ArrayList<>();
    private List<User> list4 = new ArrayList<>();
    private List<User> list5 = new ArrayList<>();
    private List<User> list6 = new ArrayList<>();
    private List<User> parentList = new ArrayList<>();
    private List<String> unitName;
    private List<List<User>> users;
    protected Context mContext;
    protected MyFilter mFilter;
    private int position1, position2, position3, position4;
    private boolean flag = false;

    public SimpleExpandAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
        this.list1.addAll(list);
        this.parentList.addAll(list);
        initUserData(list);
    }

    private void initUserData(List<User> list_p) {
        users = new ArrayList<>();
        unitName = new ArrayList<>();
        List<User> userList = null;
        for (User u : list_p) {
            if (!unitName.contains(u.getUnitName())) {
                unitName.add(u.getUnitName());
                if (userList != null) {
                    users.add(userList);
                }
                userList = new ArrayList<>();
                userList.add(u);
            } else {
                if (userList != null) {
                    userList.add(u);
                }
            }
        }
        users.add(userList);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
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
        AvatarImageView iv = (AvatarImageView) view.findViewById(R.id.item_avatar);
        User user = users.get(groupPosition).get(childPosition);
//        tv.setCenterString(users.get(groupPosition).get(childPosition).getUserName());
        tv.setText(user.getUserName());
        iv.setTextAndColor(tv.getText().toString().substring(0, 1), getColor(mContext));
        qx.setText(user.getUnit().getQuXian());
        bm.setText(user.getLevel().getDepartmentName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String[] key = charSequence.toString().split(",", 2);
            FilterResults result = new FilterResults();
            List<User> users;
            if (TextUtils.isEmpty(key[1])) {//当过滤的关键字为空的时候，我们则显示所有的数据
                users = list;
            } else {//否则把符合条件的数据对象添加到集合中
                users = new ArrayList<>();
                if (!Boolean.parseBoolean(key[0])) {
                    for (User user : list) {
                        if (user.toString().contains(key[1].toLowerCase())) {
                            users.add(user);
                        }
                    }
                } else {
                    for (User user : list) {
                        String s = user.getUnitName() + Cn2Spell.getPinYin(user.getUnitName());
                        if (s.contains(key[1].toLowerCase())) {
                            users.add(user);
                        }
                    }
                }
            }
            result.values = users; //将得到的集合保存到FilterResults的value变量中
            result.count = users.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list = (List<User>) filterResults.values;
           /* if (filterResults.count > 0) {
                notifyDataSetChanged();//通知数据发生了改变
            } else {
                notifyDataSetInvalidated();//通知数据失效
            }*/
            initUserData(list);
            notifyDataSetChanged();
        }
    }

    public void initData() {
        list = parentList;
        initUserData(list);
        notifyDataSetChanged();
    }

    public void repaceData() {
        if (list.size() == 0) {
            list = parentList;
        }
    }

    public List<User> getData() {
        return list;
    }

    public void setData(List<User> l) {
        list = l;
    }

    public void filtUser(int spinnerId, int selectedId) {
        switch (spinnerId) {
            case 0://单位选择
                if (selectedId == 0) {
                    list = list1;
                } else {
                    list = getList(selectedId);
                    list2.clear();
                    list2.addAll(list);
                }
                position1 = selectedId;
                break;
            case 1://区级选择
                if (selectedId == 0) {
                    list = list2;
                } else {
                    list = getSecondList(selectedId);
                    list3.clear();
                    list3.addAll(list);
                }
                position2 = selectedId;
                break;
            case 2:
                if (selectedId == 0) {
                    list = list3;
                } else {
                    list = getThirdList(selectedId);
                    list4.clear();
                    list4.addAll(list);
                }
                position3 = selectedId;
                break;
            case 3:
                if (selectedId == 0) {
                    list = list4;
                } else {
                    list = getFourthList(selectedId);
                    list5.clear();
                    list5.addAll(list);
                }
                position4 = selectedId;
                break;
            case 4:
                if (selectedId == 0) {
                    list = list5;
                } else {
                    list = getFifthList(selectedId);
                    list6.clear();
                    list6.addAll(list);
                }
                break;
            case 5:
                if (selectedId == 0) {
                    list = list6;
                } else {
                    list = getSixthList(selectedId);
                }
                break;
        }
        parentList.clear();
        parentList.addAll(list);
        initUserData(list);
        notifyDataSetChanged();
    }

    private List<User> getList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list1) {
            if (id == 1) {
                if (user.getLevel().getLevel() == 0 || user.getLevel().getLevel() == 1) {
                    users.add(user);
                }
            } else {
                if (user.getLevel().getLevel() == 2) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    private List<User> getSecondList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list2) {
            if (position1 == 1) {
                if (id == 1) {
                    if (user.getLevel().getLevel() == 0) {
                        users.add(user);
                    }
                } else {
                    if (user.getLevel().getLevel() == 1) {
                        users.add(user);
                    }
                }

            } else {
                if (user.getLevel().getKind1() == id) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    private List<User> getThirdList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list3) {
            if (position1 == 2) {
                if (user.getLevel().getKind2() == id) {
                    users.add(user);
                }
            } else {
                if (position2 == 1) {
                    if (user.getLevel().getKind1() == id) {
                        users.add(user);
                    }
                } else {
                    if (user.getLevel().getQuxin() == id) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    private List<User> getFourthList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list4) {
            if (position2 == 1) {//选择市级
                if (user.getLevel().getKind2() == id) {
                    users.add(user);
                }
            } else {
                if (user.getLevel().getKind1() == id) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    private List<User> getFifthList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list5) {
            if (user.getLevel().getKind2() == id) {
                users.add(user);
            }
        }
        return users;
    }

    private List<User> getSixthList(int id) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : list6) {
            if (user.getLevel().getKind3() == id) {
                users.add(user);
            }
        }
        return users;
    }

    public User getUser(int group, int child) {
        return users.get(group).get(child);
    }

    public static int getColor(Context context) {
        Random random = new Random();
        int[] colors = {R.color.colorPrimary, R.color.red, R.color.yellow,R.color.green, R.color.colorAccent};
        return context.getResources().getColor(colors[random.nextInt(4)]);
    }

}