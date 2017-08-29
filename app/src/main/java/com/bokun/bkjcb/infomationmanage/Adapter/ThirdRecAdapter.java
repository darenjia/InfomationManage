package com.bokun.bkjcb.infomationmanage.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.List;

/**
 * Created by DengShuai on 2017/8/25.
 */

public class ThirdRecAdapter extends SimpleRecAdapter<Emergency, ThirdRecAdapter.ThridViewHolder> {
    public ThirdRecAdapter(Context context) {
        super(context);
    }

    public ThirdRecAdapter(Context context, ItemCallback<Emergency> callback) {
        super(context, callback);
    }

    public ThirdRecAdapter(Context context, List<Emergency> data) {
        super(context, data);
    }

    public ThirdRecAdapter(Context context, List<Emergency> data, ItemCallback<Emergency> callback) {
        super(context, data, callback);
    }

    @Override
    public ThridViewHolder newViewHolder(View itemView) {
        return new ThridViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_view;
    }

    @Override
    public void onBindViewHolder(ThridViewHolder holder, final int position) {
        final Emergency emergency = getItemData(position);
        holder.tv.setLeftString(emergency.getTel())
                .setRightString(emergency.getUnit())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getItemClick().onItemClick(position, emergency, 0);
                    }
                });
        if (!TextUtils.isEmpty(emergency.getArea()) && !TextUtils.isEmpty(emergency.getRemarks())) {
            holder.tv.setCenterString(emergency.getArea() + "(" + emergency.getRemarks() + ")");
        } else if (!TextUtils.isEmpty(emergency.getArea()) && TextUtils.isEmpty(emergency.getRemarks())) {
            holder.tv.setCenterString(emergency.getArea());
        } else {
            holder.tv.setCenterString("");
        }
//        setAnimation(holder.tv, position);
    }

    public static class ThridViewHolder extends RecyclerView.ViewHolder {
        SuperTextView tv;

        public ThridViewHolder(View itemView) {
            super(itemView);
            tv = (SuperTextView) itemView.findViewById(R.id.third_content);
        }
    }

    private int lastPosition = -1;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
            animation.setDuration(300);
            animation.start();
            lastPosition = position;
        }
    }

}
