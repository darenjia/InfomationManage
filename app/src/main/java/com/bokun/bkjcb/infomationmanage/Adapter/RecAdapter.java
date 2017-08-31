package com.bokun.bkjcb.infomationmanage.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.List;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class RecAdapter extends SimpleRecAdapter<Level, RecAdapter.MyViewHolder> {
    int[] colors = new int[]{R.color.random_1, R.color.random_2, R.color.random_3, R.color.random_4};

    public RecAdapter(Context context) {
        super(context);
    }

    public RecAdapter(Context context, ItemCallback callback) {
        super(context, callback);
    }

    public RecAdapter(Context context, List data) {
        super(context, data);
    }

    public RecAdapter(Context context, List data, ItemCallback callback) {
        super(context, data, callback);
    }

    @Override
    public MyViewHolder newViewHolder(View itemView) {
        return new MyViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycler_child;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Level l = data.get(position);
        holder.textView.setText(data.get(position).getDepartmentNameA());
//        holder.textView.setBackgroundColor(context.getResources().getColor(colors[position % 4]));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSimpleItemClick() != null) {
                    getSimpleItemClick().onItemClick(position, l, 0);
                }
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.view_text);
        }
    }
}
