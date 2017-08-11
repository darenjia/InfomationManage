package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends BaseAdapter implements Filterable {

    protected List<User> list = null;
    private List<User> list1 = new ArrayList<>();
    private List<User> list2 = new ArrayList<>();
    private List<User> list3 = new ArrayList<>();
    private List<User> list4 = new ArrayList<>();
    private List<User> list5 = new ArrayList<>();
    private List<User> list6 = new ArrayList<>();
    private List<User> parentList = new ArrayList<>();
    protected Context mContext;
    protected MyFilter mFilter;
    private int position1, position2, position3, position4;

    public SortAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
        this.list1.addAll(list);
        this.parentList.addAll(list);
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final User user = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            viewHolder.quXian = (TextView) view.findViewById(R.id.quXian);
            viewHolder.departmentName = (TextView) view.findViewById(R.id.departmentName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(catalog)) {
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
        } else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getUserName());
        viewHolder.quXian.setText(this.list.get(position).getUnit().getQuXian());
        viewHolder.departmentName.setText(this.list.get(position).getLevel().getDepartmentName());
        return view;

    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
        TextView quXian;
        TextView departmentName;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<User> users;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                users = list;
            } else {//否则把符合条件的数据对象添加到集合中
                users = new ArrayList<>();
                for (User user : list) {
                    if (user.toString().contains(charSequence)) {
                        users.add(user);
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
            notifyDataSetChanged();
        }
    }

    public void initData() {
        list = parentList;
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
}