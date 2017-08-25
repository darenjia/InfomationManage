package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * Created by DengShuai on 2017/8/24.
 */

public class MultiTypeAdapter extends SimpleRecAdapter<User, MultiTypeAdapter.MultiTypeViewHolder> {

    public MultiTypeAdapter(Context context) {
        super(context);
    }

    public MultiTypeAdapter(Context context, ItemCallback callback) {
        super(context, callback);
    }

    public MultiTypeAdapter(Context context, List data) {
        super(context, data);
    }

    public MultiTypeAdapter(Context context, List data, ItemCallback callback) {
        super(context, data, callback);
    }

    @Override
    public MultiTypeViewHolder newViewHolder(View itemView) {
        return new MultiTypeViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.child_view;
    }

    @Override
    public void onBindViewHolder(MultiTypeViewHolder holder, final int position) {
        final User user = getItemData(position);
        holder.tv.setText(user.getUserName());
        holder.iv.setTextAndColor(user.getUserName().substring(0, 1), SimpleExpandAdapter.getColor(context));
        holder.qx.setText(user.getQuXian());
        holder.bm.setText(user.getDuty());
        if (user.getRole_a() != 0) {
            holder.role_a.setVisibility(View.VISIBLE);
        } else {
            setGone(holder.role_a);
        }
        if (user.getRole_b() != 0) {
            holder.role_b.setVisibility(View.VISIBLE);
        } else {
            setGone(holder.role_b);
        }
        if (user.getRole_c() != 0) {
            holder.role_c.setVisibility(View.VISIBLE);
        } else {
            setGone(holder.role_c);
        }
        if (user.getRole_d() != 0) {
            holder.role_d.setVisibility(View.VISIBLE);
        } else {
            setGone(holder.role_d);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemClick().onItemClick(position, user, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class MultiTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private TextView bm;
        private TextView qx;
        private ImageView role_a;
        private ImageView role_b;
        private ImageView role_c;
        private ImageView role_d;
        private AvatarImageView iv;
        private RelativeLayout layout;

        public MultiTypeViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.info_tv);
            bm = (TextView) itemView.findViewById(R.id.info_dz);
            qx = (TextView) itemView.findViewById(R.id.info_qx);
            role_a = (ImageView) itemView.findViewById(R.id.class_a);
            role_b = (ImageView) itemView.findViewById(R.id.class_b);
            role_c = (ImageView) itemView.findViewById(R.id.class_c);
            role_d = (ImageView) itemView.findViewById(R.id.class_d);
            iv = (AvatarImageView) itemView.findViewById(R.id.item_avatar);
            layout = (RelativeLayout) itemView.findViewById(R.id.user_info);
        }
    }
}
