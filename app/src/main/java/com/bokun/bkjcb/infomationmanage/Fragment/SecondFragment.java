package com.bokun.bkjcb.infomationmanage.Fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Adapter.HistoryAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.MultiTypeAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.RecAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleItemCallback;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class SecondFragment extends BaseFragment implements View.OnClickListener {
    public static SecondFragment fragment;
    private RecyclerView recyclerView;
    private RecyclerView recycler_list;
    private RecyclerView recycler_shiji;
    private RecyclerView recycler_quji;
    private RecyclerView recycler_history;
    private RecAdapter adapter;
    private RecAdapter adapter_shiji;
    private RecAdapter adapter_quji;
    private MultiTypeAdapter typeAdapter;
    private int flag = 0;
    private int flag1 = 0;
    private int flag2 = 0;
    private int flag3 = 0;
    private Level flagLevel;
    private Level lastLevel;
    private int layer;
    private int recyclerId = 0;
    private ArrayList<String> strings;
    private TagContainerLayout layout;
    private CardView result;
    private CardView nav_card;
    private CardView history_card;
    private SuperTextView header, history;
    private TextView title_qiye, title_shiji, title_quji;
    private HistoryAdapter historyAdapter;
    private Button btn_return;

    public static SecondFragment newInstance() {
        if (fragment == null) {
            fragment = new SecondFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_two, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        recycler_history = (RecyclerView) view.findViewById(R.id.recycler_history);
        recycler_shiji = (RecyclerView) view.findViewById(R.id.recycler_shiji);
        recycler_quji = (RecyclerView) view.findViewById(R.id.recycler_quji);
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        btn_return = (Button) view.findViewById(R.id.ret_btn);
        header = (SuperTextView) view.findViewById(R.id.header);
        history = (SuperTextView) view.findViewById(R.id.history_header);
        Button btn_home = (Button) view.findViewById(R.id.home_btn);
        layout = (TagContainerLayout) view.findViewById(R.id.tag_layout);
        result = (CardView) view.findViewById(R.id.result);
        nav_card = (CardView) view.findViewById(R.id.nav_card);
        history_card = (CardView) view.findViewById(R.id.history_card);
        title_qiye = (TextView) view.findViewById(R.id.title_qiye);
        title_shiji = (TextView) view.findViewById(R.id.title_shiji);
        title_quji = (TextView) view.findViewById(R.id.title_quji);
        strings = new ArrayList<>();
        layout.setTags(strings);
        btn_home.setOnClickListener(this);
        btn_return.setOnClickListener(this);

        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler_history.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getContext());
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);

        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(getContext());
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setJustifyContent(JustifyContent.FLEX_START);

        FlexboxLayoutManager layoutManager3 = new FlexboxLayoutManager(getContext());
        layoutManager3.setFlexDirection(FlexDirection.ROW);
        layoutManager3.setJustifyContent(JustifyContent.FLEX_START);

        recyclerView.setLayoutManager(layoutManager1);
        recycler_shiji.setLayoutManager(layoutManager2);
        recycler_quji.setLayoutManager(layoutManager3);

        adapter = new RecAdapter(getContext(), getData(2, -1, -1, -1, -1));
        typeAdapter = new MultiTypeAdapter(getContext());
        adapter_shiji = new RecAdapter(getContext(), getData(0, -1, -1, -1, -1));
        adapter_quji = new RecAdapter(getContext(), getData());

        recyclerView.setAdapter(adapter);
        recycler_quji.setAdapter(adapter_quji);
        recycler_shiji.setAdapter(adapter_shiji);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recycler_list.setItemAnimator(new DefaultItemAnimator());
    }

    private ArrayList<Level> getFirstData() {
        ArrayList<Level> list = new ArrayList<>();
        Level level1 = new Level();
        level1.setDepartmentNameA("管理单位");
        list.add(level1);
        Level level2 = new Level();
        level2.setDepartmentNameA("企业单位");
        level2.setLevel(2);
        list.add(level2);
        return list;
    }

    private ArrayList<Level> getSecondData() {
        ArrayList<Level> list = new ArrayList<>();
        Level level1 = new Level();
        level1.setDepartmentNameA("市级单位");
        level1.setLevel(0);
        list.add(level1);
        Level level2 = new Level();
        level2.setDepartmentNameA("区级单位");
        level2.setLevel(1);
        list.add(level2);
        return list;
    }

    @Override
    protected void setListener() {
        adapter.setItemClick(new SimpleItemCallback<Level, RecAdapter.MyViewHolder>() {
            @Override
            public void onItemClick(int position, Level model, int tag) {
                setVisible(nav_card);
                setGone(history_card, recycler_shiji, title_shiji, recycler_quji, title_quji);
                lastLevel = flagLevel;
                flagLevel = model;
                recyclerId = 0;
                btn_return.setBackgroundResource(R.color.colorPrimary);
                switch (layer) {
                    case 0:
                        adapter.setData(getData(2, model.getKind1(), -1, -1, -1));
                        layer = 1;
                        break;
                    case 1:
                        typeAdapter.setData(getUserData(model));
                        changeList(false, model.getDepartmentNameA(), recyclerView);
                        layer = 2;
                        break;
                }
                setStrings(model.getDepartmentNameA());
            }
        });
        adapter_shiji.setItemClick(new SimpleItemCallback<Level, RecAdapter.MyViewHolder>() {
            @Override
            public void onItemClick(int position, Level model, int tag) {
                recyclerId = 1;
                setVisible(nav_card);
                setGone(recyclerView, title_qiye, recycler_quji, title_quji, history_card);
                lastLevel = flagLevel;
                flagLevel = model;
                btn_return.setBackgroundResource(R.color.colorPrimary);
                switch (layer) {
                    case 0:
                        typeAdapter.setData(getUserData(model));
                        adapter_shiji.setData(getData(0, model.getKind1(), -1, -1, -1));
                        changeList(true, model.getDepartmentNameA(), recycler_shiji);
                        layer = 1;
                        break;
                    case 1:
                        typeAdapter.setData(getUserData(model));
                        changeList(false, model.getDepartmentNameA(), recycler_shiji);
                        layer = 2;
                        break;
                }
                setStrings(model.getDepartmentNameA());
            }
        });
        adapter_quji.setItemClick(new SimpleItemCallback<Level, RecAdapter.MyViewHolder>() {
            @Override
            public void onItemClick(int position, Level model, int tag) {
                recyclerId = 2;
                setVisible(nav_card);
                setGone(recyclerView, title_qiye, recycler_shiji, title_shiji, history_card);
                lastLevel = flagLevel;
                flagLevel = model;
                btn_return.setBackgroundResource(R.color.colorPrimary);
                switch (layer) {
                    case 0:
                        adapter_quji.setData(getData(1, -1, -1, -1, model.getQuxian()));
                        layer = 1;
                        break;
                    case 1:
                        if (position == 0) {
                            typeAdapter.setData(getUserData(model));
                            changeList(true, model.getDepartmentNameA(), recycler_quji);
                        }
                        adapter_quji.setData(getData(1, model.getKind1(), -1, -1, model.getQuxian()));
                        layer = 2;
                        flag2 = position;
                        break;
                    case 2:
//                        if (flag2 == 2) {
                        adapter_quji.setData(getData(1, model.getKind1(), model.getKind2(), -1, model.getQuxian()));
//                        } else {
                        typeAdapter.setData(getUserData(model));
                        changeList(true, model.getDepartmentNameA(), recycler_quji);
//                        }
                        layer = 3;
                        flag3 = position;
                        break;
                    case 3:
                        typeAdapter.setData(getUserData(model));
                        changeList(false, model.getDepartmentNameA(), recycler_quji);
                        layer = 4;
                        break;
                }
                setStrings(model.getDepartmentNameA());
            }
        });


    }

    private void changeList(boolean isShow, String s, RecyclerView view) {
        if (recycler_list.getAdapter() == null) {
            recycler_list.setAdapter(typeAdapter);
        } else {
            typeAdapter.notifyDataSetChanged();
        }
        if (isShow) {
            setVisible(view);
        } else {
            setGone(view);
        }
        if (s != null) {
            setHeaderView(s, String.valueOf(typeAdapter.getItemCount()));
        }
        setVisible(result);
    }

    private ArrayList<Level> getData(int level, int kind1, int kind2, int kind3, int quxian) {
        return DBManager.newInstance(getContext()).queryLevel(level, kind1, kind2, kind3, quxian);
    }

    private ArrayList<Level> getData() {
        return DBManager.newInstance(getContext()).queryAllQuName();
    }

    private ArrayList<User> getUserData(Level level) {
        return DBManager.newInstance(getContext()).queryUser(level);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ret_btn:
                if (layer == 0) {
                    return;
                } else if (layer == 1) {
                    if (recyclerId == 0) {
                        adapter.setData(getData(2, -1, -1, -1, -1));
                    } else if (recyclerId == 1) {
                        adapter_shiji.setData(getData(0, -1, -1, -1, -1));
                    } else {
                        adapter_quji.setData(getData());
                    }
                    setGone(result);
                    layer = 0;
                } else if (layer == 2) {
                    if (recyclerId == 0) {
                        adapter.setData(getData(2, lastLevel.getKind1(), -1, -1, -1));
                        setGone(result);
                        setVisible(recyclerView);
                    } else if (recyclerId == 1) {
                        typeAdapter.setData(getUserData(lastLevel));
                        adapter_shiji.setData(getData(0, lastLevel.getKind1(), -1, -1, -1));
                        changeList(true, lastLevel.getDepartmentNameA(), recycler_shiji);
                        setVisible(result);
                    } else {
                        setGone(result);
                        setVisible(recycler_quji);
                        adapter_quji.setData(getData(1, -1, -1, -1, lastLevel.getQuxian()));
                    }
                    layer = 1;
                } else if (layer == 3) {
                    if (recyclerId == 2) {
                        if (flag2 == 0) {
                            typeAdapter.setData(getUserData(lastLevel));
                            changeList(true, lastLevel.getDepartmentNameA(), recycler_quji);
                        } else {
                            setGone(result);
                            setVisible(recycler_quji);
                        }
                        adapter_quji.setData(getData(1, lastLevel.getKind1(), -1, -1, lastLevel.getQuxian()));
                    }
                    layer = 2;
                } else if (layer == 4) {
//                    setGone(result);
                    typeAdapter.setData(getUserData(lastLevel));
                    changeList(true, lastLevel.getDepartmentNameA(), recycler_quji);

                    setVisible(recycler_quji);
                    adapter_quji.setData(getData(1, lastLevel.getKind1(), lastLevel.getKind2(), -1, lastLevel.getQuxian()));
                    layer = 3;

                }
                if (layer == 0) {
                    btn_return.setBackgroundResource(R.color.black);
                }
                setStrings(null);
                break;
            case R.id.home_btn:
                adapter.setData(getData(2, -1, -1, -1, -1));
                adapter_shiji.setData(getData(0, -1, -1, -1, -1));
                adapter_quji.setData(getData());

                layer = 0;
                layout.removeAllTags();
                setGone(result);
                setVisible(recyclerView, recycler_shiji, recycler_quji, title_qiye, title_quji, title_shiji, history_card);
                setGone(nav_card);
                break;
        }
        flagLevel = lastLevel;
    }

    private void setStrings(String s) {
        if (s == null) {
            strings.remove(strings.size() - 1);
            layout.removeTag(layout.getTags().size() - 1);
        } else {
            strings.add(s);
            layout.addTag(s);
        }
    }

    private void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }

    }

    private void setVisible(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    private void setHeaderView(String name, String num) {
        header.setLeftString(name);
        header.setRightString(num);
    }

    @Override
    public void onStart() {
        super.onStart();
        initHistory();
    }

    private void initHistory() {
        ArrayList<HistoryItem> historyItems = DBManager.newInstance(getContext()).getAllHistoryItem("8");
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
    }
}
