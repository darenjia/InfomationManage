package com.bokun.bkjcb.infomationmanage.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Activity.AboutActivity;
import com.bokun.bkjcb.infomationmanage.Activity.MainActivity;
import com.bokun.bkjcb.infomationmanage.Adapter.MultiTypeAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.RecAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleItemCallback;
import com.bokun.bkjcb.infomationmanage.Adapter.TypeItemCallback;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.View.DividerItemDecoration;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by DengShuai on 2017/9/15.
 */

public class ChildOneFragment extends Fragment implements View.OnClickListener {

    private TextView title;
    private RecyclerView recycler_list;
    private RecyclerView recyclerView;
    private int fragmentType;
    private static ChildOneFragment fragment;
    private RecAdapter adapter;
    private MultiTypeAdapter typeAdapter;
    private Button btn_home;
    private Button btn_return;
    private Level flagLevel;
    private Level lastLevel;
    private int layer;
    private int recyclerId;
    private ArrayList<String> strings;
    private TagContainerLayout layout;
    private SuperTextView header;
    private CardView result;
    private int flag2;
    private View.OnClickListener listener;
    private View rootView;
    private MainActivity activity;
    private List<User> userList;
    private Level level;


    public static ChildOneFragment newInstance(int type) {
        if (fragment == null) {
            fragment = new ChildOneFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("Type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initView(inflater, container);
        }
        init();
        setListener();
        return rootView;
    }

    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_child_one, null);
        //level标题
        title = (TextView) view.findViewById(R.id.child_title);
        //level列表
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //结果列表
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        //结果标题
        header = (SuperTextView) view.findViewById(R.id.header);
        btn_home = (Button) view.findViewById(R.id.home_btn);
        btn_return = (Button) view.findViewById(R.id.ret_btn);
        //标签
        layout = (TagContainerLayout) view.findViewById(R.id.tag_layout);
        //结果视图
        result = (CardView) view.findViewById(R.id.result);
        strings = new ArrayList<>();
        layout.setTags(strings);
        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getContext());
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager1);
        recycler_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    private void init() {
        recyclerId = fragmentType = getArguments().getInt("Type");
        layout.removeAllTags();
        setGone(result);
        flagLevel = new Level();
        userList = null;
        layer = 0;
        btn_return.setBackgroundResource(R.color.black_3);
        switch (fragmentType) {
            case 0://市级
                adapter = new RecAdapter(getContext(), getData(0, -1, -1, -1, -1));
                title.setText("市级单位");
                title.setBackgroundColor(getResources().getColor(R.color.color_type_1));
                flagLevel.setDepartmentNameA("市级单位");
                break;
            case 1://区级
                adapter = new RecAdapter(getContext(), getData());
                title.setText("区级单位");
                title.setBackgroundColor(getResources().getColor(R.color.color_type_2));
                flagLevel.setDepartmentNameA("区级单位");
                break;
            case 2://企业
                adapter = new RecAdapter(getContext(), getData(2, -1, -1, -1, -1));
                title.setText("企业单位");
                title.setBackgroundColor(getResources().getColor(R.color.color_type_0));
                flagLevel.setDepartmentNameA("企业单位");
                break;
        }
        typeAdapter = new MultiTypeAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recycler_list.setAdapter(typeAdapter);
        recycler_list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        setVisible(recyclerView);
    }

    protected void setListener() {
        btn_return.setOnClickListener(this);
        btn_home.setOnClickListener(listener);
        typeAdapter.setItemClick(new TypeItemCallback() {
            @Override
            public void onItemClick(int position, User model, int tag) {
                super.onItemClick(position, model, tag);
                activity.showDetail(model);
            }
        });
        adapter.setItemClick(new SimpleItemCallback<Level, RecAdapter.MyViewHolder>() {
            @Override
            public void onItemClick(int position, Level model, int tag) {
                lastLevel = flagLevel;
                flagLevel = model;
                btn_return.setBackgroundResource(R.color.colorPrimary);
                if (recyclerId == 0) {
                    switch (layer) {
                        case 0:
                            adapter.setData(getData(0, model.getKind1(), -1, -1, -1));
                            layer = 1;
                            break;
                        case 1:
                            adapter.setData(getData(0, model.getKind1(), model.getKind2(), -1, -1));
                            layer = 2;
                            break;
                    }
                } else if (recyclerId == 1) {
                    switch (layer) {
                        case 0:
                            adapter.setData(getData(1, -1, -1, -1, model.getQuxin()));
                            layer = 1;
                            break;
                        case 1:
                            adapter.setData(getData(1, model.getKind1(), -1, -1, model.getQuxin()));
                            layer = 2;
                            flag2 = position;
                            break;
                        case 2:
                            adapter.setData(getData(1, model.getKind1(), model.getKind2(), -1, model.getQuxin()));
                            layer = 3;
                            break;
                        case 3:
                            adapter.setData(getData(1, model.getKind1(), model.getKind2(), model.getKind3(), model.getQuxin()));
                            layer = 4;
                            break;
                    }
                } else {
                    switch (layer) {
                        case 0:
                            adapter.setData(getData(2, model.getKind1(), -1, -1, -1));
                            layer = 1;
                            break;
                        case 1:
                            adapter.setData(getData(2, model.getKind1(), model.getKind2(), -1, -1));
                            layer = 2;
                            break;
                    }
                }
                setDataAndView(model);
                setStrings(model.getDepartmentNameA());
            }
        });
    }

    private void setDataAndView(final Level model) {
        userList = getUserData(model);
        ArrayList<Level> levels = null;
        if (fragmentType != 2) {
            levels = DBManager.newInstance(getContext()).queryLevel(model);
        } else {
            levels = DBManager.newInstance(getContext()).queryLevel_G(model);
        }
        if (userList.size() > 0 || adapter.getItemCount() == 0) {
            setVisible(result);
            typeAdapter.setData(userList);
//            changeList(model.getDepartmentNameA());
            header.setLeftString(model.getDepartmentNameA());
            header.setRightString(typeAdapter.getItemCount() + "");
            if (levels != null && levels.size() > 0) {
                header.setLeftString(model.getDepartmentNameA() + "(" + levels.size() + ")");
                header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutActivity.comeIn(model, getContext(), 1);
                    }
                });
            } else {
                header.setLeftString(model.getDepartmentNameA());
                header.setOnClickListener(null);
            }
        } else {
            setGone(result);
        }
        if (adapter.getItemCount() == 0) {
            setGone(recyclerView, title);
        } else {
            title.setText(model.getDepartmentNameA());
            setVisible(recyclerView, title);
        }
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
                    if (recyclerId == 2) {
                        adapter.setData(getData(2, -1, -1, -1, -1));
                    } else if (recyclerId == 0) {
                        adapter.setData(getData(0, -1, -1, -1, -1));
                    } else {
                        adapter.setData(getData());
                    }
                    layer = 0;
                } else if (layer == 2) {
                    if (recyclerId == 2) {
                        adapter.setData(getData(2, flagLevel.getKind1(), -1, -1, -1));
                    } else if (recyclerId == 0) {
                        adapter.setData(getData(0, flagLevel.getKind1(), -1, -1, -1));
                    } else {
                        adapter.setData(getData(1, -1, -1, -1, flagLevel.getQuxin()));
                    }
                    layer = 1;
                } else if (layer == 3) {
                    if (recyclerId == 1) {
                        adapter.setData(getData(1, lastLevel.getKind1(), -1, -1, lastLevel.getQuxin()));
                    }
                    layer = 2;
                } else if (layer == 4) {

                    adapter.setData(getData(1, lastLevel.getKind1(), lastLevel.getKind2(), -1, lastLevel.getQuxin()));
                    layer = 3;

                }
                if (flagLevel.getId() != lastLevel.getId()) {
                    flagLevel = lastLevel;
                } else {
                    lastLevel.setId(-1);
                }
                setDataAndView(lastLevel);
                if (layer == 0) {
                    btn_return.setBackgroundResource(R.color.black_3);
                }
                setStrings(null);
                break;
        }
    }

    private void changeList(String s) {
        if (s != null) {
            setHeaderView(s, String.valueOf(typeAdapter.getItemCount()));
        }
    }

    private void setStrings(String s) {
        if (s == null) {
            strings.remove(strings.size() - 1);
            if (strings.size() > 0) {
                String newTitle = strings.get(strings.size() - 1);
                title.setText(newTitle);
            } else {
                switch (fragmentType) {
                    case 0:
                        title.setText("市级单位");
                        break;
                    case 1:
                        title.setText("区级级单位");
                        break;
                    case 2:
                        title.setText("企业单位");
                        break;
                }
            }
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

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ChildOneFragment setActivity(MainActivity activity) {
        this.activity = activity;
        return fragment;
    }

    public void setData(Level l) {
        this.level = l;
    }
}
