package com.bokun.bkjcb.infomationmanage.Fragment;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.infomationmanage.Adapter.ItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.ThirdItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.ThirdRecAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class ThridFragment extends BaseFragment {
    public static ThridFragment fragment;
    private ThirdRecAdapter adapter;
    private ArrayList<Emergency> list;
    private RecyclerView recyclerView;
    private ItemCallback callback;

    public static ThridFragment newInstance() {
        if (fragment == null) {
            fragment = new ThridFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_three, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    protected void setListener() {
        callback = new ThirdItemCallback() {
            @Override
            public void onItemClick(int position, Emergency model, int tag) {
                activity.actionCall(model.getTel());
            }
        };
        new LoadDataTask().execute();
    }

    class LoadDataTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            list = DBManager.newInstance(getContext()).getAllEmergency();
            adapter = new ThirdRecAdapter(getContext(), list);
            adapter.setData(list);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapter);
            adapter.setItemClick(callback);
        }
    }

}
