package com.bokun.bkjcb.infomationmanage.Adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2017/8/28.
 */

public class UpdateAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public UpdateAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public UpdateAdapter(@Nullable List data) {
        super(data);
    }

    public UpdateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder holder, String string) {
        holder.addOnClickListener(R.id.update_info);
        TextView tv = holder.getView(R.id.update_info);
        tv.setText(string);
    }
}
