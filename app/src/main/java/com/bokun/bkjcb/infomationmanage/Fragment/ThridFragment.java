package com.bokun.bkjcb.infomationmanage.Fragment;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bokun.bkjcb.infomationmanage.Adapter.ItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.ThirdAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.ThirdRecAdapter;
import com.bokun.bkjcb.infomationmanage.Animate.SlidAnimate;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class ThridFragment extends BaseFragment {
    public static ThridFragment fragment;
    private ThirdRecAdapter adapter;
    private ThirdAdapter thirdAdapter;
    private ArrayList<Emergency> list;
    private RecyclerView recyclerView;
    private ItemCallback callback;
    private BaseQuickAdapter.OnItemChildClickListener listener;
    private Emergency emergency;
    private BottomDialog dialog;

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
       /* callback = new ThirdItemCallback() {
            @Override
            public void onItemClick(int position, Emergency model, int tag) {
                activity.actionCall(model.getTel());
            }
        };*/
        listener = new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                emergency = (Emergency) adapter.getItem(position);
                creatTip();
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
            adapter = new ThirdRecAdapter(getContext());
            thirdAdapter = new ThirdAdapter(R.layout.recycler_view, list);
            thirdAdapter.openLoadAnimation(new SlidAnimate());
            thirdAdapter.isFirstOnly(false);
            adapter.setData(list);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            recyclerView.setAdapter(adapter);
//            adapter.setItemClick(callback);
            recyclerView.setAdapter(thirdAdapter);
            thirdAdapter.setOnItemChildClickListener(listener);
        }
    }

    private void creatTip() {
        boolean isShow = (boolean) SPUtils.get(getContext(), "isShowTip", true);
        if (isShow) {
            dialog = BottomDialog.create(activity.getSupportFragmentManager())
                    .setViewListener(new BottomDialog.ViewListener() {
                        @Override
                        public void bindView(View v) {
                            initTipView(v);
                        }
                    })
                    .setLayoutRes(R.layout.tip_view)
                    .setDimAmount(0.5f)
                    .setTag("Dialog");
            dialog.show();
        } else {
            activity.actionCall(emergency.getTel());
        }
    }

    private void initTipView(View v) {
        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.tip_box);
        final Button call = (Button) v.findViewById(R.id.tip_call);
        Button cancel = (Button) v.findViewById(R.id.tip_cancel);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    buttonView.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tip_call) {
                    dialog.dismiss();
                    if (checkBox.isChecked()) {
                        SPUtils.put(getContext(), "isShowTip", false);
                    }
                    activity.actionCall(emergency.getTel());
                } else if (v.getId() == R.id.tip_cancel) {
                    dialog.dismiss();
                }
            }
        };
        call.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
    }
}
