package com.bokun.bkjcb.infomationmanage.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Activity.AboutActivity;
import com.bokun.bkjcb.infomationmanage.Activity.MainActivity;
import com.bokun.bkjcb.infomationmanage.Adapter.HistoryAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/9/14.
 */

public class PartOneFragment extends BaseFragment implements View.OnClickListener, MainActivity.UpdateListener {

    private FrameLayout layout;
    private SuperTextView history;
    private TextView btn_shiji;
    private TextView btn_quji;
    private TextView btn_qiye;
    private TextView btn_jinji;
    private TextView notify;
    private RecyclerView recycler_history;
    private HistoryAdapter historyAdapter;
    private static PartOneFragment fragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ChildOneFragment oneFragment;
    private View.OnClickListener listener;
    private CardView history_card;

    public static PartOneFragment newInstance() {
        if (fragment == null) {
            fragment = new PartOneFragment();
        }
        return fragment;
    }

    public void setLayoutAndManager(FrameLayout layout, FragmentManager manager) {
        this.layout = layout;
        this.manager = manager;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_first, null);
        history = (SuperTextView) view.findViewById(R.id.history_header);
        btn_shiji = (TextView) view.findViewById(R.id.btn_shiji);
        btn_quji = (TextView) view.findViewById(R.id.btn_quji);
        btn_qiye = (TextView) view.findViewById(R.id.btn_qiye);
        btn_jinji = (TextView) view.findViewById(R.id.btn_jinji);
        notify = (TextView) view.findViewById(R.id.notify);
        recycler_history = (RecyclerView) view.findViewById(R.id.recycler_history);
        history_card = (CardView) view.findViewById(R.id.history_card);
        init();
        return view;
    }

    private void init() {
        recycler_history.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void setListener() {
        history.setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
            @Override
            public void onClickListener() {
                DBManager.newInstance(getContext()).deleteHistory();
                Toast.makeText(getContext(), "记录已清空", Toast.LENGTH_SHORT).show();
                historyAdapter.setNewData(null);
            }
        });
        btn_jinji.setOnClickListener(this);
        btn_quji.setOnClickListener(this);
        btn_shiji.setOnClickListener(this);
        btn_qiye.setOnClickListener(this);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = manager.beginTransaction();
                transaction.replace(layout.getId(), fragment);
                transaction.commit();
            }
        };
        history.setLeftTvClickListener(new SuperTextView.OnLeftTvClickListener() {
            @Override
            public void onClickListener() {
                AboutActivity.comeIn(null, getActivity(), 3);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setUpdateListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initHistory();
      /*  if (activity.hasNew) {
            L.i("搞什么啊");
            notify.setVisibility(View.VISIBLE);
        }*/
    }

    private void initHistory() {
        ArrayList<HistoryItem> historyItems = DBManager.newInstance(getContext()).getAllHistoryItem("8");
        if (historyItems.size() > 0) {
            history_card.setVisibility(View.VISIBLE);
            if (historyAdapter == null) {
                historyAdapter = new HistoryAdapter(R.layout.history_view);
                recycler_history.setAdapter(historyAdapter);
                historyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        HistoryItem item = (HistoryItem) adapter.getItem(position);
                        User user = DBManager.newInstance(getContext()).queryUserById(item.getUserId());
                        activity.showDetail(user);
                    }
                });
            }
            historyAdapter.setNewData(historyItems);
        } else {
            history_card.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        transaction = manager.beginTransaction();
        if (v.getId() == R.id.btn_jinji) {
            ThridFragment thridFragment = ThridFragment.newInstance().setActivity(activity);
            thridFragment.setClickListener(listener);
            transaction.replace(layout.getId(), thridFragment);
        } else {
            switch (v.getId()) {
                case R.id.btn_qiye:
                    oneFragment = ChildOneFragment.newInstance(2).setActivity(activity);
                    break;
                case R.id.btn_quji:
                    oneFragment = ChildOneFragment.newInstance(1).setActivity(activity);
                    break;
                case R.id.btn_shiji:
                    oneFragment = ChildOneFragment.newInstance(0).setActivity(activity);
                    break;
            }
            oneFragment.setListener(listener);
            transaction.replace(layout.getId(), oneFragment);
        }
        transaction.commit();
    }

    @Override
    public void onChanged(boolean is) {
        if (is) {
            notify.setVisibility(View.VISIBLE);
        } else {
            notify.setVisibility(View.GONE);
        }
    }
}
