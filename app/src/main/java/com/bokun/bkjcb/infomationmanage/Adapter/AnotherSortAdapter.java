package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.List;

public class AnotherSortAdapter extends SortAdapter {


    public AnotherSortAdapter(Context mContext, List<User> list) {
        super(mContext, list);
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
            viewHolder.catalog.setText(user.getUnitName());
        } else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getUserName());
        viewHolder.quXian.setText(this.list.get(position).getUnit().getQuXian());
        viewHolder.departmentName.setText(this.list.get(position).getLevel().getDepartmentName());
        return view;

    }

}