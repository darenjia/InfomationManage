package com.bokun.bkjcb.infomationmanage.Adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2017/8/28.
 */

public class ThirdAdapter extends BaseQuickAdapter<Emergency, BaseViewHolder> {
    public ThirdAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public ThirdAdapter(@Nullable List data) {
        super(data);
    }

    public ThirdAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, Emergency emergency) {
        holder.addOnClickListener(R.id.third_content);
        SuperTextView tv = holder.getView(R.id.third_content);
        tv.setLeftString(emergency.getTel())
                .setRightString(emergency.getUnit());
        if (!TextUtils.isEmpty(emergency.getArea()) && !TextUtils.isEmpty(emergency.getRemarks())) {
            tv.setCenterString(emergency.getArea() + "(" + emergency.getRemarks() + ")");
        } else if (!TextUtils.isEmpty(emergency.getArea()) && TextUtils.isEmpty(emergency.getRemarks())) {
            tv.setCenterString(emergency.getArea());
        } else {
            tv.setCenterString("");
        }
    }
}
