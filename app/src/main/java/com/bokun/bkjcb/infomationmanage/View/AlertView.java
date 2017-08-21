package com.bokun.bkjcb.infomationmanage.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/7/13.
 */

public class AlertView {
    private ViewHolder holder;

    public View build(User user, Context context, View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.tel = (TextView) view.findViewById(R.id.phoneNumber);
            holder.quXian = (TextView) view.findViewById(R.id.quXian);
            holder.department = (TextView) view.findViewById(R.id.department);
            holder.address = (TextView) view.findViewById(R.id.unit_address);
            holder.phone = (TextView) view.findViewById(R.id.unit_phone);
            holder.fax = (TextView) view.findViewById(R.id.unit_fax);
            holder.zipCode = (TextView) view.findViewById(R.id.unit_zipcode);
            holder.callBtn = (ImageView) view.findViewById(R.id.call);
        }
      /*  holder.name.setText(user.getUserName());
        holder.tel.setText(user.getTel());
        holder.quXian.setText(user.getUnit().getQuXian());
        holder.department.setText(user.getLevel().getDepartmentName());
        holder.address.setText(user.getUnit().getAddress());
        holder.phone.setText(user.getUnit().getTel());
        holder.fax.setText(user.getUnit().getFax());
        holder.zipCode.setText(user.getUnit().getZipCode());
        holder.callBtn.setOnClickListener(listener);*/
        return view;
    }

    static class ViewHolder {
        TextView name;
        TextView tel;
        TextView quXian;
        TextView department;
        TextView address;
        TextView phone;
        TextView fax;
        TextView zipCode;
        ImageView callBtn;
    }

}
