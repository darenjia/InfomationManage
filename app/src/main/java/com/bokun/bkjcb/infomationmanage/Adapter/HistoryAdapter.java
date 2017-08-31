package com.bokun.bkjcb.infomationmanage.Adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DengShuai on 2017/8/31.
 */

public class HistoryAdapter extends BaseQuickAdapter<HistoryItem, HistoryAdapter.MyViewHolder> {

    public HistoryAdapter(int layoutResId, @Nullable List<HistoryItem> data) {
        super(layoutResId, data);
    }

    public HistoryAdapter(@Nullable List<HistoryItem> data) {
        super(data);
    }

    public HistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(MyViewHolder helper, HistoryItem item) {
        helper.addOnClickListener(R.id.history_info);
        helper.tv.setLeftString(item.getTel()).setCenterString(item.getUserName()).setRightString(formatData(item.getTime()));
    }

    class MyViewHolder extends BaseViewHolder {
        SuperTextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (SuperTextView) view.findViewById(R.id.history_info);
        }
    }

    private String formatData(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        return format.format(new Date(time));
    }
}
