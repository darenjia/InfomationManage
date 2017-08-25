package com.bokun.bkjcb.infomationmanage.Fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Adapter.MultiTypeAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.RecAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.TypeItemCallback;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.L;
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
    private RecAdapter adapter;
    private MultiTypeAdapter typeAdapter;
    private int flag = 0;
    private int flag1 = 0;
    private int flag2 = 0;
    private int flag3 = 0;
    private Level flagLevel;
    private Level lastLevel;
    private int layer;
    private ArrayList<String> strings;
    private TagContainerLayout layout;
    private CardView result;
    private SuperTextView header;

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
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        ImageView btn_return = (ImageView) view.findViewById(R.id.ret_btn);
        header = (SuperTextView) view.findViewById(R.id.header);
        ImageView btn_home = (ImageView) view.findViewById(R.id.home_btn);
        layout = (TagContainerLayout) view.findViewById(R.id.tag_layout);
        result = (CardView) view.findViewById(R.id.result);
        strings = new ArrayList<>();
        layout.setTags(strings);
        btn_home.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        ArrayList<Level> list = getFirstData();
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecAdapter(getContext(), list);
        typeAdapter = new MultiTypeAdapter(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recycler_list.setItemAnimator(new DefaultItemAnimator());
        return view;
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
                lastLevel = flagLevel;
                flagLevel = model;
                switch (layer) {
                    case 0:
                        if (position == 0) {
                            adapter.setData(getSecondData());
                        } else {
                            adapter.setData(getData(2, -1, -1, -1, -1));
                        }
                        setStrings(model.getDepartmentNameA());
                        layer = 1;
                        flag = position;
                        break;
                    case 1:
                        if (flag == 0) {
                            if (position == 0) {
                                adapter.setData(getData(0, -1, -1, -1, -1));
                            } else {
                                adapter.setData(getData());
                            }
                        } else {
                            adapter.setData(getData(2, model.getKind1(), -1, -1, -1));
                        }
                        layer = 2;
                        flag1 = position;
                        setStrings(model.getDepartmentNameA());
                        break;
                    case 2:
                        if (flag == 1) {
//                            L.i("做响应了");
                            typeAdapter.setData(getUserData(model));
                            changeList(false, model.getDepartmentNameA());
                        } else {
                            if (flag1 == 0) {
//                                L.i("开始筛选了市级单位了");
                                typeAdapter.setData(getUserData(model));
                                adapter.setData(getData(0, model.getKind1(), -1, -1, -1));
                                changeList(true, model.getDepartmentNameA());
                            } else {
                                adapter.setData(getData(1, -1, -1, -1, model.getQuxin()));
                            }
                        }
                        layer = 3;
                        flag2 = position;
                        setStrings(model.getDepartmentNameA());
                        break;
                    case 3:
                        if (flag1 == 0) {
//                            L.i("市级第二级");
                            typeAdapter.setData(getUserData(model));
                            changeList(false, model.getDepartmentNameA());
                        } else {
//                            L.i("做区级筛选");
                            if (position == 0) {
                                typeAdapter.setData(getUserData(model));
                                changeList(true, model.getDepartmentNameA());
                            }
                            adapter.setData(getData(1, model.getKind1(), -1, -1, model.getQuxin()));
                        }
                        flag3 = position;
                        layer = 4;
                        setStrings(model.getDepartmentNameA());
                        break;
                    case 4:
                        typeAdapter.setData(getUserData(model));
                        changeList(false, model.getDepartmentNameA());
                        layer = 5;
                        setStrings(model.getDepartmentNameA());
                        break;
                    case 5:
                        break;
                }
            }
        });
        typeAdapter.setItemClick(new TypeItemCallback() {
            @Override
            public void onItemClick(int position, User model, int tag) {
                super.onItemClick(position, model, tag);
                L.i("wtf");
                activity.showDetail(model);
            }
        });
    }

    private void changeList(boolean isShow, String s) {
        if (recycler_list.getAdapter() == null) {
            recycler_list.setAdapter(typeAdapter);
        } else {
            typeAdapter.notifyDataSetChanged();
        }
        if (isShow) {
            setVisible(recyclerView);
        } else {
            setGone(recyclerView);
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
//        L.i("level" + level.getQuxin() + ":" + level.getLevel() + ":" + level.getKind1() + ":" + level.getKind2());
        return DBManager.newInstance(getContext()).queryUser(level);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ret_btn:
                if (layer == 0) {
                    return;
                } else if (layer == 1) {
                    adapter.setData(getFirstData());
                    layer = 0;
                } else if (layer == 2) {
                    if (flag == 0) {
                        adapter.setData(getSecondData());
                    } else {
                        adapter.setData(getData(2, -1, -1, -1, -1));
                    }
                    layer = 1;
                    setGone(result);
                } else if (layer == 3) {
                    if (flag == 0) {
                        if (flag1 == 0) {
                            adapter.setData(getData(0, -1, -1, -1, -1));
                        } else {
                            adapter.setData(getData());
                        }
                    } else {

                    }
                    layer = 2;
                    setGone(result);
                } else if (layer == 4) {
                   /* if (flag == 1) {
                        L.i("wtf");
                        typeAdapter.setData(getUserData(flagLevel));
                        changeList(false);
                        setGone(recycler_list);
                    } else {*/
                    if (flag1 == 0) {
                        typeAdapter.setData(getUserData(lastLevel));
                        adapter.setData(getData(0, lastLevel.getKind1(), -1, -1, -1));
                        changeList(true, lastLevel.getDepartmentNameA());
                        setVisible(result);
                    } else {
                        setGone(result);
                        adapter.setData(getData(1, -1, -1, -1, lastLevel.getQuxin()));
                    }
//                    }
//                    adapter.setData(getData(1, -1, -1, -1, flagLevel.getQuxin()));
                    layer = 3;
                } else if (layer == 5) {
                    if (flag3 == 0) {
                        typeAdapter.setData(getUserData(lastLevel));
                        changeList(true, lastLevel.getDepartmentNameA());

                    } else {
                        setGone(result);
                    }
                    adapter.setData(getData(1, lastLevel.getKind1(), -1, -1, lastLevel.getQuxin()));
                    layer = 4;
                }
                setStrings(null);
                break;
            case R.id.home_btn:
                adapter.setData(getFirstData());
                layer = 0;
                layout.removeAllTags();
                setGone(result);
                break;
        }
        flagLevel = lastLevel;
        setVisible(recyclerView);
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

    private void setGone(View view) {
        view.setVisibility(View.GONE);
    }

    private void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setHeaderView(String name, String num) {
        header.setLeftString(name);
        header.setRightString(num);
    }
}
